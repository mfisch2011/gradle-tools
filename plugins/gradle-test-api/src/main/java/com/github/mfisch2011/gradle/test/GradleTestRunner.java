/**
 * 
 */
package com.github.mfisch2011.gradle.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * TODO:documentation...
 *
 */
public class GradleTestRunner extends Runner {
	
	/**
	 * TODO:documentation...
	 */
	protected final Class<?> testClass;

	/**
	 * TODO:documentation...
	 *
	 * @param testClass
	 */
	public GradleTestRunner(Class<?> testClass) {
		this.testClass = testClass;
	}

	@Override
	public Description getDescription() {
		return Description.createTestDescription(
			testClass, "GradleTestRunner");
	}

	@Override
	public void run(RunNotifier notifier) {
		try {
			Object instance = newInstance();
			for(Method method : testClass.getMethods()) {
				GradleTest annotation = method.getAnnotation(GradleTest.class);
				if(annotation!=null) {
					fireTestStarted(notifier,method);
					File dir = setup(annotation);
					BuildResult result = build(dir,annotation);
					//TODO:only pass result and dir if required
					try {
						method.invoke(instance, result,dir);
					} catch(Exception e) {
						e.printStackTrace(System.err);
						fireTestFailure(notifier,method,e,annotation);
					}
					dir.delete(); //TODO:other cleanup?
					fireTestFinished(notifier, method);
				}
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO:documentation...
	 * @param notifier
	 * @param method
	 * @param e
	 */
	protected void fireTestFailure(RunNotifier notifier, Method method, 
	Exception e,GradleTest annotation) {
		Description description = Description.createTestDescription("Test failed.",
				method.getName(),annotation);
		Failure failure = new Failure(description,e);
		notifier.fireTestFailure(failure);
	}

	/**
	 * TODO:documentation...
	 * @param notifier
	 * @param method
	 */
	protected void fireTestFinished(RunNotifier notifier, Method method) {
		notifier.fireTestFinished(Description.createTestDescription(
				testClass, method.getName()));
	}

	/**
	 * TODO:documentation...
	 * @param notifier
	 * @param method
	 */
	protected void fireTestStarted(RunNotifier notifier, Method method) {
		notifier.fireTestStarted(Description.createTestDescription(
				testClass, method.getName()));
	}

	/**
	 * TODO:documentation...
	 * @param annotation
	 * @return
	 * @throws IOException 
	 */
	protected File setup(GradleTest annotation) throws IOException {
		File dir = Files.createTempDirectory("tmp").toFile();
		//TODO:copy resources
		
		//create build.gradle
		if(!isNullOrEmpty(annotation.build())) {
			File file = new File(dir,"build.gradle");
			Files.writeString(file.toPath(),annotation.build());
		}
		
		//create settings.gradle
		if(!isNullOrEmpty(annotation.settings())) {
			File file = new File(dir,"settings.gradle");
			Files.writeString(file.toPath(),annotation.settings());
		}
		
		//create gradle.properties
		if(!isNullOrEmpty(annotation.properties())) {
			File file = new File(dir,"gradle.properties");
			Files.writeString(file.toPath(),annotation.properties());
		}
		
		return dir;
	}

	/**
	 * TODO:documentation...
	 * @param properties
	 * @return
	 */
	protected static boolean isNullOrEmpty(String text) {
		return text==null || text.isEmpty();
	}

	/**
	 * TODO:documentation...
	 * @param dir
	 * @param annotation
	 * @return
	 */
	protected BuildResult build(File dir, GradleTest annotation) {
		GradleRunner runner = GradleRunner.create();
		runner.withProjectDir(dir);
		runner.withPluginClasspath();
		runner.withArguments(annotation.args());
		return runner.build();
	}

	/**
	 * TODO:documentation...
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	protected Object newInstance() throws
	NoSuchMethodException, SecurityException, InstantiationException,
	IllegalAccessException, IllegalArgumentException,
	InvocationTargetException {
		Constructor<?> constructor = testClass.getConstructor();
		return constructor.newInstance();
	}

}
