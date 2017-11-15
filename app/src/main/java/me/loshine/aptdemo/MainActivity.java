package me.loshine.aptdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.loshine.annotation.BindLayout;
import me.loshine.annotation.Knife;

@BindLayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Knife.bind(this);
    }
}
