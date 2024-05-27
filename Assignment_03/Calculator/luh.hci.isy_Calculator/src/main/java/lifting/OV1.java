package lifting;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author michaelrohs
 */
public class OV1<A, R> extends EventStream<R> {

    private final ObservableValue<A> ovs;
    private final Function1<A, R> f;

    public OV1(ObservableValue<A> ovs, Function1<A, R> f) {
        this.ovs = ovs;
        this.f = f;
        ovs.addListener(new ChangeListener<A>() {
            @Override
            public void changed(ObservableValue<? extends A> observable, A sOld, A sNew) {
                R rOld = f.apply(sOld);
                R rNew = f.apply(sNew);
                for (ChangeListener l : changeListeners) {
                    l.changed(OV1.this, rOld, rNew);
                }
            }
        });
        ovs.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                for (InvalidationListener l : invalidationListeners) {
                    l.invalidated(OV1.this);
                }
            }
        });
    }

    @Override
    public R getValue() {
        A s = ovs.getValue();
        R r = f.apply(s);
        return r;
    }
}
