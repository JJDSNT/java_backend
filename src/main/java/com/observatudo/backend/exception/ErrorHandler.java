package com.observatudo.backend.exception;

public class ErrorHandler {
    public static void logError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
    }

    public static void logWarning(String message) {
        System.out.println("WARNING: " + message);
    }
}
