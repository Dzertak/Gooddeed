package com.kravchenko.apps.gooddeed.screen.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.databinding.FragmentRatingBinding;

import java.util.HashMap;
import java.util.Map;

public class RatingFragment extends Fragment {

    private FragmentRatingBinding binding;
    private String personId;
    private boolean ratingIsTouched;
    private String initiativeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRatingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingIsTouched = true);
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        if (getArguments() != null) {
            personId = getArguments().getString("person_id");
            initiativeId = getArguments().getString("initiative_id");
        }
        if (personId != null && initiativeId != null) {
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(personId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                String personName = (documentSnapshot.get("firstName") + " " + documentSnapshot.get("lastName")).trim();
                if (personName.equals("")) personName = getString(R.string.no_name);
                binding.tvName.setText(personName);
                if (!(documentSnapshot.get("imageUrl") + "").equals("")
                        && !(documentSnapshot.get("imageUrl") + "").equals("default"))
                    Glide.with(view).load(Uri.parse(documentSnapshot.get("imageUrl") + "")).into(binding.circleImageViewAvatar);
            });
            binding.btnRate.setOnClickListener(v -> {
                if (ratingIsTouched) {
                    Map<String, Object> rating = new HashMap<>();
                    rating.put("rate", binding.ratingBar.getRating());
                    rating.put("initiativeId", initiativeId);
                    rating.put("comment", binding.etComment.getText().toString());
                    FirebaseFirestore.getInstance().collection("users").document(personId)
                            .collection("ratings").add(rating)
                            .addOnSuccessListener(documentReference -> requireActivity().onBackPressed());
                }
            });
        }
    }
}