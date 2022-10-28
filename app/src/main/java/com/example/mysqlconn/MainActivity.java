package com.example.mysqlconn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LOGO = "logo";
    private static final String TAG_CATGRY = "catgry";

    JSONArray brands = null;

    ArrayList<HashMap<String, String>> brandList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.listView);
        brandList = new ArrayList<>();

        getData("http://localhost/PHP_connection.php");

    }

    protected void showList() {
        try {
            JSONObject jsonObject = new JSONObject(myJSON);
            brands = jsonObject.getJSONArray(TAG_RESULTS);

            for(int i=0; i<brands.length(); i++) {
                JSONObject c = brands.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String logo = c.getString(TAG_LOGO);
                String catgry = c.getString(TAG_CATGRY);

                HashMap<String, String> map = new HashMap<>();

                map.put(TAG_ID, id);
                map.put(TAG_NAME, name);
                map.put(TAG_LOGO, logo);
                map.put(TAG_CATGRY, catgry);

                brandList.add(map);
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,
                    brandList,
                    R.layout.list_item,
                    new String[]{TAG_ID, TAG_NAME, TAG_LOGO, TAG_CATGRY},
                    new int[]{R.id.store_number, R.id.store_name, R.id.store_logo, R.id.store_catgry}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(String url) {

        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {

                String uri = strings[0];

                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuffer sb = new StringBuffer();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                myJSON = s;
                showList();
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}