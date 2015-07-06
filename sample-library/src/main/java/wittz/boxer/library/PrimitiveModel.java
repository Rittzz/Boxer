package wittz.boxer.library;

import org.parceler.Parcel;

import java.util.Arrays;

import wittz.boxer.Box;

/**
 * Created on 7/3/15.
 */
@Box
@Parcel
public class PrimitiveModel {
    char primChar;
    byte primByte;
    //short primShort;
    int primInteger;
    long primLong;
    float primfloat;
    double primDouble;
    boolean primBoolean;

    char[] primChars;
    byte[] primBytes;
    //short[] primShorts;
    int[] primIntegers;
    long[] primLongs;
    float[] primfloats;
    double[] primDoubles;
    boolean[] primBooleans;

    public PrimitiveModel() {
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

        PrimitiveModel that = (PrimitiveModel) o;

        if (primChar != that.primChar) {
            return false;
        }
        if (primByte != that.primByte) {
            return false;
        }
        //if (primShort != that.primShort) {
        //    return false;
        //}
        if (primInteger != that.primInteger) {
            return false;
        }
        if (primLong != that.primLong) {
            return false;
        }
        if (Float.compare(that.primfloat, primfloat) != 0) {
            return false;
        }
        if (Double.compare(that.primDouble, primDouble) != 0) {
            return false;
        }
        if (primBoolean != that.primBoolean) {
            return false;
        }
        if (!Arrays.equals(primChars, that.primChars)) {
            return false;
        }
        if (!Arrays.equals(primBytes, that.primBytes)) {
            return false;
        }
        //if (!Arrays.equals(primShorts, that.primShorts)) {
        //    return false;
        //}
        if (!Arrays.equals(primIntegers, that.primIntegers)) {
            return false;
        }
        if (!Arrays.equals(primLongs, that.primLongs)) {
            return false;
        }
        if (!Arrays.equals(primfloats, that.primfloats)) {
            return false;
        }
        if (!Arrays.equals(primDoubles, that.primDoubles)) {
            return false;
        }
        return Arrays.equals(primBooleans, that.primBooleans);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) primChar;
        result = 31 * result + (int) primByte;
        //result = 31 * result + (int) primShort;
        result = 31 * result + primInteger;
        result = 31 * result + (int) (primLong ^ (primLong >>> 32));
        result = 31 * result + (primfloat != +0.0f ? Float.floatToIntBits(primfloat) : 0);
        temp = Double.doubleToLongBits(primDouble);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (primBoolean ? 1 : 0);
        result = 31 * result + (primChars != null ? Arrays.hashCode(primChars) : 0);
        result = 31 * result + (primBytes != null ? Arrays.hashCode(primBytes) : 0);
        //result = 31 * result + (primShorts != null ? Arrays.hashCode(primShorts) : 0);
        result = 31 * result + (primIntegers != null ? Arrays.hashCode(primIntegers) : 0);
        result = 31 * result + (primLongs != null ? Arrays.hashCode(primLongs) : 0);
        result = 31 * result + (primfloats != null ? Arrays.hashCode(primfloats) : 0);
        result = 31 * result + (primDoubles != null ? Arrays.hashCode(primDoubles) : 0);
        result = 31 * result + (primBooleans != null ? Arrays.hashCode(primBooleans) : 0);
        return result;
    }
}
