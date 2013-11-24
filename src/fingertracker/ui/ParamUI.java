package fingertracker.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.JComponent;

import fingertracker.BooleanParameter;
import fingertracker.NumericParameter;
import fingertracker.Param;

public class ParamUI {
	private static final HashMap<Class<? extends Param<?>>, Class<? extends JComponent>> uiMap = new HashMap<Class<? extends Param<?>>, Class<? extends JComponent>>();
	static {
		uiMap.put(NumericParameter.class, NumericParameterUI.class);
		uiMap.put(BooleanParameter.class, BooleanParameterUI.class);
	}
	
	public static JComponent getUI(Param<?> param) {
		Class<? extends JComponent> uiClass = uiMap.get(param.getClass());
		try {
			return (JComponent)uiClass.getConstructor(param.getClass()).newInstance(param);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
