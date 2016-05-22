package com.example.annisaartijayanti.jsonarray;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Muhammad Kholil A A on 5/17/2016.
 */
public class TambahProduk {
    private static final String url = "http://qiscusinterview.herokuapp.com/products";

    public static Call inputProduk(String nama, String  harga, String diskripsi){
        String input = url+"?product[name]="+nama+"&product[price]="+harga+"&product[description]="+diskripsi;
//        String total = Double.toString(harga);
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                /*.add("name", nama)
                .add("price", harga)
                .add("description", diskripsi)*/
                .build();
        Request request = new Request.Builder()
                .url(input)
                .post(formBody)
                .build();

        System.out.println(request.url().toString());
//        System.out.print(url);
        return client.newCall(request);
    }
}
