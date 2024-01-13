package org.example;

public class Main {

    public static void main(String[] args) {
        StringBuilder logMessage = new StringBuilder();
        for (String arg : args) {

            logMessage.append("Params: \n").append(arg).append("\n");
        }
        logMessage.append("\nEnvironment values :\n");

        for (String envName : System.getenv().keySet()) {
            String envValue = System.getenv().get(envName);
            logMessage.append(envName).append(" = ").append(envValue).append("\n");
        }
        try (Logger logger = new Logger()) {
            logger.writeTo(logger.getDateNow() + " " + logger.getTimeNow() + " " + logMessage + "\n");
            logger.debug("Debug message is: \n");
            logger.info("Info message is: \n");
            logger.warning("Warning message is: \n");
            logger.error("Error message is: \n");
        }


    }
}