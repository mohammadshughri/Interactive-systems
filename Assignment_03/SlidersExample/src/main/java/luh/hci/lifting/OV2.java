package luh.hci.lifting;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author michaelrohs
 */
public class OV2<A, B, R> extends EventStream<R> {

    private final ObservableValue<A> ovs;
    private final ObservableValue<B> ovt;
    private final Function2<A, B, R> f;

    public OV2(ObservableValue<A> ovs, ObservableValue<B> ovt, Function2<A, B, R> f) {
        this.ovs = ovs;
        this.ovt = ovt;
        this.f = f;
        ovs.addListener(new ChangeListener<A>() {
            @Override
            public void changed(ObservableValue<? extends A> observable, A sOld, A sNew) {
                B t = ovt.getValue();
                R rOld = f.apply(sOld, t);
                R rNew = f.apply(sNew, t);
                for (ChangeListener l : changeListeners) {
                    l.changed(OV2.this, rOld, rNew);
                }
            }
        });
        ovt.addListener(new ChangeListener<B>() {
            @Override
            public void changed(ObservableValue<? extends B> observable, B tOld, B tNew) {
                A s = ovs.getValue();
                R rOld = f.apply(s, tOld);
                R rNew = f.apply(s, tNew);
                for (ChangeListener l : changeListeners) {
                    l.changed(OV2.this, rOld, rNew);
                }
            }
        });
        ovs.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                for (InvalidationListener l : invalidationListeners) {
                    l.invalidated(OV2.this);
                }
            }
        });
        ovt.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                for (InvalidationListener l : invalidationListeners) {
                    l.invalidated(OV2.this);
                }
            }
        });
    }

    @Override
    public R getValue() {
        A s = ovs.getValue();
        B b = ovt.getValue();
        R r = f.apply(s, b);
        return r;
    }

}
