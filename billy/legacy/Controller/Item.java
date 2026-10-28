package Controller;

public class Item {
    private int itemRoomID;
    private int itemID;
    private String itemName;
    private String itemType;
    private double itemAddHP;
    private double itemAddMaxHP;
    private int itemAddMP;
    private double itemAddDP;
    private double itemAddAP;
    private int itemIsConsumable;

    public int getItemRoomID() {
        return itemRoomID;
    }

    public void setItemRoomID(int itemRoomID) {
        this.itemRoomID = itemRoomID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public double getItemAddHP() {
        return itemAddHP;
    }

    public void setItemAddHP(double itemAddHP) {
        this.itemAddHP = itemAddHP;
    }

    public double getItemAddMaxHP() {
        return itemAddMaxHP;
    }

    public void setItemAddMaxHP(double itemAddMaxHP) {
        this.itemAddMaxHP = itemAddMaxHP;
    }

    public int getItemAddMP() {
        return itemAddMP;
    }

    public void setItemAddMP(int itemAddMP) {
        this.itemAddMP = itemAddMP;
    }

    public double getItemAddDP() {
        return itemAddDP;
    }

    public void setItemAddDP(double itemAddDP) {
        this.itemAddDP = itemAddDP;
    }

    public double getItemAddAP() {
        return itemAddAP;
    }

    public void setItemAddAP(double itemAddAP) {
        this.itemAddAP = itemAddAP;
    }

    public int getItemIsConsumable() {
        return itemIsConsumable;
    }

    public void setItemIsConsumable(int itemIsConsumable) {
        this.itemIsConsumable = itemIsConsumable;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemRoomID=" + itemRoomID +
                ", itemID=" + itemID +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", itemAddHP=" + itemAddHP +
                ", itemAddMaxHP=" + itemAddMaxHP +
                ", itemAddMP=" + itemAddMP +
                ", itemAddDP=" + itemAddDP +
                ", itemAddAP=" + itemAddAP +
                ", itemIsConsumable=" + itemIsConsumable +
                '}';
    }
}
