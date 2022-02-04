package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.shared.model.domain.AuthToken;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowerUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetStoryUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.RegisterHandler;

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

}
