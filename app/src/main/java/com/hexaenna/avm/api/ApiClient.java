package com.hexaenna.avm.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 10/13/2017.
 */

public class ApiClient {

    public static Retrofit retrofit = null;
    public static final String BASE_URL = "http://avmlabs.in/avm_db/";

    public static Retrofit getClient()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
