package com.pangchavez.lottopseudo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void btnClick(View view)
    {
            Intent mainMenu = new Intent(this, MainActivity.class);
            startActivity(mainMenu);
    }


}
