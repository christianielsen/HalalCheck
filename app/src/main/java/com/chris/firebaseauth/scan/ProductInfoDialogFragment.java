package com.chris.firebaseauth.scan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.DialogFragment;

import com.chris.firebaseauth.R;

public class ProductInfoDialogFragment extends DialogFragment {
    private String productName;
    ImageButton backButton;

    public ProductInfoDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    // Create a static method to create and show the dialog
    public static ProductInfoDialogFragment newInstance(String productName) {
        ProductInfoDialogFragment fragment = new ProductInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("productName", productName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_info_popup, container, false);
        TextView productNameTextView = view.findViewById(R.id.productNameTextView);

        // Set the product information to the TextViews or UI elements here
        productNameTextView.setText(productName);

        backButton = view.findViewById(R.id.sa_dialog_close_btn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productName = getArguments().getString("productName");
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
    }


}
