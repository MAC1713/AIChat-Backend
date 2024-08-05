package com.ai.aichatbackend.utils;

import com.ai.aichatbackend.domain.Note;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ai.aichatbackend.common.Constants.AIChatConstants.NOTEBOOK_FILE;

/**
 * @author mac
 */
@Slf4j
@Component
public class NotebookUtils {
    private static final String NOTE_REGEX = "(?s)\\[NOTE]\\s*(.*?)\\[/NOTE]";
    private static final Pattern NOTE_PATTERN = Pattern.compile(NOTE_REGEX);
    private static final Pattern TAG_PATTERN = Pattern.compile("Tag:\\s*(.*?)\\s*(?=Content:|$)", Pattern.DOTALL);
    private static final Pattern CONTENT_PATTERN = Pattern.compile("Content:\\s*(.*?)\\s*(?=Importance:|$)", Pattern.DOTALL);
    private static final Pattern IMPORTANCE_PATTERN = Pattern.compile("Importance:\\s*(\\d+(?:\\.\\d+)?)");
    private List<Note> notes;

    public List<Note> getNotes() {
        this.notes = loadNotebook();
        return new ArrayList<>(this.notes);
    }

    public void checkAndUpdateNotebook(String aiResponse) {
        log.info("aiResponse = " + aiResponse);
        List<Note> newNotes = extractNotesFromResponse(aiResponse);
        for (Note note : newNotes) {
            addNote(note.getContent(), note.getTag(), note.getImportance());
        }
    }

    public String getFormattedNotes() {
        StringBuilder sb = new StringBuilder();
        sb.append("AI Notebook:\n");
        for (Note note : notes) {
            sb.append("- [").append(note.tag).append("] (Importance: ").append(note.importance)
                    .append(") ").append(note.content).append(" (Added: ").append(note.timestamp).append(")\n");
        }
        return sb.toString();
    }

    /**
     * 正则批量处理ai返回notebook指令
     *
     * @param aiResponse ai返回的notebook指令
     * @return 需要写入notebook的数据
     */
    public List<Note> extractNotesFromResponse(String aiResponse) {
        List<Note> newNotes = new ArrayList<>();
        Matcher noteMatcher = NOTE_PATTERN.matcher(aiResponse);

        while (noteMatcher.find()) {
            String noteContent = noteMatcher.group(1);
            String tag = extractField(TAG_PATTERN, noteContent);
            String content = extractField(CONTENT_PATTERN, noteContent);
            double importance = extractImportance(noteContent);

            if (tag != null && content != null && importance >= 0) {
                newNotes.add(new Note(content, tag, importance, LocalDateTime.now()));
            }
        }

        return newNotes;
    }

    private String extractField(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    private double extractImportance(String text) {
        String importanceStr = extractField(IMPORTANCE_PATTERN, text);
        try {
            return importanceStr != null ? Double.parseDouble(importanceStr) : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void addNote(String content, String tag, double importance) {
        notes.add(new Note(content, tag, importance, LocalDateTime.now()));
        saveNotebook(notes);
    }

    private static final DateTimeFormatter TIMESTAMP_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final Pattern LOAD_NOTE_PATTERN = Pattern.compile("\\[(.*?)]\\s*\\(Importance:\\s*(\\d+\\.\\d+)\\)\\s*((?:.|\\n)*?)\\s*\\(Added:\\s*(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\)", Pattern.DOTALL);

    private void saveNotebook(List<Note> newNotes) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOTEBOOK_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (Note note : newNotes) {
                writer.write(String.format("[%s] (Importance: %.1f) %s (Added: %s)",
                        note.tag, note.importance, note.content, note.timestamp.format(TIMESTAMP_PATTERN)));
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Error saving notebook: " + e.getMessage());
        }
    }

    private List<Note> loadNotebook() {
        List<Note> currNotes = new ArrayList<>();
        Path path = Paths.get(NOTEBOOK_FILE);

        if (Files.exists(path)) {
            try {
                String content = Files.readString(path);
                Matcher matcher = LOAD_NOTE_PATTERN.matcher(content);
                while (matcher.find()) {
                    String tag = matcher.group(1).trim();
                    double importance = Double.parseDouble(matcher.group(2));
                    String noteContent = matcher.group(3).trim();
                    LocalDateTime timestamp = LocalDateTime.parse(matcher.group(4), TIMESTAMP_PATTERN);

                    currNotes.add(new Note(noteContent, tag, importance, timestamp));
                }
            } catch (IOException e) {
                log.error("Error loading notebook: " + e.getMessage());
            }
        }
        return currNotes;
    }

    /**
     * 删除返回的 [NOTE]标签
     *
     * @param input aiResponse
     * @return 删除
     */
    public String removeNoteTags(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String startTag = "[NOTE]";
        String endTag = "[/NOTE]";

        int startIndex = input.indexOf(startTag);
        int endIndex = input.lastIndexOf(endTag);

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return input.substring(startIndex + startTag.length(), endIndex).trim();
        } else {
            return input;
        }
    }


}
