package com.example.trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText emailEt, passwordEt;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.rollInBtnRegister);
        registerBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view == registerBtn)
        {
            emailEt = findViewById(R.id.emailRegisterEt);
            passwordEt = findViewById(R.id.passwordRegisterEt);

            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();


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
                Toast.makeText(Register.this, invalidString, Toast.LENGTH_LONG).show();
            }
            else // creating user and adding user to the database
            {
                CreateUser(email, password);
            }
        }
    }

    public void CreateUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Registered", Toast.LENGTH_LONG).show();
                            Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                            startActivity(profileIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(Register.this, "Invalid Email", Toast.LENGTH_LONG).show();
                            }
                            else if (task.getException() instanceof FirebaseNetworkException)
                            {
                                Toast.makeText(Register.this, "Offline", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(Register.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }
}