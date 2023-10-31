package com.example.emotioncomprehension;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class denied extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denied);

        Button btnHome = (Button)findViewById(R.id.btnReturn);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });
    }
    public void openHome(){
        Intent intent =new Intent(this, home.class);
        startActivity(intent);
    }
}