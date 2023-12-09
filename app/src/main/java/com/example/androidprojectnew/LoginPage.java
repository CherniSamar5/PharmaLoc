package com.example.androidprojectnew;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button login_button;

    TextView myRegister;
    FirebaseAuth mAuth;
    DatabaseReference dataBaseRefernace = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-login-signup-69d6b-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        login_button = findViewById(R.id.login_button);
        myRegister = findViewById(R.id.registertext);
        mAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("champ vide ! remplissez-le");
                } else if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("champ vide ! remplissez-le");
                } else {
                    dataBaseRefernace.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String email1 = encodeEmail(email);

                            if(snapshot.hasChild(email1)){
                                final String getPassword = snapshot.child(email1).child("Password").getValue(String.class);
                                if(getPassword.equals(password)){
                                    Toast.makeText(LoginPage.this , "Logged in succesfully" ,Toast.LENGTH_SHORT).show();
                                    final String getUsername = snapshot.child(email1).child("UserName").getValue(String.class);

                                    // Create a new instance of HomeFragment and pass the username
                                    HomeFragment homeFragment = HomeFragment.newInstance(getUsername, null);
                                    // Replace the current fragment with HomeFragment
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fromLogin,new HomeFragment() )
                                            .addToBackStack(null)
                                            .commit();
                                } else {
                                    Toast.makeText(LoginPage.this , "Wrong Password" , Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginPage.this , "Wrong Email which doesn't exists" , Toast.LENGTH_SHORT).show();
                            }

                            SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("cle", snapshot.child(email1).child("UserName").getValue(String.class));
                            editor.apply();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });
                }
            }
        });

        myRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle registration
            }
        });
    }

    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }
}
