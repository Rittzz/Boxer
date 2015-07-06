package wittz.boxer;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created on 7/3/15.
 */
public class BoxWrapperParcelable<T> implements Parcelable {

    private static final String LOG_TAG = "BoxWrapperParcelable";
    private static final boolean LOG_TIMING = false;

    private static final String NULL = "_NULL_";

    private final T model;

    public BoxWrapperParcelable(T model) {
        this.model = model;
    }

    private BoxWrapperParcelable(Parcel in) {
        try {
            long now = SystemClock.uptimeMillis();

            final String className = in.readString();

            if (!NULL.equals(className)) {
                final Class<?> clazz = Class.forName(className);

                //noinspection unchecked
                final wittz.boxer.BoxConverter<T> converter = (wittz.boxer.BoxConverter<T>) Boxer.getConverterForClass(clazz);
                model = converter.fromParcel(in);
            }
            else {
                model = null;
            }

            if (LOG_TIMING) {
                Log.d(LOG_TAG, "Took " + (SystemClock.uptimeMillis() - now) + "ms to unparcel " + className);
            }
        }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException("You may only pass parcels that have created by 'Boxer.wrap'", ex);
        }
    }

    public T getModel() {
        return model;
    }

    public static final Creator<BoxWrapperParcelable> CREATOR = new Creator<BoxWrapperParcelable>() {
        @Override
        public BoxWrapperParcelable createFromParcel(Parcel in) {
            return new BoxWrapperParcelable(in);
        }

        @Override
        public BoxWrapperParcelable[] newArray(int size) {
            return new BoxWrapperParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        long now = SystemClock.uptimeMillis();

        final String className = model != null ? model.getClass().getName() : NULL;
        dest.writeString(className);

        if (model != null) {
            final BoxConverter<T> converter = (BoxConverter<T>) Boxer.getConverterForClass(model.getClass());
            converter.writeToParcel(model, dest);
        }

        if (LOG_TIMING) {
            Log.d(LOG_TAG, "Took " + (SystemClock.uptimeMillis() - now) + "ms to parcel " + className);
        }
    }
}
