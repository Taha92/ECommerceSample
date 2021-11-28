package com.example.ecommerceapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.http.RestService;
import com.example.ecommerceapp.models.BaseResponse;
import com.example.ecommerceapp.models.ItemData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextProductName;
    private EditText editTextProductPrice;
    private TextView textViewWarningEnterName;
    private TextView textViewWarningEnterPrice;
    private Button buttonUpdate;
    private static final String OBJECT_KEY = "productObject";
    private String productId;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

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
            editTextProductName = findViewById(R.id.editTextName);
            editTextProductPrice = findViewById(R.id.editTextPrice);
            textViewWarningEnterName = findViewById(R.id.textViewWarningEnterName);
            textViewWarningEnterPrice = findViewById(R.id.textViewWarningEnterPrice);
            buttonUpdate = findViewById(R.id.buttonUpdate);
            buttonUpdate.setOnClickListener(this);

            ItemData itemData = (ItemData) getIntent().getSerializableExtra(OBJECT_KEY);
            productId = itemData.getId();

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(itemData.getName());
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateData() {

        try {
            if (editTextProductName.getText().toString().trim().equals("")) {
                textViewWarningEnterName.setVisibility(View.VISIBLE);
                textViewWarningEnterName.setText(R.string.text_enter_product_name);
                return;
            } else {
                textViewWarningEnterName.setVisibility(View.GONE);
            }

            if (editTextProductPrice.getText().toString().trim().equals("")) {
                textViewWarningEnterPrice.setVisibility(View.VISIBLE);
                textViewWarningEnterPrice.setText(R.string.text_enter_product_price);
                return;
            } else {
                textViewWarningEnterPrice.setVisibility(View.GONE);
            }

            ItemData itemData = new ItemData();
            itemData.setName(String.valueOf(editTextProductName.getText()));
            itemData.setPrice(String.valueOf(editTextProductPrice.getText()));

            Call<BaseResponse> dataUpdateResponseCall = RestService.getHttpService().updateItem(productId, itemData);
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setCancelable(false);
            }

            dataUpdateResponseCall.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {

                    try {
                        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();

                        if (response.body() != null) {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                    if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.buttonUpdate:
                updateData();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
