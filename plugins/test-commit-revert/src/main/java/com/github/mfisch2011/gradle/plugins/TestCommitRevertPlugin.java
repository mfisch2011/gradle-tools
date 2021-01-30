/**
 * Copyright Jan 30, 2021 Matt Fischer <mfish2011@gmail.com>
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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.testing.Test;

import groovy.lang.Closure;

/**
 * TODO:documentation...
 */
public class TestCommitRevertPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		createExtension(project);		
		//TODO:should this be after evaluation?
		addCommitRevertClosure(project);
	}

	/**
	 * TODO:documentation...
	 * @param project
	 */
	protected void addCommitRevertClosure(Project project) {
		for(Task task : project.getTasks()) {
			if(task instanceof Test) {
				Test test = (Test)task;
				test.afterSuite(commitRevert(test));
			}
		}
	}

	/**
	 * TODO:documentation...
	 * @param test
	 * @return
	 */
	protected Closure<Void> commitRevert(Test test) {
		return new CommitRevertClosure(test);
	}

	/**
	 * TODO:documentation...
	 * @param project
	 */
	protected void createExtension(Project project) {
		TestCommitRevertExtension.configure(project);
	}
}
