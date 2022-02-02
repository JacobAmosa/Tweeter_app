package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.client.R;
import edu.byu.cs.shared.model.domain.Status;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.view.main.MainActivity;

public class MainActivityPresenter {

    private View view;
    private FollowService followService;
    private StatusService statusService;
    private UserService userService;

    public interface View {
        void displayErrorMessage(String message);
        void setFollowButton(boolean isFollower);
        void updateUserInfo(boolean follow);
        void finishStatusCreation();
        void finishLogout();
        void setFollowerCount(int count);
        void setFolloweeCount(int count);
    }

    public MainActivityPresenter(View view){
        this.view = view;
        this.followService = new FollowService();
        this.statusService = new StatusService();
        this.userService = new UserService();
    }

    public void isFollower(User selectedUser) {
        followService.isFollowerTask(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, new GetIsFollowerObserver());
    }

    public void unfollowUser(User selectedUser) {
        followService.unfollowUserTask(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetMainUnfollowObserver());
    }

    public void followUser(User selectedUser) {
        followService.followUserTask(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetMainFollowObserver());
    }

    public void logoutUser() {
        userService.logoutUserTask(Cache.getInstance().getCurrUserAuthToken(), new GetLogoutObserver());
    }

    public void createStatus(Status newStatus) {
        statusService.createStatusTask(Cache.getInstance().getCurrUserAuthToken(), newStatus, new GetCreateStatusObserver());
    }


    public void getFollowersCount(User selectedUser, ExecutorService executor) {
        followService.getFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(), selectedUser, executor, new GetFollowersCountObserver());
    }

    public void getFollowingCount(User selectedUser, ExecutorService executor) {
        followService.getFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(), selectedUser, executor, new GetFollowingCountObserver());
    }

    public class GetFollowingCountObserver implements FollowService.GetFollowingCountObserver{
        @Override
        public void handleSuccess(int count) {
            view.setFolloweeCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get following count because of exception: " + ex.getMessage());

        }
    }

    public class GetFollowersCountObserver implements FollowService.GetFollowersCountObserver{
        @Override
        public void handleSuccess(int count) {
            view.setFollowerCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get followers count because of exception: " + ex.getMessage());
        }
    }


    public class GetCreateStatusObserver implements StatusService.GetCreateStatusObserver {
        @Override
        public void handleSuccess() {
            view.finishStatusCreation();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to post status because of exception: " + ex.getMessage());
        }
    }

    public class GetLogoutObserver implements UserService.GetLogoutObserver {

        @Override
        public void handleSuccess() {
            view.finishLogout();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to logout because of exception: " + ex.getMessage());
        }
    }

    public class GetIsFollowerObserver implements FollowService.GetIsFollowerObserver {
        @Override
        public void handleSuccess(boolean isFollower) {
            view.setFollowButton(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
        }
    }

    public class GetMainUnfollowObserver implements FollowService.GetMainUnfollowObserver{
        @Override
        public void handleSuccess() {
            view.updateUserInfo(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }
    }

    public class GetMainFollowObserver implements FollowService.GetMainFollowObserver{
        @Override
        public void handleSuccess() {
            view.updateUserInfo(false);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to follow because of exception: " + ex.getMessage());
        }
    }



}
