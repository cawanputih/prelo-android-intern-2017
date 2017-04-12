package purba.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    String TAG = ProfileActivity.class.getSimpleName();
    ProgressDialog pDialog;
    String pesan;
    TextView tvfullname;
    TextView tvusername;
    TextView tvemail;
    TextView tvlokasi;
    CircleImageView ciwprofpict;
    RecyclerView rv;
    ArrayList<Wish> wishlist;

    String intenttoken ;
    String intentpict ;
    String intentfullname ;
    String intentusername;
    String intentemail ;
    String intentkecamatan ;
    String intentregion;
    String intentprovinsi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

         intenttoken = getIntent().getStringExtra("key_token");
         intentpict = getIntent().getStringExtra("key_pict");
         intentfullname = getIntent().getStringExtra("key_fullname");
         intentusername= getIntent().getStringExtra("key_username");
         intentemail = getIntent().getStringExtra("key_email");
         intentkecamatan = getIntent().getStringExtra("key_kecamatan");
         intentregion= getIntent().getStringExtra("key_region");
         intentprovinsi= getIntent().getStringExtra("key_provinsi");

        setTitle(intentusername);

        tvfullname = (TextView) findViewById(R.id.idfullname);
        tvusername = (TextView) findViewById(R.id.idusername);
        tvemail= (TextView) findViewById(R.id.idemail);
        tvlokasi= (TextView) findViewById(R.id.idlokasi);
        ciwprofpict= (CircleImageView) findViewById(R.id.idprofpic);


        wishlist = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.idrecview);
        rv.setLayoutManager( new LinearLayoutManager(this));

        new ProfileGet().execute();
    }


    private class ProfileGet extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.getrequest(intenttoken);
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
                        int price = c.getInt("price");
                        JSONArray dp = c.getJSONArray("display_picts");
                        String srcimage = dp.getString(0);

                        wish= new Wish();

                        wish.setName(name);
                        wish.setPrice(price);
                        wish.setSrcimage(srcimage);

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
