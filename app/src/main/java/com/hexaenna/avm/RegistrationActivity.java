package com.hexaenna.avm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        btnRegistration = (Button) findViewById(R.id.btn_registration);
        btnRegistration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_registration:
                Intent intent = new Intent(getApplicationContext(),CollectionRequest.class);
                startActivity(intent);
                RegistrationActivity.this.finish();
                break;
        }
    }
}

