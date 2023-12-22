package com.sm.message;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Getter
@Entity
@NoArgsConstructor
@ToString
public class MessageQ {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "value")
    private String value;

    public MessageQ(String value) {
        this.value = value;
    }
}
