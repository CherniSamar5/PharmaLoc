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
import android.widget.TextView;
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

public class LoginPage extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button login_button;
    private TextView myRegister; // Renamed to follow camelCase convention
    FirebaseAuth mAuth;
    private static final String TAG = "LoginPage";

    DatabaseReference dataBaseRefernace = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-login-signup-69d6b-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        // Obtention des références des éléments de l'interface utilisateur
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        login_button = findViewById(R.id.login_button);
        myRegister = findViewById(R.id.registertext); // Assuming you have a TextView with ID my_register_textview
        mAuth = FirebaseAuth.getInstance();

        // Définition d'un écouteur de clic pour le bouton de connexion
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Récupération des saisies utilisateur
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Vérification si les champs sont vides
                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("champ vide ! remplissez-le");
                } else if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("champ vide ! remplissez-le");
                } else {
                    dataBaseRefernace.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String email1 = encodeEmail(email);

                            // check if email is exists in firebase
                            if(snapshot.hasChild(email1)){

                                //ge the password from firebase
                                final String getPassword = snapshot.child(email1).child("Password").getValue(String.class);
                                if(getPassword.equals(password)){
                                    Toast.makeText(LoginPage.this , "Logged in succesfully" ,Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(LoginPage.this , CurrentLocation.class));
                                    //finish();
                                }
                                else
                                    Toast.makeText(LoginPage.this , "Wrong Password" , Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginPage.this , "Wrong Email which doesn't exists" , Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    // Tentative de connexion
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // La connexion a réussi
                                        Log.d(TAG, "signInWithEmail:success");

                                        // Récupérer le mot de passe (Notez que cela n'est généralement pas recommandé pour des raisons de sécurité)
                                        String retrievedPassword = password;

                                        // Faire quelque chose avec le mot de passe récupéré, si nécessaire

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LoginPage.this, "Login success", Toast.LENGTH_SHORT).show();

                                        // Rediriger vers une autre activité
                                        Intent intent = new Intent(LoginPage.this, CurrentLocation.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        startActivity(intent);
                                    } else {
                                        // La connexion a échoué
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        myRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private String encodeEmail(String email) {
        return email.replace(".", ",");
    }

}
