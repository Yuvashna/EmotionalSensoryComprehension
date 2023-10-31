package com.example.emotioncomprehension;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    static String gUsername;
    static String sPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        TextView username =(TextView) findViewById(R.id.username);
        TextView password =(TextView) findViewById(R.id.password);


        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

            //admin and admin

        loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(MainActivity.this, "Please wait...", Toast.LENGTH_SHORT).show();

                    gUsername=username.getText().toString();
                    sPassword=password.getText().toString();

                    if (gUsername.equals("Clara")){
                        if(sPassword.equals("0123")){
                            Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
                            openHome();
                        }
                    }
                }
            });
    }

        public static String getgUsername(){
            return gUsername;
        }

    //@Override
    public void onBackedPressed() {finishAffinity();
    }

    public void openHome(){
        Intent intent =new Intent(this, home.class);
        startActivity(intent);
    }

}