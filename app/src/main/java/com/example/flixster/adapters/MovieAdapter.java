package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context mcontext;
    List<Movie> mmovies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.mcontext = context;
        this.mmovies = movies;
    }

    // Usually involves inflating a layout XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(mcontext).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Populating the movie data into the Item Holder
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        Movie movie = mmovies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return mmovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

            // In order to acknowledge when a movie was clicked
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            int placeholder;
            int width;
            int height;
            if (mcontext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                placeholder = R.drawable.flicks_backdrop_placeholder;
                width = 1600;
                height = 900;
            }
            else {
                imageUrl = movie.getPosterPath();
                placeholder = R.drawable.flicks_movie_placeholder;
                width = 200;
                height = 300;
            }
            Glide.with(mcontext)
                .load(imageUrl)
                //.centerCrop()
                //.transform(new RoundedCornersTransformation(30, 0))
                .placeholder(placeholder)
                //.override(width,height)
                .into(ivPoster);
        }

        @Override
        public void onClick(View v) {
            // Get the movie that was clicked
            int position = getAdapterPosition();
            // Ensures a valid position
            if (position != RecyclerView.NO_POSITION) {
                Movie movie = mmovies.get(position);
                // Send data to the Movie Details Activity
                Intent intent = new Intent(mcontext, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // Shows the Movie Details page
                mcontext.startActivity(intent);
            }
        }
    }
}
