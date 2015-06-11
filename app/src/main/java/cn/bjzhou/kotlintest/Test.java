package cn.bjzhou.kotlintest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by bjzhou on 15-6-11.
 */
public class Test extends ListView implements View.OnClickListener {

    public Test(Context context) {
        this(context, null);
    }

    public Test(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Test(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        main();
    }

    public void main() {
        final String[] strs = {"aaa", "bbb"};
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                main();
                return true;
            }
        });
        float a = 1.0f;
        int b = (int) a;
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        for (int i=0;i<getAdapter().getCount();i++) {

        }
    }
}
