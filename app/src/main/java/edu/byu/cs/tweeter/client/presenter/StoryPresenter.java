package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.shared.model.domain.Status;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class StoryPresenter {
    private static final int PAGE_SIZE = 10;
    private Status lastStatus;
    private boolean hasMorePages;
    private boolean isLoading = false;
    private View view;
    private UserService userService;
    private StatusService statusService;

    public interface View {
        void displayErrorMessage(String message);
        void changeActivity(User user);
        void setLoadingStatus(Boolean value);
        void addStatuses(List<Status> statuses);
    }

    public StoryPresenter(View view) {
        this.view = view;
        this.userService = new UserService();
        this.statusService = new StatusService();
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

    public void getUserProfile(String userAlias) {
        userService.getStoryUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetStoryUserObserver());
    }

    public void getClickableUser(String clickable) {
        userService.getStoryClickable(Cache.getInstance().getCurrUserAuthToken(), clickable, new GetStoryUserObserver());
    }

    public void getStory(User user) {
        if (!isLoading) {
            isLoading = true;
            view.setLoadingStatus(true);
            statusService.getUserStory(Cache.getInstance().getCurrUserAuthToken(),user, PAGE_SIZE, lastStatus, new GetStoryObserver());
        }
    }

    public class GetStoryObserver implements StatusService.GetStoryObserver{
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
            view.displayErrorMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get story because of exception: " + ex.getMessage());
        }
    }



    public class GetStoryUserObserver implements UserService.GetStoryUserObserver{

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
