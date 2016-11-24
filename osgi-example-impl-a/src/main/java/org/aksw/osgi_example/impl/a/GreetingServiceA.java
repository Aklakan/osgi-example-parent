package org.aksw.osgi_example.impl.a;

import org.aksw.osgi_example.api.GreetingService;

public class GreetingServiceA
	implements GreetingService
{
	@Override
	public String getGreetingMessage(String name) {
		return "A warm welcome to " + name;
	}
}
