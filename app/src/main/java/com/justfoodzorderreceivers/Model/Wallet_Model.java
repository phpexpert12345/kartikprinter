package com.justfoodzorderreceivers.Model;

public class Wallet_Model {

    String id,restaurant_id,customer_id, wallet_payment_status,wallet_payment_heading,wallet_order_id,wallet_recieve_amount,wallet_payment_date,
            wallet_payment_time,Billed_from_name,Billed_from_wallet_number,Billed_from_email_address,Billed_from_mobile_number, Site_Company_Name,
            Site_Company_Address,wallet_payment_status_icon;

    public Wallet_Model( String id, String restaurant_id, String customer_id,
                         String  wallet_payment_status, String wallet_payment_heading,
                         String wallet_order_id, String wallet_recieve_amount,
                         String wallet_payment_date, String wallet_payment_time,
                         String Billed_from_name, String Billed_from_wallet_number,
                         String Billed_from_email_address, String Billed_from_mobile_number,
                         String  Site_Company_Name, String Site_Company_Address,String wallet_payment_status_icon)
    {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.customer_id = customer_id;
        this.wallet_payment_status = wallet_payment_status;
        this.wallet_payment_heading = wallet_payment_heading;
        this.wallet_order_id = wallet_order_id;
        this.wallet_recieve_amount = wallet_recieve_amount;
        this.wallet_payment_date = wallet_payment_date;
        this.wallet_payment_time = wallet_payment_time;
        this.Billed_from_name = Billed_from_name;
        this.Billed_from_wallet_number = Billed_from_wallet_number;
        this.Billed_from_email_address = Billed_from_email_address;
        this.Billed_from_mobile_number = Billed_from_mobile_number;
        this.Site_Company_Name = Site_Company_Name;
        this.Site_Company_Address = Site_Company_Address;
        this.wallet_payment_status_icon = wallet_payment_status_icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getWallet_payment_status() {
        return wallet_payment_status;
    }

    public void setWallet_payment_status(String wallet_payment_status) {
        this.wallet_payment_status = wallet_payment_status;
    }

    public String getWallet_payment_heading() {
        return wallet_payment_heading;
    }

    public void setWallet_payment_heading(String wallet_payment_heading) {
        this.wallet_payment_heading = wallet_payment_heading;
    }

    public String getWallet_order_id() {
        return wallet_order_id;
    }

    public void setWallet_order_id(String wallet_order_id) {
        this.wallet_order_id = wallet_order_id;
    }

    public String getWallet_recieve_amount() {
        return wallet_recieve_amount;
    }

    public void setWallet_recieve_amount(String wallet_recieve_amount) {
        this.wallet_recieve_amount = wallet_recieve_amount;
    }

    public String getWallet_payment_date() {
        return wallet_payment_date;
    }

    public void setWallet_payment_date(String wallet_payment_date) {
        this.wallet_payment_date = wallet_payment_date;
    }

    public String getWallet_payment_time() {
        return wallet_payment_time;
    }

    public void setWallet_payment_time(String wallet_payment_time) {
        this.wallet_payment_time = wallet_payment_time;
    }

    public String getBilled_from_name() {
        return Billed_from_name;
    }

    public void setBilled_from_name(String billed_from_name) {
        Billed_from_name = billed_from_name;
    }

    public String getBilled_from_wallet_number() {
        return Billed_from_wallet_number;
    }

    public void setBilled_from_wallet_number(String billed_from_wallet_number) {
        Billed_from_wallet_number = billed_from_wallet_number;
    }

    public String getBilled_from_email_address() {
        return Billed_from_email_address;
    }

    public void setBilled_from_email_address(String billed_from_email_address) {
        Billed_from_email_address = billed_from_email_address;
    }

    public String getBilled_from_mobile_number() {
        return Billed_from_mobile_number;
    }

    public void setBilled_from_mobile_number(String billed_from_mobile_number) {
        Billed_from_mobile_number = billed_from_mobile_number;
    }

    public String getSite_Company_Name() {
        return Site_Company_Name;
    }

    public void setSite_Company_Name(String site_Company_Name) {
        Site_Company_Name = site_Company_Name;
    }

    public String getSite_Company_Address() {
        return Site_Company_Address;
    }

    public void setSite_Company_Address(String site_Company_Address) {
        Site_Company_Address = site_Company_Address;
    }

    public String getwallet_payment_status_icon() {
        return wallet_payment_status_icon;
    }

    public void setwallet_payment_status_icon(String wallet_payment_status_icon) {
        wallet_payment_status_icon = wallet_payment_status_icon;
    }
}
