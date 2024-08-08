package com.ai.aichatbackend.utils;

import com.ai.aichatbackend.common.Constants.R;
import com.ai.aichatbackend.domain.Notebook;
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
    private List<Notebook> notebooks;

    public List<Notebook> getNotes() {
        this.notebooks = loadNotebook();
        return new ArrayList<>(this.notebooks);
    }

    public void checkAndUpdateNotebook(String aiResponse) {
        log.info("aiResponse = \n" + aiResponse);
        List<Notebook> newNotebooks = extractNotesFromResponse(aiResponse);
        for (Notebook notebook : newNotebooks) {
            addNote(notebook.getContent(), notebook.getTag(), notebook.getImportance());
        }
    }

    public String getFormattedNotes() {
        StringBuilder sb = new StringBuilder();
        sb.append("AI Notebook:\n");
        for (Notebook notebook : notebooks) {
            sb.append("- [").append(notebook.tag).append("] (Importance: ").append(notebook.importance)
                    .append(") ").append(notebook.content).append(" (Added: ").append(notebook.timestamp).append(")\n");
        }
        return sb.toString();
    }

    /**
     * 正则批量处理ai返回notebook指令
     *
     * @param aiResponse ai返回的notebook指令
     * @return 需要写入notebook的数据
     */
    public List<Notebook> extractNotesFromResponse(String aiResponse) {
        List<Notebook> newNotebooks = new ArrayList<>();
        Matcher noteMatcher = NOTE_PATTERN.matcher(aiResponse);

        while (noteMatcher.find()) {
            String noteContent = noteMatcher.group(1);
            String tag = extractField(TAG_PATTERN, noteContent);
            String content = extractField(CONTENT_PATTERN, noteContent);
            double importance = extractImportance(noteContent);

            if (tag != null && content != null && importance >= 0) {
                newNotebooks.add(new Notebook(content, tag, importance, LocalDateTime.now()));
            }
        }

        return newNotebooks;
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
        notebooks.add(new Notebook(content, tag, importance, LocalDateTime.now()));
        saveNotebook(notebooks);
    }

    private static final DateTimeFormatter TIMESTAMP_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final Pattern LOAD_NOTE_PATTERN = Pattern.compile("\\[(.*?)]\\s*\\(Importance:\\s*(\\d+\\.\\d+)\\)\\s*((?:.|\\n)*?)\\s*\\(Added:\\s*(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\)", Pattern.DOTALL);

    private void saveNotebook(List<Notebook> newNotebooks) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOTEBOOK_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (Notebook notebook : newNotebooks) {
                writer.write(String.format("[%s] (Importance: %.1f) %s (Added: %s)",
                        notebook.tag, notebook.importance, notebook.content, notebook.timestamp.format(TIMESTAMP_PATTERN)));
                writer.newLine();
            }
        } catch (IOException e) {
            log.error("Error saving notebook: " + e.getMessage());
        }
    }

    private List<Notebook> loadNotebook() {
        List<Notebook> currNotebooks = new ArrayList<>();
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

                    currNotebooks.add(new Notebook(noteContent, tag, importance, timestamp));
                }
            } catch (IOException e) {
                log.error("Error loading notebook: " + e.getMessage());
            }
        }
        return currNotebooks;
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


    /**
     * 保存笔记
     * @param notebooks 笔记列表
     * @return 保存结果
     */
    public R saveNotes(List<Notebook> notebooks) {
        try {
            Path path = Path.of(NOTEBOOK_FILE);
            Files.delete(path);
            saveNotebook(notebooks);
        } catch (IOException e) {
            log.error("Failed to save notes: " + e.getMessage());
        }
        return R.ok(loadNotebook());
    }

    /**
     * 清空特定属性笔记本
     *
     * @param notebook 属性设置
     */
    public R cleanupNotes(Notebook notebook) {
        if (notebook == null) {
            return R.error("缺少参数");
        }
        double importance = notebook.getImportance();
        LocalDateTime time = notebook.getTimestamp();
        List<Notebook> currNotebooks = loadNotebook();
        if (notebook.getTimestamp() != null && notebook.getImportance() != 0)  {
            currNotebooks.removeIf(note -> note.getImportance() <= importance && note.getTimestamp().isBefore(time));
        }else if (notebook.getTimestamp() != null) {
            currNotebooks.removeIf(note -> note.getTimestamp().isBefore(time));
        }else if (notebook.getImportance() != 0) {
            currNotebooks.removeIf(note -> note.getImportance() <= importance);

        }
        saveNotes(currNotebooks);
        return R.ok(loadNotebook());
    }
}
