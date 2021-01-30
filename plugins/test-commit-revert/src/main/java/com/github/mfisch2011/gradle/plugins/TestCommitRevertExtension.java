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

import org.gradle.api.Project;

/**
 * TODO:documentation...
 */
public class TestCommitRevertExtension {
	
	/**
	 * TODO:documentation...
	 * @param project
	 */
	public static final String PLUGIN_EXT = "testCommitRevert";

	/**
	 * TODO:documentation...
	 * @param project
	 */
	public static void configure(Project project) {
		project.getExtensions().create(PLUGIN_EXT,
			TestCommitRevertExtension.class,project);
	}

	/**
	 * TODO:documentation
	 */
	protected String commitCommand = "git commit --all --message %MESSAGE%";
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public String getCommitCommand() {
		return commitCommand;
	}
	
	/**
	 * TODO:documentation...
	 * @param command
	 */
	public void setCommitCommand(String command) {
		commitCommand = command;
	}
	
	/**
	 * TODO:documentation
	 */
	protected String revertCommand = "git reset --hard HEAD";
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public String getRevertCommand() {
		return revertCommand;
	}
	
	/**
	 * TODO:documentation...
	 * @param command
	 */
	public void setRevertCommand(String command) {
		revertCommand = command;
	}
			
	/**
	 * TODO:documentation
	 */
	protected Boolean promptForMessage = false;
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public Boolean getPromptForMessage() {
		return promptForMessage;
	}
	
	/**
	 * TODO:documentation...
	 * @param set
	 */
	public void setPromptForMessage(Boolean set) {
		promptForMessage = set;
	}
	
}
