package edu.uw.dengz6.checkmate;

public class ShoppingListData {
    public String owner;
    public int totalCost;
    public int totalShoppingItem;
    public String createdOn;


    public ShoppingListData(String owner, int totalCost, int totalShoppingLists, String createdOn) {
        this.owner = owner;
        this.totalCost = totalCost;
        this.totalShoppingItem = totalShoppingLists;
        this.createdOn = createdOn;
    }
}
