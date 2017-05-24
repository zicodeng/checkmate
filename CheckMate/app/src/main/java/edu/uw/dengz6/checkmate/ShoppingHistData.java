package edu.uw.dengz6.checkmate;

public class ShoppingHistData {
    public String member;
    public String totalCost;
    public String totalShoppingList;
    public String since;


    public ShoppingHistData(String member, String totalCost, String totalShoppingList, String since) {
        this.member = member;
        this.totalCost = totalCost;
        this.totalShoppingList = totalShoppingList;
        this.since = since;
    }
}
