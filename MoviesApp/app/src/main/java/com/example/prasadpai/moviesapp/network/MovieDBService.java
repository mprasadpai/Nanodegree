package com.example.prasadpai.moviesapp.network;

import com.example.prasadpai.moviesapp.models.GetFilmsResponse;
import com.example.prasadpai.moviesapp.models.GetReviewResponse;
import com.example.prasadpai.moviesapp.models.GetTrailerResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by prasadpai on 26/02/16.
 */
public interface MovieDBService {

    @GET("/discover/movie")
    void getMovies(@Query("api_key") String api, @Query("sort_by") String sortby, Callback<GetFilmsResponse> callback);

    @GET("/movie/{id}/videos")
    void getTrailers(@Path("id") int id,@Query("api_key") String api, Callback<GetTrailerResponse> callback);

    @GET("/movie/{id}/reviews")
    void getReviews(@Path("id") int id,@Query("api_key") String api, Callback<GetReviewResponse> callback);


}
