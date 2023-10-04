package com.chris.firebaseauth.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chris.firebaseauth.R;
import com.chris.firebaseauth.scan.models.History;

import java.util.List;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder> {

    private List<History> barcodeList;
    private OnItemClickListener listener;

    public BarcodeAdapter(List<History> barcodeList, OnItemClickListener listener) {
        this.barcodeList = barcodeList;
        this.listener = listener;
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
        History product = barcodeList.get(position);
        holder.barcodeTV.setText(product.getBarcode());
        holder.productNameTV.setText(product.getProductName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onItemClick(product.getBarcode());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return barcodeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView barcodeTV;
        private TextView productNameTV;
        public ViewHolder(View view) {
            super(view);
            barcodeTV = view.findViewById(R.id.barcodeTV);
            productNameTV = view.findViewById(R.id.productNameTV);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String barcode);
    }
}
