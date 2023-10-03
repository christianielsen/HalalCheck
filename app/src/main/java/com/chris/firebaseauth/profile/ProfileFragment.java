package com.chris.firebaseauth.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.RadioAccessSpecifier;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chris.firebaseauth.auth.Login;
import com.chris.firebaseauth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    TextView textView;
    Button button;
    FirebaseUser user;
    RecyclerView recyclerView;
    BarcodeAdapter adapter;
    List<String> barcodeList = new ArrayList<>();


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        button = view.findViewById(R.id.logout);
//        textView = view.findViewById(R.id.user_details);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView = view.findViewById(R.id.barcodeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BarcodeAdapter(barcodeList);
        recyclerView.setAdapter(adapter);

        if (user == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            getActivity().finish();
        } else {
//            textView.setText("Email: " + user.getEmail() + "Uid: " + user.getUid() + "ProviderData: " + user.getProviderData());
            fetchBarcodeData();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void fetchBarcodeData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();

        CollectionReference historyRef = db.collection("users").document(userId).collection("history");
        historyRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                barcodeList.clear();
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String barcode = documentSnapshot.getId();
                    barcodeList.add(barcode);
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.i("AJB", "Error");
            }
        });
    }
}