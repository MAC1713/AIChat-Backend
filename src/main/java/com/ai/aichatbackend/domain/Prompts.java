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
@Table(name = "prompts")
public class Prompts implements Serializable {
    @Serial
    private static final long serialVersionUID = -4638568901977628326L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "prompt_type", nullable = false)
    private String promptType;

    @Column(name = "prompt_data", nullable = false, length = 10000)
    private String promptData;

    @Column(name = "tokens")
    private Integer tokens;
}
