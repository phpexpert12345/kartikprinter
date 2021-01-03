package com.justfoodzorderreceivers;

import java.util.ArrayList;

public class Model_OrderComboItemOption {

    private String ComboOptionName;

    public String getComboOptionName() {
        return ComboOptionName;
    }

    public void setComboOptionName(String comboOptionName) {
        ComboOptionName = comboOptionName;
    }

    public String getComboOptionItemName() {
        return ComboOptionItemName;
    }

    public void setComboOptionItemName(String comboOptionItemName) {
        ComboOptionItemName = comboOptionItemName;
    }

    public String getComboOptionItemSizeName() {
        return ComboOptionItemSizeName;
    }

    public void setComboOptionItemSizeName(String comboOptionItemSizeName) {
        ComboOptionItemSizeName = comboOptionItemSizeName;
    }

    public ArrayList<Model_OrderComboItemExtra> getOrderComboItemExtra() {
        return OrderComboItemExtra;
    }

    public void setOrderComboItemExtra(ArrayList<Model_OrderComboItemExtra> orderComboItemExtra) {
        OrderComboItemExtra = orderComboItemExtra;
    }

    private String ComboOptionItemName;
    private String ComboOptionItemSizeName;
    private ArrayList<Model_OrderComboItemExtra>OrderComboItemExtra;
}
