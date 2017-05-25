package edu.uw.dengz6.checkmate;

public class ShoppingListData {
    public String shoppingListName;
    public String ownerId;
    public String ownerName;
    public String itemList;
    public int totalCost;
    public int totalShoppingItems;
    public String createdOn;

    public ShoppingListData() {

    }

    public ShoppingListData(String shoppingListName, String ownerId, String ownerName,
                            String itemList, int totalCost, int totalShoppingItems, String createdOn) {
        this.shoppingListName = shoppingListName;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.itemList = itemList;
        this.totalCost = totalCost;
        this.totalShoppingItems = totalShoppingItems;
        this.createdOn = createdOn;
    }
}
