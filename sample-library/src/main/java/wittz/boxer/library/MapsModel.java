package wittz.boxer.library;

import android.os.Bundle;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

import wittz.boxer.Box;

/**
 * Created on 7/3/15.
 */
@Box
@Parcel
public class MapsModel {
    Map<Boolean, String> booleans = new HashMap<>();
    Map<String, String> strings = new HashMap<>();
    Map<String, Bundle> bundles = new HashMap<>();

    public MapsModel() {
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

        MapsModel that = (MapsModel) o;

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
