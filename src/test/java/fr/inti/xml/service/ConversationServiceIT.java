package fr.inti.xml.service;

import fr.inti.xml.XmLApp;
import fr.inti.xml.domain.Conversation;
import fr.inti.xml.domain.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = XmLApp.class)
public class ConversationServiceIT {

    @Autowired
    static String id_user1;
    @Autowired
    static String id_user2;
    @Autowired
    static String id_user3;
    @Autowired
    static Conversation conversation;
    @Autowired
    static Message message;
    @Autowired
    static ConversationService conversationService;
    @BeforeAll
     static void setup() {
        id_user1="Samir";
        id_user2="Adrien";
        id_user3="Remy";
        conversation=new Conversation();
        conversation.setIdUser1(id_user1);
        conversation.setIdUser2(id_user2);
        message=new Message();
        message.setIdUserSender(id_user1);
        message.setIdUserRecipient(id_user2);
        message.setContentMessage("wesh la famille");
    }

    @Test
    public void createConversationTest(){
       conversationService.createConversation(id_user1,id_user2);
       Conversation conversation2= conversationService.readConversation(id_user1,id_user2);

        assertThat(conversation2).isNull();
    }



}
