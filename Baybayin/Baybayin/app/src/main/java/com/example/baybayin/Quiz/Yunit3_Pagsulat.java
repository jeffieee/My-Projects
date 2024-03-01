package com.example.baybayin.Quiz;

import static com.example.baybayin.Fragment.Home.Yunit3_intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.baybayin.Drawing.Yunit2_Drawing;
import com.example.baybayin.Drawing.Yunit3_Drawing;
import com.example.baybayin.R;
import com.example.baybayin.databinding.Yunit3PagsulatBinding;

public class Yunit3_Pagsulat extends AppCompatActivity {

    Yunit3PagsulatBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = Yunit3PagsulatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.susunod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Yunit3_Pagsulat.this, Yunit3_Drawing.class);
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