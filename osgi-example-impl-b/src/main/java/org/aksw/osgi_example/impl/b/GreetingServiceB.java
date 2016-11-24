package org.aksw.osgi_example.impl.b;

import org.aksw.osgi_example.api.GreetingService;

public class GreetingServiceB
	implements GreetingService
{
	@Override
	public String getGreetingMessage(String name) {
		return "Be welcome, " + name;
	}
}
