package com.singleevent.sdk.Left_Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.singleevent.sdk.Custom_View.Util;
import com.singleevent.sdk.model.Feed;
import com.singleevent.sdk.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Miroslaw Stanek on 02.12.2015.
 */
public class FeedItemAnimator extends DefaultItemAnimator {
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimationsMap = new HashMap<>();
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();

    private int lastAddAnimatedItem = -2;

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    return new FeedItemHolderInfo((String) payload);
                }
            }
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getLayoutPosition() > lastAddAnimatedItem) {
            lastAddAnimatedItem++;
            runEnterAnimation((FeedAdapter_co.MyViewHolder) viewHolder);
            return false;
        }

        dispatchAddFinished(viewHolder);
        return false;
    }

    private void runEnterAnimation(final FeedAdapter_co.MyViewHolder holder) {
        final int screenHeight = Util.getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {
        cancelCurrentAnimationIfExists(newHolder);

        if (preInfo instanceof FeedItemHolderInfo) {
            FeedItemHolderInfo feedItemHolderInfo = (FeedItemHolderInfo) preInfo;
            FeedAdapter_co.MyViewHolder holder = (FeedAdapter_co.MyViewHolder) newHolder;

            animateHeartButton(holder, (Feed) holder.itemView.getTag());
//            updateLikesCounter(holder, (Feed) holder.itemView.getTag());
        }

        return false;
    }

    private void cancelCurrentAnimationIfExists(RecyclerView.ViewHolder item) {
        if (likeAnimationsMap.containsKey(item)) {
            likeAnimationsMap.get(item).cancel();
        }
        if (heartAnimationsMap.containsKey(item)) {
            heartAnimationsMap.get(item).cancel();
        }
    }

    private void animateHeartButton(final FeedAdapter_co.MyViewHolder holder, Feed feed) {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator rotationAnim = null, bounceAnimX = null, bounceAnimY = null;
        if (feed.getLike_status() == 1) {
            rotationAnim = ObjectAnimator.ofFloat(holder.like, "rotation", 0f, 360f);
            rotationAnim.setDuration(300);
            rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

            bounceAnimX = ObjectAnimator.ofFloat(holder.like, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(300);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            bounceAnimY = ObjectAnimator.ofFloat(holder.like, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(300);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    holder.like.setImageResource(R.drawable.s_ic_heart);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    heartAnimationsMap.remove(holder);
                    dispatchChangeFinishedIfAllAnimationsEnded(holder);
                }
            });
        } else {
            rotationAnim = ObjectAnimator.ofFloat(holder.like, "rotation", 360f, 0f);
            rotationAnim.setDuration(300);
            rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

            bounceAnimX = ObjectAnimator.ofFloat(holder.like, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(300);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            bounceAnimY = ObjectAnimator.ofFloat(holder.like, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(300);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    holder.like.setImageResource(R.drawable.s_ic_heart_wofilled);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    heartAnimationsMap.remove(holder);
                    dispatchChangeFinishedIfAllAnimationsEnded(holder);
                }
            });
        }


        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

        heartAnimationsMap.put(holder, animatorSet);
    }

    private void updateLikesCounter(FeedAdapter_co.MyViewHolder holder, Feed feed) {
//
//        String likesCountTextFrom = feed.getLikesCount() > 0 ? holder.likes.getResources().getQuantityString(R.plurals.likes_count, feed.getLikesCount(), feed.getLikesCount()) : "Like";
//
//        holder.likes.setCurrentText(likesCountTextFrom);

        String likesCountTextTo = holder.likes.getResources().getQuantityString(R.plurals.likes_count, feed.getLikesCount(), feed.getLikesCount());
        holder.likes.setText(likesCountTextTo);
    }


    private void dispatchChangeFinishedIfAllAnimationsEnded(FeedAdapter_co.MyViewHolder holder) {
        if (likeAnimationsMap.containsKey(holder) || heartAnimationsMap.containsKey(holder)) {
            return;
        }

        dispatchAnimationFinished(holder);
    }

    private void resetLikeAnimationState(FeedAdapter_co.MyViewHolder holder) {
        holder.like.setVisibility(View.INVISIBLE);
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelCurrentAnimationIfExists(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
        for (AnimatorSet animatorSet : likeAnimationsMap.values()) {
            animatorSet.cancel();
        }
    }

    public static class FeedItemHolderInfo extends ItemHolderInfo {
        public String updateAction;

        public FeedItemHolderInfo(String updateAction) {
            this.updateAction = updateAction;
        }
    }
}
