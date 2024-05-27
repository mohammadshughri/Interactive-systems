package lifting;

import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author michaelrohs
 */
public abstract class EventStream<R> implements ObservableValue<R> {

	protected final ArrayList<ChangeListener<? super R>> changeListeners;
	protected final ArrayList<InvalidationListener> invalidationListeners;

	public EventStream() {
		System.out.println("EventStream: constructor");
		this.changeListeners = new ArrayList();
		this.invalidationListeners = new ArrayList();
	}

	@Override
	public void addListener(ChangeListener<? super R> listener) {
		System.out.println("EventStream: adding ChangeListener: " + listener);
		changeListeners.add(listener);
	}

	@Override
	public void removeListener(ChangeListener<? super R> listener) {
		System.out.println("EventStream: removing ChangeListener: " + listener);
		changeListeners.remove(listener);
	}

	@Override
	public void addListener(InvalidationListener listener) {
		System.out.println("EventStream: adding InvalidationListener: " + listener);
		invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		System.out.println("EventStream: removing InvalidationListener: " + listener);
		invalidationListeners.remove(listener);
	}

	public <A, R> EventStream<R> map(Function1<A, R> f) {
		System.out.println("EventStream: map " + f);
		OV1<A, R> ov1 = new OV1(this, f);
		return ov1;
	}

}
