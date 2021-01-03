package com.justfoodzorderreceivers;

import com.justfoodzorderreceivers.Model.FoodItemList;

import java.util.ArrayList;

public class Model_Combo {
    private String ItemsName;
    private String ItemsDescriptionName;

    public String getItemsName() {
        return ItemsName;
    }

    public void setItemsName(String itemsName) {
        ItemsName = itemsName;
    }

    public String getItemsDescriptionName() {
        return ItemsDescriptionName;
    }

    public void setItemsDescriptionName(String itemsDescriptionName) {
        ItemsDescriptionName = itemsDescriptionName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getMenuprice() {
        return menuprice;
    }

    public void setMenuprice(String menuprice) {
        this.menuprice = menuprice;
    }

    public ArrayList<Model_OrderComboItemOption> getOrderComboItemOption() {
        return OrderComboItemOption;
    }

    public void setOrderComboItemOption(ArrayList<Model_OrderComboItemOption> orderComboItemOption) {
        OrderComboItemOption = orderComboItemOption;
    }

    private String quantity;
    private String Currency;
    private String menuprice;

    private ArrayList<Model_OrderComboItemOption> OrderComboItemOption;


}
