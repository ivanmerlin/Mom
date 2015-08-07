package com.alibaba.middleware.race.mom.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by ivan.wang on 2015/8/7.
 */
public class NetStreamUtils {
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;
    static int TIME_OUT = 10;

    public NetStreamUtils() {
    }

    public NetStreamUtils(Socket socket) {
        this.socket = socket;
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeObject(Object object) {
        try {
            output.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readString() {
        try {
            socket.setSoTimeout(TIME_OUT * 1000);
            return input.readUTF();
        } catch (SocketException e) {
            return  null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeString(String str) {
        try {
            output.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
