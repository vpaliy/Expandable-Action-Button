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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.popularmovies.vpaliy.bottomtoolbar.core.AnimatorPath;
import com.popularmovies.vpaliy.bottomtoolbar.core.PathEvaluator;
import com.popularmovies.vpaliy.bottomtoolbar.core.PathPoint;
import java.util.LinkedList;
import java.util.List;

import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;


public class ExpandableButtonView extends FrameLayout{


    private static final String TAG= ExpandableButtonView.class.getSimpleName();

    private FloatingActionButton actionButton;
    private LinearLayout toolbarLayout;

    private List<View> itemList=new LinkedList<>();
    private List<View> wrappedItems=new LinkedList<>();

    private final int DEFAULT_MARGIN=-1;
    private final long ANIMATION_DELAY=50;
    private final long ANIMATION_DURATION=300;

    private int rightMargin;
    private int bottomMargin;
    private int topMargin;
    private int leftMargin;

    static final int IDLE=1;
    static final int RUNNING=0;
    static final int FINISHED=-1;

    private int flag=IDLE;
    private long duration=ANIMATION_DURATION;

    private ObjectAnimator actionButtonAnimator;
    private Drawable fabDrawable;


    public ExpandableButtonView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ExpandableButtonView(@NonNull Context context,
                                @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableButtonView(@NonNull Context context,
                                @Nullable AttributeSet attrs,
                                @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttrs(attrs);
    }

    public ExpandableButtonView(@NonNull Context context, @Nullable AttributeSet attrs,
                                @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        initAttrs(attrs);
    }

    private void initAttrs(@Nullable AttributeSet attrs) {
        if (attrs == null)
            return;

        float density = getResources().getDisplayMetrics().density;
        TypedArray array = getContext().obtainStyledAttributes(attrs,
                R.styleable.ExpandableButtonView);

        for(int index=0;index<array.getIndexCount();index++) {
            int attr=array.getIndex(index);
            if(attr==R.styleable.ExpandableButtonView_button_width) {
                setButtonWidth(array.getDimensionPixelSize(attr, (int) (56 * density)));
            }else if(attr==R.styleable.ExpandableButtonView_button_height) {
                setButtonHeight(array.getDimensionPixelSize(attr, (int) (56 * density)));
            }else if(attr==R.styleable.ExpandableButtonView_button_color) {
                setButtonColor(array.getColor(attr, 0));
            }else if(attr==R.styleable.ExpandableButtonView_button_icon) {
                setButtonIcon(array.getResourceId(attr, -1));
            }else if(attr==R.styleable.ExpandableButtonView_toolbar_color) {
                setToolbarColor(array.getColor(attr, -1));
            }else if(attr==R.styleable.ExpandableButtonView_duration) {
                setDuration(array.getInteger(attr, 300));
            }
        }
        array.recycle();
    }

    private void init(Context context){
        actionButton=new FloatingActionButton(context);
        toolbarLayout=new LinearLayout(getContext());
        float density = getResources().getDisplayMetrics().density;
        addView(actionButton,(int)(56*density),(int)(56*density));
        FrameLayout.LayoutParams params=
                FrameLayout.LayoutParams.class.cast(actionButton.getLayoutParams());
        params.gravity= Gravity.CENTER;
        actionButton.setLayoutParams(params);
        toolbarLayout.setGravity(Gravity.CENTER);
        actionButton.setImageResource(android.R.drawable.ic_menu_share);
        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });

