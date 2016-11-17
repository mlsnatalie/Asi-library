package org.asi.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.asi.core.R;

/**
 * Created by asi on 2016/11/16.
 */

public class StatusView extends FrameLayout {

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private LayoutParams mLayoutParams;
    private OnClickListener mOnRetryListener;
    private TextView status_view_tv_error;

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();


    }

    private void initView() {
        mLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.status_view_layout_loading, null);
        mErrorView = LayoutInflater.from(getContext()).inflate(R.layout.status_view_layout_error, null);
        mEmptyView = LayoutInflater.from(getContext()).inflate(R.layout.status_view_layout_empty, null);
        status_view_tv_error = (TextView) mErrorView.findViewById(R.id.status_view_tv_error);
        addView(mLoadingView, mLayoutParams);
        addView(mErrorView, mLayoutParams);
        addView(mEmptyView, mLayoutParams);

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnRetryListener != null) {
                    showLoading();
                    mOnRetryListener.onClick(view);
                }
            }
        });
        showContent();
    }
    public void setOnRetryListener(OnClickListener listener) {
        mOnRetryListener = listener;
    }


    public void showLoading() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public void showError() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        mErrorView.setVisibility(View.VISIBLE);
    }

    public void showError(String msg) {
        showError();
        status_view_tv_error.setText(msg);

    }

    public void showEmpty() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        mEmptyView.setVisibility(View.VISIBLE);
    }

    public void showContent() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setVisibility(View.GONE);
        }
        getChildAt(getChildCount() - 1).setVisibility(View.VISIBLE);
    }
}

