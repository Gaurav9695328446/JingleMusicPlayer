package com.example.suneel.musicapp.Activities;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.suneel.musicapp.R;

public class BaseActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        dl = (DrawerLayout) findViewById(R.id.dl);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        final NavigationView nav_view=(NavigationView)findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id= item.getItemId();

                if(id==R.id.song1)
                {
                    Toast.makeText(BaseActivity.this, "Song1", Toast.LENGTH_SHORT).show();

                }

                if(id==R.id.playlist1)
                {
                    Toast.makeText(BaseActivity.this, "Playlist1", Toast.LENGTH_SHORT).show();

                }

                if(id==R.id.artists1)
                {
                    Toast.makeText(BaseActivity.this, "Artist1", Toast.LENGTH_SHORT).show();

                }

                if(id==R.id.genres1)
                {
                    Toast.makeText(BaseActivity.this, "Genres1", Toast.LENGTH_SHORT).show();

                }

                if(id==R.id.albums1)
                {
                    Toast.makeText(BaseActivity.this, "Albums1", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        abdt.syncState();
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
