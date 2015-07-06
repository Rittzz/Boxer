package wittz.boxer.library;

import org.parceler.Parcel;

import wittz.boxer.Box;

/**
 * Created on 7/4/15.
 */
public abstract class AbstractModel {
    PrimitiveModel primModel;

    public AbstractModel() {}

    @Box
    @Parcel
    public static class ConcreteModel extends AbstractModel {
        String string;

        public ConcreteModel() {}
    }
}
