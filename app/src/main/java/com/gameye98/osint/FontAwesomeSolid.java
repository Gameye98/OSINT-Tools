package com.gameye98.osint;

import android.content.*;
import android.widget.*;
import android.util.*;
import android.graphics.*;

public class FontAwesomeSolid extends Button {


    public FontAwesomeSolid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FontAwesomeSolid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesomeSolid(Context context) {
        super(context);
        init();
    }

    private void init() {
		//Font name should not contain "/".
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/fa-solid-900.ttf");
        setTypeface(tf);
    }

}
