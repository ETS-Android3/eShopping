package com.developeralamin.ecommerceapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.developeralamin.ecommerceapp.R;
import com.developeralamin.ecommerceapp.activities.ShowAllActivity;
import com.developeralamin.ecommerceapp.adapter.CategoryAdapter;
import com.developeralamin.ecommerceapp.adapter.NewProductAdapter;
import com.developeralamin.ecommerceapp.adapter.PopularProductAdapter;
import com.developeralamin.ecommerceapp.models.CategoryModel;
import com.developeralamin.ecommerceapp.models.NewProductsModel;
import com.developeralamin.ecommerceapp.models.PopularProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView catShowAll, popularShowAll,newProductShowll;

    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    RecyclerView catRecylerView,newProductRecylerView,popularRecylerview;
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //new Product RecylerView

    NewProductAdapter newProductAdapter;
    List<NewProductsModel> newProductsModelList;

    //Popular Products

    PopularProductAdapter popularProductAdapter;
    List<PopularProductModel> popularProductModelList;


    //Firebase
    FirebaseFirestore bd;


    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        bd = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getActivity());
        catRecylerView = root.findViewById(R.id.rec_category);
        newProductRecylerView = root.findViewById(R.id.new_product_rec);
        popularRecylerview = root.findViewById(R.id.popular_rec);

        catShowAll = root.findViewById(R.id.category_see_all);
        popularShowAll = root.findViewById(R.id.popular_see_all);
        newProductShowll = root.findViewById(R.id.newProducts_see_all);

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newProductShowll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        popularShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });


        linearLayout = root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);

        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.banner1, "Discount On Shoes Items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2, "Discount On Perfune", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3, "70% OFF", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);


        progressDialog.setTitle("Welcome To My ECommerce App");
        progressDialog.setMessage("please wait......");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        //category


        catRecylerView.setLayoutManager(new LinearLayoutManager(getActivity(),  RecyclerView.HORIZONTAL,false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(),categoryModelList);
        catRecylerView.setAdapter(categoryAdapter);


        bd.collection("Category").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //New Products

        newProductRecylerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        newProductsModelList = new ArrayList<>();
        newProductAdapter = new NewProductAdapter(getContext(),newProductsModelList);
        newProductRecylerView.setAdapter(newProductAdapter);

        bd.collection("NewProducts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                NewProductsModel newProductsModel = document.toObject(NewProductsModel.class);
                                newProductsModelList.add(newProductsModel);
                                newProductAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //PoputlaerProcduct


        popularRecylerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        popularProductModelList = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(getContext(),popularProductModelList);
        popularRecylerview.setAdapter(popularProductAdapter);


        bd.collection("AllProducts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                PopularProductModel popularProductModel = document.toObject(PopularProductModel.class);
                                popularProductModelList.add(popularProductModel);
                                popularProductAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        return  root;

    }
}