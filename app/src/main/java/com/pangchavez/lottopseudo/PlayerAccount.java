package com.pangchavez.lottopseudo;

public class PlayerAccount {

    double currentBalance;

    public static final String INSUFFICIENT_FUNDS = "Insufficient funds";
    public static final String SUCCESSFUL_TRANSACTION = "Successful Transaction";

    public double getCurrentBalance() {
        return this.currentBalance;
    }

    public void setCurrentBalance(double newBalance){
        this.currentBalance = newBalance;
    }

    public String deductFromBalance(double amountToPay){
        if(checkIfEnoughMoney(amountToPay)){
            return  SUCCESSFUL_TRANSACTION;
        } else {
            return INSUFFICIENT_FUNDS;
        }
    }

    public boolean checkIfEnoughMoney(double amountToPay){
        boolean isEnough = false;

        if(currentBalance > amountToPay)
            isEnough = true;

        return isEnough;
    }

}
