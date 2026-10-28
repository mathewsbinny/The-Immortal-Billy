package Model;

import Controller.HelpMenu;
import GameExceptions.GameException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HelpMenuDB {
    /**
     * Method: getHelpMenu
     * Purpose: gets all help menu rows
     * @return ArrayList<Room>
     * @throws SQLException
     */
    public ArrayList<HelpMenu> getHelpMenu() throws GameException {
        ArrayList<HelpMenu> helpMenu = new ArrayList<HelpMenu>();
        try {
            SQLiteDB sdb = new SQLiteDB();
            String sql = "Select * from HelpMenu";
            ResultSet rs = sdb.queryDB(sql);
            while (rs.next()) {
                HelpMenu hm = new HelpMenu();
                hm.setHintID(rs.getInt("hintID"));
                hm.setHintName(rs.getString("hintName"));
                hm.setHintDescription(rs.getString("hintDescription"));
                helpMenu.add(hm);
            }
            //Close the SQLiteDB connection since SQLite only allows one updater
            sdb.close();
        }
        catch(SQLException | ClassNotFoundException ex) {
            throw new GameException("Problem reading database");
        }
        return helpMenu;
    }
}
