package com.example.demo;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class Smack {


    public static void main(String[] args) throws InterruptedException {
        try {
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();

            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

            config.setUsernameAndPassword("larry", "1234");

            config.setHost("192.168.11.61");

            config.setXmppDomain("desktop-4r2cgah.tgfc.tw");
            config.setDebuggerEnabled(true);

            XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
            mConnection.connect(); //Establishes a connection to the server
            mConnection.login(); //Logs in

            //聊天
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            EntityBareJid jid = JidCreate.entityBareFrom("wesley@desktop-4r2cgah.tgfc.tw");
            Chat chat = chatManager.chatWith(jid);
            chat.send("請訂便當");
            System.out.println("Esta conectat? " + mConnection.isConnected());
            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    System.out.println("New message from " + from + ": " + message.getBody());
                }
            });
            while (true) {
                Thread.sleep(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }

        while (true) {
            Thread.sleep(0);
        }
    }
}