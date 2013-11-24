package fingertracker;

import java.util.ArrayList;

public abstract class Param<T> {
	protected T value;
	private final String name;
	protected final ArrayList<ParameterListener<T>> listeners = new ArrayList<ParameterListener<T>>();
	
	public Param(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addListener(ParameterListener<T> listener) {
		this.listeners.add(listener);
		listener.valueChanged(getValue());
	}
	
	public  T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
}

