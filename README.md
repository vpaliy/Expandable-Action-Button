# Expandable Action Button

# How does it work? #

This sample shows transforming of floating action button into a toolbar upon press or from a toolbar upon scroll. The toolbar can contain different items, such as text, buttons, search fields, or any other items that would be useful at hand.

Moreover, this sample has implmentation of FAB animation(fading in and out) when the user scrolls down or upwards.

When the user presses the button, it goes down and inflates another layout which contains all important stuff. You can add listeners and work with the inflated layout as with plain `ViewGroup`.

# Screenshot #
![](https://github.com/vpaliyX/Expandable-Action-Button/blob/master/gifs/ezgif.com-video-to-gif.gif)

## Video [example](https://www.youtube.com/watch?v=AJ5TQtPsuY8) ##

## This is based on Google's  Material Design guidelines:##

![](https://github.com/vpaliyX/Expandable-Action-Button/blob/master/gifs/ezgif.com-video-to-gif%20(1).gif)
![](https://github.com/vpaliyX/Expandable-Action-Button/blob/master/gifs/ezgif.com-video-to-gif%20(2).gif)


In order to imitate motion of the button, when it goes down or up, I used a curved motion concept, you can check it out in this [video](https://www.youtube.com/watch?v=JVGg4zPRHNE).

## How to use the ExpandableButtonView? ##

First of all, import the expandable-button module into your project, then follow these steps:

1. Specify the view in your XML file:
    ```xml
     `<com.popularmovies.vpaliy.bottomtoolbar.ExpandableButtonView
        android:id="@+id/expandableButton"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        app:button_icon="@android:drawable/ic_menu_share"
        android:layout_alignParentEnd="true"
        android:layout_marginEnd="16dp"
        android:layout_marginRight="16dp"
        android:layout_marginBottom="16dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">
    </com.popularmovies.vpaliy.bottomtoolbar.ExpandableButtonView>
    ```
    You need to specify the margins of the button.
    Also note that `layout_width` and `layout_height` should be `wrap_content`.<br>
    You can specify the width and height of the button by using:
     `app:button_width` and `app:button_height`
    
2. Then go ahead and add some buttons, for example:
  ```java
  
     ButtomItem item=new ButtonItem.Builder(context)
                        .setImageResource(R.id.image)
                        .setClickListener(listener).build();
     expandableButtonView.addItem(item);
     
  ```
  Or 
 
  ```java
    
     expandableButtonView.addItem(new ButtonItem(context)
                                  .setImageResource(R.id.image)
                                  .setClickListener(listener));
  ```
  
  The items which have been added to the `ExpandableButtonView` will always be in the center.
  
3. So far I have written only a listener for a `RecyclerView` so it works only with that.
  
  ```java
     recyclerView.addOnScrollListener(new ScrollListener(expandableButtonView));
  ```
  However, there is a method `removeBottomToolbar` which folds the toolbar back, 
  so you use that every time you need to return to the button view.
  


## License ##

``````
MIT License

Copyright (c) 2017 Vasyl Paliy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
``````
