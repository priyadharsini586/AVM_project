package com.hexaenna.avm;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.hexaenna.avm.api.ApiClient;
import com.hexaenna.avm.api.ApiInterface;
import com.hexaenna.avm.model.Login;
import com.hexaenna.avm.model.RegisterRequest;
import com.hexaenna.avm.model.RequestJson;
import com.hexaenna.avm.utils.Constants;
import com.hexaenna.avm.utils.NetworkChangeReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegistration;
    EditText edtName,edtCompanyName,edtCity,edtPinCode,edtMbl,edtE_mail;
    NetworkChangeReceiver networkChangeReceiver;
    String isConnection = null;
    TSnackbar snackbar;
    View snackbarView;
    ScrollView sclRegisterMain;
    TextInputLayout txtInputName,txtInputCompanyName,txtInputCity,txtInputPinCode,txtInputMobile,txtInputEMail;
    ApiInterface apiInterface;
    ProgressBar progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.registration_activity);

        btnRegistration = (Button) findViewById(R.id.btn_registration);
        btnRegistration.setOnClickListener(this);

        edtName = (EditText) findViewById(R.id.input_name);
        edtCompanyName = (EditText) findViewById(R.id.input_cmpy);
        edtCity = (EditText) findViewById(R.id.input_city);
        edtPinCode = (EditText) findViewById(R.id.input_pin);
        edtMbl = (EditText) findViewById(R.id.input_mbl);
        edtE_mail = (EditText) findViewById(R.id.input_email);


        sclRegisterMain  = (ScrollView) findViewById(R.id.sclRegisterMain);

        txtInputName = (TextInputLayout) findViewById(R.id.txtInputName);
        txtInputCompanyName = (TextInputLayout) findViewById(R.id.txtInputCompanyName);
        txtInputCity = (TextInputLayout) findViewById(R.id.txtInputCity);
        txtInputPinCode = (TextInputLayout) findViewById(R.id.txtInputPinCode);
        txtInputMobile = (TextInputLayout) findViewById(R.id.txtInputMobile);
        txtInputEMail = (TextInputLayout) findViewById(R.id.txtInputEMail);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle.getString("email") != null) {
            String b = bundle.getString("email");
            edtE_mail.setText(b);
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        isNameValidate();
        isCompanyNameValidate();
        isCityValidate();
        isPinValidate();
        isMobileValidate();
        isE_mailValidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_registration:

                checkValidation();
               /* Intent intent = new Intent(getApplicationContext(),CollectionRequest.class);
                startActivity(intent);
                RegistrationActivity.this.finish();*/
                break;
        }
    }

    private void checkValidation() {

        if (isNameValidate() && isCompanyNameValidate() && isCityValidate() && isPinValidate() && isMobileValidate() && isE_mailValidate())
        {
            progressBar.setVisibility(View.VISIBLE);
            registerDetails();

        }

    }

    private void registerDetails() {

        if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            String name = edtName.getText().toString().trim();
            String companyName = edtCompanyName.getText().toString().trim();
            String cityname  =  edtCity.getText().toString().trim();
            String pincode =edtPinCode.getText().toString().trim();
            String mbl = edtMbl.getText().toString().trim();
            final String e_mail = edtE_mail.getText().toString().trim();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name",name);
                jsonObject.put("comp_name",companyName);
                jsonObject.put("city",cityname);
                jsonObject.put("pincode",pincode);
                jsonObject.put("mobile",mbl);
                jsonObject.put("email",edtE_mail.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final RegisterRequest requestJson = new RegisterRequest(name,companyName,cityname,pincode,mbl,e_mail);

            Call<Login> call = apiInterface.registerDetails(jsonObject);
            call.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    if (response.isSuccessful()) {
                        Login login = response.body();

                        Log.e("output",  login.getStatus_code() + login.getStatus_message());


                        if (login.getStatus_code() != null) {
                            if (login.getStatus_code().equals(Constants.status_code1)) {

                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(),E_MailValidation.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("email",e_mail);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }else
                            {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Log.e("output", t.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            });

        }else
        {
            snackbar = TSnackbar
                    .make(sclRegisterMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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

    public boolean isNameValidate()
    {
        final boolean[] isName = {false};
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputName.setErrorEnabled(true);
                    txtInputName.setError("Enter the Name");
                    isName[0] = false;

                }else
                {
                    isName[0] = true;
                    txtInputName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if(edtName.getText().toString().isEmpty())
        {

            isName[0] = false;

        }else
        {
            isName[0] = true;

        }

     return isName[0];
    }

    public boolean isCompanyNameValidate()
    {
        final boolean[] isCompanyName = {false};
        edtCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputCompanyName.setErrorEnabled(true);
                    txtInputCompanyName.setError("Enter the Company Name");
                    isCompanyName[0] = false;

                }else
                {
                    isCompanyName[0] = true;
                    txtInputCompanyName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtCompanyName.getText().toString().isEmpty())
        {

            isCompanyName[0] = false;

        }else
        {
            isCompanyName[0] = true;

        }
        return isCompanyName[0];
    }

    public boolean isCityValidate()
    {
        final boolean[] isCityName = {false};
        edtCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputCity.setErrorEnabled(true);
                    txtInputCity.setError("Enter the City Name");
                    isCityName[0] = false;

                }else
                {
                    isCityName[0] = true;
                    txtInputCity.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        if(edtCity.getText().toString().isEmpty())
        {

            isCityName[0] = false;

        }else
        {
            isCityName[0] = true;

        }

        return isCityName[0];
    }

    public boolean isPinValidate()
    {
        final boolean[] isPincode = {false};
        edtPinCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() < 6)
                {
                    txtInputPinCode.setErrorEnabled(true);
                    txtInputPinCode.setError("Enter the correct Pin number");
                    isPincode[0] = false;

                }else
                {
                    isPincode[0] = true;
                    txtInputPinCode.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtPinCode.getText().toString().isEmpty())
        {

            isPincode[0] = false;

        }else
        {
            isPincode[0] = true;

        }
        return isPincode[0];
    }

    public boolean isMobileValidate()
    {
        final boolean[] isMblNum = {false};
        edtMbl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() < 10)
                {
                    txtInputMobile.setErrorEnabled(true);
                    txtInputMobile.setError("Enter the correct mobile number");
                    isMblNum[0] = false;

                }else
                {
                    isMblNum[0] = true;
                    txtInputMobile.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtMbl.getText().toString().isEmpty())
        {

            isMblNum[0] = false;

        }else
        {
            isMblNum[0] = true;

        }
        return isMblNum[0];
    }
    public boolean isE_mailValidate()
    {
        final boolean[] isE_mail = {false};
        edtE_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();

                if (s.length() == 0)
                {
                    txtInputEMail.setErrorEnabled(true);
                    txtInputEMail.setError("Enter the correct e_mail");
                    isE_mail[0] = false;

                }else
                {
                    if (isValidEmail(s.toString()))
                    {
                        isE_mail[0] = true;
                        txtInputEMail.setErrorEnabled(false);

                    }else {
                        txtInputEMail.setErrorEnabled(true);
                        txtInputEMail.setError("Enter the correct email");
                        isE_mail[0] = false;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        if(edtE_mail.getText().toString().isEmpty())
        {

            isE_mail[0] = false;

        }else
        {
            isE_mail[0] = true;

        }

        return isE_mail[0];
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(sclRegisterMain, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
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
}

