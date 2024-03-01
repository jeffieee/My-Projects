package com.example.baybayin.Lessons;

import static com.example.baybayin.Fragment.Home.Quiz_Note;
import static com.example.baybayin.Fragment.Home.SHARED_PREF_NAME;
import static com.example.baybayin.Fragment.Profile.Yunit3_Ba_ka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.baybayin.Fragment.Home;
import com.example.baybayin.Quiz_Note.Quiz_Note;
import com.example.baybayin.R;
import com.example.baybayin.databinding.Yunit3LessonBakaBinding;

public class Yunit3_Lesson_Ba_Ka extends AppCompatActivity {

    Yunit3LessonBakaBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Yunit3LessonBakaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();


        binding.susunod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Home.Quiz_Note, "baka");
                editor.putFloat(Yunit3_Ba_ka, 0.1f);
                editor.commit();
                Intent intent = new Intent(Yunit3_Lesson_Ba_Ka.this, com.example.baybayin.Quiz_Note.Quiz_Note.class);
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