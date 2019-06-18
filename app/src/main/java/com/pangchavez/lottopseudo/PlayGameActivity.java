package com.pangchavez.lottopseudo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PlayGameActivity extends AppCompatActivity implements TopFragment.OnFragmentInteractionListener {

    int attempts;
    int maximum_number;

    Dialog gameDialog;
    ImageView btnClosePopUp;
    Button btnSubmit, btnPlayNewGame;
    EditText txtGuess;
    TextView txtAttemptsRemaining, txtGameHint, txtPrizeMoney;
    MediaPlayer soundWrong, soundGameOver, soundWin;

    public static final String SHARED_PREFS_NAME = "Player Account Balance";
    public static final String SHARED_PREFS_TEXT = "Account Balance";

    TopFragment topFragment;
    OneNumberLottoGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtGuess = findViewById(R.id.txtGuess);
        txtGameHint = findViewById(R.id.txtGameHint);
        txtAttemptsRemaining = findViewById(R.id.txtAttemptsRemaining);

        topFragment = (TopFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

        // Add sound effects
        soundWrong = MediaPlayer.create(getApplicationContext(), R.raw.wrong);
        soundGameOver = MediaPlayer.create(getApplicationContext(), R.raw.gameover);
        soundWin = MediaPlayer.create(getApplicationContext(), R.raw.winner);

        TextView textView = findViewById(R.id.linkToMenu);
        SpannableString content = new SpannableString("Play Another Game");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(PlayGameActivity.this, ChooseGameActivity.class);
                startActivity(mainIntent);
            }
        });

        gameDialog = new Dialog(this);

        Intent gameIntent = getIntent();
        maximum_number = gameIntent.getIntExtra(ChooseGameActivity.EXTRA_MAXIMUM_NUMBER, 9);
        attempts = gameIntent.getIntExtra(ChooseGameActivity.EXTRA_NUMBER_OF_ATTEMPTS, 3);

        btnSubmit.setOnClickListener(new onSubmitButtonClick());
        txtGuess.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtGuess.setTextColor(Color.WHITE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        game = new OneNumberLottoGame();
        loadData();
        NewGame();
    }

    private void NewGame()
    {
        game.NewGame(maximum_number, attempts);
        txtGuess.setHint("0 - " + maximum_number);
        txtAttemptsRemaining.setText(String.valueOf(attempts));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    public void showWinnerPopup(int prize){
        gameDialog.setContentView(R.layout.popup_winner);
        btnClosePopUp = gameDialog.findViewById(R.id.btnClosePopUp);
        btnPlayNewGame = gameDialog.findViewById(R.id.btnPlayNewGame);
        txtPrizeMoney = gameDialog.findViewById(R.id.txtPrizeMoney);

        txtPrizeMoney.setText("You won $" + prize + ".00!");

        btnClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDialog.dismiss();
            }
        });

        btnPlayNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(PlayGameActivity.this, ChooseGameActivity.class);
                startActivity(mainIntent);
            }
        });

        gameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameDialog.show();
    }

    public void showLostPopup(){
        gameDialog.setContentView(R.layout.popup_lose);
        btnClosePopUp = gameDialog.findViewById(R.id.btnClosePopUp);
        btnPlayNewGame = gameDialog.findViewById(R.id.btnPlayNewGame);

        btnClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDialog.dismiss();
            }
        });

        btnPlayNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(PlayGameActivity.this, ChooseGameActivity.class);
                startActivity(mainIntent);
            }
        });

        gameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        gameDialog.show();
    }

    class onSubmitButtonClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // Check if user inputted a guess
            if(txtGuess.getText().toString().length() > 0){
                int guess = Integer.parseInt(txtGuess.getText().toString());
                attempts--;
                txtAttemptsRemaining.setText(String.valueOf(attempts));
                game.guessCounter++;

                // Check if there are attempts remaining
                if(attempts > 0) {
                    if (game.IsCorrect(guess)) {
                        // Show Winner Dialog
                        txtGameHint.setText("YOU GOT IT!");
                        int prize = game.CalculatePrize();
                        topFragment.addToBalance(prize);

                        showWinnerPopup(prize);
                        soundWin.start();
                    } else {
                        if (game.IsNumberLower(guess)) {
                            // Change Hint Text
                            txtGameHint.setText("Think SMALLER!");
                        } else {
                            txtGameHint.setText("Think BIGGER");
                        }
                        txtGuess.setTextColor(Color.RED);
                        soundWrong.start();
                    }
                } else {
                    // Show No more Attempts Dialog
                    showLostPopup();
                    soundGameOver.start();
                }
            } else {
                txtGameHint.setText("Input a guess.");
            }

            saveBalance();
        }
    }
}

