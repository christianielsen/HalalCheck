package com.chris.firebaseauth.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chris.firebaseauth.R;
import com.chris.firebaseauth.databinding.FragmentResultsTabBinding;
import com.chris.firebaseauth.map.models.NearbyPlaces;

import java.util.ArrayList;
import java.util.List;

public class ResultsTab extends Fragment {

    private FragmentResultsTabBinding binding;
    private MapViewModel viewModel;
    ResultsAdapter adapter;
    RecyclerView recyclerView;
    SearchView searchView;
    List<NearbyPlaces.Result> results;

    public ResultsTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentResultsTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchView = root.findViewById(R.id.searchbar);
        recyclerView = root.findViewById(R.id.recyclerview);

        initRecyclerView();

        viewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        viewModel.getResultsData().observe(getViewLifecycleOwner(), results -> {
            this.results = results;
            adapter.setResultsList(results);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //When the user adds a letter, filter through
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return root;
    }

    private void initRecyclerView() {
        adapter = new ResultsAdapter(new ArrayList<>());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void filter(String text) {
        List<NearbyPlaces.Result> filteredList = new ArrayList<>();

        for (NearbyPlaces.Result result : results) {
            if (result.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(result);
            }
        }

        adapter.setResultsList(filteredList);
    }


}