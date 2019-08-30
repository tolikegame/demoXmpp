package com.example.demo;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class TaxiChatManagerListener implements ChatManagerListener {

    public void chatCreated(Chat chat, boolean arg1) {
        chat.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Message msg) {

                XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
                config.setUsernameAndPassword("test3","1234");
                config.setHost("127.0.0.1");
                try {
                    config.setXmppDomain("desktop-ph8lrjf.tgfc.tw");
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                config.setDebuggerEnabled(true);
                XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
                try {
                    mConnection.connect(); //Establishes a connection to the server
                    mConnection.login();
                } catch (SmackException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //登录用户
//                StringUtils.parseName(XmppConnection.getInstance().getConnection().getUser());
                //发送消息用户
                msg.getFrom();
                //消息内容
                String body = msg.getBody();
                boolean left = body.substring(0, 1).equals("{");
                boolean right = body.substring(body.length() - 1, body.length()).equals("}");
                if (left && right) {
                    try {
                        JSONObject obj = new JSONObject(body);
                        String type = obj.getString("messageType");
                        String chanId = obj.getString("chanId");
                        String chanName = obj.getString("chanName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}