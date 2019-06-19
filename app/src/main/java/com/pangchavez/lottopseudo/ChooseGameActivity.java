package com.pangchavez.lottopseudo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ChooseGameActivity extends AppCompatActivity implements TopFragment.OnFragmentInteractionListener {

    Button btnGuessToNine, btnGuessToFifty, btnBorrowMoney;
    ImageView btnClosePopUp, imgLogo;

    Dialog gameDialog;
    MediaPlayer soundPay;

    // These constants are used to access the StringExtra placed in the Intent
    public static final String EXTRA_MAXIMUM_NUMBER = "MAXIMUM NUMBER";
    public static final String EXTRA_NUMBER_OF_ATTEMPTS = "NUMBER OF ATTEMPTS";

    // There constants are used for SharedPreferences
    public static final String SHARED_PREFS_NAME = "Player Account Balance";
    public static final String SHARED_PREFS_TEXT = "Account Balance";

    TopFragment topFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        topFragment = (TopFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        gameDialog = new Dialog(this);

        btnGuessToNine = findViewById(R.id.btnGuessToNine);
        btnGuessToFifty = findViewById(R.id.btnGuessToFifty);

        soundPay = MediaPlayer.create(getApplicationContext(), R.raw.pay);

        final Intent gameIntent = new Intent(this, PlayGameActivity.class);

        btnGuessToNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Deduct $2 from balance

                if(topFragment.DeductFromBalance(2)){
                    gameIntent.putExtra(EXTRA_MAXIMUM_NUMBER, 9);
                    gameIntent.putExtra(EXTRA_NUMBER_OF_ATTEMPTS, 3);

                    soundPay.start();
                    saveBalance();

                    startActivity(gameIntent);
                } else{
                      showInsufficientFundsPopUp();
                }
            }
        });

        btnGuessToFifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Deduct $5 from balance

                if(topFragment.DeductFromBalance(5)){
                    gameIntent.putExtra(EXTRA_MAXIMUM_NUMBER, 50);
                    gameIntent.putExtra(EXTRA_NUMBER_OF_ATTEMPTS, 5);

                    saveBalance();
                    soundPay.start();

                    startActivity(gameIntent);
                } else{
                    showInsufficientFundsPopUp();
                }
            }
        });

        // Retrieve the saved balance
        loadData();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void showInsufficientFundsPopUp()
    {
        gameDialog.setContentView(R.layout.popup_insufficient_funds);
        btnClosePopUp = gameDialog.findViewById(R.id.btnClosePopUp);
        btnBorrowMoney = gameDialog.findViewById(R.id.btnBorrowMoney);

        btnClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDialog.dismiss();
            }
        });

        btnBorrowMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topFragment.addToBalance(10);
                saveBalance();
                gameDialog.dismiss();
            }
        });

        gameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameDialog.show();
    }

    private void saveBalance()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SHARED_PREFS_TEXT, topFragment.getBalanceAmount());

        editor.apply();
    }

    private void loadData()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_NAME, MODE_PRIVATE);
        int savedBalance = sharedPreferences.getInt(SHARED_PREFS_TEXT, 10);
        topFragment.setBalanceAmount(savedBalance);
    }
}
