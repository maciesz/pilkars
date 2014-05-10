package main.gala.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import main.gala.common.StaticContent;

/**
 * TextView rozszerzony o czcionkę purica.
 *
 * @author Maciej Andrearczyk <maciej.andrearczyk@coi.gov.pl>
 */
public class PuricaTextView extends TextView {

    public PuricaTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PuricaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PuricaTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                StaticContent.textFontLocation);
        setTypeface(tf);
    }

}