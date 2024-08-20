package com.ai.aichatbackend.domain;

import lombok.Data;

import java.sql.Date;

/**
 * @author mac
 */
@Data
public class PromptsAll {

    private String id;

    private String promptType;

    private String promptData;

    private Integer tokens;

    private String description;

    private Date updateTime;
}
