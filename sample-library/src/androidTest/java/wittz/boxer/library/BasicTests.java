package wittz.boxer.library;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.parceler.Parcels;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import wittz.boxer.BoxWrapperParcelable;
import wittz.boxer.Boxer;
import wittz.boxer.library.child.ChildClass;

/**
 * Created on 7/3/15.
 */
public class BasicTests extends TestCase {

    private static final String TAG = "@BasicTests@";
    private static final boolean LOG_TIMING = true;
    private static final boolean STRESS_TEST = true;

    private static <T> T process(T obj) {
        final int repeatCount = 100;
        long boxerAverage = 0;
        long parcelerAverage = 0;

        T data = null;

        for (int i = 0; i < (STRESS_TEST ? repeatCount : 1); i++) {
            long timestamp = System.currentTimeMillis();

            final Parcel parcel = Parcel.obtain();

            final Parcelable parcelable = Boxer.wrap(obj);
            parcel.writeParcelable(parcelable, 0);
            parcel.setDataPosition(0);
            final Parcelable newParcelable = parcel.readParcelable(BoxWrapperParcelable.class.getClassLoader());
            data = Boxer.unwrap(newParcelable);

            parcel.recycle();

            boxerAverage += (System.currentTimeMillis() - timestamp);
        }

        for (int i = 0; i < (STRESS_TEST ? repeatCount : 1); i++) {
            long timestamp = System.currentTimeMillis();

            final Parcel parcel = Parcel.obtain();

            final Parcelable parcelable = Parcels.wrap(obj);
            parcel.writeParcelable(parcelable, 0);
            parcel.setDataPosition(0);
            final Parcelable newParcelable = parcel.readParcelable(BoxWrapperParcelable.class.getClassLoader());
            data = Parcels.unwrap(newParcelable);

            parcel.recycle();

            parcelerAverage += (System.currentTimeMillis() - timestamp);
        }

        if (LOG_TIMING) {
            final String className = obj != null ? obj.getClass().getCanonicalName() : "NULL";
            Log.i(TAG, "Boxing " + className + " took " + (boxerAverage/(double)repeatCount));
            Log.i(TAG, "Parceler " + className + " took " + (parcelerAverage/(double)repeatCount));
        }

        return data;
    }

    private void assertParcelEquality(final Object source) {
        final Object dest = process(source);
        Assert.assertTrue(source.equals(dest));
    }

    private void assertEquals(final List expected, final List actual) {
        if (expected == actual) {
            return;
        }

        if (expected != null && actual == null) {
            assertGeneralFailure(expected, actual);
        }

        if (actual != null && expected == null) {
            assertGeneralFailure(expected, actual);
        }

        if (expected.size() != actual.size()) {
            assertGeneralFailure(expected, actual);
        }

        for (int i = 0; i < expected.size(); i++) {
            if (!Objects.equals(expected.get(i), actual.get(i))) {
                assertGeneralFailure(expected, actual);
            }
        }
    }

    private void assertEquals(final Map expected, final Map actual) {
        if (expected == actual) {
            return;
        }

        if (expected != null && actual == null) {
            assertGeneralFailure(expected, actual);
        }

        if (actual != null && expected == null) {
            assertGeneralFailure(expected, actual);
        }

        if (expected.size() != actual.size()) {
            assertGeneralFailure(expected, actual);
        }

        Assert.assertTrue("expected:" + expected +" but was:" + actual, expected.equals(actual));
    }

    private void assertGeneralFailure(final Object expected, final Object actual) {
        Assert.fail("expected:" + expected +" but was:" + actual);
    }

    public void testNulls() {
        Assert.assertNull(process(null));
    }

    private PrimitiveModel createPrimitiveModel() {
        final PrimitiveModel model = new PrimitiveModel();
        model.primChar = 'b';
        model.primByte = 1;
        //model.primShort = 2;
        model.primInteger = 3;
        model.primLong = 4;
        model.primfloat = 1.2f;
        model.primDouble = 32.4d;
        model.primBoolean = true;

        model.primChars = new char[] {'a', 'b', 'c'};
        model.primBytes = new byte[] {1, 2, 3};
        //model.primShorts = new short[] {1, 2, 3};
        model.primIntegers = new int[] {1, 2, 3};
        model.primLongs = new long[] {1, 2, 3};
        model.primfloats = new float[] {1.1f, 2.2f, 3.3f};
        model.primDoubles = new double[] {1.1, 2.2, 3.3};
        model.primBooleans = new boolean[] {true, false, true};

        return model;
    }

    public void testBasicPrimitives() {
        final PrimitiveModel source = createPrimitiveModel();
        assertParcelEquality(source);
    }

    private ChildClass createChild() {
        final ChildClass model = new ChildClass();
        model.setChildString("Hello");
        return model;
    }

    public void testChild() {
        final ChildClass source = createChild();
        assertParcelEquality(source);
    }

    private Bundle createBundle() {
        final Bundle bundle = new Bundle();
        bundle.putString("test", "test");
        return bundle;
    }

    private ListsModel createListsModel() {
        final int ARRAY_SIZE = 100;

        final ListsModel model = new ListsModel();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            model.booleans.add(i % 2 == 0);
            model.strings.add(Integer.toString(i));
            model.bundles.add(createBundle());
        }
        return model;
    }

    public void testListsModel() {
        final ListsModel source = createListsModel();
        final ListsModel dest = process(source);

        assertEquals(dest.booleans, source.booleans);
        assertEquals(dest.strings, source.strings);
        Assert.assertNotNull(dest.bundles);
    }

    private MapsModel createMapsModel() {
        final MapsModel model = new MapsModel();
        model.booleans.put(true, "test");
        model.booleans.put(false, "test");
        model.strings.put("test", "test");
        model.strings.put("test2", "test2");
        model.bundles.put("test", createBundle());
        return model;
    }

    public void testMapsModel() {
        final MapsModel source = createMapsModel();
        final MapsModel dest = process(source);

        assertEquals(dest.booleans, source.booleans);
        assertEquals(dest.strings, source.strings);
        Assert.assertNotNull(dest.bundles);
    }

    private Nested createNested() {
        final Nested model = new Nested();
        model.lists = createListsModel();
        model.maps = createMapsModel();
        model.prim = createPrimitiveModel();
        return model;
    }

    public void testNested() {
        final Nested source = createNested();
        final Nested dest = process(source);

        assertEquals(source.prim, dest.prim);

        assertEquals(dest.lists.booleans, source.lists.booleans);
        assertEquals(dest.lists.strings, source.lists.strings);
        Assert.assertNotNull(dest.lists.bundles);

        assertEquals(dest.maps.booleans, source.maps.booleans);
        assertEquals(dest.maps.strings, source.maps.strings);
        Assert.assertNotNull(dest.maps.bundles);
    }

    public void testAbstract() {
        final AbstractModel.ConcreteModel cm = new AbstractModel.ConcreteModel();
        cm.primModel = createPrimitiveModel();
        cm.string = "test";
        process(cm);
    }
}
