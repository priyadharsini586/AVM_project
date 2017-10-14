package com.hexaenna.avm.api;

import com.hexaenna.avm.model.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by admin on 10/13/2017.
 */

public interface ApiInterface {

    @GET("check_login.php")
    Call<Login> checkEmail(@Query("x") String email);


}
