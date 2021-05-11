package com.kravchenko.apps.gooddeed.screen.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.PersonWrapper;
import com.kravchenko.apps.gooddeed.databinding.FragmentChatInfoBinding;
import com.kravchenko.apps.gooddeed.screen.adapter.message.ExecutorsAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MembersAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class ChatInfoFragment extends Fragment {

    private FragmentChatInfoBinding binding;
    private String currentInitiativeId;
    private MembersAdapter membersAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        if (getArguments() != null) {
            currentInitiativeId = getArguments().getString("initiative_id");
        }
        if (currentInitiativeId != null) {
            FirebaseDatabase.getInstance().getReference("Initiatives")
                    .child(currentInitiativeId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String owner = String.valueOf(snapshot.child("owner").getValue());
                    String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    boolean isUserAnOwner = userId.equals(owner);
                    if (isUserAnOwner) {binding.buttonLeaveChat.setVisibility(View.GONE);}
                    binding.tvChatTitle.setText(String.valueOf(snapshot.child("title").getValue()));
                    if (!String.valueOf(snapshot.child("imageUrl").getValue()).equals("default")) {
                        Glide.with(view).load(Uri.parse(String.valueOf(snapshot.child("imageUrl").getValue()))).into(binding.circleImageViewAvatar);
                    }
                    String peopleInChat = getString(R.string.people_in_chat) + " " + snapshot.child("chat").child("members").getChildrenCount();
                    binding.tvPeopleInChat.setText(peopleInChat);

                    binding.recyclerExecutors.setHasFixedSize(true);
                    binding.recyclerExecutors.setLayoutManager(new LinearLayoutManager(getContext()));
                    if (snapshot.child("executors").getValue() != null) {
                        ArrayList<String> executorsIds = (ArrayList<String>) snapshot.child("executors").getValue();
                        ArrayList<PersonWrapper> executors = new ArrayList<>();
                        if (executorsIds != null) {
                            for (String executorId : executorsIds) {
                                FirebaseFirestore.getInstance().collection("users")
                                        .document(executorId).get().addOnSuccessListener(documentSnapshot -> {
                                    String personName = (documentSnapshot.get("firstName") + " " + documentSnapshot.get("lastName")).trim();
                                    if (personName.equals("")) personName = getString(R.string.no_name);
                                    PersonWrapper personWrapper = new PersonWrapper((String) documentSnapshot.get("imageUrl"), personName,executorId);
                                    executors.add(personWrapper);
                                    ExecutorsAdapter executorsAdapter = new ExecutorsAdapter(executors,isUserAnOwner,getActivity(),currentInitiativeId);
                                    binding.recyclerExecutors.setAdapter(executorsAdapter);
                                });
                            }
                        }
                    } else {
                        String text = getString(R.string.executors) + " " + getString(R.string.absent);
                        binding.tvExecutorsTitle.setText(text);
                    }
                    binding.recyclerChatMembers.setHasFixedSize(true);
                    binding.recyclerChatMembers.setLayoutManager(new LinearLayoutManager(getContext()));
                    if (snapshot.child("chat").child("members").getValue() != null) {
                        ArrayList<String> membersIds = (ArrayList<String>) snapshot.child("chat").child("members").getValue();
                        ArrayList<PersonWrapper> members = new ArrayList<>();
                        if (membersIds != null) {
                            for (String memberId : membersIds) {
                                FirebaseFirestore.getInstance().collection("users")
                                        .document(memberId).get().addOnSuccessListener(documentSnapshot -> {
                                    String personName = (documentSnapshot.get("firstName") + " " + documentSnapshot.get("lastName")).trim();
                                    if (personName.equals("")) personName = getString(R.string.no_name);
                                    PersonWrapper personWrapper = new PersonWrapper((String) documentSnapshot.get("imageUrl"), personName,memberId);
                                    members.add(personWrapper);
                                    membersAdapter = new MembersAdapter(members,isUserAnOwner,getActivity(),currentInitiativeId);
                                    binding.recyclerChatMembers.setAdapter(membersAdapter);
                                });
                            }
                        }
                    } else {
                        String text = getString(R.string.chat_members) + " " + getString(R.string.absent);
                        binding.tvChatMembersTitle.setText(text);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        binding.buttonLeaveChat.setOnClickListener(v -> {
            //TODO
        });
    }
}