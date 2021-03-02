package com.byl.mvp.ui.login.prestener;

import com.byl.mvp.api.HttpResult;
import com.byl.mvp.api.presenter.BaseObserver;
import com.byl.mvp.api.presenter.BasePresenter;
import com.byl.mvp.api.subscribe.AccountSubscribe;
import com.byl.mvp.ui.login.model.UserBean;
import com.byl.mvp.ui.login.mvpview.LoginMvpView;


public class LoginPresenter extends BasePresenter<LoginMvpView> {

    public void login(String userName, String password) {
        AccountSubscribe.newInstance().login(userName, password)
                .subscribe(new BaseObserver<HttpResult<UserBean>>(disposables) {
                    @Override
                    protected void onSuccess(HttpResult<UserBean> result) {
                        if (getMvpView() == null) return;
                        getMvpView().safeExecute(view -> view.loginSuccess(result.result));
                    }

                    @Override
                    protected void onFailure(int error_code, String error_msg) {
                        if (getMvpView() == null) return;
                        getMvpView().safeExecute(view -> view.loginFail(error_code, error_msg));
                    }
                });

    }

}
