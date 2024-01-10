package org.example;

import lombok.Data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Data
public class Logger {
    LocalDate date;
    LocalTime time;
    String message;
    Title title;


    public Logger() {
        this.date = LocalDate.now();
        this.time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);;

    }

    public void debug(Boolean toFile) {
        chooseOutput(toFile, message);
    }

    public void info(Boolean toFile) {
        chooseOutput(toFile, message);
    }

    public void warning(Boolean toFile) {
        chooseOutput(toFile, message);
    }

    public void error(Boolean toFile) {
        chooseOutput(toFile, message);
    }

    private String readFromSettingsFile() {
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

    public Map<String, String> parseToCommand() {
        String content = readFromSettingsFile();
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
    public void chooseOutput(Boolean toFile, String inputInfo) {
        if (toFile) {
            try (FileOutputStream stream = new FileOutputStream("output.log")) {
                stream.write(inputInfo.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(inputInfo);
        }
    }

}
