package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.shared.model.domain.AuthToken;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;

// RegisterHandler
public class RegisterHandler extends Handler {
    private UserService.GetRegisterUserObserver observer;

    public RegisterHandler(UserService.GetRegisterUserObserver observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
        if (success) {
            User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
            observer.handleUserSuccess(registeredUser, authToken);
        } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }
}
