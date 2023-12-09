package com.example.androidprojectnew;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androidprojectnew.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    TextView usernameTextView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
        // Replace the contents of the FrameLayout with the HomeFragment


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameTextView = getView().findViewById(R.id.username_textview);


        SharedPreferences preferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String storedValue = preferences.getString("cle", "defaultValue");
        usernameTextView.setText(" " + storedValue);

        // Check if arguments are not null and set the username
        if (getArguments() != null) {
            String username = getArguments().getString(ARG_PARAM1);

            if (username != null) {
                // Do something with the username in your fragment
                Log.d("HomeFragment", "Received Username: " + username);

                // For example, display the username in a TextView
                //usernameTextView.setText("  " + "samar");
            }
        }

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

    // Your existing method for replacing fragments
    protected void replaceFragment(Fragment fragment) {

        getParentFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();



    }

}
