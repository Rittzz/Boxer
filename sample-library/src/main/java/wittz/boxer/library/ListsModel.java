package wittz.boxer.library;

import android.os.Bundle;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import wittz.boxer.Box;

/**
 * Created on 7/3/15.
 */
@Box
@Parcel
public class ListsModel {
    List<Boolean> booleans = new ArrayList<>();
    List<String> strings = new ArrayList<>();
    List<Bundle> bundles = new ArrayList<>();
    List<PrimitiveModel> primModels = new ArrayList<>();

    public ListsModel() {
        // Empty
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ListsModel that = (ListsModel) o;

        if (booleans != null ? !booleans.equals(that.booleans) : that.booleans != null) {
            return false;
        }
        if (strings != null ? !strings.equals(that.strings) : that.strings != null) {
            return false;
        }
        return !(bundles != null ? !bundles.equals(that.bundles) : that.bundles != null);

    }

    @Override
    public int hashCode() {
        int result = booleans != null ? booleans.hashCode() : 0;
        result = 31 * result + (strings != null ? strings.hashCode() : 0);
        result = 31 * result + (bundles != null ? bundles.hashCode() : 0);
        return result;
    }
}
