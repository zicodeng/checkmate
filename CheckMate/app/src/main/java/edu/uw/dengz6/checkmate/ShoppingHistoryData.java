package edu.uw.dengz6.checkmate;

/**
 * Created by Zico Deng on 5/27/2017.
 */

public class ShoppingHistoryData {
    public String userName;
    public int totalSpend;
    public int totalShoppingLists;
    public String since;

    public ShoppingHistoryData() {

    }

    public ShoppingHistoryData(String userName, int totalSpend, int totalShoppingLists, String since) {
        this.userName = userName;
        this.totalSpend = totalSpend;
        this.totalShoppingLists = totalShoppingLists;
        this.since = since;
    }
}
