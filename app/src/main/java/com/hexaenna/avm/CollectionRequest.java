package com.hexaenna.avm;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CollectionRequest extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ldtHome,ldtCertificate,ldtOtherServices,ldtCalibration,ldtSales,ldtRepair,ldtCollectionRequest,ldtOnsiteRequest,ldtMblCalibration,ldtProduct,ldtDownload,ldtContact;
    DrawerLayout dLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navView;
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


        toolbar = (Toolbar) findViewById(R.id.toolbarDrawer);
        setSupportActionBar(toolbar);

        setNavigationDrawer();
    }

    private void setNavigationDrawer() {


        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        navView = (NavigationView) findViewById(R.id.navigation_view);
        navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                dLayout.closeDrawers();
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);

                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navView.getMenu().getItem(0).setChecked(true);
                        intent.putExtra("where","home");
                        startActivity(intent);
                        Log.e("click","pressed");
                        break;
                    case R.id.nav_certification:
                        navView.getMenu().getItem(1).setChecked(true);
                        intent.putExtra("where","certificate");
                        startActivity(intent);
                        Log.e("click","pressed");
                        break;
                    case R.id.nav_otherService:
                        navView.getMenu().getItem(2).setChecked(true);
                        intent.putExtra("where","otherServices");
                        startActivity(intent);
                        break;
                    case R.id.nav_calibration:
                        navView.getMenu().getItem(3).setChecked(true);
                        intent.putExtra("where","calibration");
                        startActivity(intent);
                        break;
                    case R.id.nav_sales:
                        navView.getMenu().getItem(4).setChecked(true);
                        intent.putExtra("where","sales");
                        startActivity(intent);
                        break;
                    case R.id.nav_repair:
                        navView.getMenu().getItem(5).setChecked(true);
                        intent.putExtra("where","repair");
                        startActivity(intent);
                        break;
                    case R.id.nav_collection_request:
                        navView.getMenu().getItem(6).setChecked(true);
                        Intent collectionIntent = new Intent(getApplicationContext(),RequestActivity.class);
                        startActivity(collectionIntent);
                        break;
                    case R.id.nav_onsite_request:
                        navView.getMenu().getItem(7).setChecked(true);
                        break;
                    case R.id.nav_mblClaibration:
                        navView.getMenu().getItem(8).setChecked(true);
                        break;
                    case R.id.nav_product:
                        navView.getMenu().getItem(9).setChecked(true);
                        break;
                    case R.id.nav_download:
                        navView.getMenu().getItem(10).setChecked(true);
                        Intent downloadIntent = new Intent(getApplicationContext(),DownloadActivity.class);
                        startActivity(downloadIntent);
                        break;
                    case R.id.nav_contact_us:
                        navView.getMenu().getItem(11).setChecked(true);
                        intent.putExtra("where","contact");
                        startActivity(intent);
                        break;

                }
                return false;
            }
        });

        View header = navView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.name);
        tv_email.setText("raj.amalw@learn2crack.com");

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,dLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        dLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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

            case R.id.ldtDownload:
                Intent downloadIntent = new Intent(getApplicationContext(),DownloadActivity.class);
                startActivity(downloadIntent);
                break;

            case R.id.ldtContactUs:
                intent.putExtra("where","contact");
                startActivity(intent);
                break;

            case R.id.ldtCollectionRequest:
                Intent collectionIntent = new Intent(getApplicationContext(),RequestActivity.class);
                startActivity(collectionIntent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","OnResume");

        int size = navView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestart","OnRestart");
    }
}
