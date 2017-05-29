package com.popularmovies.vpaliy.bottomtoolbar;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

public class ButtonItem extends AppCompatImageButton{


    public ButtonItem(@NonNull Context context) {
        super(context);
    }

    public ButtonItem(@NonNull Builder builder){
        super(builder.context);
        setOnClickListener(builder.listener);
        if(builder.resourceId!=-1) {
            setImage(builder.resourceId);
        }else if(builder.drawable!=null){
            setImage(builder.drawable);
        }
    }


    public ButtonItem setImage(@DrawableRes int resource){
        setImageResource(resource);
        return this;
    }

    public ButtonItem setImage(@NonNull Drawable drawable){
        setImageDrawable(drawable);
        return this;
    }

    public ButtonItem setImage(@NonNull Bitmap bitmap){
        setImageBitmap(bitmap);
        return this;
    }

    public ButtonItem setOnClick(OnClickListener onClickListener){
        setOnClickListener(onClickListener);
        return this;
    }


    public static class Builder {

        @DrawableRes
        private int resourceId=-1;
        private Drawable drawable;
        private OnClickListener listener;
        private Context context;

        public Builder(Context context){
            this.context=context;
        }

        public Builder setImageResource(@DrawableRes int resourceId){
            this.resourceId=resourceId;
            return this;
        }

        public Builder setImageDrawable(@NonNull Drawable drawable){
            this.drawable=drawable;
            return this;
        }

        public Builder setImageBitmap(@NonNull Bitmap bitmap){
            drawable=new BitmapDrawable(bitmap);
            return this;
        }

        public Builder setListener(OnClickListener onClickListener){
            this.listener=onClickListener;
            return this;
        }

        public ButtonItem build(){
            return build(this);
        }

        public static ButtonItem build(@NonNull Builder builder){
            return new ButtonItem(builder);
        }
    }
}
