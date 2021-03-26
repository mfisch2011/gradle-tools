/**
 * 
 */
package com.github.mfisch2011.gradle.test;

import static org.junit.Assert.*;

import java.io.File;

import org.gradle.testkit.runner.BuildResult;
import org.junit.runner.RunWith;

/**
 * TODO:documentation...
 *
 */
@RunWith(GradleTestRunner.class)
public class GradleTestRunnerTest {

	/**
	 * Test method for {@link com.github.mfisch2011.gradle.test.GradleTestRunner#run(org.junit.runner.notification.RunNotifier)}.
	 */
	@GradleTest(args={"greeting"},
	build=
	"plugins {" +
	"  id('com.github.mfisch2011.gradle.test.greeting')" +
	"}")
	public void testBuild(BuildResult result,File dir) {
		assertTrue(result.getOutput().contains("Hello from plugin 'com.github.mfisch2011.gradle.test.greeting'"));
	}

}
