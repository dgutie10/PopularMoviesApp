package com.example.android.popularmoviesapp;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by diegog on 3/14/2017.
 */

public class MainFragment extends Fragment {

    private MoviesAdapter mMoviesAdapter;
    private ArrayList<? extends Movies> mMovieList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_main, container, false);


        mMoviesAdapter = new MoviesAdapter(getActivity(), new ArrayList<Movies>());

        GridView gridView = (GridView) rootView.findViewById(R.id.main_grid);
        gridView.setAdapter(mMoviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movies currentMovie = mMoviesAdapter.getItem(i);

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("currentMovie", (Parcelable) currentMovie);
                startActivity(intent);
            }
        });

        return  rootView;
    }

    private void updateMovieList () {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        moviesTask.execute(sorting);
    }

    @Override
    public void onStart() {
        updateMovieList();
        super.onStart();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movies>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        //Please enter your own API key
        //TODO: Enter API Key
        private static final String API_KEY="";

        private static final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie/";


        @Override
        protected ArrayList<Movies> doInBackground(String... strings) {
            HttpURLConnection urlConnection  = null;
            BufferedReader reader = null;
            String jsonStr = null;


            final String API_PARAM = "api_key";

            try {
                String uriString = MOVIE_BASE_URL+strings[0]+"?";
                Uri builUri = Uri.parse(uriString).buildUpon().appendQueryParameter(API_PARAM, API_KEY).build();


                URL url  = new URL(builUri.toString());


                //Create request
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read data
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine())!= null){
                    buffer.append(line+"\n");
                }

                if (buffer.length() == 0) return  null;

                jsonStr = buffer.toString();

            } catch (IOException e){
                Log.e(LOG_TAG, "Error ", e);
            } finally {
                if (urlConnection != null) urlConnection.disconnect();

                if(reader != null) {
                    try{
                        reader.close();
                    }catch (final IOException e ){
                        Log.e(LOG_TAG, "Error closing stream :", e);
                    }
                }
            }
            try {
                return getMoviesFromJson(jsonStr);
            }catch (JSONException e ){
                Log.e(LOG_TAG,e.getMessage(), e);
                e.printStackTrace();
            }
            return  null;
        }

        private ArrayList<Movies> getMoviesFromJson(String jsonString) throws JSONException{
            final String MOVIE_RESULTS = "results";
            final String MOVIE_ID = "id";
            final String MOVIE_TITLE = "original_title";
            final String MOVIE_POSTER = "poster_path";
            final String MOVIE_OVERVIEW  = "overview";
            final String MOVIE_RELEASE = "release_date";
            final String MOVIE_RATING = "vote_average";
            ArrayList<Movies> movies = new ArrayList<Movies>();

            try {
                JSONObject movieObject = new JSONObject(jsonString);
                JSONArray moviesArray = movieObject.getJSONArray(MOVIE_RESULTS);

                for (int i = 0; i < moviesArray.length(); i++) {
                    String poster;
                    String overview;
                    String title;
                    String release;
                    String rating;

                    int id;

                    JSONObject currentMovie = moviesArray.getJSONObject(i);

                    id = currentMovie.getInt(MOVIE_ID);

                    overview = currentMovie.getString(MOVIE_OVERVIEW);
                    poster = currentMovie.getString(MOVIE_POSTER);
                    title = currentMovie.getString(MOVIE_TITLE);
                    release = currentMovie.getString(MOVIE_RELEASE);
                    rating = currentMovie.getString(MOVIE_RATING);
                    movies.add(new Movies(title, overview, poster, id,release,rating));
                }
            }catch (JSONException e){
                Log.e(LOG_TAG, "Problem parsing the movies JSON string.");
            }

            return movies;

        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            mMoviesAdapter.clear();
            if (movies != null && !movies.isEmpty()) {
                mMoviesAdapter.addAll(movies);
                mMovieList = movies;
            }

        }
    }
}
