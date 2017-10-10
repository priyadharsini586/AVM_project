package com.hexaenna.avm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity  {


    TextView txtHomeContent,txtOtherServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

      /*  txtHomeContent = (TextView) findViewById(R.id.txtHomeContent);
        txtOtherServices = (TextView) findViewById(R.id.txtOtherServices);
        txtOtherServices.setText(Html.fromHtml(getString(R.string.other_services_content)));*/



    }


}
