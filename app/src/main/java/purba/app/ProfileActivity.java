package purba.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    String TAG = ProfileActivity.class.getSimpleName();
    ProgressDialog pDialog;
    String intenttoken;
    String pesan;
    TextView txttok;
    TextView txtbalasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txttok = (TextView) findViewById(R.id.idshowtoken);
        txtbalasan = (TextView) findViewById(R.id.idshowbalasan);
        intenttoken = getIntent().getStringExtra("key_token");

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
            /*if (jsonStr != null) {
                try {
                    String crappyPrefix = "null";

                    if(jsonStr.startsWith(crappyPrefix)){
                        jsonStr = jsonStr.substring(crappyPrefix.length(), jsonStr.length());
                    }

                    JSONObject jobj = new JSONObject(jsonStr);
                    JSONObject jdata = jobj.getJSONObject("_data");
                    mtoken = jdata.optString("token");

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

            }*/

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(getApplicationContext(),
                    pesan,
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(TAG,intenttoken);
            Log.d(TAG,pesan);
            txttok.setText(intenttoken);
            txtbalasan.setText(pesan);
        }
    }
}
