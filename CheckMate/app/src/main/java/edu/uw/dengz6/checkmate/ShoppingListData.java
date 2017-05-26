package edu.uw.dengz6.checkmate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class ShoppingListData {

    @JsonIgnoreProperties(ignoreUnknown = true)

    public String shoppingListID;
    public String shoppingListName;
    public String ownerId;
    public String ownerName;
    public int totalCost;
    public int totalShoppingItems;
    public String createdOn;

    public ShoppingListData() {

    }

    public ShoppingListData(String shoppingListID, String shoppingListName, String ownerId, String ownerName,
                            int totalCost, int totalShoppingItems, String createdOn) {

        this.shoppingListID = shoppingListID;
        this.shoppingListName = shoppingListName;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.totalCost = totalCost;
        this.totalShoppingItems = totalShoppingItems;
        this.createdOn = createdOn;
    }
}
