package com.example.project2;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.kakao.util.helper.Utility;


public class MainActivity extends AppCompatActivity {
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    RecommendationFragment rankingFragment;
    RankingFragment recommendataionFragment;
    HomeFragment homeFragment;
    TabLayout tabs;

    private View decorView;
    private int	uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility( uiOption );

        if(UserData.getInstance().getIdData() == null) {
            SharedPreferences preferences = getSharedPreferences("UserData", MODE_PRIVATE);
            UserData.getInstance().setIdData(preferences.getString("id",null));
            UserData.getInstance().setNicknameData(preferences.getString("nickname",null));
            UserData.getInstance().setProfileImageData(preferences.getString("profileImage",null));
            UserData.getInstance().setRecordData(preferences.getString("record",null));
        }
        if(UserData.getInstance().getIdData() != null) {
            View profile = findViewById(R.id.profile_button);
            //new ImageLoadToMenu(UserData.getInstance().getProfileImageData(), profile).execute();
        }

        Log.d("hash", Utility.getKeyHash(this));

        tabs = (TabLayout) findViewById(R.id.tabs);
        transaction = fragmentManager.beginTransaction();

        homeFragment = new HomeFragment();
        rankingFragment = new RecommendationFragment();
        recommendataionFragment = new RankingFragment();

        tabs.addTab(tabs.newTab().setText("Home"));
        tabs.addTab(tabs.newTab().setText("Recommendation"));
        tabs.addTab(tabs.newTab().setText("Ranking"));

        fragmentManager.beginTransaction().add(R.id.frame, homeFragment).commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("position","${position}");

                if(position == 0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment).commit();
                }
                else if(position == 1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, rankingFragment).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, recommendataionFragment).commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택되지 않음으로 변경.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO : 이미 선택된 tab이 다시
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.find_button:
                findButtonPushed();
                return true;
            case R.id.profile_button:
                profileButtonPushed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void findButtonPushed() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(intent);
    }

    public void profileButtonPushed() {
        if(UserData.getInstance().getIdData() == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), MyprofileActivity.class);
            startActivity(intent);
        }
    }

    public TabLayout getTabs() {
        return tabs;
    }

}
