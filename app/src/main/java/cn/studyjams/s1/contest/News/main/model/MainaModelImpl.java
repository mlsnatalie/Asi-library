package cn.studyjams.s1.contest.News.main.model;

import org.asi.core.http.HttpResult;
import org.asi.core.http.ServiceFactory;
import org.asi.core.http.TransformUtils;

import java.util.List;

import cn.studyjams.s1.contest.News.main.api.GankService;
import cn.studyjams.s1.contest.News.main.contract.MainContract;
import rx.Observable;


/**
* Created by asi on 2016/11/16
*/

public class MainaModelImpl implements MainContract.Model{

    @Override
    public Observable<HttpResult<List<String>>> loadRecentlyDate() {
        return ServiceFactory.getInstance().createService(GankService.class).getRecentlyDate().compose(TransformUtils.<HttpResult<List<String>>>defaultSchedulers());
    }
}