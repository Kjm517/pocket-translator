package com.example.bistalk.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bistalk.R;
import com.example.bistalk.model.Account;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    FirebaseDatabase database;
    Account account = new Account();

    EditText fNameText;
    EditText lNameText;
    EditText usernameText;
    EditText passwordText;
    TextView login;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        fNameText = (EditText)findViewById(R.id.fNameTxt);
        lNameText = (EditText)findViewById(R.id.lNameTxt);
        usernameText = (EditText)findViewById(R.id.usernameTxt);
        passwordText = (EditText)findViewById(R.id.passwordTxt);
        submit = (Button)findViewById(R.id.submit);
        login = (TextView) findViewById(R.id.login);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    account.setFirstName(fNameText.getText().toString());
                    account.setLastName(lNameText.getText().toString());
                    account.setUsername(usernameText.getText().toString());
                    account.setPassword(passwordText.getText().toString());

                    myRef.child("account").child(account.getUsername()).setValue(account);

                    Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}