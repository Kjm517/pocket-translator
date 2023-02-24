package com.example.bistalk.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bistalk.R;
import com.example.bistalk.model.Wordbank;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WordListActivity extends AppCompatActivity {

    Button btnAdd;
    Switch btnEdit;
    Button btnSearch;
    Button btnDelete;
    EditText searchText;
    ProgressBar progressBar;
    BottomNavigationView navigationView;

    Timer timer;
    int count = 0;

    Wordbank wordbank;
    WordListAdapter adapter;
    RecyclerView recyclerView;

    FirebaseDatabase database;

    private ArrayList<Wordbank> wordbankArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnEdit = (Switch) findViewById(R.id.btnEdit);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        searchText = (EditText) findViewById(R.id.searchText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        navigationView = findViewById(R.id.bottomNavigation);
        navigationView.setSelectedItemId(R.id.home);

        wordbankArrayList = new ArrayList<>();

        setProgressBar();
        getData(myRef, wordbankArrayList);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;

                switch (item.getItemId()) {
                    case R.id.addWord:
                        intent = new Intent(WordListActivity.this, AddWordActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.home:
                        intent = new Intent(WordListActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        intent = new Intent(WordListActivity.this, LoginActivity.class);
                        startActivity(intent);
                }
                return true;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("wordbank").child(searchText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        wordbank = dataSnapshot.getValue(Wordbank.class);

                        if (wordbank == null) {
                            Toast.makeText(WordListActivity.this, "Word not found", Toast.LENGTH_SHORT).show();

                        } else if(wordbank.getEnglish() == null) {
                            setAdapter(wordbankArrayList);
                        } else {
                                ArrayList<Wordbank> arrayWord = new ArrayList<>();
                                arrayWord.add(wordbank);
                                setAdapter(arrayWord);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(WordListActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                    final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    if(isChecked) {
                        holder.itemView.findViewById(R.id.btnConfirm).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.btnDelete).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.englishText).setEnabled(true);
                        holder.itemView.findViewById(R.id.cebuanoText).setEnabled(true);
                        holder.itemView.findViewById(R.id.pronunciationText).setEnabled(true);
                    } else {
                        holder.itemView.findViewById(R.id.btnConfirm).setVisibility(View.INVISIBLE);
                        holder.itemView.findViewById(R.id.btnDelete).setVisibility(View.INVISIBLE);
                        holder.itemView.findViewById(R.id.englishText).setEnabled(false);
                        holder.itemView.findViewById(R.id.cebuanoText).setEnabled(false);
                        holder.itemView.findViewById(R.id.pronunciationText).setEnabled(false);
                        btnEdit.setChecked(false);
                    }
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordListActivity.this, AddWordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setAdapter(ArrayList<Wordbank> wordbankArrayList) {
        recyclerView = (RecyclerView) findViewById(R.id.rvWordList);
        adapter = new WordListAdapter(wordbankArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<Wordbank> getData(DatabaseReference myRef, ArrayList<Wordbank> wordbankArray) {

        myRef.child("wordbank").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    Wordbank fromDB = ds.getValue(Wordbank.class);
                    wordbankArray.add(fromDB);
                }

                for(int i = 0; i < wordbankArray.size(); i++) {
                    Log.v("Test:", wordbankArray.get(i).getCebuano());
                }
                progressBar.setVisibility(View.INVISIBLE);
                setAdapter(wordbankArray);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return wordbankArray;
    }

    private void setProgressBar() {
        timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                count++;
                progressBar.setProgress(count);

                if(count == 3) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask, 0, 3);
    }
}