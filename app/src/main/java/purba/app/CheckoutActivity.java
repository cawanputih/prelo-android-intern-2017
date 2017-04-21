package purba.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckoutActivity extends AppCompatActivity {

    String intentidbarang;
    ProgressDialog pDialog;
    String pesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        intentidbarang = getIntent().getStringExtra("key_idbarang");

        new GetBarang().execute();
    }

    private class GetBarang extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CheckoutActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getrequestbarang(intentidbarang);
            pesan = jsonStr;
            if (jsonStr != null) {
                try {
                    String crappyPrefix = "null";

                    if(jsonStr.startsWith(crappyPrefix)){
                        jsonStr = jsonStr.substring(crappyPrefix.length(), jsonStr.length());
                    }

                    JSONObject jobj = new JSONObject(jsonStr);
                    JSONArray jdata = jobj.getJSONArray("_data");

                    wishlist.clear();
                    Wish wish;

                    for (int i = 0; i < jdata.length(); i++) {
                        JSONObject c = jdata.getJSONObject(i);

                        String name = c.getString("name");
                        String idbarang = c.getString("_id");
                        int price = c.getInt("price");
                        JSONArray dp = c.getJSONArray("display_picts");
                        String srcimage = dp.getString(0);

                        wish= new Wish();

                        wish.setName(name);
                        wish.setPrice(price);
                        wish.setSrcimage(srcimage);
                        wish.setid(idbarang);
                        wishlist.add(wish);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            Log.d(TAG,intenttoken);
            Log.d(TAG,pesan);

            tvfullname.setText(intentfullname);
            tvusername.setText(intentusername);
            tvemail.setText(intentemail);
            tvlokasi.setText(intentkecamatan+", "+intentregion+", "+intentprovinsi);

            Glide.with(ProfileActivity.this).load(intentpict).into(ciwprofpict);
            rv.setAdapter(new WishAdapter(ProfileActivity.this,wishlist));

        }
    }
}
