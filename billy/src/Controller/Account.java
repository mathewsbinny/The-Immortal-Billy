package Controller;

import GameExceptions.GameException;

public class Account {

    //Variables
    private int accountID;

    private String accountUserName;

    private String accountPassword;

    //Getters
    public int getAccountID(){
        return this.accountID;
    } //End getAccountID Method

    public String getAccountUserName(){
        return this.accountUserName;
    } //End getAccountUserName Method

    public String getAccountPassword(){
        return this.accountPassword;
    } //End getAccountPassword Method


    // Setters

    public void setAccountID(int accountID){
        this.accountID = accountID;
    } // End setAccountID Method

    public void setAccountUserName(String accountUserName){
        this.accountUserName = accountUserName;
    } // End setAccountUserName Method

    public void setAccountPassword(String accountPassword) throws GameException {
        if(accountPassword.length() < 4){
            throw new GameException("Password must be at least four characters");
        }
        else{
            this.accountPassword = accountPassword;
        }

    } // End setAccountPassword Method

    public String createDBName(){
        return accountUserName + "-" + accountPassword + ".db";
    }






} //End Account Class
