package com.example.task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.task.ApiPackage.ApiClient;
import com.example.task.adaptor.List_Adaptor;
import com.example.task.model.List_Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rc_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rc_list = findViewById(R.id.rc_list);
        rc_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));

       employeelist();
    }

    private void employeelist() {
        if (!ApiClient.isNetworkAvailable(MainActivity.this)){
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        Dialog.setMessage("Please Wait...");
        Dialog.show();


        Call<List<List_Model>> call = ApiClient.getUserService().getList();
        call.enqueue(new Callback<List<List_Model>>() {
            @Override
            public void onResponse(Call<List<List_Model>> call, Response<List<List_Model>> response) {

                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.e("ok", ""+response.code());
                        List_Adaptor adaptor = new List_Adaptor(response.body(),MainActivity.this);
                        rc_list.setAdapter(adaptor);
                    }
                } else {
                    Log.e("Not ok", ""+response.code());
                }
                Dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<List_Model>> call, Throwable t) {
                Log.e("throwable", t.getMessage());
                Dialog.dismiss();
            }
        });




    }


}