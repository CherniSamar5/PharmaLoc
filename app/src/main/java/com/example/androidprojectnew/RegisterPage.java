package com.example.androidprojectnew;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity {


    DatabaseReference dataBaseRefernace = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-login-signup-69d6b-default-rtdb.firebaseio.com/");
    EditText userNameText, emailText, passwordText, confirmPasswordText;
    Button register;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        mAuth = FirebaseAuth.getInstance();

        userNameText = findViewById(R.id.username_edittext);
        emailText = findViewById(R.id.email_edittext);
        passwordText = findViewById(R.id.password_edittext);
        confirmPasswordText = findViewById(R.id.confirmPwd_edittext);
        register = findViewById(R.id.register_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName, email, password, confirmPassword;
                userName = userNameText.getText().toString();
                email = emailText.getText().toString();
                password = passwordText.getText().toString();
                confirmPassword = confirmPasswordText.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    userNameText.setError("Champ vide ! Remplissez-le");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    emailText.setError("Champ vide ! Remplissez-le");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordText.setError("Champ vide ! Remplissez-le");
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    confirmPasswordText.setError("Champ vide ! Remplissez-le");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    confirmPasswordText.setError("Les mots de passe ne correspondent pas");
                    return;
                } else {

                    dataBaseRefernace.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String email1 = encodeEmail(email);

                            //check if email is not registred

                            if(snapshot.hasChild(email1)){
                                Toast.makeText(RegisterPage.this , "email already exists" , Toast.LENGTH_SHORT).show();
                            }
                            else{

                                //sending data to firebase real Time
                                dataBaseRefernace.child("users").child(email1).child("UserName").setValue(userName);
                                dataBaseRefernace.child("users").child(email1).child("Password").setValue(password);

                                Intent intent = new Intent(RegisterPage.this, Flowapp2.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(RegisterPage.this, "Sign-up successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterPage.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }

}
