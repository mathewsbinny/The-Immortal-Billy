package Model;

import Controller.Account;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDB {
    /**
     * Method: getAccount
     * Purpose: Gets an Account based upon the supplied username & password
     * @param accountUsername
     * @return Account
     * @throws SQLException
     */
    public Account getAccount(String accountUsername, String accountPassword) throws GameException {
        Account acc = new Account();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from Account WHERE accountUsername = " + accountUsername + " AND accountPassword = " + accountPassword;
            ResultSet rs = sdb.queryDB(sql);
            if (rs.next()) {
                System.out.println(rs.getInt("accountID"));
                acc.setAccountID(rs.getInt("accountID"));
                acc.setAccountUserName(rs.getString("accountUsername"));
                acc.setAccountPassword(rs.getString("accountPassword"));
            } else {
                throw new SQLException("Account for " + accountUsername + " not found");
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new GameException("Account for " + accountUsername + "was not found.");
        }
        return acc;
    }

    /**
     * Method updateAccount
     * Updates the account in the DB
     * throws an exception if the account is not found
     * @param acc - The Account that is being updated
     *
    public void updateAccount(Account acc) throws GameException {
        String sql = "UPDATE Account SET accountUsername = '" + getAccount().getAccountUserName() + "' WHERE roomNumber = " + room.getRoomID() ;
        try {
            SQLiteDB sdb = new SQLiteDB();
            sdb.updateDB(sql);
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new GameException("Update encountered a problem.\n" + e.getMessage());
        }
    }
    */
}
