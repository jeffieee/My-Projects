package com.example.baybayin.Utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baybayin.R;
import com.example.baybayin.databinding.UpdateNotesBinding;

import org.bson.types.ObjectId;

import io.realm.Realm;

public class Update_Notes extends AppCompatActivity {

    UpdateNotesBinding binding;
    Realm realm;
    ObjectId noteId;



    EditText updateTitle, updateContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button eastBack = binding.eastBack;
        updateTitle = binding.updateTitle;
        updateContent = binding.updateContent;
        Button updateBtn = binding.saveNote;

        // Get the noteId passed from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String noteIdString = extras.getString("noteId");
            noteId = new ObjectId(noteIdString);
        }

        eastBack.setOnClickListener(view -> {
            finish();
        });

        // Load existing note details and display for updating
        loadNoteDetails();

        updateBtn.setOnClickListener(view -> {
            String updatedTitle = updateTitle.getText().toString();
            String updatedContent = updateContent.getText().toString();

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Realm realm = Realm.getDefaultInstance();
                    Note existingNote = realm.where(Note.class).equalTo("_id", noteId).findFirst();

                    if (existingNote != null) {
                        try {
                            realm.executeTransaction(r -> {
                                existingNote.setTitle(updatedTitle);
                                existingNote.setDescription(updatedContent);
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Handle the error or show an error message if the transaction fails
                            runOnUiThread(() -> Toast.makeText(Update_Notes.this, "Failed to update note. Please try again.", Toast.LENGTH_SHORT).show());
                        } finally {
                            realm.close();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    // Finish the activity after the transaction is completed
                    finish();
                }
            }.execute();
        });

    }

    private void loadNoteDetails() {
        Realm realm = Realm.getDefaultInstance();
        Note existingNote = realm.where(Note.class).equalTo("_id", noteId).findFirst();

        if (existingNote != null) {
            // Display existing note details for updating
            binding.updateTitle.setText(existingNote.getTitle());
            binding.updateContent.setText(existingNote.getDescription());
        }

        realm.close();
    }
}

