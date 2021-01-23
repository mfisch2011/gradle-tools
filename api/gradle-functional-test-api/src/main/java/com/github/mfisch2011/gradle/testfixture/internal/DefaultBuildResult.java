/**
 * Copyright Jan 23, 2021 Matt Fischer <mfish2011@gmail.com>
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
package com.github.mfisch2011.gradle.testfixture.internal;

import java.io.File;
import java.util.List;

import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.TaskOutcome;

import com.github.mfisch2011.gradle.testfixture.BuildResult;

/**
 * TODO:documentation...
 */
public class DefaultBuildResult implements BuildResult {
	
	protected final File projectDir;
	protected final org.gradle.testkit.runner.BuildResult result;

	/**
	 * TODO:documentation...
	 * @param projectDir
	 * @param result
	 */
	public DefaultBuildResult(File projectDir,org.gradle.testkit.runner.BuildResult result) {
		this.projectDir = projectDir;
		this.result = result;
	}

	@Override
	public String getOutput() {
		return result.getOutput();
	}

	@Override
	public List<BuildTask> getTasks() {
		return result.getTasks();
	}

	@Override
	public BuildTask task(String name) {
		return result.task(name);
	}

	@Override
	public List<String> taskPaths(TaskOutcome outcome) {
		return result.taskPaths(outcome);
	}

	@Override
	public List<BuildTask> tasks(TaskOutcome outcome) {
		return result.tasks(outcome);
	}

	@Override
	public File getProjectDir() {
		return projectDir;
	}

}
