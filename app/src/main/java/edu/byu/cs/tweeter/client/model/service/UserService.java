package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.shared.model.domain.AuthToken;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;

public class UserService {
    //******************************** Followers Presenter **************************************************

    public interface GetFollowerUserObserver {
        void handleUserSuccess(User user);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void getFollowerUser(AuthToken currUserAuthToken, String userAlias, GetFollowerUserObserver getFollowerUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias , new GetFollowerUserHandler(getFollowerUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetFollowerUserHandler extends Handler {
        private GetFollowerUserObserver observer;

        public GetFollowerUserHandler(GetFollowerUserObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.handleUserSuccess(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }


    //******************************** FollowingPresenter **************************************************

    public interface GetFollowingUserObserver {
        void handleUserSuccess(User user);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void getFollowingUser(AuthToken currUserAuthToken, String userAlias, GetFollowingUserObserver GetFollowingUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias, new GetFollowingUserHandler(GetFollowingUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    private class GetFollowingUserHandler extends Handler {
        private GetFollowingUserObserver observer;

        public GetFollowingUserHandler(GetFollowingUserObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.handleUserSuccess(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }


    //******************************** StoryPresenter **************************************************
    public interface GetStoryUserObserver {
        void handleUserSuccess(User user);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void getStoryUser(AuthToken currUserAuthToken, String userAlias, GetStoryUserObserver getStoryUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias, new GetStoryUserHandler(getStoryUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void getStoryClickable(AuthToken currUserAuthToken, String clickable, GetStoryUserObserver getStoryUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                clickable, new GetStoryUserHandler(getStoryUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetStoryUserHandler extends Handler {
        GetStoryUserObserver observer;

        public GetStoryUserHandler(GetStoryUserObserver observer){
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.handleUserSuccess(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    //******************************** Login Task **************************************************
    public interface GetLoginUserObserver {
        void handleUserSuccess(User loggedInUser, AuthToken authToken);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void loginUserTask(String userAlias, String password, GetLoginUserObserver getLoginUserObserver) {
        LoginTask loginTask = new LoginTask(userAlias, password,
                new LoginHandler(getLoginUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends Handler {
        private GetLoginUserObserver observer;

        public LoginHandler(GetLoginUserObserver observer){
            this.observer = observer;
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) {
                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);
                observer.handleUserSuccess(loggedInUser, authToken);
            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
            }
        }
    }

    //    *****************************   MainActivity logout ***************************************

    public interface GetLogoutObserver {
        void handleSuccess();
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void logoutUserTask(AuthToken currUserAuthToken, GetLogoutObserver getLogoutObserver) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new LogoutHandler(getLogoutObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }

    // LogoutHandler
    private class LogoutHandler extends Handler {
        GetLogoutObserver observer;

        public LogoutHandler(GetLogoutObserver observer){
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


    //******************************** Register Task **************************************************
    public interface GetRegisterUserObserver {
        void handleUserSuccess(User registeredUser, AuthToken authToken);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void registerUserTask(String firstName, String lastName, String userAlias, String password,
                                 String img, GetRegisterUserObserver getRegisterUserObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                userAlias, password, img, new RegisterHandler(getRegisterUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(registerTask);
    }

    // RegisterHandler
    private class RegisterHandler extends Handler {
        private GetRegisterUserObserver observer;

        public RegisterHandler(GetRegisterUserObserver observer){
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

}
