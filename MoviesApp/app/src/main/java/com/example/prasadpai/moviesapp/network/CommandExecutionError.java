package com.example.prasadpai.moviesapp.network;

/**
 * Created by prasadpai on 26/02/16.
 */
public class CommandExecutionError extends Exception {

    public CommandExecutionError(String message) {
        super(message);
    }

    public CommandExecutionError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public int getCode() {
        return 0;
    }

    public CommandExecutionError(Throwable throwable) {
        super(throwable);
    }
}
