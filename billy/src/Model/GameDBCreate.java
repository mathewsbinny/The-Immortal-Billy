package Model;

import GameExceptions.GameException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Scanner;

public class GameDBCreate {
    SQLiteDB sDB;
    private String dbName;

    public GameDBCreate() {
        dbName = "billy.db"; // might have to be the account method
    }

    public GameDBCreate(String dbName) {
        this.dbName = "billy.db";
    }

    public void buildTables() throws GameException {
        //buildAccount();
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
            throw new GameException("Error reading db");
        }
    }

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
            throw new GameException("Error reading db");
        }
    }

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
            throw new GameException("Error reading db");
        }
    }

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
            throw new GameException("Error reading db");
        }

    }

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
            throw new GameException("Error reading db");
        }
    }

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
            throw new GameException("Error reading db");
        }

    }

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
            throw new GameException("Error reading db");
        }

    }

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
            throw new GameException("Error reading db");
        }
    }

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
            throw new GameException("Error reading db");
        }
    }

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
            throw new GameException("Error reading db");
        }
    }

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
            throw new GameException("Error reading db");
        }
    }

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
            throw new GameException("Error reading db");
        }
    }

}
