package com.kravchenko.apps.gooddeed.screen.adapter.profile;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.Review;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> reviewArrayList;

    public ReviewAdapter(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseFirestore.getInstance().collection("users")
                .document(reviewArrayList.get(position).getReviewerId()).get().addOnSuccessListener(documentSnapshot -> {
            Glide.with(holder.itemView).load(Uri.parse(String.valueOf(documentSnapshot.get("imageUrl")))).into(holder.circleImageView);
            String name = documentSnapshot.get("firstName") + " " + documentSnapshot.get("lastName");
            if (name.trim().equals("")) {
                holder.reviewersName.setText(R.string.no_name);
            } else holder.reviewersName.setText(name);
        });
        holder.reviewText.setText(reviewArrayList.get(position).getComment());
        holder.ratingBar.setRating((float) reviewArrayList.get(position).getRate());
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView reviewersName, reviewText;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.imageview_reviewer_photo);
            reviewersName = itemView.findViewById(R.id.tv_reviewers_name);
            reviewText = itemView.findViewById(R.id.tv_review_text);
            ratingBar = itemView.findViewById(R.id.review_rating_bar);
        }
    }
}
