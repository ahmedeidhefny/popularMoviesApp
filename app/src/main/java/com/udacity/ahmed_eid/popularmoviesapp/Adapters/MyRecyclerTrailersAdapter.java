package com.udacity.ahmed_eid.popularmoviesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.ahmed_eid.popularmoviesapp.R;

import java.util.ArrayList;

public class MyRecyclerTrailersAdapter extends RecyclerView.Adapter<MyRecyclerTrailersAdapter.MyViewHolder> {

    private Context myContext;
    ArrayList<String> trailerTitles ;
    ArrayList<String> trailerImages ;



    ArrayList<String> keys ;

    public MyRecyclerTrailersAdapter(Context myContext, ArrayList<String> trailerTitles, ArrayList<String> trailerImages,ArrayList<String> keys) {
        this.myContext = myContext;
        this.trailerTitles = trailerTitles;
        this.trailerImages = trailerImages;
        this.keys = keys;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView ;
        myView = LayoutInflater.from(myContext)
                .inflate(R.layout.recycler_traliers,parent,false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.trailerName.setText(trailerTitles.get(position));
        Picasso.with(myContext)
                .load(trailerImages.get(position))
                .placeholder(R.drawable.loadimage)
                .error(R.drawable.error)
                .into(holder.trailerImage);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/watch?v="+keys.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                myContext.startActivity(intent);
            }
        });
        //Determine if the device is a smartphone or tablet?
        //Determine if the device is a smartphone or tablet?
        boolean isTabletVar = isTablet(myContext);
        if (isTabletVar == true) {

        } else {

        }

    }

    public Boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public int getItemCount() {
        return trailerTitles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView trailerImage ;
        TextView trailerName ;
        LinearLayout rootLayout ;

        public MyViewHolder(View itemView) {
            super(itemView);
            trailerImage = itemView.findViewById(R.id.trailerImage);
            trailerName = itemView.findViewById(R.id.trailerTitle);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }

    }
}

