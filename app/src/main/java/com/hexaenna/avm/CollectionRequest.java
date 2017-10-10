package com.hexaenna.avm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class CollectionRequest extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ldtHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_request_activity);
        ldtHome = (LinearLayout) findViewById(R.id.ldtHome);
        ldtHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ldtHome:
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                break;
        }
    }
}
