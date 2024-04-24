package Controller;

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

    public void setAccountPassword(String accountPassword){
        this.accountPassword = accountPassword;
    } // End setAccountPassword Method

    public String buildPasswordCombination(){
        return  accountUserName + " - " + accountPassword;
    }





} //End Account Class
