package wittz.boxer.library.child;

import org.parceler.Parcel;

import wittz.boxer.Box;
import wittz.boxer.library.PrimitiveModel;

/**
 * Created on 7/4/15.
 */
@Box
@Parcel
public class ChildClass extends PrimitiveModel {

    private static final String TEST = "TEST";

    String childString;

    public ChildClass() {}

    public String getChildString() {
        return childString;
    }

    public void setChildString(String childString) {
        this.childString = childString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ChildClass that = (ChildClass) o;

        return !(childString != null ? !childString.equals(that.childString) : that.childString != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (childString != null ? childString.hashCode() : 0);
        return result;
    }
}
