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
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Smack {

    public static JFrame frame = new JFrame();

    public static void main(String[] args)   {
        try {
            String userNameAndPasswor = user();
            XMPPTCPConnection.setUseStreamManagementDefault(false);
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder(); //XmppDomain 配置

            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); //不開啟安全模式

            config.setUsernameAndPassword("tgfc045", "Tgfc@3B1234");
//            config.setUsernameAndPassword(userNameAndPasswor, userNameAndPasswor);
            config.setConnectTimeout(86400000);
//            config.setHost("104.199.161.206");
            config.setHost("127.0.0.1");


//            config.setXmppDomain("ubuntu2.asia-east1-b.c.balmy-cab-247906.internal");
            config.setXmppDomain("desktop-ph8lrjf.tgfc.tw");
            config.build();
//            config.setDebuggerEnabled(true);

            XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
            mConnection.connect(); //Establishes a connection to the server
            mConnection.login(); //Logs in
            //聊天
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
            EntityBareJid jid = JidCreate.entityBareFrom("tgfc045"+"@desktop-ph8lrjf.tgfc.tw");
            Chat chat = chatManager.chatWith(jid);
            chat.send(user()+"登入了!");
            System.out.println("Esta conectat? " + mConnection.isConnected());
            OfflineMessageManager offlineMessageManager = new OfflineMessageManager(mConnection);
            offlineMessageManager.deleteMessages(); //刪除對話紀錄
            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    if (null != message.getBody()&&"tgfc045".equals(from.getLocalpart().toString())) {
//                        (null != message.getBody()&&"admin".equals(from.getLocalpart().toString()))
                        frame = new JFrame();
                        frame(message.getBody());
                        System.out.println(message.getBody());
                    }
                    System.out.println(message.getBody());
                }
            });
            while (true) {
                Thread.sleep(1);
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
    }

    public static void frame(String text) {

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JButton label = new JButton(text);
        if (text.length() >= 4) {
            label.setFont(new Font("標楷體", Font.BOLD, 300));
        } else {
            label.setFont(new Font("標楷體", Font.BOLD, 910));
        }
        label.setForeground(Color.BLACK);
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
        frame.setBounds(-1930, 0, 10000, 10000);

        frame.setTitle("訂便當系統");
        label.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }


    public static String user() {
        InetAddress inetAddress;
        String ip;
        try {
            final DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
            System.out.println("使用者名稱: " + System.getenv("USERNAME"));
            System.out.println("IP: " + ip);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return System.getenv("USERNAME");
    }

}
