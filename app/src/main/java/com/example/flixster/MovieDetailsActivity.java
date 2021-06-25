package com.example.flixster;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String VIDEO_PLAYING = "https://api.themoviedb.org/3/movie/%s/videos?api_key=b9b14aaa0ff8435ddcebc920731fd0ba";
    //This tag will be used to log data
    public static final String TAG = "MovieDetailsActivity";

    Movie mmovie;

    TextView mtvTitle;
    TextView mtvOverview;
    RatingBar mrbVoteAverage;
    TextView mreleaseDate;
    ImageView mimPoster;

    String mvideoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        mtvTitle = (TextView) findViewById(R.id.tvTitle);
        mtvOverview = (TextView) findViewById(R.id.tvOverview);
        mrbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        mreleaseDate = (TextView) findViewById(R.id.tvreleasedate);
        mimPoster = (ImageView) findViewById(R.id.imPoster);

        // Unwrap the movie data passed through the intent
        mmovie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", mmovie.getTitle()));

        mtvTitle.setText(mmovie.getTitle());
        mtvOverview.setText(mmovie.getOverview());

        float voteAverage = mmovie.getVoteAverage().floatValue();
        mrbVoteAverage.setRating(voteAverage / 2.0f);

        mreleaseDate.setText(mmovie.getReleaseDate());

        populatePoster();

        getVideoId();

    }

    public void getVideoId() {
        AsyncHttpClient client = new AsyncHttpClient();
        // Calls the API to get the youtube key using the movie id
        client.get(String.format(VIDEO_PLAYING, mmovie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    Log.d(TAG, jsonObject.toString());
                    JSONArray results = jsonObject.getJSONArray("results");
                    if (results.length() > 0) {
                        String youtube_key = (String) results.getJSONObject(0).get("key");
                        Log.d(TAG, youtube_key);
                        // Updates videoId
                        mvideoId = youtube_key;
                    }
                    /*Log.i(TAG, "Results" + results.toString());
                    mmovies.addAll(Movie.fromJSONArray(results));
                    mmovieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies" + mmovies.size());*/

                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    public void populatePoster() {
        String imageUrl;
        int placeholder;
        int width;
        int height;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUrl = mmovie.getBackdropPath();
            placeholder = R.drawable.flicks_backdrop_placeholder;
            width = 1600;
            height = 900;
        }
        else {
            imageUrl = mmovie.getPosterPath();
            placeholder = R.drawable.flicks_movie_placeholder;
            width = 600;
            height = 900;
        }

        Glide.with(this)
                .load(imageUrl)
                .centerCrop()
                .transform(new RoundedCornersTransformation(30, 20))
                .placeholder(placeholder)
                .override(width,height)
                .into(mimPoster);
    }

    public void onClick(View v) {
        Log.d(TAG, "Video Id" + mvideoId);
        if (mvideoId != null) {
            Intent intent = new Intent(this, MovieTrailerActivity.class);
            intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(mvideoId));
            this.startActivity(intent);
        }
    }
}