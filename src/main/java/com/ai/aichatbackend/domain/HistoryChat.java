package com.ai.aichatbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author mac
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "history_chat")
public class HistoryChat implements Serializable {
    @Serial
    private static final long serialVersionUID = 222472691275483327L;
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "history_chat", nullable = false, length = 100000)
    private String historyChat;

}
