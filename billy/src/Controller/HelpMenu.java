package Controller;

public class HelpMenu {
    private int hintID;
    private String hintName;
    private String hintDescription;

    public int getHintID() {
        return hintID;
    }

    public void setHintID(int hintID) {
        this.hintID = hintID;
    }

    public String getHintName() {
        return hintName;
    }

    public void setHintName(String hintName) {
        this.hintName = hintName;
    }

    public String getHintDescription() {
        return hintDescription;
    }

    public void setHintDescription(String hintDescription) {
        this.hintDescription = hintDescription;
    }

    /**
     * Method: toString
     * Purpose: Returns a String of the HelpMenu class
     * @return
     */
    @Override
    public String toString() {
        return "HelpMenu hint ID = " + hintID + "\nhint Name = " + hintName + "\ncharacter description = " + hintDescription;
    }
}
