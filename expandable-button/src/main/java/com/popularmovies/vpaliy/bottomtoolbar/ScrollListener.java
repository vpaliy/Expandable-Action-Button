package com.popularmovies.vpaliy.bottomtoolbar;

/** Vasyl Paliy 2017
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class ScrollListener extends RecyclerView.OnScrollListener {

    private float offsetY=0.f;
    private boolean hasStopped=true;

    private static final long LIMIT=150L;
    private static final long FADE_IN_OUT_DURATION=200L;

    private ExpandableButtonView expandableButton;
    private long duration=FADE_IN_OUT_DURATION;

    public ScrollListener(@NonNull ExpandableButtonView expandableButton) {
        this.expandableButton = expandableButton;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (RecyclerView.SCROLL_STATE_IDLE == newState || RecyclerView.SCROLL_STATE_SETTLING == newState) {
            if (Math.abs(offsetY) >= LIMIT) {
                if (expandableButton.getFlag() ==ExpandableButtonView.FINISHED) {
                    expandableButton.removeBottomToolbar();
                    expandableButton.setFlag(ExpandableButtonView.IDLE);
                    hasStopped = false;
                } else if (hasStopped && expandableButton.getFlag() == ExpandableButtonView.IDLE) {
                    if (expandableButton.getScaleX() > 0f) {
                        expandableButton.setClickable(false);
                        expandableButton.animate().setInterpolator(new AccelerateInterpolator())
                                .scaleY(0.f).scaleX(0.f).setDuration(FADE_IN_OUT_DURATION).start();
                    }
                }

                if (!hasStopped) {
                    hasStopped = (newState == RecyclerView.SCROLL_STATE_IDLE)
                            || (Math.abs(offsetY) >= 8 * LIMIT);
                }
                offsetY = 0;
            }

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (expandableButton.getScaleX() < 1.f) {
                    expandableButton.setClickable(true);
                    expandableButton.animate().scaleX(1.f).setListener(null).
                            setInterpolator(new DecelerateInterpolator()).scaleY(1.f).
                            setDuration(FADE_IN_OUT_DURATION / 2).start();
                }
            }
        }
    }

    public void setFadeInOutDuration(long duration){
        if(duration>0){
            this.duration=duration;
        }else{
            this.duration=FADE_IN_OUT_DURATION;
        }
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        offsetY+=dy;
    }
}
