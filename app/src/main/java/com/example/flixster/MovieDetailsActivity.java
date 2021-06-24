package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie mmovie;
    TextView mtvTitle;
    TextView mtvOverview;
    RatingBar mrbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mtvTitle = (TextView) findViewById(R.id.tvTitle);
        mtvOverview = (TextView) findViewById(R.id.tvOverview);
        mrbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        // Unwrap the movie data passed through the intent
        mmovie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", mmovie.getTitle()));

        mtvTitle.setText(mmovie.getTitle());
        mtvOverview.setText(mmovie.getOverview());

        float voteAverage = mmovie.getVoteAverage().floatValue();
        mrbVoteAverage.setRating(voteAverage / 2.0f);
    }
}