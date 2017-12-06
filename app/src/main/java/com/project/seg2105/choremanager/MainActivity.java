package com.project.seg2105.choremanager;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TaskFragment taskFragment;
    private PeopleFragment peopleFragment;
    private PagerAdapter pageAdapter;
    public User currentUser = new User("test", "test", "test", 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting up drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //TODO: List of all user in drawer layout


        //TODO: Map drawer's items to what they do

        //Page adapter for our ViewPager
        pageAdapter = new PagerAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new TaskFragment(), "Tasks");
        pageAdapter.addFragment(new PeopleFragment(), "People");
        pageAdapter.addFragment(new ShoppingFragment(), "Shopping");

        //Retrieving user
        User user = new User();
        user.setId(getIntent().getIntExtra("Id", 1));
        setUpCurrentUser(user);

        //Set user's name and avatar
        setUpCurrentUser(currentUser);

        //Setting the adapter of our ViewPager
        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pageAdapter);

        //Hooking up our TabLayout with the ViewPager
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentsTitles = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsTitles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentsTitles.add(title);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case 0:
                    taskFragment = (TaskFragment) createdFragment;
                    break;
                case 1:
                    peopleFragment = (PeopleFragment) createdFragment;
                    break;
            }
            return createdFragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void notifyFragments() {
        peopleFragment.getLoaderManager().restartLoader(0, null, peopleFragment);
        taskFragment.refreshUI();
    }

    public void setUpCurrentUser(User user) {
        /*currentUser.setId(user.getId());
        currentUser.setName(user.getName());
        currentUser.setAvatar(user.getAvatar());
        currentUser.setPassword(user.getPassword());
        currentUser.setPoints(user.getPoints());*/

        currentUser = DbHandler.getInstance(this).findUser(user);

        //Set user's name and avatar
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        ImageView avatar = header.findViewById(R.id.imageView);
        avatar.setImageResource(getResources().getIdentifier(currentUser.getAvatar(), "drawable", getPackageName()));

        TextView name = header.findViewById(R.id.name);
        name.setText(currentUser.getName());

        //refresh fragments
        pageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } /*else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
