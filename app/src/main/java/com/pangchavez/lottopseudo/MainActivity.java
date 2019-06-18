package com.pangchavez.lottopseudo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Todo: Add an onTouchListener and change button color to orange on ACTION_DOWN

    public void playButtonClick(View view)
    {
        Intent chooseIntent = new Intent(this, ChooseGameActivity.class);
        startActivity(chooseIntent);
    }

    public void aboutButtonClick(View view)
    {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
    }
}
