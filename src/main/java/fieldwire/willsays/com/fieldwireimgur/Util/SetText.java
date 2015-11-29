package fieldwire.willsays.com.fieldwireimgur.Util;

import android.view.View;
import android.widget.TextView;

/**
 * Created by jingweiwang on 11/28/15.
 */
public class SetText {

    public static void setString(TextView textView, String text) {
        textView.setText("");
        textView.setVisibility(View.INVISIBLE);
        if (text != null && text.equalsIgnoreCase("null")) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

}
