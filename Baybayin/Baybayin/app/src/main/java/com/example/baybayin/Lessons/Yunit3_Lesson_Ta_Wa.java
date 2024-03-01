package com.example.baybayin.Lessons;

import static com.example.baybayin.Fragment.Home.Quiz_Note;
import static com.example.baybayin.Fragment.Home.SHARED_PREF_NAME;
import static com.example.baybayin.Fragment.Profile.Yunit3_Ra_sa;
import static com.example.baybayin.Fragment.Profile.Yunit3_Ta_wa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.baybayin.R;
import com.example.baybayin.databinding.Yunit3LessonTaWaBinding;

public class Yunit3_Lesson_Ta_Wa extends AppCompatActivity {

    Yunit3LessonTaWaBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Yunit3LessonTaWaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        binding.susunod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Quiz_Note, "tawa");
                editor.putFloat(Yunit3_Ta_wa, 0.1f);
                editor.commit();
                Intent intent = new Intent(Yunit3_Lesson_Ta_Wa.this, com.example.baybayin.Quiz_Note.Quiz_Note.class);
                startActivity(intent);
            }
        });

        binding.bumalik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
}