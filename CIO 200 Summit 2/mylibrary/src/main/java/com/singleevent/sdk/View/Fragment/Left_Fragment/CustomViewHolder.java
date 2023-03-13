package com.singleevent.sdk.View.Fragment.Left_Fragment;


import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.feeds_class.Scheduler;

/**
 * Created by carl on 12/1/15.
 */
public class CustomViewHolder extends RecyclerView.ViewHolder {

    private ViewDataBinding mViewDataBinding;

    public CustomViewHolder(ViewDataBinding viewDataBinding, Scheduler sch, boolean isRequest, int background) {
        super(viewDataBinding.getRoot());
        mViewDataBinding = viewDataBinding;
        mViewDataBinding.setVariable(com.singleevent.sdk.BR.presenter, sch);
        mViewDataBinding.setVariable(com.singleevent.sdk.BR.isRequest, isRequest);
        mViewDataBinding.setVariable(com.singleevent.sdk.BR.background,background);
        mViewDataBinding.executePendingBindings();
    }

    public ViewDataBinding getViewDataBinding() {
        return mViewDataBinding;
    }
}
