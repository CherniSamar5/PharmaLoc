package com.example.androidprojectnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Flowapp2 extends AppCompatActivity {
   
    Button login; // Assuming this is the "Login" button
    Button signUp; // Assuming this is the "Sign Up" button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowapp2);

        signUp = findViewById(R.id.registernewaccount);
        login = findViewById(R.id.logintoexistingaccount);
         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(Flowapp2.this, LoginPage.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(intent);
             }
         });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the register page
                Intent intent = new Intent(Flowapp2.this, RegisterPage.class);
                startActivity(intent);
            }
        });

    }
}
