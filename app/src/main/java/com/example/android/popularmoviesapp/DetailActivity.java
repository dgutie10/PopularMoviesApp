package com.example.android.popularmoviesapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by diegog on 3/14/2017.
 */

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new PlaceholderFragment()).commit();
        }

    }

    public static class PlaceholderFragment extends Fragment {
        private ImageView posterImage;
        private TextView overviewText;
        private TextView titleText;
        private TextView releaseText;
        private TextView ratingText;

        public PlaceholderFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("currentMovie")){
                Movies currentMovie = intent.getParcelableExtra("currentMovie");

                posterImage = (ImageView) rootView.findViewById(R.id.movie_image_detail);
                overviewText = (TextView) rootView.findViewById(R.id.overview);
                titleText = (TextView) rootView.findViewById(R.id.title);
                releaseText = (TextView) rootView.findViewById(R.id.release_date);
                ratingText = (TextView) rootView.findViewById(R.id.rating);

                titleText.setText(currentMovie.getTitle());
                Picasso.with(getContext()).load(currentMovie.getPosterLocation()).into(posterImage);
                overviewText.setText(currentMovie.getOverview());
                ratingText.setText(currentMovie.getRating());
                releaseText.setText(currentMovie.getReleaseDate());
            }

            return rootView;
        }
    }
}