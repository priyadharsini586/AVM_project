package com.hexaenna.avm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class CollectionRequest extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ldtHome,ldtCertificate,ldtOtherServices,ldtCalibration,ldtSales,ldtRepair,ldtCollectionRequest,ldtOnsiteRequest,ldtMblCalibration,ldtProduct,ldtDownload,ldtContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_request_activity);
        ldtHome = (LinearLayout) findViewById(R.id.ldtHome);
        ldtHome.setOnClickListener(this);

        ldtCertificate = (LinearLayout) findViewById(R.id.ldtCertificate);
        ldtCertificate.setOnClickListener(this);

        ldtOtherServices = (LinearLayout) findViewById(R.id.ldtOtherServices);
        ldtOtherServices.setOnClickListener(this);

        ldtCalibration = (LinearLayout) findViewById(R.id.ldtCalibration);
        ldtCalibration.setOnClickListener(this);

        ldtSales = (LinearLayout) findViewById(R.id.ldtSales);
        ldtSales.setOnClickListener(this);

        ldtRepair = (LinearLayout) findViewById(R.id.ldtRepair);
        ldtRepair.setOnClickListener(this);

        ldtCollectionRequest = (LinearLayout) findViewById(R.id.ldtCollectionRequest);
        ldtCollectionRequest.setOnClickListener(this);

        ldtOnsiteRequest = (LinearLayout) findViewById(R.id.ldtOnsiteRequest);
        ldtOnsiteRequest.setOnClickListener(this);

        ldtMblCalibration = (LinearLayout) findViewById(R.id.ldtmblCalibration);
        ldtMblCalibration.setOnClickListener(this);

        ldtProduct = (LinearLayout) findViewById(R.id.ldtProduct);
        ldtProduct.setOnClickListener(this);

        ldtDownload = (LinearLayout) findViewById(R.id.ldtDownload);
        ldtDownload.setOnClickListener(this);

        ldtContact = (LinearLayout) findViewById(R.id.ldtContactUs);
        ldtContact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        switch (v.getId())
        {

            case R.id.ldtHome:
                intent.putExtra("where","home");
                startActivity(intent);
                break;

            case R.id.ldtCertificate:
                intent.putExtra("where","certificate");
                startActivity(intent);
                break;

            case R.id.ldtOtherServices:
                intent.putExtra("where","otherServices");
                startActivity(intent);
                break;

            case R.id.ldtCalibration:
                intent.putExtra("where","calibration");
                startActivity(intent);
                break;

            case R.id.ldtSales:
                intent.putExtra("where","sales");
                startActivity(intent);
                break;

            case R.id.ldtRepair:
                intent.putExtra("where","repair");
                startActivity(intent);
                break;
        }
    }
}
