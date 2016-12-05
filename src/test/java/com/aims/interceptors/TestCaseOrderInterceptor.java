package com.aims.interceptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

import com.aims.Controller;

public class TestCaseOrderInterceptor implements IMethodInterceptor {

	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		List<IMethodInstance> orderedInstances = new ArrayList<IMethodInstance>(methods.size());
		Map<Integer, IMethodInstance> temp = new HashMap<>();
		for (IMethodInstance method : methods) {
			Controller controller = (Controller) method.getInstance();
			temp.put(controller.getPriority(), method);
		}
		
		for (int i = 0; i < temp.size(); i++) {
			orderedInstances.add(temp.get(i));
		}
		return orderedInstances;
	}

}
