package com.chris.firebaseauth.history;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chris.firebaseauth.APICall;
import com.chris.firebaseauth.auth.Login;
import com.chris.firebaseauth.R;
import com.chris.firebaseauth.scan.models.History;
import com.chris.firebaseauth.scan.models.Product;
import com.chris.firebaseauth.scan.models.ProductResponse;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryFragment extends Fragment implements BarcodeAdapter.OnItemClickListener {

    private FirebaseAuth auth;
    private TextView productTitleTV, productIngredientTV, halalStatusTV;
    private ImageView productIV;
    private Button button;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private BarcodeAdapter adapter;
    private List<History> barcodeList = new ArrayList<>();


    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        button = view.findViewById(R.id.logout);
        productTitleTV = view.findViewById(R.id.productTitleTV2);
        productIngredientTV = view.findViewById(R.id.productIngredientTV2);
        halalStatusTV = view.findViewById(R.id.halalStatusTV2);
        productIV = view.findViewById(R.id.productIV2);
        progressBar = view.findViewById(R.id.progressBar);
        scrollView = view.findViewById(R.id.scrollView);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView = view.findViewById(R.id.barcodeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BarcodeAdapter(barcodeList, this);
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
                    String barcode = documentSnapshot.getString("barcode");
                    String productName = documentSnapshot.getString("productName");
                    History product = new History(barcode, productName);
                    barcodeList.add(product);
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.i("AJB", "Error");
            }
        });
    }

    @Override
    public void onItemClick(String barcode) {
        Toast.makeText(getActivity(), barcode, Toast.LENGTH_LONG).show();
        fetchProductByBarcode(barcode);
        showProductInfoDialog();
    }

    private void showProductInfoDialog() {
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(getActivity().findViewById(R.id.bottomSheetBehavior2));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void fetchProductByBarcode(String barcode) {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        // Make an API request using Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/api/v0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APICall apiCall = retrofit.create(APICall.class);
        Call<ProductResponse> call = apiCall.getProductByBarcode(barcode);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body().getProduct();
                    if (product != null) {
                        String ingredientsText = product.getIngredientsText();
                        String name = product.getName();
                        int ingredientNumber = product.getIngredientNumber();

                        if (ingredientsText != null && name != null && ingredientNumber > 0) {

                            productTitleTV.setPadding(20, 50, 20, 20);
                            productTitleTV.setTextSize(48);
                            productTitleTV.setText(name);

                            productIngredientTV.setPadding(20, 20, 20, 20);
                            productIngredientTV.setTextSize(24);
                            String ingredients = String.format(getString(R.string.ingredients), String.valueOf(ingredientNumber), ingredientsText);
                            productIngredientTV.setText(ingredients);

                            boolean allIngredientsHalal = checkIfHaram(ingredientsText);
                            updateHalalStatusUI(allIngredientsHalal);
                        } else {
                            productIngredientTV.setText("No ingredients found");
                        }

                        String imageUrl = product.getImageUrl();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Picasso.get()
                                    .load(imageUrl)
                                    .placeholder(R.drawable.placeholder_image)
                                    .error(R.drawable.error_image)
                                    .into(productIV);
                        } else {
                            productIV.setImageResource(R.drawable.placeholder_image);
                        }

                        // Handle success, update UI with ingredients
                    } else {
                        // Handle case where no ingredients are found
                        Log.d("AJB", "No ingredients found for this product.");
                    }
                } else {
                    // Handle API error or product not found
                    Log.d("AJB", "Product not found.");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                // Handle network or other errors
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

            }
        });
    }

    private boolean checkIfHaram(String ingredients) {
        List<String> haramIngredients = Arrays.asList(
                "cochineal",
                "gelatine",
                "pork",
                "edible bone phosphate",
                "bone",
                "shellac",
                "e120",
                "e441",
                "e542",
                "e904",
                "alcohol",
                "ethanol",
                "lard",
                "pepsin",
                "beer",
                "wine",
                "liqueur",
                "rennet",
                "bacon",
                "gelatin",
                "cider"
        );

        for (String ingredient : haramIngredients) {
            if (ingredients.toLowerCase(Locale.US).contains(ingredient)) {
                return true;
            }
        }

        return false;
    }

    private void updateHalalStatusUI(boolean allIngredientsHalal) {
        halalStatusTV.setTextSize(24);
        if (!allIngredientsHalal) {
            halalStatusTV.setText("Halal");
        } else {
            halalStatusTV.setText("Haram");
        }
    }


}