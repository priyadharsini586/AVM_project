package com.hexaenna.avm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
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
import com.hexaenna.avm.model.RequestUserDetailsResponse;
import com.hexaenna.avm.utils.Constants;
import com.hexaenna.avm.utils.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {

    String fromWhere;
    LinearLayout ldtName,ldtCompany,ldtMbl,ldtLocation,ldtPincode,ldte_mail;
    TextView txtToolbarText;
    Button btnSubmit;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    String isConnection = null;
    RelativeLayout rldMainLayout;
    TSnackbar snackbar;
    View snackbarView;
    TextInputLayout txtInputE_maill,txtInputMobile,txtInputName,txtInputLocation;
    EditText edtName,edtCompanyName,edtMobile,edtLocation,edtPincode,edtE_mail;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_activity);

        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);


                if (isConnection == null) {
                    Bundle b = intent.getExtras();
                    isConnection = b.getString(Constants.MESSAGE);
                    Log.e("newmesage", "connection connection");
                    getNetworkState();
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
          this.registerReceiver(networkChangeReceiver,
                intentFilter);

        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null)
        {
            Log.e("from",extrasBundle.getString("wherefrom"));
            fromWhere = extrasBundle.getString("wherefrom");
        }

        ldtName = (LinearLayout) findViewById(R.id.ldtName);
        ldtCompany = (LinearLayout) findViewById(R.id.ldtCompany);
        ldtMbl = (LinearLayout) findViewById(R.id.ldtMbl);
        ldtLocation = (LinearLayout) findViewById(R.id.ldtLocation);
        ldtPincode = (LinearLayout)findViewById(R.id.ldtPincode);
        ldte_mail = (LinearLayout) findViewById(R.id.ldte_mail);

        txtToolbarText = (TextView) findViewById(R.id.txtToolbarText);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        edtName = (EditText) findViewById(R.id.edtName);
        edtCompanyName = (EditText) findViewById(R.id.edtCompanyName);
        edtE_mail = (EditText) findViewById(R.id.edtE_mail);
        edtLocation = (EditText) findViewById(R.id.edtlocation);
        edtMobile = (EditText) findViewById(R.id.edtMbl);
        edtPincode = (EditText) findViewById(R.id.edtpincode);

        txtInputE_maill = (TextInputLayout) findViewById(R.id.txtInputE_mail);
        txtInputMobile = (TextInputLayout) findViewById(R.id.txtInputMobile);
        txtInputName = (TextInputLayout) findViewById(R.id.txtInputName);
        txtInputLocation = (TextInputLayout) findViewById(R.id.txtInputLocation);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (fromWhere.equals("Collection"))
        {
            ldtName.setVisibility(View.VISIBLE);
            ldtCompany.setVisibility(View.VISIBLE);
            ldtMbl.setVisibility(View.VISIBLE);
            ldtLocation.setVisibility(View.GONE);
            ldtPincode.setVisibility(View.GONE);
            ldte_mail.setVisibility(View.VISIBLE);

            txtToolbarText.setText("Collection Request");
        }else if (fromWhere.equals("Onsite"))
        {
            ldtName.setVisibility(View.VISIBLE);
            ldtCompany.setVisibility(View.VISIBLE);
            ldtMbl.setVisibility(View.VISIBLE);
            ldtLocation.setVisibility(View.VISIBLE);
            ldtPincode.setVisibility(View.GONE);
            ldte_mail.setVisibility(View.VISIBLE);

            txtToolbarText.setText("Onsite Request");
        }else if (fromWhere.equals("Mobile"))
        {
            ldtName.setVisibility(View.VISIBLE);
            ldtCompany.setVisibility(View.VISIBLE);
            ldtMbl.setVisibility(View.VISIBLE);
            ldtLocation.setVisibility(View.VISIBLE);
            ldtPincode.setVisibility(View.VISIBLE);
            ldte_mail.setVisibility(View.VISIBLE);

            txtToolbarText.setText("Mobile calibration");
        }



        edtE_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputE_maill.setErrorEnabled(true);
                    txtInputE_maill.setError("Enter the correct e_mail");

                }else
                {
                    if (isValidEmail(s.toString()))
                    {
                        txtInputE_maill.setErrorEnabled(false);

                    }else {
                        txtInputE_maill.setErrorEnabled(true);
                        txtInputE_maill.setError("Enter the correct email");

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) { }
        });



        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() < 10)
                {
                    txtInputMobile.setErrorEnabled(true);
                    txtInputMobile.setError("Enter the correct mobile number");


                }else
                {

                    txtInputMobile.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        progressBar.setVisibility(View.VISIBLE);
    }

    private void getDetailsFromServer() {

        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email",getE_mail());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<RequestUserDetailsResponse> call = apiInterface.userDetails(jsonObject);
            call.enqueue(new Callback<RequestUserDetailsResponse>() {
                @Override
                public void onResponse(Call<RequestUserDetailsResponse> call, Response<RequestUserDetailsResponse> response) {
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        RequestUserDetailsResponse requestUserDetailsResponse = response.body();

                        if (requestUserDetailsResponse.getStatus_code() != null)
                        {
                            if (requestUserDetailsResponse.getStatus_code().equals(Constants.status_code1)) {


                                edtName.setText(requestUserDetailsResponse.getName());
                                edtName.setEnabled(false);
                                edtName.requestFocus();
                                edtCompanyName.setText(requestUserDetailsResponse.getCompany());
                                edtCompanyName.setEnabled(false);
                                edtMobile.setText(requestUserDetailsResponse.getMobile());
                                edtMobile.setEnabled(false);
                                edtE_mail.setText(getE_mail());
                                edtE_mail.setEnabled(false);
                                edtPincode.setText(requestUserDetailsResponse.getPincode());
                                edtPincode.setEnabled(false);
                                edtLocation.requestFocus();
                            }else
                            {
                                Toast.makeText(getApplicationContext(),"Not able to get user details",Toast.LENGTH_LONG).show();

                            }
                        }

                    }
                }

                @Override
                public void onFailure(Call<RequestUserDetailsResponse> call, Throwable t) {
                }

            });

        }else
        {
            snackbar = TSnackbar
                    .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSubmit:
                if (!fromWhere.equals("Collection")) {
                    if (!edtLocation.getText().toString().isEmpty()) {
                        submitCollection(fromWhere);
                    } else {
                        if (edtLocation.getText().toString().isEmpty()) {
                            txtInputLocation.setErrorEnabled(true);
                            txtInputLocation.setError("Enter the location");
                        } else {
                            txtInputLocation.setErrorEnabled(false);
                        }
                    }
                }else if (fromWhere.equals("Collection"))
                {
                    submitCollection(fromWhere);
                }
                break;
        }
    }

    private void submitCollection(String from) {

        progressBar.setVisibility(View.VISIBLE);
        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("req",from);
                jsonObject.put("name",edtName.getText().toString());
                jsonObject.put("email",edtE_mail.getText().toString());
                jsonObject.put("comp_name",edtCompanyName.getText().toString());
                jsonObject.put("mobile",edtMobile.getText().toString());
                if (from.equals("Onsite"))
                    jsonObject.put("location",edtLocation.getText().toString());
                if (from.equals("Mobile")) {
                    jsonObject.put("location", edtLocation.getText().toString());
                    jsonObject.put("pincode",edtPincode.getText().toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<Login> call = apiInterface.request(jsonObject);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.isSuccessful()) {
                        Login login = response.body();

                        progressBar.setVisibility(View.GONE);
                        if (login.getStatus_code() != null)
                        {
                            if (login.getStatus_code().equals(Constants.status_code1)) {
                                Toast.makeText(getApplicationContext(),"Request has been sent.",Toast.LENGTH_LONG).show();
                                RequestActivity.this.finish();

                            }else
                            {
                                Toast.makeText(getApplicationContext(),"Request not sent.",Toast.LENGTH_LONG).show();

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
                    .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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
                        .make(rldMainLayout, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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


            }else if (isConnection.equals(Constants.NETWORK_CONNECTED))
            {
                if (snackbar != null)
                {
                    snackbar.dismiss();
                }

                if (getE_mail() != null)
                    getDetailsFromServer();
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (networkChangeReceiver == null)
        {
            Log.e("reg","Do not unregister receiver as it was never registered");
        }
        else
        {
            Log.e("reg","Unregister receiver");
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }

    public  String getE_mail()
    {
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        String e_mail = null;
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                e_mail = account.name;
                break;

            }
        }

        return e_mail;
    }
}
