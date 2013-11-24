package fingertracker;

import java.util.ArrayList;

import org.opencv.core.Mat;

public class FingerParams {
	public final BooleanParameter enableFilters = new BooleanParameter("Enable Filters", true);
	public final NumericParameter downsample = new NumericParameter("Downsample", 1, 10, 1, 2);
	public final NumericParameter overlayBlend = new NumericParameter("Overlay Blend", 0, 1, 0.01, 0.5);
	public final NumericParameter maskBlend = new NumericParameter("Mask Blend", 0, 1, 0.01, 0.5);
	
	public final NumericParameter cannyThreshLow = new NumericParameter("Canny Thresh Low", 0, 255, 1, 0);
	public final NumericParameter cannyThreshHigh = new NumericParameter("Canny Thresh High", 1, 255, 1, 128);
	
	public final NumericParameter threshold = new NumericParameter("Threshold", 0, 255, 1, 1);
	public final NumericParameter gaussSigma = new NumericParameter("GaussSigma",0,10,0.1,8.4);
	
	public final NumericParameter crLow = new NumericParameter("Cr Low", 0, 256, 1, 137);
	public final NumericParameter crHigh = new NumericParameter("Cr High", 0, 256, 1, 195);
	public final NumericParameter cbLow = new NumericParameter("Cb Low", 0, 256, 1, 126);
	public final NumericParameter cbHigh = new NumericParameter("Cb High", 0, 256, 1, 255);
	
	
	private final ArrayList<ParamChangedListener> listeners = new ArrayList<ParamChangedListener>();
	public final ArrayList<Param<?>> allParams = new ArrayList<Param<?>>();
	{
		addParam(enableFilters);
		addParam(downsample);
		addParam(overlayBlend);
		addParam(maskBlend);
		addParam(cannyThreshLow);
		addParam(cannyThreshHigh);
		addParam(threshold);
		addParam(gaussSigma);
		addParam(crLow);
		addParam(crHigh);
		addParam(cbLow);
		addParam(cbHigh);
	}
	
	public final Mat probSkin;
	
	public FingerParams(Mat probSkin) {
		this.probSkin = probSkin;
	}
	
	private <T> void addParam(Param<T> param) {
		allParams.add(param);
		
		param.addListener(new ParameterListener<T>() {
			@Override
			public void valueChanged(T value) {
				for (ParamChangedListener l : listeners) {
					l.paramUpdated();
				}
			}
		});
	}
	
	
	public void addListener(ParamChangedListener listener) {
		listeners.add(listener);
	}
}