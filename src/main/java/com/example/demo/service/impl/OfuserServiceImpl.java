package com.example.demo.service.impl;

import com.example.demo.entity.Ofuser;
import com.example.demo.repository.OfuserRepository;
import com.example.demo.service.OfuserService;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OfuserServiceImpl implements OfuserService {

    @Autowired
    OfuserRepository ofuserRepository;

    @Override
    public boolean Broadcast() {
        try {
            XMPPTCPConnection.setUseStreamManagementDefault(false);
            XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder(); //XmppDomain 配置

            config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); //不開啟安全模式

            //登入帳號
            config.setUsernameAndPassword("admin","1234");
            config.setConnectTimeout(86400000);
            config.setHost("192.168.11.49");
            config.setXmppDomain("desktop-ph8lrjf.tgfc.tw");
            config.build();
//            config.setDebuggerEnabled(true);

            XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
            mConnection.connect(); //Establishes a connection to the server
            mConnection.login(); //Logs in


            List<Ofuser> users = ofuserRepository.findAll();
            for (Ofuser ofuser:users){
                //聊天
                ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
                EntityBareJid jid = JidCreate.entityBareFrom(ofuser.getUsername()+"@desktop-ph8lrjf.tgfc.tw");
                Chat chat = chatManager.chatWith(jid);
                chat.send("登入了!");
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

        return true;
    }
}
