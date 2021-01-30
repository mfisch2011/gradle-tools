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

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.TestDescriptor;
import org.gradle.api.tasks.testing.TestResult;
import org.gradle.process.ExecSpec;

import groovy.lang.Closure;

/**
 * TODO:documentation...
 */
public class CommitRevertClosure extends Closure<Void> {

	/**
	 * TODO:documentation
	 */
	private static final long serialVersionUID = -9031380597548365722L;

	public CommitRevertClosure(Test test) {
		super(test);
	}

	@SuppressWarnings("unused")
	public void doCall(TestDescriptor desc,TestResult result) {
		if(desc.getParent()==null) {
			String command = (result.getFailedTestCount()>0) ?
				extension().getRevertCommand() :
				extension().getCommitCommand();
			getProject().exec(runCommand(command));
		}
	}
	
	/**
	 * TODO:documentation...
	 * @param command
	 * @return
	 */
	protected Action<? super ExecSpec> runCommand(String command) {
		return new Action<ExecSpec>() {
			@Override
			public void execute(ExecSpec spec) {
				spec.executable("cmd"); //TODO:enable linux and mac support...
				spec.commandLine(command);
			}
		};
	}

	/**
	 * TODO:documentation...
	 * @return
	 */
	protected TestCommitRevertExtension extension() {
		return getProject().getExtensions()
		.getByType(TestCommitRevertExtension.class);
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	protected Project getProject() {
		return getTest().getProject();
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	protected Test getTest() {
		return (Test)getOwner();
	}
}
