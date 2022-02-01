package com.developeralamin.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.developeralamin.ecommerceapp.R;
import com.developeralamin.ecommerceapp.adapter.MyCardAdpater;
import com.developeralamin.ecommerceapp.models.MyCardModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    int overAllTotalAmount;
    TextView overAllAmount;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCardModel> cardModelList;
    MyCardAdpater cardAdpater;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        toolbar = findViewById(R.id.my_card_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get data
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMeddageReceiver,new IntentFilter("MyTotalAmount"));

        overAllAmount = findViewById(R.id.textView3);
        recyclerView = findViewById(R.id.car_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardModelList = new ArrayList<>();
        cardAdpater = new MyCardAdpater(this, cardModelList);
        recyclerView.setAdapter(cardAdpater);

        firestore.collection("AddToCard").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (DocumentSnapshot doc :task.getResult().getDocuments()){

                        MyCardModel myCardModel = doc.toObject(MyCardModel.class);
                        cardModelList.add(myCardModel);
                        cardAdpater.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public BroadcastReceiver mMeddageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int totalBill = intent.getIntExtra("totalAmount",0);
            overAllAmount.setText("Total Amount : "+totalBill+"৳");

        }
    };

}