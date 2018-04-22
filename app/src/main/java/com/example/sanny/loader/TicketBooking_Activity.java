package com.example.sanny.loader;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TicketBooking_Activity extends AppCompatActivity {
    private EditText mcardnumber;
    private Button mSubmitcardnumber , mBackMain1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_booking_);


        mcardnumber=(EditText) findViewById(R.id.card_number);
        mSubmitcardnumber=(Button) findViewById(R.id.submitcardnumber);
        mBackMain1=(Button) findViewById(R.id.back_main1);


       mBackMain1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(TicketBooking_Activity.this,CutomerMenu_Activity.class);
               startActivity(intent);
               finish();
               return;
           }
       });

        mSubmitcardnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TicketBooking_Activity.this ,"sorry we are not connected to my bus DB",Toast.LENGTH_SHORT).show();

            }
        });
    }


}
