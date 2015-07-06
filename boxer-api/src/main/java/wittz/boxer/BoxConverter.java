package wittz.boxer;

import android.os.Parcel;

/**
 * Created on 6/29/15.
 */
public interface BoxConverter<T> {

    Class<T> getBaseClass();

    T fromParcel(Parcel parcel);

    void writeToParcel(T data, Parcel parcel);
}
