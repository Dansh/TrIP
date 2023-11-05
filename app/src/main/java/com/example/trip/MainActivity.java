package com.example.trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button navRegisterBtn, rollInBtn;
    EditText emailLoginEt, passwordLoginEt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        navRegisterBtn = findViewById(R.id.navRegisterBtn);
        navRegisterBtn.setOnClickListener(this);

        rollInBtn = findViewById(R.id.rollInBtnLogin);
        rollInBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == navRegisterBtn)
        {
            Intent registerIntent = new Intent(getApplicationContext(), Register.class);
            startActivity(registerIntent); // redirect to register page
        }
        else if (view == rollInBtn)
        {
            emailLoginEt = findViewById(R.id.emailLoginEt);
            passwordLoginEt = findViewById(R.id.passwordLoginEt);

            String email = emailLoginEt.getText().toString();
            String password = passwordLoginEt.getText().toString();


            String invalidString = "";
            if (!AuthInputHandler.isPasswordValid(password))
            { // checks if password is valid
                invalidString += "passwords must include over 7 symbols\n";
            }
            if (!AuthInputHandler.isEmailValid(email))
            { // checks if email is valid
                invalidString += "Invalid Email";
            }


            if (invalidString != "") // checks if the password or email are invalid
            {
                Toast.makeText(MainActivity.this, invalidString, Toast.LENGTH_LONG).show();
            }
            else // trying to log in
            {
                login(email, password);
            }
        }
    }


    public void login(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            // FirebaseUser user = mAuth.getCurrentUser();
                            Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                            startActivity(profileIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}