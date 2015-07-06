package wittz.boxer.converters;

import android.os.Parcel;

import wittz.boxer.BoxConverter;

/**
 * Created on 7/4/15.
 */
public abstract class GeneratedBoxConverter<T> implements BoxConverter<T>{
    @Override
    public abstract Class<T> getBaseClass();

    public abstract T newModel();

    public abstract void populateFromParcel(T data, Parcel parcel);

    @Override
    public final T fromParcel(Parcel parcel) {
        T model = newModel();
        populateFromParcel(model, parcel);
        return model;
    }

    @Override
    public abstract void writeToParcel(T data, Parcel parcel);
}
