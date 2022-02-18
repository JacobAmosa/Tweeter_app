package edu.byu.cs.tweeter.client.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.shared.model.domain.AuthToken;
import edu.byu.cs.shared.model.domain.Status;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;

public class MainPresenterUnitTest {

    private MainActivityPresenter.View mockView;
    private UserService mockUserService;
    private Cache mockCache;
    private StatusService mockStatusService;

    private MainActivityPresenter mainPresenterSpy;


    @Before
    public void setup(){
        mockView = Mockito.mock(MainActivityPresenter.View.class);
        mockUserService = Mockito.mock(UserService.class);
        mockCache = Mockito.mock(Cache.class);
        mockStatusService = Mockito.mock(StatusService.class);

        mainPresenterSpy = Mockito.spy(new MainActivityPresenter(mockView));
//        Mockito.doReturn(mockUserService).when(mainPresenterSpy).getUserService();
        Mockito.when(mainPresenterSpy.getUserService()).thenReturn(mockUserService);
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }

    //************************** LOGOUT TESTS FROM DEMO ********************************************

    @Test
    public void testLogout_logoutSuccessful(){
        // the answer is the replacement for the logout method in Mocking.
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.GetLogoutObserver observer = invocation.getArgument(1, MainActivityPresenter.GetLogoutObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).logoutUserTask(Mockito.any(), Mockito.any());
        mainPresenterSpy.logoutUser();
        Mockito.verify(mockView).finishLogout("Logging Out...");
    }

    @Test
    public void testLogout_logoutFailedWithMessage(){
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.GetLogoutObserver observer = invocation.getArgument(1, MainActivityPresenter.GetLogoutObserver.class);
                observer.handleFailure("");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).logoutUserTask(Mockito.any(), Mockito.any());
        mainPresenterSpy.logoutUser();
        Mockito.verify(mockView).displayErrorMessage("Failed to logout: ");
    }

    @Test
    public void testLogout_logoutFailedWithException(){
        Exception mockException = Mockito.mock(Exception.class);
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.GetLogoutObserver observer = invocation.getArgument(1, MainActivityPresenter.GetLogoutObserver.class);
                observer.handleException(mockException);
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockUserService).logoutUserTask(Mockito.any(), Mockito.any());
        mainPresenterSpy.logoutUser();
        Mockito.verify(mockView).displayErrorMessage("Failed to logout because of exception: " + mockException.getMessage());
    }

    //*************************** POST TESTS FROM DEMO *********************************************

    @Test
    public void testPost_PostSuccessful(){
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.GetCreateStatusObserver observer = invocation.getArgument(2, MainActivityPresenter.GetCreateStatusObserver.class);

                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).createStatusTask(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.createStatus(Mockito.any());
        Mockito.verify(mockView).finishStatusCreation("Successfully Posted!");
    }

    @Test
    public void testPost_PostFailedWithMessage(){
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.GetCreateStatusObserver observer = invocation.getArgument(2, MainActivityPresenter.GetCreateStatusObserver.class);
                observer.handleFailure("errorMessage");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).createStatusTask(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.createStatus(Mockito.any());
        Mockito.verify(mockView).displayErrorMessage("Failed to post status: " + "errorMessage");
    }

    @Test
    public void testPost_PostFailedWithException(){
        Exception mockException = Mockito.mock(Exception.class);
        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainActivityPresenter.GetCreateStatusObserver observer = invocation.getArgument(2, MainActivityPresenter.GetCreateStatusObserver.class);
                observer.handleException(mockException);
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).createStatusTask(Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.createStatus(Mockito.any());
        Mockito.verify(mockView).displayErrorMessage("Failed to post status because of exception: " + mockException.getMessage());
    }

    @Test
    public void testPost_PostWithValidParameters(){
        Cache mockCache = Mockito.mock(Cache.class);
        Cache.setInstance(mockCache);
        Status mockStatus = Mockito.mock(Status.class);

        Answer<Void> answer = new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                System.out.println(invocation);
                Assert.assertEquals(invocation.getArgument(0, AuthToken.class).getToken(), "456");
                Assert.assertEquals(invocation.getArgument(1, Status.class).getPost(), "my test");
                Assert.assertNotNull(invocation.getArgument(2));
                return null;
            }
        };

        Mockito.when(mockCache.getCurrUserAuthToken()).thenReturn(new AuthToken("456"));
        Mockito.when(mockStatus.getPost()).thenReturn("my test");

        Mockito.doAnswer(answer).when(mockStatusService).createStatusTask(Mockito.any(AuthToken.class), Mockito.any(Status.class),
                Mockito.any(MainActivityPresenter.GetCreateStatusObserver.class));
//        mainPresenterSpy.createStatus(new Status("my test", Mockito.any(), Mockito.any(), Mockito.anyList(), Mockito.anyList()));
        mainPresenterSpy.createStatus(mockStatus);

    }

}

