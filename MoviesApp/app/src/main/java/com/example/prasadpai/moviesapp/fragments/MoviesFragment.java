package com.example.prasadpai.moviesapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.prasadpai.moviesapp.R;
import com.example.prasadpai.moviesapp.activities.SettingsActivity;
import com.example.prasadpai.moviesapp.adapters.FilmsAdapter;
import com.example.prasadpai.moviesapp.commands.GetMoviesCommand;
import com.example.prasadpai.moviesapp.models.Film;
import com.example.prasadpai.moviesapp.network.AsyncCommand;
import com.example.prasadpai.moviesapp.network.CommandExecutionError;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by prasadpai on 25/02/16.
 */
public class MoviesFragment extends Fragment {

    private static String now_sorted_by = null;

    private String sort_by;

    @InjectView(R.id.filmsGridView)GridView filmsGridView;

    private FilmsAdapter filmsAdapter;
    private List<Film> films;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviesfragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateMovies();
    }



    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sort_by = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.populairty_key));


        if(now_sorted_by==null)
        {
            updateMovies();

        }

        else if(  sort_by.compareTo(now_sorted_by)!=0) {

            if (sort_by.compareTo(getString(R.string.populairty_key)) == 0) {
                now_sorted_by = getString(R.string.populairty_key);
            } else {
                now_sorted_by = getString(R.string.ratings_key);
            }

            updateMovies();
        }
        else
        {
            populateGridView();
        }

    }

    private void updateMovies() {

      final ProgressDialog  progressDialog = ProgressDialog.show(getContext(),"Please Wait","Fetching moveis");
      GetMoviesCommand getMoviesCommand = new GetMoviesCommand(sort_by);
      getMoviesCommand.execute(null, new AsyncCommand.Callback() {
          @Override
          public <T> void success(int statusCode, T result) {
              films = (ArrayList<Film>) result;
              progressDialog.dismiss();
              populateGridView();
          }

          @Override
          public void failure(int statusCode, String message, CommandExecutionError error) {

              progressDialog.dismiss();
              Toast.makeText(getContext(),"Server call failed", Toast.LENGTH_LONG).show();

          }
      });

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.inject(this, rootView);


        return rootView;
    }



    private void populateGridView() {

        filmsAdapter = new FilmsAdapter(getActivity(), films);
        filmsGridView.setAdapter(filmsAdapter);

    }

}
