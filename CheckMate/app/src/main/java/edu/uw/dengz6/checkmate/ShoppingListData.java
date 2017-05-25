package edu.uw.dengz6.checkmate;

public class ShoppingListData {
    private String ownerId;
    private String ownerName;
    private String itemList;
    private int totalCost;
    private int totalShoppingItems;
    private String createdOn;

    public ShoppingListData(String ownerId, String ownerName, String itemList, int totalCost, int totalShoppingItems, String createdOn) {
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.itemList = itemList;
        this.totalCost = totalCost;
        this.totalShoppingItems = totalShoppingItems;
        this.createdOn = createdOn;
    }
}
