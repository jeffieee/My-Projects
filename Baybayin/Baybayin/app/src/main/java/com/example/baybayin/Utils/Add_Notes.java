package com.example.baybayin.Utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.baybayin.R;
import com.example.baybayin.databinding.AddNotesBinding;

import org.bson.types.ObjectId;

import io.realm.Realm;

public class Add_Notes extends AppCompatActivity {

    AddNotesBinding binding;

    EditText addTitle, addContent;

    String addTitleIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button eastBack = binding.eastBack;
        addTitle = binding.addTitle;
        addContent = binding.addContent;
        Button saveBtn = binding.saveNote;

        addTitleIsEmpty = addTitle.getText().toString();



        addContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addContent.setTypeface(ResourcesCompat.getFont(Add_Notes.this, R.font.poppins_medium));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addContent.setTypeface(ResourcesCompat.getFont(Add_Notes.this, R.font.baybaying_medium));
            }
        });


        addTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                addTitle.setTypeface(ResourcesCompat.getFont(Add_Notes.this, R.font.poppins_bold));
            }



            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addTitle.setTypeface(ResourcesCompat.getFont(Add_Notes.this, R.font.baybayin_bold));
            }
        });




        eastBack.setOnClickListener(view -> {
            finish();
        });
        saveBtn.setOnClickListener(view -> {
            String title = addTitle.getText().toString();
            String content = addContent.getText().toString();
            long createdTime = System.currentTimeMillis();


            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> {
                        Note note = r.createObject(Note.class, new ObjectId());
                        if (title.isEmpty()){
                            note.setTitle("Untitled");
                        }else{
                            note.setTitle(title);
                        }

                        note.setDescription(content);
                        note.setCreatedTime(createdTime);
                    });
                    realm.close();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    finish();
                }
            }.execute();
        });

    }
}
