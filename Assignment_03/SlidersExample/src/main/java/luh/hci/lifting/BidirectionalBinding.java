package luh.hci.lifting;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
bindBidirectional(
	celsius.textProperty(), 
	fahrenheit.textProperty(), 
	c -> isNumeric(c) ? cToF(c) : fahrenheit.getText(), 
	f -> isNumeric(f) ? fToC(f) : celsius.getText());
 *
 * @author michaelrohs
 */
public class BidirectionalBinding<A, B> {
	
	private final Property<A> propertyA;
	private final Property<B> propertyB;
	private final Function1<A, B> aToB;
	private final Function1<B, A> bToA;
	private final ChangeListener<A> changeListenerA;
	private final ChangeListener<B> changeListenerB;
	
	public BidirectionalBinding(
			Property<A> a, Property<B> b, 
			Function1<A, B> aToB, 
			Function1<B, A> bToA) 
	{
		System.out.println("Converter: constructor");
		this.propertyA = a;
		this.propertyB = b;
		this.aToB = aToB;
		this.bToA = bToA;
		
		// a.bindBidirectional(this);
		a.setValue(bToA.apply(b.getValue()));

		changeListenerA = new ChangeListener<A>() {
			@Override
			public void changed(ObservableValue<? extends A> observable, A oldValue, A newValue) {
				System.out.println("Converter: constructor: changed<A>");
				// B oldValueB = aToB.apply(oldValue);
				B newValueB = aToB.apply(newValue);
				b.setValue(newValueB);
			}
		};
		a.addListener(changeListenerA);
		
		changeListenerB = new ChangeListener<B>() {
			@Override
			public void changed(ObservableValue<? extends B> observable, B oldValue, B newValue) {
				System.out.println("Converter: constructor: changed<B>");
				// A oldValueA = bToA.apply(oldValue);
				A newValueA = bToA.apply(newValue);
				a.setValue(newValueA);
			}
		};
		b.addListener(changeListenerB);
		
	}
	
	public void cancel() {
		propertyA.removeListener(changeListenerA);
		propertyB.removeListener(changeListenerB);
	}
	
}
