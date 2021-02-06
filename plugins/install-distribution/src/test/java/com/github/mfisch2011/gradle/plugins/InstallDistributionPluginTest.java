/**
 * Copyright Feb 6, 2021 Matt Fischer <mfish2011@gmail.com>
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
package com.github.mfisch2011.gradle.plugins;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.gradle.internal.impldep.com.google.common.io.Files;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * TODO:documentation...
 */
public class InstallDistributionPluginTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	/**
	 * TODO:documentation
	 */
	protected File buildGradle;
	
	/**
	 * TODO:documentation...
	 * @throws IOException 
	 */
	@Before
	public void setup() throws IOException {
		buildGradle = folder.newFile("build.gradle");
	}
	
	/**
	 * TODO:documentation...
	 * @throws IOException
	 */
	@After
	public void cleanup() throws IOException {
		folder.delete();
	}

	/**
	 * Test method for {@link com.github.mfisch2011.gradle.plugins.InstallDistributionPlugin#apply(org.gradle.api.Project)}.
	 * @throws IOException 
	 */
	@Test
	public void testApply() throws IOException {
		String text = 
		"plugins {\n"
		+ " id 'java'\n"
		+ " id 'application'\n"
		+ " id 'com.github.mfisch2011.gradle.plugins.InstallDistributionPlugin'\n"
		+ "}";
		Files.write(text.getBytes(), buildGradle);
		//TODO:add code for build?
		BuildResult result = GradleRunner.create()
			.withProjectDir(folder.getRoot())
			.withPluginClasspath()
			.withArguments("installDistribution")
			.build();
		assertNotNull(result);
		String output = result.getOutput();
		System.out.println(output); //TODO:temp debugging...
		assertNotNull(output);
		BuildTask task = result.task(":installDistribution");
		assertNotNull(task);
		TaskOutcome outcome = task.getOutcome();
		assertNotNull(outcome);
		assertEquals(TaskOutcome.SUCCESS,outcome);
		//test the task copied correctly...
	}

}
