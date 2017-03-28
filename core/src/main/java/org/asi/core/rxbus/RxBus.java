package org.asi.core.rxbus;

import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;
/**
*
 * @Title:  org.asi.core.rxbus
 * @Description:  rxbus
 * @author: asi
 * @data:  2017/3/28  13:20
 * @version: 1.0
 */
public class RxBus {
    private static volatile RxBus mInstance;
    private final Subject<Object, Object>mBus;
    private final Map<Class<?>, Object> mStickyEventMap;
    private final CompositeSubscription mSubscriptions ;

    public RxBus() {
        mBus = new SerializedSubject(PublishSubject.create());
        mStickyEventMap = new HashMap();
        mSubscriptions = new CompositeSubscription();
    }

    public static RxBus getDefault() {
        if(mInstance == null) {
            synchronized(RxBus.class) {
                if(mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }

        return mInstance;
    }

    public void post(Object event) {
        this.mBus.onNext(event);
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return this.mBus.ofType(eventType);
    }

    public boolean hasObservers() {
        return this.mBus.hasObservers();
    }

    public void reset() {
        mInstance = null;
    }

    public boolean isUnsubscribed() {
        return this.mSubscriptions.isUnsubscribed();
    }

    public void add(Subscription s) {
        if(s != null) {
            this.mSubscriptions.add(s);
        }

    }

    public void remove(Subscription s) {
        if(s != null) {
            this.mSubscriptions.remove(s);
        }

    }

    public void clear() {
        this.mSubscriptions.clear();
    }

    public void unsubscribe() {
        this.mSubscriptions.unsubscribe();
    }

    public boolean hasSubscriptions() {
        return this.mSubscriptions.hasSubscriptions();
    }




    /**
     * 发送一个新Sticky事件
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }


    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = mBus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);
            return event != null?Observable.merge(observable, Observable.create(new Observable.OnSubscribe<T>() {
                public void call(Subscriber<? super T> subscriber) {
                    subscriber.onNext(eventType.cast(event));
                }
            })):observable;
        }
    }

    /**
     * 根据eventType获取Sticky事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    public <T> T removeStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}