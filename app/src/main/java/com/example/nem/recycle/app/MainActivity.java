package com.example.nem.recycle.app;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;

/**
 * Класс основной активности, содержащей в себе фрагмент.
 */
public class MainActivity extends AppCompatActivity {

    private String FRAGMENT_INSTANCE_NAME = "fragment";
    public PlaceholderFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isOnline()) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
            setSupportActionBar(toolbar);
            FragmentManager fm = getSupportFragmentManager();

            // Если фрагмент уже существует (после поворота экрана, например), он восстанавливается, если нет - создается новый.
            fragment = (PlaceholderFragment) fm.findFragmentByTag(FRAGMENT_INSTANCE_NAME);
            if (fragment == null) {
                fragment = new PlaceholderFragment();
                fm.beginTransaction().add(R.id.container, fragment, FRAGMENT_INSTANCE_NAME).commit();
            }
        }
        else Toast.makeText(this, "Internet Error", Toast.LENGTH_LONG).show();

    }



    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return  (netInfo != null && netInfo.isConnectedOrConnecting());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        new SortingFragment().show(getSupportFragmentManager(), "sort");
        return true;
    }

}
