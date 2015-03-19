package mx.ambmultimedia.brillamexico.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ExtendableGridView extends GridView {
    public ExtendableGridView (Context context) {
        super(context);
    }

    public ExtendableGridView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendableGridView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        else {
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
