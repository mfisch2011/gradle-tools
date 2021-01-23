/**
 * Copyright Jan 21, 2021 Matt Fischer <mfish2011@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mfisch2011.gradle.testfixture;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.UnexpectedBuildFailure;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.github.mfisch2011.gradle.testfixture.internal.DefaultBuildResult;

/**
 * TODO:documentation...
 */
public class GradleRunnerTestRunner extends Runner {
	
	/**
	 * TODO:documentation
	 */
	@SuppressWarnings("rawtypes")
	protected final Class testClass;
	
	/**
	 * TODO:documentation
	 */
	protected File dir = null;
	
    /**
     * TODO:documentation...
     * @param testClass
     */
    public GradleRunnerTestRunner(Class<?> testClass) {
        super();
        this.testClass = testClass;
    }

	@Override
	public Description getDescription() {
		return Description.createTestDescription(
		testClass, "Gradle functional test using GradleRunner.");
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	protected Object constructTestObject() throws Exception {
		Constructor<?> constructor = testClass.getConstructor();
        Object testObject = constructor.newInstance();
        return testObject;
	}
	
	@Override
	public void run(RunNotifier notifier) {
		try {
			Object dut = constructTestObject();
			for(Method method : testClass.getMethods()) {
				if(method.isAnnotationPresent(Ignore.class)) {
					notifier.fireTestIgnored(describe(method));
				} else if (method.isAnnotationPresent(GradleFunctionalTest.class)) {
					run(notifier,dut,method);
				}
			}
		} catch(Throwable e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * TODO:documentation...
	 * @param notifier
	 * @param dut
	 * @param method
	 * @param annotation
	 * @throws Exception
	 */
	protected void run(RunNotifier notifier,Object dut,Method method) throws Exception {
		GradleFunctionalTest annotation = method.getAnnotation(GradleFunctionalTest.class);
		Description description = describe(method);
		try {
			notifier.fireTestStarted(description);
			configure(annotation);
			BuildResult result = build(annotation);
			method.invoke(dut,result);
			cleanup();
			notifier.fireTestFinished(description);
		} catch(UnexpectedBuildFailure e) { 
			printBuildInfo("build.gradle","BUILD:");
			printBuildInfo("settings.gradle","SETTINGS:");
			printBuildInfo("gradle.properties","PROPERTIES:");
			//TODO:how to attache build scripts and properties to e or description...
			Failure failure = new Failure(description, e);
			notifier.fireTestFailure(failure);
		}catch(InvocationTargetException e) {
			Throwable cause = e.getCause();
			if(cause instanceof AssertionError) {
				AssertionError assertion = (AssertionError)cause;
				Failure failure = new Failure(description, assertion);
				notifier.fireTestFailure(failure);
			} else {
				throw new RuntimeException(e);
			}
			
		}
	}
	
	/**
	 * TODO:documentation...
	 * @param string
	 * @param string2
	 * @throws IOException 
	 */
	protected void printBuildInfo(String filename, String header) throws IOException {
		File file = new File(dir,filename);
		byte[] bytes = (file.exists()) ? Files.readAllBytes(file.toPath()) : null;
		String text = (bytes!=null) ? new String(bytes) : "";
		System.out.printf("%s%n%s%n%n", header,text);
	}

	/**
	 * TODO:documentation...
	 * @param annotation
	 * @return
	 */
	protected BuildResult build(GradleFunctionalTest annotation) {
		GradleRunner runner = GradleRunner.create()
				.withProjectDir(dir)
				.withArguments(annotation.args());
		URL url = testClass.getResource("/plugin-under-test-metadata.properties");
		if(url!=null)
			runner.withPluginClasspath();
		return new DefaultBuildResult(dir,runner.build());
	}
	
	/**
	 * TODO:documentation...
	 * @param method
	 */
	protected Description describe(Method method) {
		return Description.createTestDescription(testClass,
			method.getName(),method.getAnnotations());
	}
	
	/**
	 * TODO:documentation...
	 */
	protected void cleanup() {
		dir.delete();
	}
	
	/**
	 * TODO:documentation...
	 * @param text
	 * @return
	 */
	protected static boolean isNullOrEmpty(String text) {
		return text==null || text.isEmpty();
	}
	
	/**
	 * TODO:documentation...
	 * @param testObject
	 * @param config
	 */
	protected void configure(GradleFunctionalTest config) throws IOException {
		//create temp project directory
		dir = Files.createTempDirectory("tmp").toFile();
		dir.deleteOnExit(); //in case we abort...
		
		//copy test resources
		if(!isNullOrEmpty(config.resources())) {
			copyResources(config.resources(),dir);
		}
		
		//create build.gradle
		if(!isNullOrEmpty(config.build())) {
			File file = new File(dir,"build.gradle");
			Files.write(file.toPath(),config.build().getBytes());
		}
		
		//create settings.gradle
		if(!isNullOrEmpty(config.settings())) {
			File file = new File(dir,"settings.gradle");
			Files.write(file.toPath(),config.settings().getBytes());
		}
		
		//create gradle.properties
		if(!isNullOrEmpty(config.properties())) {
			File file = new File(dir,"gradle.properties");
			Files.write(file.toPath(),config.properties().getBytes());
		}
	}
	
	/**
	 * TODO:documentation...
	 * 
	 * TODO:move this to a utility library...
	 * 
	 * @param src
	 * @param dest
	 */
	protected void copyResources(String src,File dest) throws IOException {
		//TODO:HOW TO FUCK TO DO THIS SINCE WE REALLY CANNOT ASSUME THAT build.gradle EXISTS!!!
		URL url = testClass.getResource(src);
		if(url!=null) {
			if(url.getProtocol().equals("file")) {
				File dir = new File(url.getPath());
				File[] files = dir.listFiles();
				if(files!=null) {
					for(File file : files)
						copy(file,dest);
				}
			}
			//TODO:how to handle jars???
		} else {
			System.out.printf("MISSING RESOURCE: %s%n", src);
		}
	}
	
	/**
	 * TODO:documentation...
	 * @param file
	 * @param dest
	 */
	protected void copy(File file,File dest) throws IOException {
		File out = new File(dest,file.getName());
		if(file.isDirectory()) {
			for(File child : file.listFiles())
				copy(child,out);
		} else {
			if(!out.getParentFile().exists())
				out.getParentFile().mkdirs();
			Files.copy(file.toPath(),out.toPath());
		}
	}

	/**
	 * TODO:documentation...
	 * @param testObject
	 * @param config
	 */
	protected void runBuild(Object testObject,GradleFunctionalTest config) {
		//TODO:
	}
}
