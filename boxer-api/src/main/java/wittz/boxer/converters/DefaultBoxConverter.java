package wittz.boxer.converters;

import android.os.Parcel;

import wittz.boxer.BoxConverter;

/**
 * The fallback converter that just uses {@link Parcel#writeValue(Object)} and {@link Parcel#readValue(ClassLoader)}
 * using {@link DefaultBoxConverter#getClass()} as the converter.
 */
public class DefaultBoxConverter implements BoxConverter {

    @Override
    public Class<?> getBaseClass() {
        // Special converter, no need for a class since write/read value cover many data types
        return null;
    }

    @Override
    public Object fromParcel(Parcel parcel) {
        return parcel.readValue(DefaultBoxConverter.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Object data, Parcel parcel) {
        parcel.writeValue(data);
    }
}
