package purba.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;


    String mtoken;
    String pict;
    String username ;
    String email;
    String fullname;
    String kecamatan;
    String region;
    String provinsi;

    EditText etnama;
    EditText etpassword;
    Button btnlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etnama = (EditText) findViewById(R.id.idnama);
        etpassword = (EditText) findViewById(R.id.idpassword);
        btnlogin = (Button) findViewById(R.id.idbtnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginPost().execute();
            }
        });
    }

    private class LoginPost extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.postrequest(etnama.getText().toString(),etpassword.getText().toString());
            if (jsonStr != null) {
                try {
                    String crappyPrefix = "null";

                    if(jsonStr.startsWith(crappyPrefix)){
                        jsonStr = jsonStr.substring(crappyPrefix.length(), jsonStr.length());
                    }

                    JSONObject jobj = new JSONObject(jsonStr);
                    JSONObject jdata = jobj.getJSONObject("_data");
                    JSONObject jprofile = jdata.getJSONObject("profile");
                    pict = jprofile.getString("pict");
                    username = jdata.optString("username");
                    email= jdata.optString("email");
                    fullname= jdata.optString("fullname");
                    JSONObject jaddress = jdata.getJSONObject("default_address");
                    kecamatan = jaddress.getString("subdistrict_name");
                    region= jaddress.getString("region_name");
                    provinsi= jaddress.getString("province_name");
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

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();


            Intent i = new Intent(LoginActivity.this,ProfileActivity.class);
            i.putExtra("key_token",mtoken);
            i.putExtra("key_pict",pict);
            i.putExtra("key_username",username);
            i.putExtra("key_email",email);
            i.putExtra("key_fullname",fullname);
            i.putExtra("key_kecamatan",kecamatan);
            i.putExtra("key_region",region);
            i.putExtra("key_provinsi",provinsi);

            startActivity(i);
        }
    }
}



