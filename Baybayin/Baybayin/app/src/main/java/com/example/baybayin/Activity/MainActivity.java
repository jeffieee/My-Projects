package com.example.baybayin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.baybayin.Drawing.Yunit2_Drawing;
import com.example.baybayin.Fragment.Home;
import com.example.baybayin.Fragment.Note_Bayascript;
import com.example.baybayin.Fragment.Profile;
import com.example.baybayin.R;
import com.example.baybayin.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new Home());



        // Set animation for BottomNavigationView
        binding.bottomNavigationView.setItemHorizontalTranslationEnabled(false);
        binding.bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        binding.bottomNavigationView.setAnimationCacheEnabled(true);
        binding.bottomNavigationView.setItemTextAppearanceActive(R.style.BottomNavigationTextActive);
        binding.bottomNavigationView.setItemTextAppearanceInactive(R.style.BottomNavigationTextInactive);

        // Get a reference to the menu item whose icon you want to change
        MenuItem Home = binding.bottomNavigationView.getMenu().findItem(R.id.home);
        MenuItem Note = binding.bottomNavigationView.getMenu().findItem(R.id.note);
        MenuItem Profile = binding.bottomNavigationView.getMenu().findItem(R.id.profile);
        Home.setIcon(R.drawable.custom_ic_home);

// Set the icon for the search menu item using your custom drawable
        Intent intent = getIntent();

        if (intent.hasExtra("Refresh")) {
            // Retrieve the boolean value
                replaceFragment(new Home());
                overridePendingTransition(0, 0);
        }



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new Home());
                Home.setIcon(R.drawable.custom_ic_home);
                // Handle home item selected

            } else{

            }if (itemId == R.id.note) {
                replaceFragment(new Note_Bayascript());
                Note.setIcon(R.drawable.custom_ic_note);

                // Handle search item selected
            } else if (itemId == R.id.profile) {
                replaceFragment(new Profile());
                Profile.setIcon(R.drawable.custom_ic_profile);

                // Handle map item selected
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public void setBottomNavigationSelectedItem(int itemId) {
            binding.bottomNavigationView.setSelectedItemId(itemId);
        }
}
