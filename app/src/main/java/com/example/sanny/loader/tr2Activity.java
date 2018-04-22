package com.example.sanny.loader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tr2Activity extends AppCompatActivity {
private Button mbacktoroute2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tr2);

        mbacktoroute2=(Button)  findViewById(R.id.backtoroute2);

        mbacktoroute2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(tr2Activity.this,BusRoute_Activity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}
