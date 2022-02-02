package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.shared.model.domain.Status;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;

public class FeedPresenter {
    private static final int PAGE_SIZE = 10;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private View view;
    private StatusService statusService;
    private UserService userService;

    public interface View {
        void displayErrorMessage(String message);
        void changeActivity(User user);
        void setLoadingStatus(boolean value);
        void addStatuses(List<Status> statuses);
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public FeedPresenter(View view) {
        this.view = view;
        this.userService = new UserService();
        this.statusService = new StatusService();
    }

    public void getUserProfile(String userAlias) {
        statusService.getStatusUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetStatusUserObserver());
    }

    public void getFeed(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());
        }
    }

    public class GetFeedObserver implements StatusService.GetFeedObserver{

        @Override
        public void handleUserSuccess(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addStatuses(statuses);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get feed: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get feed because of exception: " + ex.getMessage());
        }
    }

    public class GetStatusUserObserver implements StatusService.GetStatusUserObserver {

        @Override
        public void handleUserSuccess(User user) {
            view.changeActivity(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + ex.getMessage());
        }
    }
}
