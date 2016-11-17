package org.asi.core.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import butterknife.ButterKnife;

public abstract class BaseActivity<V extends  BaseView,T extends BasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;

    protected abstract int getLayoutResource();

    protected abstract void onInitView(Bundle bundle);
    protected abstract void onInitData( );
    protected abstract T setPresenter();

    /***
     * 用于在初始化View之前做一些事
     */
    protected void initPre(Bundle savedInstanceState) {

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initPre(savedInstanceState);
        if (getLayoutResource() != 0)
            setContentView(getLayoutResource());
        ButterKnife.inject(this);
        this.onInitView(savedInstanceState);
        mPresenter =setPresenter() ;
        mPresenter.attachView((V)this);
        this.onInitData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);

    }
    protected void addFragment(int id, Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().add(id,fragment).commit();
    }
    protected void replaceFragment(int id, Fragment fragment) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().replace(id,fragment).commit();
    }
}
