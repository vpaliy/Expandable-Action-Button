package com.popularmovies.vpaliy.bottomtoolbar;


import java.util.List;

public interface ExpandAnimationListener {
    void beforeButtonAnimation(List<ButtonItem> buttons);
    void afterButtonAnimation(List<ButtonItem> buttons);

}
