package com.mio;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import net.kdt.pojavlaunch.R;

public class MioSelectorButton extends androidx.appcompat.widget.AppCompatButton {

    public MioSelectorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MioKeySelector);
        setKey(array.getString(R.styleable.MioKeySelector_key));
        array.recycle();
    }

    private String key;

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

  
}
