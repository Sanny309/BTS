package com.example.sanny.loader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tr1Activity extends AppCompatActivity {
private Button mbacktoroute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tr1);

        mbacktoroute=(Button) findViewById(R.id.backtoroute1);


        mbacktoroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(tr1Activity.this,BusRoute_Activity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
}
