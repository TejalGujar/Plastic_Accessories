package com.example.plasticaccessories;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity
{

    private AppBarConfiguration appBarConfiguration;
    NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btm_nav_layout);

        //get runtime permission from user
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        BottomNavigationView btm_nav_home = findViewById(R.id.btm_nav_home);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.mnAddProduct,R.id.mnProfile,R.id.mnAllProducts).build();
        navController =  Navigation.findNavController(this, R.id.host_fragment);

        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(btm_nav_home,navController);
    }

}
