package com.example.annisaartijayanti.jsonarray;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.annisaartijayanti.jsonarray.models.ProdukModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private ListView _lcProduk;
    private Button _btnTambah;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

//        new JSONTask().execute("http://qiscusinterview.herokuapp.com/products");
        _lcProduk = (ListView) findViewById(R.id.lvProduk);
        _btnTambah = (Button) findViewById(R.id.btnTambah);

        _btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, kirimData.class);
                startActivity(i);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public class JSONTask extends AsyncTask<String, String, List<ProdukModel>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<ProdukModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine())!= null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONArray parentArray = new JSONArray(finalJson);

                List<ProdukModel> produkModelList = new ArrayList<>();

                for (int i =0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    ProdukModel produkModel = new ProdukModel();
                    produkModel.setName(finalObject.getString("name"));
                    produkModel.setPrice(finalObject.getDouble("price"));
                    produkModel.setDescription(finalObject.getString("description"));
                    produkModelList.add(produkModel);
                }

                return produkModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ProdukModel> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            ProdukAdapter adapter = new ProdukAdapter(getApplicationContext(), R.layout.row, result);
            _lcProduk.setAdapter(adapter);
        }
    }

    public class ProdukAdapter extends ArrayAdapter{

        private List<ProdukModel> produkModelList;
        private int resource;
        private LayoutInflater inflater;
        public ProdukAdapter(Context context, int resource, List<ProdukModel> objects) {
            super(context, resource, objects);
            produkModelList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = inflater.inflate(R.layout.row, null);
            }

            TextView _lblNProduk;
            TextView _lblHProduk;
            TextView _lblDProduk;

            _lblNProduk = (TextView) convertView.findViewById(R.id.lblNProduk);
            _lblHProduk = (TextView) convertView.findViewById(R.id.lblHProduk);
            _lblDProduk = (TextView) convertView.findViewById(R.id.lblDProduk);

            _lblNProduk.setText(produkModelList.get(position).getName());
            _lblHProduk.setText("Rp. " + produkModelList.get(position).getPrice());
            _lblDProduk.setText(produkModelList.get(position).getDescription());

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new JSONTask().execute("http://qiscusinterview.herokuapp.com/products");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
