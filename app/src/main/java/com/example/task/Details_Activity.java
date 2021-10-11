package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task.ApiPackage.ApiClient;
import com.example.task.model.List_Model;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Details_Activity extends AppCompatActivity {
    private TextView id;
    private TextView name;
    private TextView email;
    private TextView address;
    private TextView phone_number;
    private TextView address1;
    private TextView address2;
    private TextView company_name;
    private TextView company_website;
    private TextView back_btn;
    String _id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        id = (TextView) findViewById(R.id.id);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        address = (TextView) findViewById(R.id.address);
        phone_number  =  (TextView) findViewById(R.id.phone_number);
        address1 = (TextView) findViewById(R.id.address1);
        address2 = (TextView) findViewById(R.id.address2);
        company_name = (TextView) findViewById(R.id.company_name);
        company_website = (TextView) findViewById(R.id.company_website);
        back_btn = (TextView) findViewById(R.id.back_btn);

        Intent intent = getIntent();
        _id = intent.getStringExtra("EMP_ID");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getEmployeeDetails();
    }

    private void getEmployeeDetails() {
            if (!ApiClient.isNetworkAvailable(this)){
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
                return;
            }
            ProgressDialog Dialog = new ProgressDialog(Details_Activity.this);
        Dialog.setMessage("Please Wait...");
        Dialog.show();

            Call<List_Model> call = ApiClient.getUserService().getDetails(_id);

            call.enqueue(new Callback<List_Model>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<List_Model> call, Response<List_Model> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            List_Model details = response.body();
                            Log.e("ok", ""+response.code());
                            id.setText("Employee Id: "+details.getId());
                            name.setText("Name: "+details.getName());
                            email.setText(details.getEmail().toLowerCase());
                            phone_number.setText(details.getPhone());
                            //Address
                            address1.setText(details.getAddress().getSuite()+", "+details.getAddress().getStreet());
                            address2.setText(details.getAddress().getCity()+" - "+details.getAddress().getZipcode());
                            //company
                            company_name.setText("Company Name: " + details.getCompany().getName());
                            company_website.setText(details.getWebsite());

                            email.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    OpenEmail(details.getEmail().toLowerCase());
                                }
                            });
                            phone_number.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openPhoneNumber(details.getPhone());
                                }
                            });

                            company_website.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openWebsite(details.getWebsite());
                                }
                            });

                        }
                    } else {
                        Log.e("failed", ""+response.code());
                    }
                    Dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List_Model> call, Throwable t) {
                    Log.e("throwable", ""+t.getMessage());
                    Dialog.dismiss();
                }
            });

        }
    private void openWebsite(String website) {
        Uri uri = Uri.parse(website);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://"+website));
        startActivity(intent);
    }

    private void OpenEmail(String email_id) {
        String[] emails = email_id.split(",");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,emails);
        intent.putExtra(Intent.EXTRA_SUBJECT,"");
        intent.putExtra(Intent.EXTRA_TEXT,"");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose An Email"));
    }


    private void openPhoneNumber(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }




    }
