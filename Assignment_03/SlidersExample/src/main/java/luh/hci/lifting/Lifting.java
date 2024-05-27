package luh.hci.lifting;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

/**
 *
 * @author michaelrohs
 */
public class Lifting {

    public static <A, R> EventStream<R> map(
            ObservableValue<A> ova,
            Function1<A, R> f) {
        OV1<A, R> ov1 = new OV1(ova, f);
        return ov1;
    }

    public static <A, B, R> EventStream<R> map(
            ObservableValue<A> ova,
            ObservableValue<B> ovb,
            Function2<A, B, R> f) {
        OV2<A, B, R> ov2 = new OV2(ova, ovb, f);
        return ov2;
    }

    public static <A, B, C, R> EventStream<R> map(
            ObservableValue<A> ova,
            ObservableValue<B> ovb,
            ObservableValue<C> ovc,
            Function3<A, B, C, R> f) {
        OV3<A, B, C, R> ov3 = new OV3(ova, ovb, ovc, f);
        return ov3;
    }

    public static Function1<String, Double> s2d = (String s) -> {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return Double.NaN;
        }
    };

    public static Function1<Double, String> d2s = (Double d) -> {
        return String.valueOf(d);
    };

    public static Function1<Double, String> d2s(String format) {
        return (Double d) -> String.format(format, d);
    }

    public static Function1<String, Integer> s2i = (String s) -> {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return 0;
        }
    };

    public static Function1<Integer, String> i2s = (Integer i) -> {
        return String.valueOf(i);
    };

    public static Function1<String, String> s2s = (String s) -> {
        return s;
    };

    public static Function1<Double, Double> d2d = (Double d) -> {
        return d;
    };

    public static Function1<Integer, Integer> i2i = (Integer i) -> {
        return i;
    };

    public static <A, B, R, C, D, S> EventStream<R> map(
            ObservableValue<A> ova,
            ObservableValue<B> ovb,
            Function1<A, C> conva,
            Function1<B, D> convb,
            Function1<S, R> convs,
            Function2<C, D, S> f) {
        Function2<A, B, R> wrapper = (A a, B b) -> {
            C c = conva.apply(a);
            D d = convb.apply(b);
            S s = f.apply(c, d);
            R r = convs.apply(s);
            return r;
        };
        OV2<A, B, R> ov2 = new OV2(ova, ovb, wrapper);
        return ov2;
    }

    public static EventStream mapSDS(
            ObservableValue<String> ova,
            ObservableValue<String> ovb,
            Function2<Double, Double, Double> f) {
        Function2<String, String, String> converter = (String a, String b) -> {
            try {
                double x = Double.parseDouble(a);
                double y = Double.parseDouble(b);
                double z = f.apply(x, y);
                return String.valueOf(z);
            } catch (NumberFormatException ex) {
                return "";
            }
        };
        OV2<String, String, String> ov2 = new OV2(ova, ovb, converter);
        return ov2;
    }

    public static <A, R> ChangeListener<A> link(
            ObservableValue<A> source,
            Function1<A, R> f,
            WritableValue<R> target) {
//		OV1<A, R> ov1 = new OV1(source, f);

//		target.bind(ov1); // only one value may be bound to target
//		ov1.addListener(new ChangeListener<R>() {
//			@Override
//			public void changed(ObservableValue<? extends R> observable, R oldValue, R newValue) {
//				target.setValue(newValue);
//			}
//		});
//		ova.addListener(new InvalidationListener() {
//			@Override
//			public void invalidated(Observable observable) {
//				target. ...
//			}
//		});
        ChangeListener<A> listener = new ChangeListener<A>() {
            @Override
            public void changed(ObservableValue<? extends A> observable, A oldValue, A newValue) {
                target.setValue(f.apply(newValue));
            }
        };
        source.addListener(listener);
        return listener;
    }

    public static <A> ChangeListener<A> link(
            ObservableValue<A> source,
            WritableValue<A> target) {
        ChangeListener<A> listener = new ChangeListener<A>() {
            @Override
            public void changed(ObservableValue<? extends A> observable, A oldValue, A newValue) {
                target.setValue(newValue);
            }
        };
        source.addListener(listener);
        return listener;
    }

    public static <A, B, S, R> ChangeListener<A> link(
            ObservableValue<A> source,
            Function1<A, B> convAB,
            Function1<B, S> f,
            Function1<S, R> convSR,
            WritableValue<R> target) {
        ChangeListener<A> listener = new ChangeListener<A>() {
            @Override
            public void changed(ObservableValue<? extends A> observable, A oldValue, A newValue) {
                // System.out.println("s: " + source + ", t: " + target + ", o: " + observable);
                B b = convAB.apply(newValue);
                S s = f.apply(b);
                R r = convSR.apply(s);
                target.setValue(r);
            }
        };
        source.addListener(listener);
        return listener;
    }

    public static <A, B, C, D, S, R> void /*ChangeListener<A>*/ link(
                    ObservableValue<A> sourceA,
                    ObservableValue<B> sourceB,
                    Function1<A, C> convAC,
                    Function1<B, D> convBD,
                    Function2<C, D, S> f,
                    Function1<S, R> convSR,
                    WritableValue<R> target) {
                ChangeListener<A> listenerA = new ChangeListener<A>() {
                    @Override
                    public void changed(ObservableValue<? extends A> observable, A oldValue, A newValue) {
                        C c = convAC.apply(newValue);
                        D d = convBD.apply(sourceB.getValue());
                        S s = f.apply(c, d);
                        R r = convSR.apply(s);
                        target.setValue(r);
                    }
                };
                sourceA.addListener(listenerA);
                ChangeListener<B> listenerB = new ChangeListener<B>() {
                    @Override
                    public void changed(ObservableValue<? extends B> observable, B oldValue, B newValue) {
                        C c = convAC.apply(sourceA.getValue());
                        D d = convBD.apply(newValue);
                        S s = f.apply(c, d);
                        R r = convSR.apply(s);
                        target.setValue(r);
                    }
                };
                sourceB.addListener(listenerB);
//		return listener;
            }

            public static <A> void mapVoid(
                    ObservableValue<A> ova,
                    Procedure1<A> f) {
                ova.addListener(new ChangeListener<A>() {
                    @Override
                    public void changed(ObservableValue<? extends A> observable, A sOld, A sNew) {
                        f.apply(sNew);
                    }
                });
            }

            public static <Obs extends Observable, R> /*EventStream<R>*/ void linkObservable(
                            Obs source,
                            Function1<Obs, R> f,
                            WritableValue<R> target) {
                        source.addListener(new InvalidationListener() {
                            @Override
                            public void invalidated(Observable observable) {
                                R value = f.apply((Obs) observable);
                                target.setValue(value);
                            }
                        });
//		OV1<Void, R> ov1 = new OV1(o, f);
//		return ov1;
                    }

                    public static <A> void log(String tag, ObservableValue<A> ovs) {
                        Lifting.map(ovs, x -> {
                            System.out.println(tag + x);
                            return null;
                        });
                    }
                    /*
                     public static <A> ObservableValue<A> filter(
                     ObservableValue<A> ova, 
                     Function1<A, Boolean> predicate) 
                     {
                     return null;
                     }
                     */

                    public static <A, B> BidirectionalBinding<A, B> bindBidirectional(
                            Property<A> a,
                            Property<B> b,
                            Function1<A, B> aToB,
                            Function1<B, A> bToA) {
                        BidirectionalBinding<A, B> converter = new BidirectionalBinding<>(
                                a, b, aToB, bToA);
                        return converter;
                    }

}
