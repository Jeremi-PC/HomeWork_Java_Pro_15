package org.example;

import lombok.Data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
public class Logger implements Closeable {
    private OutputStream stream;
    private LocalDate dateNow = LocalDate.now();
    private LocalTime timeNow = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    private boolean isToFile;
    private LogLevel level;



    public Logger() {
        this.isToFile = "true".equals(this.parseToCommand().get("writeToFile"));
        this.level = LogLevel.valueOf(this.parseToCommand().get("levelOfLog").toUpperCase(Locale.ROOT));
        if (isToFile) {
            try {
                this.stream = new FileOutputStream("output.log", true);
            } catch (IOException e) {
                throw new RuntimeException("Error opening the file for writing.", e);
            }
        } else {
            this.stream = System.out;
        }
    }


    private Map<String, String> parseToCommand() {
        String content = readSettingsFromFile();
        Map<String, String> param = new HashMap<>();
        String[] lines = content.split("\n");

        for (String line : lines) {
            if (line.startsWith("writeToFile = ")) {
                String value = line.substring("writeToFile = ".length()).trim();
                param.put("writeToFile", value);
            } else if (line.startsWith("levelOfLog = ")) {
                String value = line.substring("levelOfLog = ".length()).trim();
                param.put("levelOfLog", value);
            }
        }
        return param;
    }

    private String readSettingsFromFile() {
        try (FileInputStream stream = new FileInputStream("settings.info")) {
            StringBuilder content = new StringBuilder();
            int data;
            while ((data = stream.read()) != -1) {
                content.append((char) data);
            }
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void debug(String message) {
        if (level.compareTo(LogLevel.DEBUG) == 0) {
            writeTo(message);
        }
    }

    public void info(String message) {
        if (level.compareTo(LogLevel.INFO) <= 0) {
            writeTo(message);
        }
    }

    public void warning(String message) {
        if (level.compareTo(LogLevel.WARNING) <= 0) {
            writeTo(message);
        }
    }

    public void error(String message) {
        if (level.compareTo(LogLevel.ERROR) <= 0) {
            writeTo(message);
        }
    }

    public void writeTo(String message) {
        try {
           stream.write(message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {
        try {
            if (stream != System.out) {
                stream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

