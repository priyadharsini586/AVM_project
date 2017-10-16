package com.hexaenna.avm.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hexaenna.avm.model.Login;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 10/13/2017.
 */

public interface ApiInterface {

    @POST("check_login.php")
    Call<Login> checkEmail(@Query("x") JSONObject email);


}
