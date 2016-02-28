package com.example.prasadpai.moviesapp.network;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by prasadpai on 26/02/16.
 */
public class ServiceGenerator {


    public final static String MOVIES_API_KEY = "ADD_YOUR_OWN_KEY";
    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}