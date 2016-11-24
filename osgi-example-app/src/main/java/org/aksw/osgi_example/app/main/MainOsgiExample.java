package org.aksw.osgi_example.app.main;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.aksw.osgi_example.api.GreetingService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class MainOsgiExample {
    public static void main(String[] args) throws Exception {

        try {
            FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();

            Map<String, String> config = new HashMap<String, String>();
            config.put(Constants.FRAMEWORK_STORAGE_CLEAN, Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);

            config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
                    String.join(",", "org.aksw.osgi_example.api;version=\"1.0.0\""));

            List<File> jarFiles = Arrays.asList("a", "b").stream().map(implStr -> {
                String jarPathStr = String
                        .format("../osgi-example-impl-%1$s/target/osgi-example-impl-%1$s-1.0.0-SNAPSHOT.jar", implStr);
                File jarFile = new File(jarPathStr);
                return jarFile;
            }).collect(Collectors.toList());

            Framework framework = frameworkFactory.newFramework(config);
            try {
                framework.init();
                framework.start();
                BundleContext context = framework.getBundleContext();

                for (File jarFile : jarFiles) {
                    String jarFileStr = jarFile.getAbsolutePath();
                    System.out.println("Loading: " + jarFileStr);

                    Bundle bundle = context.installBundle("reference:file:" + jarFileStr);
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
                        bundle.stop();
                        bundle.uninstall();
                    }
                }
            } finally {
                framework.stop();
                framework.waitForStop(0);
            }
            System.out.println("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Note: Sometimes the framework may have sleeping threads (e.g. 'felix resolver')
        // that only die after about a minute. We don't want to wait so force exit.
        System.exit(0);
    }
}
