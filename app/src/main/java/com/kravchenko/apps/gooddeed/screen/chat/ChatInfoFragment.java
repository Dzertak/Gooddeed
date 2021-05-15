package com.kravchenko.apps.gooddeed.screen.chat;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.kravchenko.apps.gooddeed.R;
import com.kravchenko.apps.gooddeed.database.entity.FirestoreUser;
import com.kravchenko.apps.gooddeed.database.entity.PersonWrapper;
import com.kravchenko.apps.gooddeed.databinding.FragmentChatInfoBinding;
import com.kravchenko.apps.gooddeed.screen.BaseFragment;
import com.kravchenko.apps.gooddeed.screen.adapter.message.ExecutorsAdapter;
import com.kravchenko.apps.gooddeed.screen.adapter.message.MembersAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatInfoFragment extends BaseFragment {

    private FragmentChatInfoBinding binding;
    private String currentInitiativeId;
    private MembersAdapter membersAdapter;
    private int executorsCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void clear() {
        requireActivity().getViewModelStore().clear();
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
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(t -> {
            clear();
            getNavController().navigateUp();
        });
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());

        if (getArguments() != null) {
            currentInitiativeId = getArguments().getString("initiative_id");
        }
        if (currentInitiativeId != null) {
            FirebaseDatabase.getInstance().getReference("chats")
                    .child(currentInitiativeId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    binding.tvChatTitle.setText(String.valueOf(snapshot.child("chatRoomName").getValue()));
                    if (!String.valueOf(snapshot.child("imageUrl").getValue()).equals("default") &&
                            snapshot.child("imageUrl").getValue() != null) {
                        Glide.with(view).load(Uri.parse(String.valueOf(snapshot.child("imageUrl").getValue()))).into(binding.circleImageViewAvatar);
                    }
                    String peopleInChatCount = getString(R.string.people_in_chat) + " " + snapshot.child("members").getChildrenCount();
                    binding.tvPeopleInChat.setText(peopleInChatCount);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            FirebaseDatabase.getInstance().getReference("chats")
                    .child(currentInitiativeId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot chatSnapshot) {
                    FirebaseFirestore.getInstance().collection("initiatives").document(currentInitiativeId)
                            .get().addOnSuccessListener(initiativeSnapshot -> {
                        String owner = String.valueOf(initiativeSnapshot.get("initiativeUserId"));
                        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        boolean isUserAnOwner = userId.equals(owner);
                        if (isUserAnOwner) {
                            binding.buttonLeaveChat.setVisibility(View.GONE);
                        }
                        binding.recyclerExecutors.setLayoutManager(new LinearLayoutManager(getContext()));
                        FirebaseFirestore.getInstance().collection("initiatives")
                                .document(currentInitiativeId).collection("executors")
                                .get().addOnSuccessListener(executorsSnapshots -> {
                            if (executorsSnapshots != null) {
                                binding.recyclerExecutors.setVisibility(View.VISIBLE);
                                ArrayList<String> executorsIds = new ArrayList<>();
                                ArrayList<PersonWrapper> executors = new ArrayList<>();
                                for (QueryDocumentSnapshot executor : executorsSnapshots) {
                                    executorsIds.add(String.valueOf(executor.get("executorId")));
                                }
                                if (executorsIds.size() != 0) {
                                    executorsCount = executorsIds.size();
                                    for (String executorId : executorsIds) {
                                        FirebaseFirestore.getInstance().collection("users")
                                                .document(executorId).get().addOnSuccessListener(documentSnapshot -> {
                                            String personName = (documentSnapshot.get("firstName") + " " + documentSnapshot.get("lastName")).trim();
                                            if (personName.equals(""))
                                                personName = getString(R.string.no_name);
                                            PersonWrapper personWrapper = new PersonWrapper((String) documentSnapshot.get("imageUrl"), personName, executorId);
                                            executors.add(personWrapper);
                                            ExecutorsAdapter executorsAdapter = new ExecutorsAdapter(executors, isUserAnOwner, getActivity(), currentInitiativeId);
                                            binding.recyclerExecutors.setAdapter(executorsAdapter);
                                        });
                                    }
                                }
                            } else {
                                executorsCount = 0;
                                String text = getString(R.string.executors) + " " + getString(R.string.absent);
                                binding.tvExecutorsTitle.setText(text);
                                binding.recyclerExecutors.setVisibility(View.GONE);
                            }
                        });
                        binding.recyclerChatMembers.setHasFixedSize(true);
                        binding.recyclerChatMembers.setLayoutManager(new LinearLayoutManager(getContext()));
                        if (chatSnapshot.child("members").getValue() != null) {
                            ArrayList<String> membersIds = (ArrayList<String>) chatSnapshot.child("members").getValue();
                            ArrayList<PersonWrapper> members = new ArrayList<>();
                            if (membersIds != null) {
                                for (String memberId : membersIds) {
                                    FirebaseFirestore.getInstance().collection("users")
                                            .document(memberId).get().addOnSuccessListener(documentSnapshot -> {
                                        String personName = (documentSnapshot.get("firstName") + " "
                                                + documentSnapshot.get("lastName")).trim();
                                        if (personName.equals(""))
                                            personName = getString(R.string.no_name);
                                        PersonWrapper personWrapper = new PersonWrapper(
                                                (String) documentSnapshot.get("imageUrl"), personName, memberId);
                                        members.add(personWrapper);
                                        membersAdapter = new MembersAdapter(members, isUserAnOwner,
                                                getActivity(), currentInitiativeId, executorsCount,
                                                String.valueOf(initiativeSnapshot.get("type")));
                                        binding.recyclerChatMembers.setAdapter(membersAdapter);
                                    });
                                }
                            }
                        } else {
                            String text = getString(R.string.chat_members) + " " + getString(R.string.absent);
                            binding.tvChatMembersTitle.setText(text);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        binding.buttonLeaveChat.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
                userRef.get().addOnSuccessListener(documentSnapshot -> {
                    FirestoreUser firestoreUser = documentSnapshot.toObject(FirestoreUser.class);
                    List<String> chats = new ArrayList<>();
                    if (firestoreUser != null && firestoreUser.getChats() != null) {
                        chats = firestoreUser.getChats();
                    }
                    if (chats.contains(currentInitiativeId)) {
                        chats.remove(currentInitiativeId);
                        Map<String, Object> map = new HashMap<>();
                        map.put("chats", chats);
                        userRef.update(map);
                    }

                    ArrayList<String> membersList = new ArrayList<>();
                    DatabaseReference membersRef = FirebaseDatabase.getInstance().getReference("chats").child(currentInitiativeId)
                            .child("members");
                    membersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot member : snapshot.getChildren()) {
                                membersList.add(String.valueOf(member.getValue()));
                            }
                            if (membersList.contains(userId)) {
                                membersList.remove(userId);
                                membersRef.setValue(membersList);
                            }
                            Navigation.findNavController(v).navigate(R.id.action_chatInfoFragment_to_mainFragment);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                });
            }
        });
    }
}