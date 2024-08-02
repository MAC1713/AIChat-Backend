package com.ai.aichatbackend.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author mac
 */
@Data
public class Note implements Serializable {
    @Serial
    private static final long serialVersionUID = -3880726229066089242L;
    public String content;
    public String tag;
    public double importance;
    public LocalDateTime timestamp;
    public boolean isPermanent;

    public Note(String content, String tag, double importance, LocalDateTime timestamp) {
        this.content = content;
        this.tag = tag;
        this.importance = importance;
        this.timestamp = timestamp;
        this.isPermanent = "Name".equalsIgnoreCase(tag) || "Identity".equalsIgnoreCase(tag);
    }
}
