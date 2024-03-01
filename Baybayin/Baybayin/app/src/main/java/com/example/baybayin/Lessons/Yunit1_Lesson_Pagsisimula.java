package com.example.baybayin.Lessons;

import static com.example.baybayin.Fragment.Home.Key;
import static com.example.baybayin.Fragment.Home.SHARED_PREF_NAME;
import static com.example.baybayin.Fragment.Profile.Yunit1Pagsisimula;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.baybayin.R;
import com.example.baybayin.databinding.LessonPagsisimulaBinding;

public class Yunit1_Lesson_Pagsisimula extends AppCompatActivity {

    LessonPagsisimulaBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LessonPagsisimulaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = preferences.edit();

        binding.susunod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(Key,"true");
                editor.putFloat(Yunit1Pagsisimula,0.5f);
                editor.commit();
                Intent intent = new Intent(Yunit1_Lesson_Pagsisimula.this, Yunit1_Lesson_Kahalagahan.class);
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