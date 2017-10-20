package com.hexaenna.avm;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.avm.api.ApiClient;
import com.hexaenna.avm.api.ApiInterface;
import com.hexaenna.avm.model.Login;
import com.hexaenna.avm.model.RequestJson;
import com.hexaenna.avm.utils.Constants;
import com.hexaenna.avm.utils.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class E_MailValidation extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBarOTP;
    Button btnSendOTP;
    EditText edtSendOTP;
    NetworkChangeReceiver networkChangeReceiver;
    String isConnection = null,e_mail  = null;
    TSnackbar snackbar;
    View snackbarView;
    ApiInterface apiInterface;
    RelativeLayout rldMainE_mail;
    LinearLayout ldtResendOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e__mail_validation_activity);

        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);


                Bundle b = intent.getExtras();
                isConnection = b.getString(Constants.MESSAGE);
                Log.e("newmesage", "" + isConnection);
                getNetworkState();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        this.registerReceiver(networkChangeReceiver,
                intentFilter);


        progressBarOTP = (ProgressBar) findViewById(R.id.progressOtp);
        progressBarOTP.setVisibility(View.GONE);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle.getString("email") != null) {
            e_mail = bundle.getString("email");
        }

        btnSendOTP = (Button) findViewById(R.id.btnSubmit);
        btnSendOTP.setOnClickListener(this);

        edtSendOTP = (EditText) findViewById(R.id.edtOTP);
        rldMainE_mail = (RelativeLayout) findViewById(R.id.rldMainE_mail);

        ldtResendOTP = (LinearLayout) findViewById(R.id.ldtResendOTP);
        ldtResendOTP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSubmit:
                sendOTP();
                break;

            case R.id.ldtResendOTP:
                resendOtp();
                break;
        }

    }

    private void resendOtp() {

        progressBarOTP.setVisibility(View.VISIBLE);
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
//            Log.e("djjkdfdhd",e_mail);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email",e_mail);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<Login> call = apiInterface.reSendOTP(jsonObject);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.isSuccessful()) {
                        progressBarOTP.setVisibility(View.GONE);
                        Login login = response.body();
                        if (login.getStatus_code() != null)
                        {
                            if (login.getStatus_code().equals(Constants.status_code1))
                            {
                                Toast.makeText(getApplicationContext(),"Verification Email sent successfully !", Toast.LENGTH_LONG).show();

                            }else if (login.getStatus_code().equals(Constants.status_code0))
                            {
                                Toast.makeText(getApplicationContext(),"Verification Email Not sent !", Toast.LENGTH_LONG).show();

                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.e("output", t.getMessage());
                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(rldMainE_mail, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Action Button", "onClick triggered");

                        }
                    });
            snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
            snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
    }


    public void sendOTP()
    {
        if (!edtSendOTP.getText().toString().trim().isEmpty())
        {
            progressBarOTP.setVisibility(View.VISIBLE);
            checkServer();
        }else
        {

        }
    }

    private void checkServer() {

        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
//            Log.e("djjkdfdhd",e_mail);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email",e_mail);
                jsonObject.put("verify_code",edtSendOTP.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<Login> call = apiInterface.validateE_mail(jsonObject);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.isSuccessful()) {
                        Login login = response.body();

                        progressBarOTP.setVisibility(View.GONE);
                        if (login.getStatus_code() != null)
                        {
                            if (login.getStatus_code().equals(Constants.status_code1))
                            {
                                Intent intent = new Intent(getApplicationContext(),CollectionRequest.class);
                                startActivity(intent);
                                E_MailValidation.this.finish();
                            }else if (login.getStatus_code().equals(Constants.status_code0))
                            {
                                Toast.makeText(getApplicationContext(),"Invalid Otp !", Toast.LENGTH_LONG).show();

                            }

                        }

                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.e("output", t.getMessage());
                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(rldMainE_mail, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Action Button", "onClick triggered");

                        }
                    });
            snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
            snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
    }


    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(rldMainE_mail, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Action Button", "onClick triggered");

                            }
                        });
                snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
                snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                textView.setTypeface(null, Typeface.BOLD);
                snackbar.show();


            } else if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }
}
