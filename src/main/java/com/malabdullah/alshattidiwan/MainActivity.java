package com.malabdullah.alshattidiwan;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create drawer menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        home_fragment home_fragment = new home_fragment();
        fragmentTransaction.add(R.id.frame_layout,home_fragment);
        fragmentTransaction.commit();

        navigationView = (NavigationView) findViewById(R.id.drawer_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.home:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        home_fragment home_fragment = new home_fragment();
                        fragmentTransaction.replace(R.id.frame_layout, home_fragment);
                        fragmentTransaction.commit();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.admin:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        login_fragment login_fragment = new login_fragment();
                        fragmentTransaction.replace(R.id.frame_layout, login_fragment);
                        fragmentTransaction.commit();
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


}
