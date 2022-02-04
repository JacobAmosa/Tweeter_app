package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;

// LogoutHandler
public class LogoutHandler extends Handler {
    UserService.GetLogoutObserver observer;

    public LogoutHandler(UserService.GetLogoutObserver observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
        if (success) {
            observer.handleSuccess();
        } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}
