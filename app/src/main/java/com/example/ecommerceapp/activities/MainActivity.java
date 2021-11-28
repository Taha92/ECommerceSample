package com.example.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.ItemsCardViewAdapter;
import com.example.ecommerceapp.http.RestService;
import com.example.ecommerceapp.models.ItemData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemsCardViewAdapter itemsCardViewAdapter;
    TextView txtViewNoData;
    private ProgressDialog progressDialog;
    private static final String OBJECT_KEY = "productObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        try {
            // Initializing REST services
            RestService.initializeSyncHttpService();
            RestService.initializeHttpService();

            initialize();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.text_loading));
            txtViewNoData = findViewById(R.id.txtViewNoData);
            recyclerView = findViewById(R.id.recycler_view);

            ViewCompat.setNestedScrollingEnabled(recyclerView, false);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                    DividerItemDecoration.HORIZONTAL);
            recyclerView.addItemDecoration(dividerItemDecoration);

            getItems();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getItems() {

        Call<ArrayList<ItemData>> getItemsResponseCall = RestService.getHttpService().getItems();
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        getItemsResponseCall.enqueue(new Callback<ArrayList<ItemData>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ItemData>> call, @NonNull Response<ArrayList<ItemData>> response) {

                try {
                    if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();

                    if (response.body() != null) {
                        ArrayList<ItemData> itemData = response.body();

                        itemsCardViewAdapter = new ItemsCardViewAdapter(getApplicationContext(), itemData, new ItemsCardViewAdapter.OnItemClickListener() {
                            @Override
                            public void OnItemClickListener(ItemData itemData, int position) {
                                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                                intent.putExtra(OBJECT_KEY, itemData);
                                MainActivity.this.startActivity(intent);
                            }
                        }, new ItemsCardViewAdapter.OnItemDeleteClickListener() {
                            @Override
                            public void OnItemClickListener(int position) {
                                itemData.remove(position);
                                itemsCardViewAdapter.notifyDataSetChanged();
                            }
                        });

                        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setLayoutManager(manager);

                        recyclerView.setAdapter(itemsCardViewAdapter);

                    } else {
                        txtViewNoData.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ItemData>> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}