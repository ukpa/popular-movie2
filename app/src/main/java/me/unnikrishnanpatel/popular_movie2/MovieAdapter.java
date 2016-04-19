package me.unnikrishnanpatel.popular_movie2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by unnikrishnanpatel on 18/04/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>  {
    private ArrayList<HashMap<String,String>> mDataset;
    public MovieCallback delegate =null;


    public MovieAdapter (ArrayList<HashMap<String,String>> myDataset, MovieCallback m) {
        mDataset = myDataset;
        delegate = m;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView)v.findViewById(R.id.poster);
        }
    }
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movieposterlyout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, final int position) {
        final Context context = holder.mImageView.getContext();

        Picasso picasso = PabloPicasso.with(context);

        picasso.load(mDataset.get(position).get("poster_path")).into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delegate.sendMovie(mDataset.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mDataset==null){
            return 0;
        }
        return mDataset.size();
    }


}


