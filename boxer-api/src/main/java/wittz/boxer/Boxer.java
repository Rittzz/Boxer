package wittz.boxer;

import android.os.Parcelable;
import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import wittz.boxer.converters.DefaultBoxConverter;
import wittz.boxer.converters.ListBoxConverter;
import wittz.boxer.converters.MapBoxConverter;

/**
 * Created on 6/30/15.
 */
public class Boxer {

    private static final String TAG = "Boxer";
    private static final boolean LOG_CACHE_MISSES = true;

    public static final String POSTFIX = "$$Boxer";

    private static final Map<Class<?>, wittz.boxer.BoxConverter<?>> converters = new ConcurrentHashMap<>();

    private static void addConverter(final BoxConverter converter) {
        converters.put(converter.getBaseClass(), converter);
    }

    private static final DefaultBoxConverter defaultConverter = new DefaultBoxConverter();

    public static final Class<?>[] SUPPORTED_CLASSES = new Class<?>[] {
            // Primitives
            byte.class, char.class, short.class, int.class, long.class, float.class, double.class, boolean.class,
            Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class,
            byte[].class, char[].class, short[].class, int[].class, long[].class, float[].class, double[].class, boolean[].class,
            // Special
            String.class, String[].class, CharSequence.class, CharSequence[].class
    };

    static {
        for (Class clazz : SUPPORTED_CLASSES) {
            converters.put(clazz, defaultConverter);
        }

        addConverter(new ListBoxConverter());
        addConverter(new MapBoxConverter());

        Object[] bolb = new Integer[2];
    }

    private Boxer() {}

    public static <T> wittz.boxer.BoxConverter<T> getConverterForClass(Class<T> clazz) {
        if (converters.get(clazz) == null) {
            try {
                final Class<?> converterClass = Class.forName(clazz.getName() + POSTFIX);
                final wittz.boxer.BoxConverter converter = (wittz.boxer.BoxConverter) converterClass.newInstance();
                converters.put(clazz, converter);
            }
            catch (Exception ex) {
                // Special cases
                if (Parcelable.class.isAssignableFrom(clazz)) {
                    converters.put(clazz, defaultConverter);
                }
                else if (List.class.isAssignableFrom(clazz)) {
                    converters.put(clazz, new ListBoxConverter());
                }
                else if (Map.class.isAssignableFrom(clazz)) {
                    converters.put(clazz, new MapBoxConverter());
                }
                else {
                    throw new RuntimeException("Couldn't find a converter for " + clazz.getCanonicalName(), ex);
                }
            }

            if (LOG_CACHE_MISSES) {
                Log.i(TAG, "Cache miss for " + clazz.getCanonicalName());
            }
        }

        //noinspection unchecked
        return (wittz.boxer.BoxConverter<T>) converters.get(clazz);
    }

    public static void registerConverter(final wittz.boxer.BoxConverter<?> converter) {
        converters.put(converter.getBaseClass(), converter);
    }

    public static <T> BoxWrapperParcelable wrap(final T object) {
        //noinspection
        return new BoxWrapperParcelable(object);
    }

    public static <T> T unwrap(final Parcelable parcelable) {
        //noinspection
        final BoxWrapperParcelable<T> parcelWrapper = (BoxWrapperParcelable<T>)parcelable;
        return parcelWrapper.getModel();
    }
}
