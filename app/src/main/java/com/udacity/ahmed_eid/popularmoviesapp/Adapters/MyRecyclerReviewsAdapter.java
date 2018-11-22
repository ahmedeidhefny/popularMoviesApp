package com.udacity.ahmed_eid.popularmoviesapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.ahmed_eid.popularmoviesapp.R;

import java.util.ArrayList;

public class MyRecyclerReviewsAdapter extends RecyclerView.Adapter<MyRecyclerReviewsAdapter.MyViewHolder> {


    private Context myContext;
    ArrayList<String> authers;
    ArrayList<String> contents;

    public MyRecyclerReviewsAdapter(Context myContext, ArrayList<String> authers, ArrayList<String> contents) {
        this.myContext = myContext;
        this.authers = authers;
        this.contents = contents;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView ;
        myView = LayoutInflater.from(myContext)
                .inflate(R.layout.item_review,parent,false);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.author.setText(authers.get(position));
        holder.content.setText(contents.get(position));

    }

    @Override
    public int getItemCount() {
        return authers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView author ;
        TextView content ;

        public MyViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
        }
    }


}