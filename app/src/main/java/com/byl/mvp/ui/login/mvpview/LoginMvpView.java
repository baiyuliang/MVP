package com.byl.mvp.ui.login.mvpview;


import com.byl.mvp.api.presenter.BaseMvpView;
import com.byl.mvp.ui.login.model.UserBean;

public interface LoginMvpView extends BaseMvpView {

    void loginSuccess(UserBean userBean);

    void loginFail(int error_code, String error_msg);

}
