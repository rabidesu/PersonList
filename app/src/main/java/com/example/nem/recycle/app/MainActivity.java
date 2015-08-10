package com.example.nem.recycle.app;



import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;

/**
 * Класс основной активности, содержащей в себе фрагмент.
 */
public class MainActivity extends AppCompatActivity {

    private static String FRAGMENT_INSTANCE_NAME = "fragment";
    public PlaceholderFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fm = getSupportFragmentManager();

        // Если фрагмент уже существует (после поворота экрана, например), он восстанавливается, если нет - создается новый.
        fragment = (PlaceholderFragment) fm.findFragmentByTag(FRAGMENT_INSTANCE_NAME);
        if(fragment == null){
            fragment = new PlaceholderFragment();
            fm.beginTransaction().add(R.id.container, fragment, FRAGMENT_INSTANCE_NAME).commit();
        }

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
