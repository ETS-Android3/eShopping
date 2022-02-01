package com.developeralamin.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developeralamin.ecommerceapp.R;
import com.developeralamin.ecommerceapp.models.NewProductsModel;
import com.developeralamin.ecommerceapp.models.PopularProductModel;
import com.developeralamin.ecommerceapp.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class DetailesActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,description,price,quantity;
    Button addToCard,buyNow;
    ImageView addItems,removeItems;

    androidx.appcompat.widget.Toolbar toolbar;

    int totalQuantity = 1;
    int totalPrice = 0;
    //New Products
    NewProductsModel newProductsModel = null;

    //Show All
    ShowAllModel showAllModel = null;

    FirebaseAuth auth;

    //Popular Prodcuts
    PopularProductModel popularProductModel = null;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes);

        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        final  Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel ){
            newProductsModel = (NewProductsModel) obj;
        }else if (obj instanceof PopularProductModel){
            popularProductModel = (PopularProductModel) obj;
        }else if (obj instanceof ShowAllModel){
            showAllModel = (ShowAllModel) obj;
        }



        detailedImg = findViewById(R.id.detailed_img);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);

        addToCard = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //New Products
        if (newProductsModel != null) {
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            name.setText(newProductsModel.getName());
            rating.setText(newProductsModel.getRating());
            description.setText(newProductsModel.getDescription());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            name.setText(newProductsModel.getName());

            totalPrice = newProductsModel.getPrice() * totalQuantity;

        }


        //Popular Products
        if (popularProductModel != null) {
            Glide.with(getApplicationContext()).load(popularProductModel.getImg_url()).into(detailedImg);
            name.setText(popularProductModel.getName());
            rating.setText(popularProductModel.getRating());
            description.setText(popularProductModel.getDescription());
            price.setText(String.valueOf(popularProductModel.getPrice()));
            name.setText(popularProductModel.getName());

            totalPrice = popularProductModel.getPrice() * totalQuantity;

        }



        //Show All Products
        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;

        }


        //Buy Now

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailesActivity.this,AddressActivity.class);

                if (newProductsModel != null){
                    intent.putExtra("item", newProductsModel);
                }
                if (popularProductModel !=null){
                    intent.putExtra("item",popularProductModel);
                }
                if (showAllModel !=null){
                    intent.putExtra("item",showAllModel);
                }

                startActivity(intent);

            }
        });

        //Add to Card

        addToCard.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
               addtoCard();
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity<10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if (newProductsModel != null){
                        totalPrice = newProductsModel.getPrice() * totalQuantity;
                    }
                    if (popularProductModel != null){
                        totalPrice = popularProductModel.getPrice() * totalQuantity;
                    }
                    if (showAllModel != null){
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }
            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addtoCard() {

        String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd, yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String, Object> carMap = new HashMap<>();
        carMap.put("productName",name.getText().toString());
        carMap.put("productPrice",price.getText().toString());
        carMap.put("currentTime",saveCurrentTime);
        carMap.put("currentData",saveCurrentDate);

        carMap.put("totalQuantity",quantity.getText().toString());
        carMap.put("totalPrice",totalPrice);

        firestore.collection("AddToCard").document(auth.getCurrentUser().getUid())
                .collection("User").add(carMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailesActivity.this, "Added To a Cart", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}