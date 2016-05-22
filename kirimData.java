package com.example.annisaartijayanti.jsonarray;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.annisaartijayanti.jsonarray.models.ProdukModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class kirimData extends AppCompatActivity{
    Button _btnSimpan;
    EditText _txtNProduk;
    EditText _txtHProduk;
    EditText _txtDProduk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_data);

        _btnSimpan = (Button) findViewById(R.id.btnSimpan);
        _txtNProduk = (EditText) findViewById(R.id.txtNProduk);
        _txtHProduk = (EditText) findViewById(R.id.txtHProduk);
        _txtDProduk = (EditText) findViewById(R.id.txtDProduk);

        _btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputNama = _txtNProduk.getText().toString();
                String inputHarga = _txtHProduk.getText().toString();
                String inputDiskripsi = _txtDProduk.getText().toString();
//                String inputTotal = Double.toString(inputHarga);

                if (inputNama.isEmpty()){
                    Toast.makeText(kirimData.this, "Nama Produk harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (inputHarga.isEmpty()){
                    Toast.makeText(kirimData.this, "Harga Produk harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (inputDiskripsi.isEmpty()){
                    Toast.makeText(kirimData.this, "Diskripsi harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                TambahProduk.inputProduk(inputNama, inputHarga, inputDiskripsi).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String jsonString = response.body().toString();
                        boolean inputBerhasil = false;
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            inputBerhasil = jsonObject.getBoolean("succeeded");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (inputBerhasil){
                            Intent intent = new Intent(kirimData.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}
