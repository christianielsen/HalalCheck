package com.chris.firebaseauth.map;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chris.firebaseauth.R;
import com.chris.firebaseauth.map.models.NearbyPlaces;

import java.util.List;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {
    List<NearbyPlaces.Result> results;

    public ResultsAdapter(List<NearbyPlaces.Result> results) {
        this.results = results;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setResultsList(List<NearbyPlaces.Result> resultsList) {
        this.results = resultsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        NearbyPlaces.Result result = results.get(position);
        String rName = null;
        if (result.getName() != null) {
            rName = result.getName().toString();
        }
        holder.name.setText(rName);
        String address = null;
        if (result.getVicinity() != null) {
            address = result.getVicinity();
        }
        holder.address.setText(address);
        String rating = null;
        if (result.getRating() != null) {
            rating = String.valueOf(result.getRating());
        }
        holder.rating.setText(rating);
        String totalRating = null;
        if (result.getUserRatingsTotal() != null) {
            totalRating = String.valueOf(result.getUserRatingsTotal());
        }
        holder.totalRating.setText(totalRating);
        String openingHours = null;
        String status = null;
        if (result.getOpeningHours() != null &&
                result.getOpeningHours().getOpenNow() != null) {
            openingHours = result.getOpeningHours().getOpenNow().toString();
            if (openingHours.equals("true")) {
                status = "Open";
            } else {
                status = "Closed";
            }
        } else {
            status = "No data";
        }
        holder.isOpen.setText(status);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, address, rating, totalRating, isOpen;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            rating = itemView.findViewById(R.id.rating);
            totalRating = itemView.findViewById(R.id.totalRating);
            isOpen = itemView.findViewById(R.id.isOpen);
        }
    }
}
