package com.example.amit_.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.params.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private String apiUrl= "https://api.coindesk.com/v1/bpi/currentprice.json";
    Context contx;
    private final String Tag = "currentPrice";
    ProgressDialog pd;
    JSONObject json_currPrice;
    JSONObject currValueUSD;
    JSONObject currValueGBP;
    JSONObject currValueEUR;
    Button b_getprice;
    TextView et_usd,et_gbp,et_eur;
    String gbp,usd,eur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contx = this;
            new ReadJson().execute("");
        b_getprice = (Button) findViewById(R.id.btn_GetPrices);
        et_gbp = (TextView) findViewById(R.id.textView);
        et_usd = (TextView) findViewById(R.id.textView3);
        et_eur = (TextView) findViewById(R.id.textView4);

        b_getprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReadJson().execute("");
                Toast.makeText(contx, "Please wait. The updated rates are being loaded....", Toast.LENGTH_LONG).show();
            }
        });
    }


    public class ReadJson extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
                String res= "";
            try{
                json_currPrice = fetchCurrentRate();

                if(json_currPrice==null)
                {
                    Log.i(Tag,"No price found");
                }
                else
                {
                    Log.i(Tag, "Price found");
                    res = "Price found";
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s)
        {
            et_gbp.setText(gbp);
            et_usd.setText(usd);
            et_eur.setText(eur);
            //super.onPostExecute("");

        }

    }



    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public JSONObject fetchCurrentRate(){
        Log.i(Tag,"Loading current price.....");

        //pd = ProgressDialog.show(contx,"CurrentPrice","Loading current price please wait");

        JSONParser jParser = new JSONParser();
        JSONObject json = jParser.getJSONFromUrl("http://mylink/db.json");
        String str = "tmp";

        try {
            str = json.getString("disclaimer");
            JSONObject currValue = json.getJSONObject("bpi");
            currValueUSD = currValue.getJSONObject("USD");
            currValueGBP = currValue.getJSONObject("GBP");
            currValueEUR = currValue.getJSONObject("EUR");


            gbp=currValueGBP.getString("rate");
            usd = currValueUSD.getString("rate");
            eur = currValueEUR.getString("rate");


            Log.i("Gotdata",str);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    public class JSONParser {
        private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31";

        public JSONObject getJSONFromUrl(String url) {

            try {
                final HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                connection.setRequestProperty("User-Agent", USER_AGENT);

                if (connection.getResponseCode() == 200) {
                    final BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    String line = null;
                    final StringBuffer buffer = new StringBuffer(4096);
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    reader.close();

                    final JSONObject jObj = new JSONObject(buffer.toString());
                    return jObj;
                } else {
                    return null;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
