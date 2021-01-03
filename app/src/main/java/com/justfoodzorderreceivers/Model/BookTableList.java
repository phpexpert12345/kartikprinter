package com.justfoodzorderreceivers.Model;

public class BookTableList {

    String id,booking_id,noofgusts,booking_name,booking_email,
            booking_mobile,TableNumberDisplay,booking_status_msg,orderid,booking_instruction,booking_date,booking_time;

    public BookTableList(String id,String booking_id,String noofgusts,String booking_name,String booking_email,String booking_mobile
    ,String TableNumberDisplay,String booking_status_msg,String orderid,String booking_instruction,String booking_date,String booking_time)
    {
        this.id = id;
        this.booking_id = booking_id;
        this.noofgusts = noofgusts;
        this.booking_name = booking_name;
        this.booking_email = booking_email;
        this.booking_mobile = booking_mobile;
        this.TableNumberDisplay = TableNumberDisplay;
        this.booking_status_msg = booking_status_msg;
        this.orderid = orderid;
        this.booking_instruction = booking_instruction;
        this.booking_date = booking_date;
        this.booking_time = booking_time;
    }

    public String getBooking_instruction() {
        return booking_instruction;
    }

    public void setBooking_instruction(String booking_instruction) {
        this.booking_instruction = booking_instruction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getNoofgusts() {
        return noofgusts;
    }

    public void setNoofgusts(String noofgusts) {
        this.noofgusts = noofgusts;
    }

    public String getBooking_name() {
        return booking_name;
    }

    public void setBooking_name(String booking_name) {
        this.booking_name = booking_name;
    }

    public String getBooking_email() {
        return booking_email;
    }

    public void setBooking_email(String booking_email) {
        this.booking_email = booking_email;
    }

    public String getBooking_mobile() {
        return booking_mobile;
    }

    public void setBooking_mobile(String booking_mobile) {
        this.booking_mobile = booking_mobile;
    }

    public String getTableNumberDisplay() {
        return TableNumberDisplay;
    }

    public void setTableNumberDisplay(String TableNumberDisplay) {
        this.TableNumberDisplay = TableNumberDisplay;
    }

    public String getBooking_status_msg() {
        return booking_status_msg;
    }

    public void setBooking_status_msg(String booking_status_msg) {
        this.booking_status_msg = booking_status_msg;
    }

    public String getorderid() {
        return orderid;
    }

    public void setorderid(String orderid) {
        this.orderid = orderid;
    }



    public String getbooking_date() {
        return booking_date;
    }

    public void setbooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getbooking_time() {
        return booking_time;
    }

    public void setbooking_time(String booking_time) {
        this.booking_time = booking_time;
    }
}
