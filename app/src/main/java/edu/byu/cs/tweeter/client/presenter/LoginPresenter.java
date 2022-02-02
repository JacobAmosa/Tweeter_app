package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;

import edu.byu.cs.shared.model.domain.AuthToken;
import edu.byu.cs.shared.model.domain.User;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class LoginPresenter {

    private View view;
    private UserService userService;

    public interface View{
        void displayErrorMessage(String message);
        void finishSuccessfulLogin(User loggedInUser);
    }

    public LoginPresenter(View view) {
        this.view = view;
        this.userService = new UserService();
    }

    public void loginUser(String userAlias, String password) {
        userService.loginUserTask(userAlias, password, new GetLoginUserObserver());
    }

    public void validateLogin(EditText alias, EditText password) {
        if (alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public class GetLoginUserObserver implements UserService.GetLoginUserObserver{

        @Override
        public void handleUserSuccess(User loggedInUser, AuthToken authToken) {
            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            view.finishSuccessfulLogin(loggedInUser);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayErrorMessage("Failed to login because of exception: " + ex.getMessage());
        }
    }


}
