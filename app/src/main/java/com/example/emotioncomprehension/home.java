package com.example.emotioncomprehension;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView greeting=(TextView)findViewById(R.id.GreetingUser) ;
        greeting.setText(greeting.getText()+MainActivity.getgUsername());
        //find transact button
        Button transactbtn = (Button) findViewById(R.id.btnTransactHome);
        //transact button listener
        transactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransact(v); //show the transact sub-menu
                //find transfer button
                Button transferbtn = (Button) findViewById(R.id.btnTransfer);
                //check visibility
                if (transferbtn.getVisibility()==View.VISIBLE){
                    transferbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(home.this,"Loading...",Toast.LENGTH_SHORT).show();
                            openTransfer();
                        }
                    });
                }
            }
        });


    }

    public void openTransfer(){
        Intent intent =new Intent(this, Transfer_pg.class);
        startActivity(intent);
    }

    public void showTransact(View view) {
        TextView transactBgrd=(TextView) findViewById(R.id.Transactbckgrd);
        ImageView imgSend =(ImageView) findViewById(R.id.sendimg);
        ImageView imgTransfer =(ImageView) findViewById(R.id.transferimg);
        ImageView imgPay =(ImageView) findViewById(R.id.payimg);
        Button btnTransfer = (Button)findViewById(R.id.btnTransfer);

        transactBgrd.setVisibility(View.VISIBLE);
        imgSend.setVisibility(View.VISIBLE);
        imgPay.setVisibility(View.VISIBLE);
        imgTransfer.setVisibility(View.VISIBLE);
        btnTransfer.setVisibility(View.VISIBLE);
    }


    //@Override
    public void onBackedPressed() {finishAffinity();
    }

}