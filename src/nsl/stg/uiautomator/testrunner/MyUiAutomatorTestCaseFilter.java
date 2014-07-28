package nsl.stg.uiautomator.testrunner;

import java.lang.reflect.Method;

import com.android.uiautomator.testrunner.TestCaseCollector.TestCaseFilter;

public class MyUiAutomatorTestCaseFilter implements TestCaseFilter {

	@Override
	public boolean accept(Method method) {
		return ((method.getParameterTypes().length == 0) && (method.getName().startsWith("test")) && (method.getReturnType().getSimpleName().equals("void")));
	}

	@Override
	public boolean accept(Class<?> clazz) {
		return MyUiAutomatorTestCase.class.isAssignableFrom(clazz);
	}
}
