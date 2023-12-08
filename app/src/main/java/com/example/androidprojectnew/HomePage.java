package com.example.androidprojectnew;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.androidprojectnew.R;
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



    }

    protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}