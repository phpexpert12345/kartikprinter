package com.justfoodzorderreceivers.Model;

public class FoodItemList {

    String ItemsName,quantity,menuprice,item_size,ExtraTopping,Currency;

    public FoodItemList (String ItemsName, String quantity, String menuprice,String item_size,String ExtraTopping, String Currency){

        this.ItemsName = ItemsName;
        this.quantity =  quantity;
        this.menuprice = menuprice;
        this.item_size = item_size;
        this.ExtraTopping = ExtraTopping;
        this.Currency = Currency;

    }

    public String getItemsName() {
        return ItemsName;

    }

    public void setItemsName(String itemsName) {
        this.ItemsName = ItemsName;
    }

    public String getquantity() {
        return quantity;
    }

    public void setquantity(String quantity) {
        this.quantity = quantity;
    }

    public String getmenuprice() {
        return menuprice;
    }

    public void setmenuprice(String menuprice) {
        this.menuprice = menuprice;
    }


    public String getItem_size() {
        return item_size;
    }

    public void setItem_size(String item_size) {
        this.item_size = item_size;
    }

    public String getExtraTopping() {
        return ExtraTopping;
    }

    public void setExtraTopping(String extraTopping) {
        ExtraTopping = extraTopping;
    }


    public String getCurrency() {
        return Currency;
    }

    public void setmCurrency(String Currency) {
        this.Currency = Currency;
    }
}
