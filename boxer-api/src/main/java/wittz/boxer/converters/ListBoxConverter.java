package wittz.boxer.converters;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

import wittz.boxer.BoxConverter;
import wittz.boxer.Boxer;
import wittz.boxer.BoxWrapperParcelable;

/**
 * Created on 7/3/15.
 */
public class ListBoxConverter implements BoxConverter<List> {
    @Override
    public Class<List> getBaseClass() {
        return List.class;
    }

    @Override
    public List fromParcel(Parcel parcel) {
        final int size = parcel.readInt();
        final ArrayList aList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            final BoxWrapperParcelable parcelWrapper = parcel.readParcelable(Boxer.class.getClassLoader());
            aList.add(Boxer.unwrap(parcelWrapper));
        }
        return aList;
    }

    @Override
    public void writeToParcel(List data, Parcel parcel) {
        parcel.writeInt(data.size());
        for (int i = 0; i < data.size(); i++) {
            parcel.writeParcelable(Boxer.wrap(data.get(i)), 0);
        }
    }
}
