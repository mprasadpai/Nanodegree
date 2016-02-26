package com.example.prasadpai.moviesapp.commands;

import android.os.Handler;

import com.example.prasadpai.moviesapp.models.Film;
import com.example.prasadpai.moviesapp.models.GetFilmsResponse;
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
public class GetMoviesCommand implements AsyncCommand {
    private final String sortBy;

    public GetMoviesCommand(String sortBy) {
        this.sortBy = sortBy;
    }

    private void runCommand(String sortby, final Callback callback) {
        MovieDBService movieDBServiceService = ServiceGenerator.createService(MovieDBService.class);

        movieDBServiceService.getMovies(ServiceGenerator.MOVIES_API_KEY, sortBy, new retrofit.Callback<GetFilmsResponse>() {
            @Override
            public void success(GetFilmsResponse getFilmsResponse, Response response) {

                List<Film> filmDtoList = getFilmsResponse.getResults();
                callback.success(0, filmDtoList);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(-1, null, new CommandExecutionError(error.getMessage()));
            }
        });
    }

    @Override
    public void execute(Handler handler, Callback callback) {
        runCommand(sortBy, callback);
    }
}

