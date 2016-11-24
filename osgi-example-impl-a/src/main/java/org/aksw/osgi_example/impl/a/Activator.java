package org.aksw.osgi_example.impl.a;

import java.util.Hashtable;

import org.aksw.osgi_example.api.GreetingService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{
    public void start(BundleContext context)
    {
        context.registerService(GreetingService.class, new GreetingServiceA(), new Hashtable<>());
    }

    public void stop(BundleContext context)
    {
        // NOTE: The service is automatically unregistered.
    }
}
