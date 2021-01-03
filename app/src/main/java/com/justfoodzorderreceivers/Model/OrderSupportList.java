package com.justfoodzorderreceivers.Model;

public class OrderSupportList {


    String id,complaint_id,orderIssue,contact_name,contact_email,contact_phone,resid,orderIDNumber,orderIssueEmail,orderIssueMessage,restaurant_order_issue_reply,restaurant_order_issue_date,restaurant_name,restaurant_mobile_number;

    public OrderSupportList(String id, String complaint_id, String orderIssue, String contact_name ,String contact_email,String contact_phone,String resid,String orderIDNumber,String orderIssueEmail,String orderIssueMessage,
                            String restaurant_order_issue_reply,String restaurant_order_issue_date,String restaurant_name,String restaurant_mobile_number )
    {

        this.id = id;
        this.complaint_id =  complaint_id;
        this.orderIssue = orderIssue;
        this.contact_name = contact_name;
        this.contact_email = contact_email;
        this.contact_phone = contact_phone;
        this.resid = resid;
        this.orderIDNumber = orderIDNumber;
        this.orderIssueEmail = orderIssueEmail;
        this.orderIssueMessage = orderIssueMessage;
        this.restaurant_order_issue_reply = restaurant_order_issue_reply;
        this.restaurant_order_issue_date = restaurant_order_issue_date;
        this.restaurant_name = restaurant_name;
        this.restaurant_mobile_number = restaurant_mobile_number;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComplaint_id() {
        return complaint_id;
    }

    public void setComplaint_id(String complaint_id) {
        this.complaint_id = complaint_id;
    }

    public String getOrderIssue() {
        return orderIssue;
    }

    public void setOrderIssue(String orderIssue) {
        this.orderIssue = orderIssue;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getOrderIDNumber() {
        return orderIDNumber;
    }

    public void setOrderIDNumber(String orderIDNumber) {
        this.orderIDNumber = orderIDNumber;
    }

    public String getOrderIssueEmail() {
        return orderIssueEmail;
    }

    public void setOrderIssueEmail(String orderIssueEmail) {
        this.orderIssueEmail = orderIssueEmail;
    }

    public String getOrderIssueMessage() {
        return orderIssueMessage;
    }

    public void setOrderIssueMessage(String orderIssueMessage) {
        this.orderIssueMessage = orderIssueMessage;
    }

    public String getRestaurant_order_issue_reply() {
        return restaurant_order_issue_reply;
    }

    public void setRestaurant_order_issue_reply(String restaurant_order_issue_reply) {
        this.restaurant_order_issue_reply = restaurant_order_issue_reply;
    }

    public String getRestaurant_order_issue_date() {
        return restaurant_order_issue_date;
    }

    public void setRestaurant_order_issue_date(String restaurant_order_issue_date) {
        this.restaurant_order_issue_date = restaurant_order_issue_date;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public String getRestaurant_mobile_number() {
        return restaurant_mobile_number;
    }

    public void setRestaurant_mobile_number(String restaurant_mobile_number) {
        this.restaurant_mobile_number = restaurant_mobile_number;
    }
}
