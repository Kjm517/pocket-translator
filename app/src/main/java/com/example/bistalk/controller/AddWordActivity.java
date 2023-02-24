package com.example.bistalk.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bistalk.R;
import com.example.bistalk.model.Wordbank;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddWordActivity extends AppCompatActivity {
    Button btnCreate;
    Button btnCancel;
    EditText cebuanoText;
    EditText englishText;
    EditText pronunciationText;

    FirebaseDatabase database;

    Wordbank wordbank = new Wordbank();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        cebuanoText = (EditText) findViewById(R.id.cebuanoText);
        englishText = (EditText) findViewById(R.id.englishText);
        pronunciationText = (EditText) findViewById(R.id.pronunciationText);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddWordActivity.this, WordListActivity.class);
                startActivity(intent);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    wordbank.setCebuano(cebuanoText.getText().toString());
                    wordbank.setEnglish(englishText.getText().toString());
                    wordbank.setPronunciation(pronunciationText.getText().toString());

                    myRef.child("wordbank").child(wordbank.getEnglish()).setValue(wordbank);
                    Toast.makeText(AddWordActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddWordActivity.this, WordListActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AddWordActivity.this, "FAIL TO ADD WORD!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}