package com.space.search.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import android.os.Bundle;
import com.space.search.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To prevent creating a new fragment on screen rotation
        if (savedInstanceState == null) {
            NavHostFragment host = NavHostFragment.create(R.navigation.navigation);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, host)
                    .setPrimaryNavigationFragment(host)
                    .commit();
        }
    }
}
