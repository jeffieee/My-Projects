package com.example.baybayin.Lessons;

import static com.example.baybayin.Fragment.Home.SHARED_PREF_NAME;
import static com.example.baybayin.Fragment.Profile.Yunit2Intro;
import static com.example.baybayin.Fragment.Profile.Yunit2Patinig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.baybayin.Fragment.Home;
import com.example.baybayin.Quiz_Note.Quiz_Note;
import com.example.baybayin.R;
import com.example.baybayin.databinding.Yunit2LessonPatinigBinding;

public class Yunit2_Lesson_Patinig extends AppCompatActivity {

    Yunit2LessonPatinigBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Yunit2LessonPatinigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        binding.susunod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Home.Quiz_Note, "yunit2");
                editor.putFloat(Yunit2Patinig, 0.5f);
                editor.commit();
                Intent intent = new Intent(Yunit2_Lesson_Patinig.this, Quiz_Note.class);
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