package cn.studyjams.s1.contest.News.main.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.asi.core.base.BaseFragment;
import org.asi.core.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import cn.studyjams.s1.contest.News.R;
import cn.studyjams.s1.contest.News.main.contract.MainContract;
import cn.studyjams.s1.contest.News.main.presenter.MainPresenterImpl;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends BaseFragment<MainContract.View,MainContract.Presenter> implements MainContract.View {
    @InjectView(R.id.rv)
    RecyclerView mRv;
    @InjectView(R.id.sl)
    SwipeRefreshLayout mSl;
    private QuickAdapter mQuickAdapter;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_blank;
    }

    @Override
    protected MainContract.Presenter initPresenter() {
        return new MainPresenterImpl();
    }


    @Override
    protected void onInitView(Bundle savedInstanceState) {
        mRv.setLayoutManager(new StaggeredGridLayoutManager(4,
                StaggeredGridLayoutManager.VERTICAL));
        mRv.addItemDecoration(new SpacesItemDecoration(10));
        mQuickAdapter = new QuickAdapter(R.layout.item, new ArrayList<String>());
        mRv.setAdapter(mQuickAdapter);
        mSl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSl.setRefreshing(true);
             mPresenter.onStart();
            }
        });
    }
    public class QuickAdapter extends BaseQuickAdapter<String> {


        public QuickAdapter(int layoutResId, List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, String s) {
            baseViewHolder.setText(R.id.tv,s);
        }
    }

    @Override
    protected void onInitData() {

        mPresenter.onStart();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void setData(List<String> strings) {
        mQuickAdapter.setNewData(strings);
        mSl.setRefreshing(false);
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError(String errMsg) {
        mSl.setRefreshing(false);
    }
}
