package com.example.bistalk.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bistalk.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.common.model.RemoteModelManager;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslateRemoteModel;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final int RECOGNIZER_RESULT = 1;
    private SpeechRecognizer speechRecognizer;
    private int MIC_PERMISSION_CODE = 1;
    private String languageFrom;
    private String languageTo;
    private String getWordFromMic;
    private String translatedWord;
    private String[] languageArray;
    private int counter = 0;
    int count = 0;

    Spinner translateFrom;
    Spinner translateTo;
    ProgressBar progressBar;
    TextView textDisplay;
    ImageView btnMic;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnMic = findViewById(R.id.btnMic);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        navigationView = findViewById(R.id.bottomNavigation);
        translateFrom = findViewById(R.id.translateFrom);
        translateTo = findViewById(R.id.translateTo);
        progressBar = findViewById(R.id.progressBar);
        textDisplay = findViewById(R.id.textDisplay);


        navigationView.setSelectedItemId(R.id.home);

        // REQUEST PERMISSION
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        // Set adapter for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.language, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        translateFrom.setAdapter(adapter);
        translateTo.setAdapter(adapter);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction;
                Intent intent;

                switch (item.getItemId()) {
                    case R.id.addWord:
                        intent = new Intent(MainActivity.this, AddWordActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.wordList:
                        intent = new Intent(MainActivity.this, WordListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                }
                return true;
            }
        });

        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to text");
                startActivityForResult(speechIntent, RECOGNIZER_RESULT);
            }
        });

        translateFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);

                if (item != null) {
                    languageFrom = item.toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        translateTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null) {
                    languageTo = item.toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        languageArray = getResources().getStringArray(R.array.language);

        setProgressBar();
        btnMic.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        textDisplay.setText("Downloading data.. Please wait");

        for(int i = 0; i < languageArray.length; i++) {
            downloadTranslation(this.getLanguage(languageArray[i]));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            getWordFromMic = matches.get(0).toString();
            translateLanguage();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},MIC_PERMISSION_CODE);
        }
    }

    private void translateLanguage() {
        System.out.println("Language From: " + this.getLanguage(languageFrom));
        System.out.println("Language To: " + this.getLanguage(languageTo));

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(this.getLanguage(languageFrom))
                        .setTargetLanguage(this.getLanguage(languageTo))
                        .build();
        final Translator translator = Translation.getClient(options);
        getLifecycle().addObserver(translator);

        translator.translate(getWordFromMic)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                translatedWord = String.valueOf(o);
                                System.out.println("Translate: " + translatedWord);
                                Intent intent = new Intent(MainActivity.this, TranslationActivity.class);
                                intent.putExtra("translatedWord", translatedWord);
                                startActivity(intent);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
    }

    private String getLanguage(String selectedSpinnerItem) {
        switch (selectedSpinnerItem) {
            case "Tagalog":
                return TranslateLanguage.TAGALOG;
            case "English":
                return TranslateLanguage.ENGLISH;
            case "Japanese":
                return TranslateLanguage.JAPANESE;
            case "Korean":
                return TranslateLanguage.KOREAN;
            case "French":
                return TranslateLanguage.FRENCH;
            case "Italian":
                return TranslateLanguage.ITALIAN;
        }
        return selectedSpinnerItem;
    }


    private void downloadTranslation(String language) {

        System.out.println("Language: " + language);

        RemoteModelManager modelManager = RemoteModelManager.getInstance();

        // Get translation models stored on the device.
        modelManager.getDownloadedModels(TranslateRemoteModel.class)
                .addOnSuccessListener(new OnSuccessListener<Set<TranslateRemoteModel>>() {
                    @Override
                    public void onSuccess(Set<TranslateRemoteModel> translateRemoteModels) {
                        System.out.println("DONE DOWNLOAD MANAGER");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("FAIL DOWNLOAD MANAGER");
                    }
                });

//        // Delete the model if it's on the device.
//        TranslateRemoteModel deleteLanguageModel =
//                new TranslateRemoteModel.Builder(language).build();
//        modelManager.deleteDownloadedModel(deleteLanguageModel)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        System.out.println("DONE DELETE LANGUAGE MODEL");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        System.out.println("FAIL DELETE LANGUAGE MODEL");
//                    }
//                });

        TranslateRemoteModel downloadLanguageModel =
                new TranslateRemoteModel.Builder(language).build();
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        modelManager.download(downloadLanguageModel, conditions)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("SUCCESS DOWNLOAD LANGUAGE MODEL");


                        // If done downloading all languages
                        counter++;
                        if(counter == languageArray.length) {
                            progressBar.setVisibility(View.INVISIBLE);
                            textDisplay.setText("Tap to speak");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("ERROR: " + e);
                        System.out.println("FAIL DOWNLOAD LANGUAGE MODEL");
                    }
                });
    }

    private void setProgressBar() {
        Timer timer = new Timer();

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