package wittz.boxer.converters;

import android.os.Parcel;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import wittz.boxer.BoxConverter;
import wittz.boxer.Boxer;

/**
 * Created on 7/3/15.
 */
public class MapBoxConverter implements BoxConverter<Map> {
    @Override
    public Class<Map> getBaseClass() {
        return Map.class;
    }

    @Override
    public Map fromParcel(Parcel parcel) {
        final int size = parcel.readInt();
        final Map map = new HashMap(size);
        for (int i = 0; i < size; i++) {
            final Object key = Boxer.unwrap(parcel.readParcelable(Boxer.class.getClassLoader()));
            final Object value = Boxer.unwrap(parcel.readParcelable(Boxer.class.getClassLoader()));
            map.put(key, value);
        }
        return map;
    }

    @Override
    public void writeToParcel(Map data, Parcel parcel) {
        final Set<Map.Entry> entries = data.entrySet();
        parcel.writeInt(entries.size());
        for (Map.Entry entry : entries) {
            parcel.writeParcelable(Boxer.wrap(entry.getKey()), 0);
            parcel.writeParcelable(Boxer.wrap(entry.getValue()), 0);
        }
    }
}
