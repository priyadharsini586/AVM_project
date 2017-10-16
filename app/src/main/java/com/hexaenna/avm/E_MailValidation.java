package com.hexaenna.avm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class E_MailValidation extends AppCompatActivity {

    ProgressBar progressBarOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e__mail_validation_activity);

        progressBarOTP = (ProgressBar) findViewById(R.id.progressOtp);

    }
}
