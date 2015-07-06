package wittz.boxer.library;

import org.parceler.Parcel;

import java.util.Arrays;

import wittz.boxer.Box;

/**
 * Created on 7/4/15.
 */
@Box
@Parcel
public class ArrayModel {

    String[] strings;
    PrimitiveModel[] primitiveModels;

    public ArrayModel() {
        //
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArrayModel that = (ArrayModel) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(strings, that.strings)) {
            return false;
        }
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(primitiveModels, that.primitiveModels);

    }

    @Override
    public int hashCode() {
        int result = strings != null ? Arrays.hashCode(strings) : 0;
        result = 31 * result + (primitiveModels != null ? Arrays.hashCode(primitiveModels) : 0);
        return result;
    }
}
