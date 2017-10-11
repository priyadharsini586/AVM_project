package com.hexaenna.avm;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity  {


    TextView txtHomeContent,txtOtherServices;
    private WebView webHome;
    LinearLayout ldtOtherServices;
    String fromWhere = "";
    ScrollView scrMainView;
    TextView txtToolbarText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);


        scrMainView = (ScrollView) findViewById(R.id.scrMain);

        txtToolbarText = (TextView) findViewById(R.id.txtToolbarText);
        ldtOtherServices = (LinearLayout) findViewById(R.id.ldtOtherServices);
        webHome = (WebView) findViewById(R.id.webHome);

        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null)
        {
            Log.e("from",extrasBundle.getString("where"));
            fromWhere = extrasBundle.getString("where");
        }

        if (fromWhere.contains("home"))
        {
            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/home.html");

            ldtOtherServices.setVisibility(View.GONE);
            webHome.setBackgroundColor(Color.TRANSPARENT);
            txtToolbarText.setText("Home");
        }else if (fromWhere.contains("certificate"))
        {
            txtToolbarText.setText("Certificate");
            ldtOtherServices.setVisibility(View.GONE);
            webHome.setVisibility(View.GONE);
        }else if (fromWhere.contains("otherServices"))
        {
            txtToolbarText.setText("Other Services");
            ldtOtherServices.setVisibility(View.GONE);
            webHome.setVisibility(View.GONE);
            ldtOtherServices.setVisibility(View.VISIBLE);

        }else if (fromWhere.contains("sales")){
            txtToolbarText.setText("Sales");
            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/sales.html");
            webHome.setBackgroundColor(Color.TRANSPARENT);
           /* ldtOtherServices.setVisibility(View.GONE);
            ldtCalibration.setVisibility(View.GONE);*/

        }else if (fromWhere.contains("repair")){


            webHome.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/repair.html");
            txtToolbarText.setText("Repair");
            webHome.setBackgroundColor(Color.TRANSPARENT);

           /* ldtOtherServices.setVisibility(View.GONE);
            ldtCalibration.setVisibility(View.GONE);*/
        }



    }


}
