package wittz.boxer.library;

import org.parceler.Parcel;

import wittz.boxer.Box;

/**
 * Created on 7/4/15.
 */
@Box
@Parcel
public class Nested {
    ListsModel lists;
    MapsModel maps;
    PrimitiveModel prim;

    public Nested() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Nested nested = (Nested) o;

        if (lists != null ? !lists.equals(nested.lists) : nested.lists != null) {
            return false;
        }
        if (maps != null ? !maps.equals(nested.maps) : nested.maps != null) {
            return false;
        }
        return !(prim != null ? !prim.equals(nested.prim) : nested.prim != null);

    }

    @Override
    public int hashCode() {
        int result = lists != null ? lists.hashCode() : 0;
        result = 31 * result + (maps != null ? maps.hashCode() : 0);
        result = 31 * result + (prim != null ? prim.hashCode() : 0);
        return result;
    }
}
