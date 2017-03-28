package org.asi.core.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import org.asi.core.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity<V extends  BaseView,T extends BasePresenter<V>> extends AppCompatActivity {

    private  CompositeSubscription mSubscriptions ;
    protected T mPresenter;
    /**
     * 权限授权回掉
     */
    PermissionListener mPermissionListener;
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
        registerEvent();
    }

    /**
     * 注册RxBus监听事件
     */
    protected void registerEvent() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
        if (mSubscriptions != null){
            mSubscriptions.clear();
        }
    }

    public <T> void addRxBusEvent(Class<T> eventType, Action1<? super  T>onNext ) {
        Subscription subscribe = RxBus.getDefault().toObservable(eventType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext);
        addSubscription(subscribe);
    }

    private void addSubscription(Subscription subscribe) {
        if (mSubscriptions==null){
            mSubscriptions=new CompositeSubscription();
        }
        mSubscriptions.add(subscribe);
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



    /**
     * 权限申请
     * @param permissions
     * @param listener
     */
    protected void requestRunTimePermission(String[] permissions, PermissionListener listener) {

        //todo 获取栈顶activity，如果null。return；

        this.mPermissionListener = listener;

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
            }
        }
        if(!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else{
            listener.onGranted();
        }
    }

    /**
     * 申请结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    List<String> deniedPermissions = new ArrayList<>();
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED){
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }else{
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()){
                        mPermissionListener.onGranted();
                    }else{
                        mPermissionListener.onDenied(deniedPermissions);
                        mPermissionListener.onGranted(grantedPermissions);
                    }
                }
                break;
        }
    }
}
