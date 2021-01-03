package com.justfoodzorderreceivers.Utils;

import com.justfoodzorderreceivers.Model.PrinterElements;
import com.justfoodzorderreceivers.Model_OrderComboItemExtra;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.SettingEnum;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.TextSetting;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Util {
    public void PrintorderReceipt(RTPrinter rtPrinter, PrinterElements printerElements) throws UnsupportedEncodingException {
        if (rtPrinter != null) {
            CmdFactory escFac = new EscFactory();
            Cmd escCmd = escFac.create();
            escCmd.append(escCmd.getHeaderCmd());
            escCmd.setChartsetName(printerElements.mChartsetName);
            TextSetting textSetting = new TextSetting();
            textSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
            if (printerElements.collectionTime.equalsIgnoreCase("null")) {
                escCmd.append(escCmd.getTextCmd(textSetting, printerElements.RequestAtDate));
            } else {
                escCmd.append(escCmd.getTextCmd(textSetting, printerElements.RequestAtDate + "  " + printerElements.collectionTime));
            }
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting textSetting1 = new TextSetting();
            textSetting1.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(textSetting1, printerElements.restaurant_name));
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting textSetting2 = new TextSetting();
            textSetting2.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(textSetting2, printerElements.restaurant_address));
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting txtmobile = new TextSetting();
            txtmobile.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(txtmobile, printerElements.restaurant_mobile_number));
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting txt_reastudash = new TextSetting();
            txt_reastudash.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(txt_reastudash, "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting txtOrderType = new TextSetting();
            txtOrderType.setAlign(CommonEnum.ALIGN_MIDDLE);
            txtOrderType.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(txtOrderType, printerElements.OrderType));

            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting txtOrderNo = new TextSetting();
            txtOrderNo.setAlign(CommonEnum.ALIGN_MIDDLE);
            txtOrderNo.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(txtOrderNo, printerElements.order_reference_number));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting orderDate = new TextSetting();
            orderDate.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(orderDate, printerElements.RequestAtDate + " / " + printerElements.RequestAtTime));
            escCmd.append(escCmd.getLFCRCmd());


        /*    TextSetting orderTime = new TextSetting();
            orderTime.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(orderTime,  RequestAtTime));
            escCmd.append(escCmd.getLFCRCmd());*/

            TextSetting orderReadyAt = new TextSetting();
            orderReadyAt.setAlign(CommonEnum.ALIGN_MIDDLE);
            if (printerElements.collectionTime.equalsIgnoreCase("null")) {
                escCmd.append(escCmd.getTextCmd(orderReadyAt, printerElements.order_ready_at));
            } else {
                escCmd.append(escCmd.getTextCmd(orderReadyAt, printerElements.order_ready_at + printerElements.collectionTime));
            }
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting text12 = new TextSetting();
            text12.setAlign(CommonEnum.ALIGN_BOTH_SIDES);
            escCmd.append(escCmd.getTextCmd(text12, printerElements.payment_type + "   " + printerElements.PaymentMethod));
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting txt_paymentdash = new TextSetting();
            txt_paymentdash.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(txt_paymentdash, "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());
            if (!printerElements.instruction_note.equalsIgnoreCase("")) {
                TextSetting txt_customerNote = new TextSetting();
                txt_customerNote.setAlign(CommonEnum.ALIGN_LEFT);
                escCmd.append(escCmd.getTextCmd(txt_customerNote, printerElements.customer_note + ":"));
                escCmd.append(escCmd.getLFCRCmd());


                TextSetting txt = new TextSetting();
                txt.setAlign(CommonEnum.ALIGN_LEFT);
                escCmd.append(escCmd.getTextCmd(txt, printerElements.instruction_note));
                escCmd.append(escCmd.getLFCRCmd());
            }

            TextSetting txt_CustomerNotedash = new TextSetting();
            txt_CustomerNotedash.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(txt_CustomerNotedash, "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());
            TextSetting txt_CustomerInfo1 = new TextSetting();
            txt_CustomerInfo1.setAlign(CommonEnum.ALIGN_LEFT);
            txt_CustomerInfo1.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(txt_CustomerInfo1, printerElements.Customer_info + ":"));
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting txt_CustomerInfo = new TextSetting();
            txt_CustomerInfo.setAlign(CommonEnum.ALIGN_LEFT);
            escCmd.append(escCmd.getTextCmd(txt_CustomerInfo, printerElements.name_customer));
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting txt_Customeradd = new TextSetting();
            txt_Customeradd.setAlign(CommonEnum.ALIGN_LEFT);
            txt_Customeradd.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(txt_Customeradd, printerElements.customer_address));
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting txt_CustomerMobile = new TextSetting();
            txt_CustomerMobile.setAlign(CommonEnum.ALIGN_LEFT);
            escCmd.append(escCmd.getTextCmd(txt_CustomerMobile, printerElements.customer_phone));
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting txt_Customeremail = new TextSetting();
            txt_Customeremail.setAlign(CommonEnum.ALIGN_LEFT);
            escCmd.append(escCmd.getTextCmd(txt_Customeremail, printerElements.customer_email));
            escCmd.append(escCmd.getLFCRCmd());

            TextSetting txt_Customeremaildash = new TextSetting();
            txt_Customeremaildash.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(txt_Customeremaildash, "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());
            if (printerElements.number_of_customer_order.equalsIgnoreCase("")) {
                TextSetting txt_backorders = new TextSetting();
                txt_backorders.setAlign(CommonEnum.ALIGN_MIDDLE);
                escCmd.append(escCmd.getTextCmd(txt_backorders, printerElements.Back_orders_from_customer + ":" + printerElements.number_of_customer_order));
                escCmd.append(escCmd.getLFCRCmd());
            }
            TextSetting txt_PayOptionStatus = new TextSetting();
            txt_PayOptionStatus.setAlign(CommonEnum.ALIGN_MIDDLE);
            txt_PayOptionStatus.setBold(SettingEnum.Enable);
            escCmd.append(escCmd.getTextCmd(txt_PayOptionStatus, printerElements.PayOptionStatus));
            escCmd.append(escCmd.getLFCRCmd());


            TextSetting txtpaystatusdash = new TextSetting();
            txtpaystatusdash.setAlign(CommonEnum.ALIGN_MIDDLE);
            escCmd.append(escCmd.getTextCmd(txtpaystatusdash, "--------------------------------"));
            escCmd.append(escCmd.getLFCRCmd());
            TextSetting txtitemname = new TextSetting();
            txtitemname.setAlign(CommonEnum.ALIGN_LEFT);
            escCmd.append(escCmd.getTextCmd(txtitemname, printerElements.Item_Name + "       " + printerElements.Price));
            escCmd.append(escCmd.getLFCRCmd());
            escCmd.append(escCmd.getLFCRCmd());
            TextSetting textSetting3 = new TextSetting();
            textSetting3.setAlign(CommonEnum.ALIGN_MIDDLE);


                }





        }

}
