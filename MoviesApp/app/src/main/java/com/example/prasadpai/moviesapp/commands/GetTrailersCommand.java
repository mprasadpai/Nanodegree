package com.example.prasadpai.moviesapp.commands;

import android.os.Handler;

import com.example.prasadpai.moviesapp.models.Film;
import com.example.prasadpai.moviesapp.models.GetFilmsResponse;
import com.example.prasadpai.moviesapp.models.GetTrailerResponse;
import com.example.prasadpai.moviesapp.models.Trailer;
import com.example.prasadpai.moviesapp.network.AsyncCommand;
import com.example.prasadpai.moviesapp.network.CommandExecutionError;
import com.example.prasadpai.moviesapp.network.MovieDBService;
import com.example.prasadpai.moviesapp.network.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by prasadpai on 26/02/16.
 */
public class GetTrailersCommand implements AsyncCommand {


    private int id;

    public GetTrailersCommand(int id) {
        this.id =id;
    }

    private void runCommand(int id, final Callback callback) {
        MovieDBService movieDBServiceService = ServiceGenerator.createService(MovieDBService.class);

        movieDBServiceService.getTrailers(id, ServiceGenerator.MOVIES_API_KEY, new retrofit.Callback<GetTrailerResponse>() {
            @Override
            public void success(GetTrailerResponse getTrailerResponse, Response response) {


                List<Trailer> trailerList = getTrailerResponse.getResults();


                callback.success(0, trailerList);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(-1, null, new CommandExecutionError(error.getMessage()));
            }
        });
    }

    @Override
    public void execute(Handler handler, Callback callback) {
        runCommand(id, callback);
    }
}

