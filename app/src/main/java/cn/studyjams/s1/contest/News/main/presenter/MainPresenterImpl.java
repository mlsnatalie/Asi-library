package cn.studyjams.s1.contest.News.main.presenter;

import org.asi.core.http.HttpResultSubscriber;
import org.asi.core.utils.Logger;

import java.util.List;

import cn.studyjams.s1.contest.News.main.contract.MainContract;
import cn.studyjams.s1.contest.News.main.model.MainaModelImpl;

/**
* Created by asi on 2016/11/16
*/

public class MainPresenterImpl extends MainContract.Presenter {
MainContract.Model mModel;
    public MainPresenterImpl() {
        mModel = new MainaModelImpl();
    }

    @Override
    public void onStart() {
        mModel.loadRecentlyDate().subscribe(new HttpResultSubscriber<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                        getView().setData(strings);
                Logger.e("onSuccess"+strings.size());
            }

            @Override
            public void _onError(Throwable e) {
                Logger.e("_onError");
            }
        });
    }
}