package CustomControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class TextViewGothamMedium extends TextView {
    public TextViewGothamMedium(Context context) {
        super(context);
        setTypeface(context);
    }

    public TextViewGothamMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public TextViewGothamMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }
    private void setTypeface(Context context) {
        //setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/gotham-medium_1.ttf"));

    }
}
