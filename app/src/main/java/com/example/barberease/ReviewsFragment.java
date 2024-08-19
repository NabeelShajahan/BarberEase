package com.example.barberease;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {

    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviewList;
    private DatabaseReference databaseReference;
    private String barberId;
    private TextView noReviewsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reviews_fragment, container, false);
        reviewsRecyclerView = view.findViewById(R.id.reviews_recycler_view);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noReviewsTextView = view.findViewById(R.id.no_reviews_text_view);
        reviewList = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(reviewList);
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("barbers");
        barberId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadReviewsData();

        return view;
    }

    private void loadReviewsData() {
        databaseReference.child(barberId).child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                        Review review = reviewSnapshot.getValue(Review.class);
                        reviewList.add(review);
                    }
                }
                reviewsAdapter.notifyDataSetChanged();
                updateNoReviewsMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void updateNoReviewsMessage() {
        if (reviewList.isEmpty()) {
            noReviewsTextView.setVisibility(View.VISIBLE);
            reviewsRecyclerView.setVisibility(View.GONE);
        } else {
            noReviewsTextView.setVisibility(View.GONE);
            reviewsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
