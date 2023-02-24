package com.example.bistalk.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.bistalk.R;
import com.example.bistalk.model.Wordbank;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class TranslationActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    TextView translatedWord;

    BottomNavigationView navigationView;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        translatedWord = (TextView) findViewById(R.id.translatedWord);


        // Get word from speech to text
        intent = getIntent();
        translatedWord.setText(intent.getStringExtra("translatedWord"));
    }
}

































//        // Get DB using keyword
//        String translatedWord = intent.getStringExtra("english");
//        searchWord(translatedWord);
//
//        navigationView = findViewById(R.id.bottomNavigation);
//        navigationView.setSelectedItemId(R.id.home);
//
//        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                FragmentTransaction fragmentTransaction;
//                Intent intent;
//
//                switch (item.getItemId()) {
//                    case R.id.addWord:
//                        intent = new Intent(TranslationActivity.this, AddWordActivity.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.wordList:
//                        intent = new Intent(TranslationActivity.this, WordListActivity.class);
//                        startActivity(intent);
//                        break;
//                    case R.id.logout:
//                        intent = new Intent(TranslationActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                }
//                return true;
//            }
//        });
//    }
//
//    public void searchWord(String msg) {
//        myRef.child("wordbank").child(msg).addListenerForSingleValueEvent(new ValueEventListener() {
//            Wordbank newWordbank = new Wordbank();
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                newWordbank = dataSnapshot.getValue(Wordbank.class);
//
//                if(newWordbank == null){
//                    Toast.makeText(TranslationActivity.this, "Cannot find word!", Toast.LENGTH_SHORT).show();
//                    cebuanoText.setText("Cannot Find Word. Please try again!");
//                    pronunciation.setVisibility(View.INVISIBLE);
//                    audioBtn.setVisibility(View.INVISIBLE);
//                }else{
//                    Toast.makeText(TranslationActivity.this, "Translated!", Toast.LENGTH_SHORT).show();
//
//                    // Get audio from DB
//                    StorageReference mImageRef =
//                            FirebaseStorage.getInstance().getReference("image/" + intent.getStringExtra("english") + ".png");
//                    final long ONE_MEGABYTE = 1024 * 1024;
//                    mImageRef.getBytes(ONE_MEGABYTE)
//                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                @Override
//                                public void onSuccess(byte[] bytes) {
//                                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                    DisplayMetrics dm = new DisplayMetrics();
//                                    getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//                                    imageView.setMinimumHeight(dm.heightPixels);
//                                    imageView.setMinimumWidth(dm.widthPixels);
//
//                                    cebuanoText.setText(newWordbank.getCebuano());
//                                    pronunciation.setText(newWordbank.getPronunciation());
//                                    imageView.setImageBitmap(bm);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle any errors
//                            exception.printStackTrace();
//                        }
//                    });
////                    cebuanoText.setText(newWordbank.getCebuano());
////                    pronunciation.setText(newWordbank.getPronunciation());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(TranslationActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }