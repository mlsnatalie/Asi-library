package cn.studyjams.s1.contest.News.main.contract;

import org.asi.core.base.BaseModel;
import org.asi.core.base.BasePresenter;
import org.asi.core.base.BaseView;
import org.asi.core.http.HttpResult;

import java.util.List;

import rx.Observable;

/**
 * Created by asi on 2016/11/16.
 */

public interface MainContract {

    interface View extends BaseView {
            void showLoading();
        void setData(List<String> strings);
            void hideLoading();
            void showError(String errMsg);
    }

    interface Model extends BaseModel {
        Observable<HttpResult<List<String>>> loadRecentlyDate();
    }

    abstract  class Presenter extends BasePresenter<View> {
    }
}