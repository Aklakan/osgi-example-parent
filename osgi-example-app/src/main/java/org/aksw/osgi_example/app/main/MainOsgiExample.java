package org.aksw.osgi_example.app.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.aksw.osgi_example.api.GreetingService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class MainOsgiExample {
	public static void main(String[] args) throws Exception {

		FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();

		Map<String, String> config = new HashMap<String, String>();
		config.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
		config.put(Constants.FRAMEWORK_BOOTDELEGATION, "*");

		config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, String.join(",",
				"org.aksw.osgi_example.api"
		));

		String jarFileStr = new File("../osgi-example-impl-a/target/osgi-example-impl-a-1.0.0-SNAPSHOT.jar").getAbsolutePath();
		System.out.println("Loading file: " + jarFileStr);


		Framework framework = frameworkFactory.newFramework(config);
		try {
			framework.init();
			framework.start();
			BundleContext context = framework.getBundleContext();
			Bundle bundle = context.installBundle(
					"reference:file:" + jarFileStr);
			try {
				bundle.start();

				ServiceReference<GreetingService> sr = context.getServiceReference(GreetingService.class);
				if (sr == null) {
					throw new RuntimeException("Service reference is null");
				}
				GreetingService service = context.getService(sr);
				String msg = service.getGreetingMessage("Anne");
				System.out.println("RESULT: " + msg);
			} finally {
				// bundle.uninstall();
				bundle.stop();
			}
		} finally {

			framework.stop();
			framework.waitForStop(0);
		}
		System.out.println("done.");
		System.exit(0);
		return;
	}
}
