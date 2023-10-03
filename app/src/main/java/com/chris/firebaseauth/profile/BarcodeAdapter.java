package com.chris.firebaseauth.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chris.firebaseauth.R;

import java.util.List;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder> {

    private List<String> barcodeList;

    public BarcodeAdapter(List<String> barcodeList) {
        this.barcodeList = barcodeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_barcode, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String barcode = barcodeList.get(position);
        holder.barcodeTV.setText(barcode);
    }

    @Override
    public int getItemCount() {
        return barcodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barcodeTV;
        public ViewHolder(View view) {
            super(view);
            barcodeTV = view.findViewById(R.id.barcodeTV);
        }
    }
}
