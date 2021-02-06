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

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import com.github.mfisch2011.gradle.tasks.InstallDistribution;

/**
 * TODO:documentation...
 */
public class InstallDistributionPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		applyPlugins(project);
		createExtension(project);
		addTasks(project);
	}

	/**
	 * TODO:documentation...
	 * @param project
	 */
	protected void applyPlugins(Project project) {
		project.getPluginManager().apply("java");
		project.getPluginManager().apply("application");
	}

	/**
	 * TODO:documentation...
	 * @param project
	 */
	protected void addTasks(Project project) {
		InstallDistribution.configure(project);
	}

	/**
	 * TODO:documentation...
	 * @param project
	 */
	protected void createExtension(Project project) {
		// TODO Auto-generated method stub
		
	}

}
