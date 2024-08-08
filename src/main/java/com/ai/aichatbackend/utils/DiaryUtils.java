package com.ai.aichatbackend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.ai.aichatbackend.common.Constants.AIChatConstants.DIARY_FILE;

/**
 * @author mac
 */
@Slf4j
@Component
public class DiaryUtils {

    public String getDiary() {
        return loadDiary();
    }

    public void saveDiary(String aiResponse) {
        cleanDiary();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIARY_FILE), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(aiResponse);
        } catch (IOException e) {
            log.error("Error saving notebook: " + e.getMessage());
        }
    }

    private String loadDiary() {
        Path path = Paths.get(DIARY_FILE);
        if (Files.exists(path)) {
            try {
                return Files.readString(path);
            } catch (IOException e) {
                log.error("Error loading notebook: " + e.getMessage());
            }
        }
        return "There is no diary which you had written, Emma, sorry.";
    }

    private void cleanDiary() {
        try {
            Files.deleteIfExists(Paths.get(DIARY_FILE));
        } catch (IOException e) {
            log.error("Error deleting notebook: " + e.getMessage());
        }
    }
}