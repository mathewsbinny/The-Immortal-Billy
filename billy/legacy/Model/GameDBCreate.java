package Model;

import GameExceptions.GameException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Class: GameDBCreate
 * @author: Mathews Binny, Jinyi Zhen
 * @version: 1.0
 * Course: ITEC 3860
 * Written: April 28, 2024
 * Purpose: This class creates the billy.db if it doesn't exist already
 */

public class GameDBCreate {
    SQLiteDB sDB;
    private String dbName;

    public GameDBCreate() {
        dbName = "billy.db"; // might have to be the account method
    }

    public GameDBCreate(String dbName) {
        this.dbName = "billy.db"; // might have to be the account method
    }

    /**
     * Method: buildTables
     * Purpose: Build all tables
     * @return void
     * @throws SQLException
     */
    public void buildTables() throws GameException {
        buildAccount();
        buildCharacter();
        buildRoom();
        buildExit();
        buildItem();
        buildMonster();
        buildTracking();
        buildHelpMenu();
        buildCharacterItem();
        buildItemRoom();
        buildCharacterAttack();
        buildMonsterAttack();
        buildPlayerInput();
        buildPuzzle();
        buildPuzzleReward();
        buildPuzzleCollider();
        buildPuzzleChoice();
    }

    /**
     * Method: buildAccount
     * Purpose: Build the Account table and load data
     * @return void
     * @throws SQLException
     */
    public void buildAccount() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/Account.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("Account.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildCharacter
     * Purpose: Build the Character table and load data
     * @return void
     * @throws SQLException
     */
    public void buildCharacter() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/Character.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("Character.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildRoom
     * Purpose: Build the Room table and load data
     * @return void
     * @throws SQLException
     */
    public void buildRoom() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/Room.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("Room.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildExit
     * Purpose: Build the Exit table and load data
     * @return void
     * @throws SQLException
     */
    public void buildExit() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/Exit.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("Exit.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildMonster
     * Purpose: Build the Monster table and load data
     * @return void
     * @throws SQLException
     */
    public void buildMonster() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/Monster.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("Monster.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildItem
     * Purpose: Build the Item table and load data
     * @return void
     * @throws SQLException
     */
    public void buildItem() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/Item.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("Item.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildHelpMenu
     * Purpose: Build the HelpMenu table and load data
     * @return void
     * @throws SQLException
     */
    public void buildHelpMenu() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/HelpMenu.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("HelpMenu.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildTracking
     * Purpose: Build the Tracking table and load data
     * @return void
     * @throws SQLException
     */
    public void buildTracking() throws GameException{
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/Tracking.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("Tracking.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }

    }

    /**
     * Method: buildCharacterAttack
     * Purpose: Build the CharacterAttack table and load data
     * @return void
     * @throws SQLException
     */
    public void buildCharacterAttack() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/CharacterAttack.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("CharacterAttack.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildMonsterAttack
     * Purpose: Build the MonsterAttack table and load data
     * @return void
     * @throws SQLException
     */
    public void buildMonsterAttack() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/MonsterAttack.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("MonsterAttack.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildCharacterItem
     * Purpose: Build the CharacterItem table and load data
     * @return void
     * @throws SQLException
     */
    public void buildCharacterItem() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/CharacterItem.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("CharacterItem.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }

    }

    /**
     * Method: buildItemRoom
     * Purpose: Build the ItemRoom table and load data
     * @return void
     * @throws SQLException
     */
    public void buildItemRoom() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/ItemRoom.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("ItemRoom.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }

    }

    /**
     * Method: buildPuzzle
     * Purpose: Build the Puzzle table and load data
     * @return void
     * @throws SQLException
     */
    public void buildPuzzle() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/Puzzle.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("Puzzle.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildPlayerInput
     * Purpose: Build the PlayerInput table and load data -- this is for puzzle functionalities
     * @return void
     * @throws SQLException
     */
    public void buildPlayerInput() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/PlayerInput.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("PlayerInput.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e) {
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildPuzzleCollidor
     * Purpose: Build the PuzzleCollider table and load data
     * @return void
     * @throws SQLException
     */
    public void buildPuzzleCollider() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/PuzzleCollider.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("PuzzleCollider.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildPuzzleReward
     * Purpose: Build the PuzzleReward table and load data
     * @return void
     * @throws SQLException
     */
    public void buildPuzzleReward() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/PuzzleReward.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("PuzzleReward.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }

    /**
     * Method: buildPuzzleChoice
     * Purpose: Build the PuzzleChoice table and load data
     * @return void
     * @throws SQLException
     */
    public void buildPuzzleChoice() throws GameException {
        try{
            sDB = new SQLiteDB(dbName);
            FileReader fr;
            try{
                fr = new FileReader("src/PuzzleChoice.txt");
                Scanner inFile = new Scanner(fr);
                while(inFile.hasNextLine()){
                    String sql = inFile.nextLine();
                    sDB.updateDB(sql);
                }
                inFile.close();
            }catch(FileNotFoundException e){
                throw new GameException("PuzzleChoice.txt was not found");
            }
            sDB.close();
        }catch(SQLException | ClassNotFoundException e){
            throw new GameException("Error reading db " + e.getMessage());
        }
    }
}
