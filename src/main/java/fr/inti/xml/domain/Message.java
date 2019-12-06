package fr.inti.xml.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Message.
 */
@Document(collection = "message")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("id_user_sender")
    private Integer idUserSender;

    @Field("id_user_recipient")
    private Integer idUserRecipient;

    @Field("content_message")
    private String contentMessage;

    @Field("date_message")
    private Instant dateMessage;

    @Field("read_message")
    private Boolean readMessage;

    @DBRef
    @Field("conversation")
    @JsonIgnoreProperties("messages")
    private Conversation conversation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdUserSender() {
        return idUserSender;
    }

    public Message idUserSender(Integer idUserSender) {
        this.idUserSender = idUserSender;
        return this;
    }

    public void setIdUserSender(Integer idUserSender) {
        this.idUserSender = idUserSender;
    }

    public Integer getIdUserRecipient() {
        return idUserRecipient;
    }

    public Message idUserRecipient(Integer idUserRecipient) {
        this.idUserRecipient = idUserRecipient;
        return this;
    }

    public void setIdUserRecipient(Integer idUserRecipient) {
        this.idUserRecipient = idUserRecipient;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public Message contentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
        return this;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public Instant getDateMessage() {
        return dateMessage;
    }

    public Message dateMessage(Instant dateMessage) {
        this.dateMessage = dateMessage;
        return this;
    }

    public void setDateMessage(Instant dateMessage) {
        this.dateMessage = dateMessage;
    }

    public Boolean isReadMessage() {
        return readMessage;
    }

    public Message readMessage(Boolean readMessage) {
        this.readMessage = readMessage;
        return this;
    }

    public void setReadMessage(Boolean readMessage) {
        this.readMessage = readMessage;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public Message conversation(Conversation conversation) {
        this.conversation = conversation;
        return this;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", idUserSender=" + getIdUserSender() +
            ", idUserRecipient=" + getIdUserRecipient() +
            ", contentMessage='" + getContentMessage() + "'" +
            ", dateMessage='" + getDateMessage() + "'" +
            ", readMessage='" + isReadMessage() + "'" +
            "}";
    }
}
