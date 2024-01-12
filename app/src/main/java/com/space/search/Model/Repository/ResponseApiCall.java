package com.space.search.Model.Repository;

import com.space.search.Model.Response;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ResponseApiCall {

    @GET("search?media_type=image")
    Single<Response> getResponse(@Query("q") String searchQuery, @Query("page") int page);
}
