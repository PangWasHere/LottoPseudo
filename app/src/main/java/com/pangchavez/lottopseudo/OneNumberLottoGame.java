package com.pangchavez.lottopseudo;

import java.util.Random;

public class OneNumberLottoGame {

    int maximum;
    int guessCounter;
    int attemptsRemaining;
    public int numberToGuess;
    int [] prizes;
     /*

            Quick Guide to the Prizes:
            For games with 0 - 9 range:
            1st attempt = $10, 2nd attempt = $5, 3rd attempt = $2
            For games with 0 - 50 range:
            1st attempt = $100, 2nd attempt = $50, 3rd attempt = $25

      */


    public void NewGame(int maximum, int attemptsRemaining)
    {
        Random randomizer = new Random();
        guessCounter = 0;
        numberToGuess = randomizer.nextInt(maximum);
        this.maximum = maximum;
        this.attemptsRemaining = attemptsRemaining;

        prizes = new int[attemptsRemaining + 1];

        switch (maximum){
            case 9:
                // No need to initialize index 0;
                prizes[1] = 10;     // accessed as prizes[guessCounter] where the player guessed the number the first time so attempt_counter is 1.
                prizes[2] = 5;
                prizes[3] = 2;
                break;
            case 50:
                prizes[1] = 100;
                prizes[2] = 50;
                prizes[3] = 25;
                prizes[4] = 10;
                prizes[5] = 5;
                break;
            default: break;
        }
    }

    public boolean IsCorrect(int guess)
    {
        boolean is_correct = false;

        if(guess == numberToGuess)
            is_correct = true;

        return is_correct;
    }

    public boolean IsNumberLower(int guess)
    {
        boolean is_lower = false;

        if(guess > numberToGuess)
            is_lower = true;

        return is_lower;
    }

    public int CalculatePrize()
    {
        return prizes[guessCounter];
    }
}
