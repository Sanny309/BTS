package com.example.sanny.loader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BusRoute_Activity extends AppCompatActivity {
 private Button mtr1 ,mtr2 , mroutetomenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_);


        mtr1=(Button) findViewById(R.id.tr1);
        mtr2=(Button) findViewById(R.id.tr2);
        mroutetomenu=(Button) findViewById(R.id.backroutetomenu);

     mtr1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(BusRoute_Activity.this,tr1Activity.class);
             startActivity(intent);
             finish();
             return;
         }
     });


        mtr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BusRoute_Activity.this,tr2Activity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mroutetomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BusRoute_Activity.this,CutomerMenu_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
