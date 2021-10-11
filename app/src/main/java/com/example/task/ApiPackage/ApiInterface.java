package com.example.task.ApiPackage;

import com.example.task.model.List_Model;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    //GetAllUsers
    @GET("users")
    Call<List<List_Model>> getList();

    @GET("users/{id}")
    Call<List_Model> getDetails(@Path("id") String id);

}
