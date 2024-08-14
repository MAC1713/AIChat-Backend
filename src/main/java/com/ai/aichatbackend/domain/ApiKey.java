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
@Table(name = "apikey")
public class ApiKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -5051826831934880230L;

    @Id
    @Column(name = "apikey", nullable = false)
    private String apikey;
}
