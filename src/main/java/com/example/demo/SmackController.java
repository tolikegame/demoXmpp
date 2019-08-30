package com.example.demo;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * info:
 * Created by shang on 2017/8/1.
 */
@RestController
public class SmackController {
    @Value("${openfire.server}")
    protected String openfireServer;

    @RequestMapping("/send")
    public Object index(String mobile, String body) {
        return sendSmackMessage(mobile, body, false);
    }


    public boolean sendSmackMessage(String mobile, String body,boolean deliveryReceiptRequest) {
        try {
            if (StringUtils.isEmpty(mobile)) {
                return false;
            }
            XMPPTCPConnection con = getXmpptcpConnection();
            con.connect();
            if (con.isConnected()) {
                SASLAuthentication.registerSASLMechanism(new SASLPlainMechanism());
//                con.loginAnonymously();//匿名登入
                UserSearchManager userSearchManager=new UserSearchManager(con);

                EntityBareJid jid2 = JidCreate.entityBareFrom("admin@desktop-ph8lrjf.tgfc.tw");

                Form searchForm=userSearchManager.getSearchForm((DomainBareJid) jid2);
                Form answerForm = searchForm.createAnswerForm();
                answerForm.setAnswer("Username", true);
                answerForm.setAnswer("search", mobile);
                ReportedData data=userSearchManager.getSearchResults(answerForm,(DomainBareJid) jid2);
                List<ReportedData.Row> list = data.getRows();
                for (ReportedData.Row row : list) {
                    for (String jid : row.getValues("jid")) {
                        Message m = new Message();
                        m.setBody(body);//設定訊息。
                        m.setTo(jid);//設定傳送目標
                        if(deliveryReceiptRequest){
                            m.setType(Message.Type.normal);
                        }else{
                            m.setType(Message.Type.headline);
                        }
                        m.setSubject(body);
                        con.sendStanza(m);
                    }
                }
            }
            con.disconnect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private XMPPTCPConnection getXmpptcpConnection() {
        XMPPTCPConnectionConfiguration.Builder config=XMPPTCPConnectionConfiguration.builder();
//        config.setServiceName(openfireServer);
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setSendPresence(true);
        config.setCompressionEnabled(false);
        XMPPTCPConnection con = new XMPPTCPConnection(config.build());
        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        SASLAuthentication.blacklistSASLMechanism("CRAM-MD5");
        return con;
    }
}
