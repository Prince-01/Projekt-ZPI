package pl.finanse.zpi.pwr.wallet;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by sebastiankotarski on 20.05.16.
 */
public final class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("APPLICATION RUNNING");
        FontsOverride.setDefaultFont(this, "DEFAULT", "Montserrat-Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Montserrat-Bold.ttf");
        FontsOverride.setDefaultFont(this, "MONTSERRAT", "Montserrat-Bold.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "Montserrat-Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Montserrat-Regular.ttf");
    }

    static final class FontsOverride {

        public static void setDefaultFont(Context context,
                                          String staticTypefaceFieldName, String fontAssetName) {
            final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                    fontAssetName);
            replaceFont(staticTypefaceFieldName, regular);
        }

        protected static void replaceFont(String staticTypefaceFieldName,
                                          final Typeface newTypeface) {
            try {
                final Field staticField = Typeface.class
                        .getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
