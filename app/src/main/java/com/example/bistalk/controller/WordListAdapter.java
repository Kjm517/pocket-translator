package com.example.bistalk.controller;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bistalk.R;
import com.example.bistalk.model.Wordbank;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder>{

    WordListActivity wordListActivity = new WordListActivity();
    private ArrayList<Wordbank> wordbankArrayList;

    ;

    public WordListAdapter(ArrayList<Wordbank> wordbankArrayList) {
        this.wordbankArrayList = wordbankArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rowitems, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Wordbank wordBankData = wordbankArrayList.get(position);

        holder.btnConfirm.setVisibility(View.INVISIBLE);
        holder.btnDelete.setVisibility(View.INVISIBLE);


        holder.englishText.setEnabled(false);
        holder.cebuanoText.setEnabled(false);
        holder.pronunciationText.setEnabled(false);

        holder.englishText.setText(wordbankArrayList.get(position).getEnglish());
        holder.cebuanoText.setText(wordbankArrayList.get(position).getCebuano());
        holder.pronunciationText.setText(wordbankArrayList.get(position).getPronunciation());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+ wordBankData.getEnglish(),Toast.LENGTH_LONG).show();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int actualPosition = holder.getAdapterPosition();
                DatabaseReference databaseReference;

                // removing data from DB
                databaseReference = FirebaseDatabase.getInstance().getReference().child("wordbank")
                        .child(wordbankArrayList.get(actualPosition).getEnglish());
                databaseReference.removeValue();


                // removing data from list recycler view
                wordbankArrayList.remove(actualPosition);
                notifyItemRemoved(actualPosition);
                notifyItemRangeChanged(actualPosition, wordbankArrayList.size());
            }
        });

        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int actualPosition = holder.getAdapterPosition();
                DatabaseReference databaseReference;
                Wordbank wordbank = new Wordbank();

                databaseReference = FirebaseDatabase.getInstance().getReference().child("wordbank")
                        .child(wordbankArrayList.get(actualPosition).getEnglish());
                databaseReference.removeValue();

                // insert data from DB
                databaseReference = FirebaseDatabase.getInstance().getReference();
                wordbank.setCebuano(holder.cebuanoText.getText().toString());
                wordbank.setEnglish(holder.englishText.getText().toString());
                wordbank.setPronunciation(holder.pronunciationText.getText().toString());

                databaseReference.child("wordbank").child(wordbank.getEnglish()).setValue(wordbank);


                // removing data from list recycler view
                wordbankArrayList.get(actualPosition).setCebuano(holder.cebuanoText.getText().toString());
                wordbankArrayList.get(actualPosition).setEnglish(holder.englishText.getText().toString());
                wordbankArrayList.get(actualPosition).setPronunciation(holder.pronunciationText.getText().toString());

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordbankArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Button btnDelete;
        public Button btnConfirm;

        public EditText englishText;
        public EditText cebuanoText;
        public EditText pronunciationText;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            this.btnConfirm = (Button) itemView.findViewById(R.id.btnConfirm);
            this.englishText = (EditText) itemView.findViewById(R.id.englishText);
            this.cebuanoText = (EditText) itemView.findViewById(R.id.cebuanoText);
            this.pronunciationText = (EditText) itemView.findViewById(R.id.pronunciationText);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}