package com.ai.aichatbackend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

/**
 * @author mac
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "prompts_type")
public class PromptsType {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "prompts_type", nullable = false)
    private String promptsType;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "update_time")
    private Date updateTime;
}
