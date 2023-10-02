package com.chris.firebaseauth.scan;

import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.chris.firebaseauth.APICall;
import com.chris.firebaseauth.BuildConfig;
import com.chris.firebaseauth.R;
import com.chris.firebaseauth.scan.models.History;
import com.chris.firebaseauth.scan.models.Product;
import com.chris.firebaseauth.scan.models.ProductResponse;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private String barcodeData;

    private TextView productTitleTV, productIngredientTV, halalStatusTV;
    private ImageView productIV;
    private Toolbar toolbar;

    private CollectionReference usersRef, historyRef;
    private DocumentReference userDocRef;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String DB_URL = BuildConfig.FIREBASE_DB_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView = findViewById(R.id.surface_view);
        productTitleTV = findViewById(R.id.productTitleTV);
        productIngredientTV = findViewById(R.id.productIngredientTV);
        productIV = findViewById(R.id.productIV);
        halalStatusTV = findViewById(R.id.halalStatusTV);
        initialiseDetectorsAndSources();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1280, 720)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanActivity.this, new
                                String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    productTitleTV.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null) {
                                productTitleTV.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                fetchProductByBarcode(barcodeData);
                                showProductInfoDialog();
                                addHistory(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                fetchProductByBarcode(barcodeData);
                                showProductInfoDialog();
                                addHistory(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }
                        }
                    });

                }
            }


        });
    }

    private void showProductInfoDialog() {
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetBehavior));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    private void fetchProductByBarcode(String barcode) {
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
                            Log.d("AJB", "No ingredients found for this product.");
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
                "lecithin",
                "bacon",
                "gelatin",
                "cider"
        );

//        String[] ingredientsArray = ingredients.split(", ");
//
//        // Iterate through the array and log each ingredient
//        for (String ingredient : ingredientsArray) {
//            String lowercaseIngredient = ingredient.toLowerCase(Locale.US);
//            if(haramIngredients.contains(lowercaseIngredient)) {
//                return true;
//            }
//        }

        for (String ingredient : haramIngredients) {
            if (ingredients.toLowerCase(Locale.US).contains(ingredient)) {
                return true;
            }
        }


//        List<String> inputString = Arrays.asList(ingredients.toLowerCase().split(", "));
//
//        return haramIngredients.stream().allMatch(inputString::contains);
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

    private void addHistory(String barcode) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = user.getUid();

        usersRef = db.collection("users");
        historyRef = usersRef.document(userId).collection("history");

        historyRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    // Document already exists; no need to add it again
                    Log.d("AJB", "History item with barcode " + barcode + " already exists");
                } else {
                    // Document does not exist; add it
                    historyRef.add(new History(barcode))
                            .addOnSuccessListener(aVoid -> {
                                Log.d("AJB", "History item added successfully");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("AJB", "Error adding history item: " + e.getMessage());
                            });
                }
            } else {
                Log.e("AJB", "Error checking for existing history items: " + task.getException().getMessage());
            }
        });
    }

}