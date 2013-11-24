package fingertracker;

public class NumericParameter extends Param<Double> {
	private final double start;
	private final double end;
	private final double inc;
	
	public NumericParameter(String name, double start, double end, double inc, double value) {
		super(name);
		this.start = start;
		this.end = end;
		this.inc = inc;
		this.value = start;
		setValue(value);
	}

	public double getStart() {
		return start;
	}
	
	public double getEnd() {
		return end;
	}
	
	public double getInc() {
		return inc;
	}
	
	public double getRange() {
		return Math.max(end - start, 0);
	}

	@Override
	public void setValue(Double value) {
		if (value < end && value >= start) {
			value = Math.floor((value - start) / inc) * inc + start;
			this.value = value;
			for (ParameterListener<Double> listener : listeners) {
				listener.valueChanged(value);
			}
		}
	}
}