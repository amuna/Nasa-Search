package com.space.search.Model.Repository;

import android.util.Log;

import com.space.search.Model.Response;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ResponseRepository {
    private final static String TAG = "ResponseRepository";
    private final String BASE_URL = "https://images-api.nasa.gov/";
    private Retrofit retrofit;
    private ResponseApiCall responseApiCall;

    public ResponseRepository() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        Log.i(TAG, "Retrofit Created");
        responseApiCall = retrofit.create(ResponseApiCall.class);
    }

    public Single<Response> getQuery(String q, int page) {
        Single<Response> responseObservable = responseApiCall.getResponse(q, page);
        Log.d(TAG, "ApiCall -> Query: " + q + " Page: " + page);
        return responseObservable;
    }
}
