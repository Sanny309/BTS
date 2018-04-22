package com.example.sanny.loader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class CutomerMenu_Activity extends AppCompatActivity {

    private Button mBusRoute ,mBusStatus , mBusTicketBooking , msearch, mSetting , MLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutomer_menu_);


        mBusRoute=(Button) findViewById(R.id.busRoute);
        mBusStatus=(Button) findViewById(R.id.busStatus);
        mBusTicketBooking=(Button) findViewById(R.id.ticketbooking);
        msearch=(Button) findViewById(R.id.search);
        mSetting=(Button) findViewById(R.id.setting);
        MLogout=(Button) findViewById(R.id.logout1);


        mBusStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CutomerMenu_Activity.this, CustomerMapActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


        mBusRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CutomerMenu_Activity.this,BusRoute_Activity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


        mBusTicketBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CutomerMenu_Activity.this,TicketBooking_Activity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        msearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CutomerMenu_Activity.this,NearMapsActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });



    mSetting.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(CutomerMenu_Activity.this,settingActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    });

        MLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(CutomerMenu_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });


    }
}



