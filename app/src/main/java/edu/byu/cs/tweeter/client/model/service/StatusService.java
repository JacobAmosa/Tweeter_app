package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.shared.model.domain.AuthToken;
import edu.byu.cs.shared.model.domain.Status;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.CreateStatusHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetStatusUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;

public class StatusService {
    //    *****************************   MainActivity createStatus ***************************************

    public interface GetCreateStatusObserver {
        void handleSuccess();
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void createStatusTask(AuthToken currUserAuthToken, Status newStatus, GetCreateStatusObserver getCreateStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                newStatus, new CreateStatusHandler(getCreateStatusObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }


    //******************************** FeedPresenter getUserStatus **************************************************

    public interface GetStatusUserObserver {
        void handleUserSuccess(User user);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void getStatusUser(AuthToken currUserAuthToken, String userAlias, GetStatusUserObserver getStatusUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAlias, new GetStatusUserHandler(getStatusUserObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }


    //******************************** Feed Presenter **************************************************
    public interface GetFeedObserver {
        void handleUserSuccess(List<Status> statuses, boolean hasMorePages);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetFeedObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(getFeedObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFeedTask);
    }

    //******************************** StoryPresenter **************************************************
    public interface GetStoryObserver {
        void handleUserSuccess(List<Status> statuses, boolean hasMorePages);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void getUserStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, StoryPresenter.GetStoryObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetStoryHandler(getStoryObserver));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getStoryTask);
    }


}
