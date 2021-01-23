/**
 * Copyright Jan 22, 2021 Matt Fischer <mfish2011@gmail.com>
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

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.gradle.testkit.runner.BuildResult;

/**
 * TODO:documentation...
 */
@RunWith(GradleRunnerTestRunner.class)
public class GradleRunnerTestRunnerTest {

	/**
	 * Test method for {@link com.github.mfisch2011.gradle.testfixture.GradleRunnerTestRunner#GradleRunnerTestRunner(java.lang.Class)}.
	 */
	@GradleFunctionalTest(build=
	"task testBuild {\r\n"
	+ "	println \"PROPERTY: This is a test string.\"\r\n"
	+ "	println \"PROJECT NAME: test-build-gradle\"\r\n"
	+ "}",args= {"testBuild"} )
	public void testGradleBuild(BuildResult result) {
		String output = result.getOutput();
		assertNotNull(output);
		assertTrue(output.contains("PROPERTY: This is a test string."));
		assertTrue(output.contains("PROJECT NAME: test-build-gradle"));
	}

	/**
	 * Test method for {@link com.github.mfisch2011.gradle.testfixture.GradleRunnerTestRunner#GradleRunnerTestRunner(java.lang.Class)}.
	 */
	@GradleFunctionalTest(build=
	"task testBuild {\r\n"
	+ "	println \"PROPERTY: This is a test string.\"\r\n"
	+ "	println \"PROJECT NAME: \" + rootProject.name\r\n"
	+ "}",
	settings=
	"rootProject.name = \"test-gradle-settings\"",
	args= {"testBuild"})
	public void testGradleSettings(BuildResult result) {
		String output = result.getOutput();
		assertNotNull(output);
		assertTrue(output.contains("PROPERTY: This is a test string."));
		assertTrue(output.contains("PROJECT NAME: test-gradle-settings"));
	}
	
	/**
	 * Test method for {@link com.github.mfisch2011.gradle.testfixture.GradleRunnerTestRunner#GradleRunnerTestRunner(java.lang.Class)}.
	 */
	@GradleFunctionalTest(build=
	"task testBuild {\r\n"
	+ "	println \"PROPERTY: \" + testProperty1\r\n"
	+ "	println \"PROJECT NAME: test-gradle-proprties\"\r\n"
	+ "}",
	properties=
	"testProperty1 = This is a test gradle property.",
	args= {"testBuild"})
	public void testGradleProperties(BuildResult result) {
		String output = result.getOutput();
		assertNotNull(output);
		assertTrue(output.contains("PROPERTY: This is a test gradle property."));
		assertTrue(output.contains("PROJECT NAME: test-gradle-proprties"));
	}
	
	/**
	 * Test method for {@link com.github.mfisch2011.gradle.testfixture.GradleRunnerTestRunner#GradleRunnerTestRunner(java.lang.Class)}.
	 */
	@GradleFunctionalTest(resources="test-resources/",
	args= {"testBuild"} )
	public void testRelativePathResources(BuildResult result) {
		String output = result.getOutput();
		System.out.println(output);
		assertNotNull(output);
		assertTrue(output.contains("PROPERTY: This is a test property."));
		assertTrue(output.contains("PROJECT NAME: test-project"));
	}
	
	//TODO:how to specify a classloader to use???
	
	//TODO:relative with root
	
	//TODO:full path test???
}
