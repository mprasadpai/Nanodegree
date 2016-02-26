package com.example.prasadpai.moviesapp.network;

/**
 * Created by prasadpai on 26/02/16.
 */

import android.os.Handler;



public interface AsyncCommand {


    interface Callback {
        <T> void success(int statusCode, T result);
        void failure(int statusCode, String message, CommandExecutionError error);
    }

    // the callback must be called on the specified handler
    void execute(Handler handler, Callback callback);
}