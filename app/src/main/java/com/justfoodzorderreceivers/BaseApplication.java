package com.justfoodzorderreceivers;

import android.app.Application;

import com.rt.printerlibrary.printer.RTPrinter;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;


public class BaseApplication extends Application {

    public static BaseApplication instance = null;
    private RTPrinter rtPrinter;
   public static Socket socket;

    @BaseEnum.CmdType
    private int currentCmdType = BaseEnum.CMD_ESC;

    @BaseEnum.ConnectType
    private int currentConnectType = BaseEnum.NONE;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ConnectSocket();

    }
    private void ConnectSocket(){
        IO.Options opts = new IO.Options();
        opts.upgrade=false;
        opts.transports = new String[] { WebSocket.NAME };
        try {
            socket=IO.socket("",opts);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public static BaseApplication getInstance(){
        return instance;
    }


    public RTPrinter getRtPrinter() {
        return rtPrinter;
    }

    public void setRtPrinter(RTPrinter rtPrinter) {
        this.rtPrinter = rtPrinter;
    }

    @BaseEnum.CmdType
    public int getCurrentCmdType() {
        return currentCmdType;
    }

    public void setCurrentCmdType(@BaseEnum.CmdType int currentCmdType) {
        this.currentCmdType = currentCmdType;
    }

    @BaseEnum.ConnectType
    public int getCurrentConnectType() {
        return currentConnectType;
    }

    public void setCurrentConnectType(@BaseEnum.ConnectType int currentConnectType) {
        this.currentConnectType = currentConnectType;
    }
}
