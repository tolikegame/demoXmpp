package com.example.demo;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;

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

public class SmackJframe {


    static JFrame frame = new JFrame();

    public static void main(String[] args) throws InterruptedException {
        try {
            String userNameAndPasswor =user();
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();

            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

            config.setUsernameAndPassword(userNameAndPasswor, userNameAndPasswor);

            config.setHost("35.234.7.83");

            config.setXmppDomain("logan-proxy.asia-east1-b.c.logan-proxy-247204.internal");
            config.setDebuggerEnabled(true);

            XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
            mConnection.connect(); //Establishes a connection to the server
            mConnection.login(); //Logs in

            //聊天
            ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
//            EntityBareJid jid = JidCreate.entityBareFrom(userName+"@desktop-4r2cgah.tgfc.tw");
//            Chat chat = chatManager.chatWith(jid);
//            chat.send("Hello admin!");
            System.out.println("Esta conectat? " + mConnection.isConnected());
            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    frame=new JFrame();
                    frame(message.getBody());
                    System.out.println(message.getBody());
                }
            });
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

    public static void frame(String text) {

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JButton label = new JButton(text);
        label.setFont(new Font("標楷體", Font.BOLD, 910));
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
        frame.addWindowListener(new CloseHandler());
        frame.setVisible(true);
    }

    protected static class CloseHandler extends WindowAdapter {
        public void windowClosing(final WindowEvent event) {
            frame.dispose();
//            System.exit(0);
        }
    }
    public static String user(){
        InetAddress inetAddress;
        String ip;
        try {
            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
                System.out.println("使用者名稱: " + System.getenv("USERNAME"));
                System.out.println("IP: " + ip);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return System.getenv("USERNAME");
    }
}