        fabDrawable=actionButton.getDrawable();
        setToolbarColor(-1);
        //


    }


    private void onButtonClick(){
        setOffContainerMargin();
        final Rect oldButtonLocation=new Rect();
        final float oldContainerTop=getTop();
        actionButton.getGlobalVisibleRect(oldButtonLocation);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);

                final float SCALE_X=2*((float)(getWidth())/(actionButton.getWidth()));
                final float SCALE_Y=4f*((float)(getHeight())/(actionButton.getHeight()));

                final Rect currentContainerLocation=new Rect();
                final Rect currentButtonLocation=new Rect();
                actionButton.getGlobalVisibleRect(currentButtonLocation);
                getGlobalVisibleRect(currentContainerLocation);

                final float deltaX=currentButtonLocation.left-oldButtonLocation.left;
                final float deltaY=currentButtonLocation.top-oldButtonLocation.top-bottomMargin;

                actionButton.animate()
                        .translationY(-deltaY)
                        .translationX(-deltaX)
                        .setDuration(0)
                        .setListener(new AnimatorListenerAdapter() {

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                animate().setDuration(0)
                                        .translationY(-(getTop()-oldContainerTop)).start();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                actionButton.setImageDrawable(null);

                                final AnimatorPath animatorPath=new AnimatorPath();
                                animatorPath.moveTo(-deltaX,-deltaY);
                                animatorPath.curveTo(-deltaX,actionButton.getTranslationY(),
                                        -deltaX-actionButton.getWidth()/2f,0,
                                        actionButton.getTranslationX()-actionButton.getWidth(),0);


                                actionButtonAnimator=ObjectAnimator.ofObject(ExpandableButtonView.this,"Location",
                                        new PathEvaluator(),animatorPath.getPoints().toArray());

                                ObjectAnimator containerDownAnimator=ObjectAnimator.ofFloat(ExpandableButtonView.this, View.TRANSLATION_Y,0f);

                                AnimatorSet downAnimatorSet=new AnimatorSet();
                                downAnimatorSet.setDuration(duration/2);
                                downAnimatorSet.setInterpolator(new DecelerateInterpolator());
                                downAnimatorSet.playTogether(actionButtonAnimator,containerDownAnimator);

                                downAnimatorSet.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        flag=RUNNING;
                                        actionButton.animate()
                                                .setListener(HOOK_UP_TOOLBAR).setDuration(duration)
                                                .setInterpolator(new DecelerateInterpolator())
                                                //.translationX(0f).translationY(0f) // <-- if you want to translate the fab to the center
                                                .scaleX(SCALE_X).scaleY(SCALE_Y).start();

                                    }
                                });
                                downAnimatorSet.start();
                            }

                        }).start();

                return true;
            }
        });
    }

    private void setOffContainerMargin(){
        if(getLayoutParams() instanceof MarginLayoutParams){

            MarginLayoutParams params=MarginLayoutParams.class.cast(getLayoutParams());
            params.width=ViewGroup.LayoutParams.MATCH_PARENT;
            this.rightMargin=checkAndAssign(params.rightMargin);
            this.bottomMargin=checkAndAssign(params.bottomMargin);
            this.topMargin=checkAndAssign(params.topMargin);
            this.leftMargin= checkAndAssign(params.leftMargin);

            params.bottomMargin=0;
            params.leftMargin=0;
            params.topMargin=0;
            params.rightMargin=0;

            setLayoutParams(params);
        }
    }

    private void setLocation(PathPoint point) {
        actionButton.setTranslationY(point.mY);
        actionButton.setTranslationX(point.mX);
    }

    private int checkAndAssign(int margin){
        switch (margin){
            case DEFAULT_MARGIN:
                return 0;
            case 0:
                return DEFAULT_MARGIN;
            default:
                return margin;
        }
    }


    public void addToolbarItem(View...items){
        for(View item:items) {
            if (item instanceof ViewGroup) {
                throw new IllegalArgumentException("Item must not be a ViewGroup");
            }

            if(!itemList.contains(item)){
                item.setScaleX(0);
                item.setScaleY(0);
                item.setBackgroundColor(Color.TRANSPARENT);
                LinearLayout layout=new LinearLayout(getContext());
                itemList.add(item);
                layout.setGravity(Gravity.CENTER);
                layout.addView(item);
                wrappedItems.add(layout);
            }

        }

    }

    private void inflateToolbarLayout(){
        addView(toolbarLayout, ViewGroup.LayoutParams.MATCH_PARENT,actionButton.getHeight());
        for(View view:wrappedItems){
            toolbarLayout.addView(view);
            LinearLayout.LayoutParams childParams=
                    LinearLayout.LayoutParams.class.cast(view.getLayoutParams());
            childParams.width=0;
            childParams.weight=1;
            view.setLayoutParams(childParams);
        }
    }



    private FrameLayout.LayoutParams getChildParams(View view){
        return FrameLayout.LayoutParams.class.cast(view.getLayoutParams());
    }


    public void removeBottomToolbar() {
        for (int index = 0; index < toolbarLayout.getChildCount(); index++) {
            itemList.get(index).animate()
                    .scaleX(0.f).scaleY(0.f).setStartDelay(index * ANIMATION_DELAY / 2).start();
        }

        toolbarLayout.removeAllViews();
        removeView(toolbarLayout);

        actionButton.setVisibility(View.VISIBLE);

        actionButton.animate().scaleX(1.f).scaleY(1.f).setDuration(duration/2).
                setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        actionButton.setImageDrawable(fabDrawable);
                        actionButtonAnimator.setDuration(duration/2);
                        actionButtonAnimator.setInterpolator(new DecelerateInterpolator());
                        actionButtonAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                animate().setDuration(duration/2).setListener(null).
                                        setInterpolator(new DecelerateInterpolator()).
                                        translationY(-bottomMargin).start();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                actionButton.setTranslationX(0);
                                actionButton.setTranslationY(0);

                                setOnMargin();

                                getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw() {
                                        getViewTreeObserver().removeOnPreDrawListener(this);
                                        setTranslationY(0f);
                                        return true;
                                    }
                                });
                            }
                        });
                        actionButtonAnimator.reverse();
                    }

                }).start();
    }


    private void setOnMargin(){
        if(getLayoutParams() instanceof MarginLayoutParams){
            MarginLayoutParams params=MarginLayoutParams.class.cast(getLayoutParams());
            params.width=ViewGroup.LayoutParams.WRAP_CONTENT;
            params.rightMargin=checkAndAssign(rightMargin);
            params.bottomMargin=checkAndAssign(bottomMargin);
            params.topMargin=checkAndAssign(topMargin);
            params.leftMargin=checkAndAssign(leftMargin);

            bottomMargin=DEFAULT_MARGIN;
            leftMargin=DEFAULT_MARGIN;
            topMargin=DEFAULT_MARGIN;
            rightMargin=DEFAULT_MARGIN;

            setLayoutParams(params);
        }
    }

    private final AnimatorListenerAdapter HOOK_UP_TOOLBAR=new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //here is the place to install  properties of LinearLayout (color, size, etc.)
            actionButton.setVisibility(View.INVISIBLE);
            inflateToolbarLayout();

            for(int index=0;index<toolbarLayout.getChildCount();index++) {
                View button=itemList.get(index);
                button.animate().scaleX(1.f).scaleY(1.f)
                        .setStartDelay(index*ANIMATION_DELAY).start();
            }

            flag=FINISHED;


        }
    };

    public void setButtonWidth(float width){
        FrameLayout.LayoutParams params=getChildParams(actionButton);
        params.width=(int)width;
        actionButton.setLayoutParams(params);
    }

    public void setButtonHeight(float height){
        FrameLayout.LayoutParams params=getChildParams(actionButton);
        params.height=(int)height;
        actionButton.setLayoutParams(params);
    }

    public void setButtonIcon(int resource){
        if(resource!=-1) {
            actionButton.setImageResource(resource);
        }
    }

    public void setButtonColor(int buttonColor){
        if(buttonColor!=-1){
            actionButton.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        }
    }

    public void setToolbarColor(int toolbarColor){
        if(toolbarColor!=-1){
            toolbarLayout.setBackgroundColor(toolbarColor);
        }else{
            if(actionButton.getBackgroundTintList()!=null) {
                toolbarLayout.setBackgroundColor(actionButton.getBackgroundTintList().getDefaultColor());
            }else{
                toolbarLayout.setBackgroundColor(actionButton.getSolidColor());
            }
        }
    }

    public void setDuration(long duration) {
        if(duration>0) {
            this.duration = duration;
        }
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}
