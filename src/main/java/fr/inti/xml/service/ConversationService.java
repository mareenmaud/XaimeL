package fr.inti.xml.service;

import fr.inti.xml.domain.Conversation;
import fr.inti.xml.domain.Message;
import fr.inti.xml.domain.User;
import fr.inti.xml.repository.ConversationRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Set;

public class ConversationService {

    ConversationRepository conversationRepository;

    @Async
    public boolean createConversation(String id_user1, String id_user2) {

        Conversation conversation = new Conversation();
        conversation.setIdUser1(id_user1);
        conversation.setIdUser2(id_user2);
        try {
            conversationRepository.save(conversation);
            return true;
        } catch (Exception e) {
            // TODO: handle exception

            // TODO Auto-generated method stub
            return false;
        }
    }

    @Async
    public Conversation readConversation(String id_User1,String id_User2) {
        Conversation conversation = conversationRepository.findConversationByUsers(id_User1, id_User2);
        // TODO Auto-generated method stub
        return conversation;
    }

    @Async
    public boolean ajouterMessageConversaton(Message message, String idConversation) {
        Conversation conversation = conversationRepository.findById(idConversation).get();
        List<Message> messages = (List<Message>) conversation.getMessages();
        messages.add(message);
        conversation.setMessages((Set<Message>) messages);
        try {
            conversationRepository.save(conversation);
            return true;
        } catch (Exception e) {
            // TODO: handle exception

            // TODO Auto-generated method stub
            return false;
        }
    }

    @Async
    public boolean deleteConversation(String idConversation) {
        Conversation conversation = conversationRepository.findById(idConversation).get();

        try {
            conversationRepository.delete(conversation);
            return true;
        } catch (Exception e) {
            // TODO: handle exception

            // TODO Auto-generated method stub
            return false;
        }
    }

    @Async
    public boolean MessageLuConversation(String idConversation, Message message) {
        Conversation conversation=conversationRepository.findById(idConversation).get();
        List<Message> messages= (List<Message>) conversation.getMessages();
        for(Message messagee : messages ) {
            if (messagee.getId()== message.getId()) {
                message.setReadMessage(true);
                return true;

            }

        }
        return false;
        // TODO Auto-generated method stub

    }
}
