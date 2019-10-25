package com.example.demo.controller;

import com.example.demo.model.XmppModel;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/xmpp")
public class XmppController {
    @ResponseBody
    @PostMapping("/message")
    public String xmppClient(@RequestBody XmppModel xmppModel) throws IOException, InterruptedException, XMPPException, SmackException {
        final String USERNAME = "admin";
        final String PASSWORD = "1234";
        final String HOST ="192.168.11.49";
        final String XMPPDOMAIN ="desktop-ph8lrjf.tgfc.tw";
        String message = xmppModel.getMessage();

        XMPPTCPConnection.setUseStreamManagementDefault(false);
        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder(); //XmppDomain 配置

        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); //不開啟安全模式

        config.setUsernameAndPassword(USERNAME,PASSWORD);
        config.setConnectTimeout(86400000);
        config.setHost(HOST);
        config.setXmppDomain(XMPPDOMAIN);
        config.build();
//            config.setDebuggerEnabled(true);

        XMPPTCPConnection mConnection = new XMPPTCPConnection(config.build());
        mConnection.connect(); //Establishes a connection to the server
        mConnection.login(); //Logs in
        //聊天




        if(xmppModel.getStatus()==1){
        }else{
            String playerAccount = xmppModel.getAccount();
            if (playerAccount.contains(",")) {
                // 多帐号
                for (String account : playerAccount.split(",")) {
                    ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
                    EntityBareJid jid = JidCreate.entityBareFrom(account+"@desktop-ph8lrjf.tgfc.tw");
                    Chat chat = chatManager.chatWith(jid);
                    chat.send(message);
                }
            } else {
                // 单帐号
                ChatManager chatManager = ChatManager.getInstanceFor(mConnection);
                EntityBareJid jid = JidCreate.entityBareFrom(xmppModel.getAccount()+"@desktop-ph8lrjf.tgfc.tw");
                Chat chat = chatManager.chatWith(jid);
                chat.send(message);
            }
        }

        return "發送成功";
    }

}
