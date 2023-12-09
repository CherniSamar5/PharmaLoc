package com.example.androidprojectnew;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidprojectnew.databinding.ActivityHomePageBinding;

public class HomePage extends AppCompatActivity {

    ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            item.setChecked(true);
            if (item.getItemId() == R.id.menu_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.menu_discover) {
                replaceFragment(new DiscoverFragment());
            } else if (item.getItemId() == R.id.menu_stores) {
                replaceFragment(new StoresFragment());
            } else if (item.getItemId() == R.id.menu_account) {
                replaceFragment(new AccountFragment());
            }
            return true;
        });

        // Uncomment the following code if you intend to use the username from an intent
        /*Intent intent = getIntent();
        if (intent != null) {
            String username = intent.getStringExtra("USERNAME");
            if (username != null) {
                // Do something with the username
                TextView usernameTextView = findViewById(R.id.username_textview);
                usernameTextView.setText("  " + username);
            }
        }*/
    }

    protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
