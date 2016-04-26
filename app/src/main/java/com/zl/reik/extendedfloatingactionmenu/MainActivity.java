package com.zl.reik.extendedfloatingactionmenu;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.zl.reik.floatingactionmenulibrary.ExtendedFloatingActionMenu;
import com.zl.reik.floatingactionmenulibrary.FloatingActionMenuButton;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ExtendedFloatingActionMenu fam = (ExtendedFloatingActionMenu) findViewById(R.id.fab);

        Button hideButton = (Button) findViewById(R.id.hide_button);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.collapseAndHide(true);
            }
        });

        Button showButton = (Button) findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.show(true);
            }
        });

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "On Click");
//                fab.toggle();
//            }
//        });
    }
}
