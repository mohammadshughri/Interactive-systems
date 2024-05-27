package luh.hci.lifting;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author michaelrohs
 */
public class OV3<A, B, C, R> extends EventStream<R> {

    private final ObservableValue<A> ova;
    private final ObservableValue<B> ovb;
    private final ObservableValue<C> ovc;
    private final Function3<A, B, C, R> f;

    public OV3(ObservableValue<A> ova, ObservableValue<B> ovb, ObservableValue<C> ovc, Function3<A, B, C, R> f) {
        this.ova = ova;
        this.ovb = ovb;
        this.ovc = ovc;
        this.f = f;
        ova.addListener(new ChangeListener<A>() {
            @Override
            public void changed(ObservableValue<? extends A> observable, A aOld, A aNew) {
                B b = ovb.getValue();
                C c = ovc.getValue();
                R rOld = f.apply(aOld, b, c);
                R rNew = f.apply(aNew, b, c);
                for (ChangeListener l : changeListeners) {
                    l.changed(OV3.this, rOld, rNew);
                }
            }
        });
        ovb.addListener(new ChangeListener<B>() {
            @Override
            public void changed(ObservableValue<? extends B> observable, B bOld, B bNew) {
                A a = ova.getValue();
                C c = ovc.getValue();
                R rOld = f.apply(a, bOld, c);
                R rNew = f.apply(a, bNew, c);
                for (ChangeListener l : changeListeners) {
                    l.changed(OV3.this, rOld, rNew);
                }
            }
        });
        ovc.addListener(new ChangeListener<C>() {
            @Override
            public void changed(ObservableValue<? extends C> observable, C cOld, C cNew) {
                A a = ova.getValue();
                B b = ovb.getValue();
                R rOld = f.apply(a, b, cOld);
                R rNew = f.apply(a, b, cNew);
                for (ChangeListener l : changeListeners) {
                    l.changed(OV3.this, rOld, rNew);
                }
            }
        });
        ova.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                for (InvalidationListener l : invalidationListeners) {
                    l.invalidated(OV3.this);
                }
            }
        });
        ovb.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                for (InvalidationListener l : invalidationListeners) {
                    l.invalidated(OV3.this);
                }
            }
        });
        ovc.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                for (InvalidationListener l : invalidationListeners) {
                    l.invalidated(OV3.this);
                }
            }
        });
    }

    @Override
    public R getValue() {
        A a = ova.getValue();
        B b = ovb.getValue();
        C c = ovc.getValue();
        R r = f.apply(a, b, c);
        return r;
    }

}
