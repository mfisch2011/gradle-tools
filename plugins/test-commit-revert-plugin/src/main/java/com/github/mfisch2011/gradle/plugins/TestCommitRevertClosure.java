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


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.tasks.userinput.UserInputHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.TestDescriptor;
import org.gradle.api.tasks.testing.TestResult;

import groovy.lang.Closure;

/**
 * TODO:documentation...
 */
public class TestCommitRevertClosure extends Closure<Void> {

	/**
	 * TODO:documentation
	 */
	private static final long serialVersionUID = -1893561572603652991L;

	public TestCommitRevertClosure(Test test) {
		super(test);
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public Test getTest() {
		return (Test)getOwner();
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public Project getProject() {
		return getTest().getProject();
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public Logger getLogger() {
		return getTest().getLogger();
	}

	/**
	 * TODO:documentation...
	 * @param desc
	 * @param result
	 * @throws Exception
	 */
	public void doCall(TestDescriptor desc,TestResult result) throws Exception {
		if(desc.getParent()==null) {
			Git git = Git.open(getProject().getRootProject().file(".git"));
			if(result.getFailedTestCount()>0) {
				getLogger().lifecycle("Reverting...");
				git.checkout().setAllPaths(true).call();
			} else {
				getLogger().lifecycle("Committing...");
				git.commit()
				.setAll(true)
				.setMessage(getMessage())
				.call();
				//TODO:commit
			}
		}
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	protected String getMessage() {
		String message = "TCR";
		if(extension().getPromptForMessage()) {
			UserInputHandler handler = ((ProjectInternal)getProject())
				.getServices().get(UserInputHandler.class);
			message = handler.askQuestion("Enter commit message: ","TCR");
		}
		return message;
	}

	/**
	 * TODO:documentation...
	 * @param git
	 * @return
	 */
	protected List<String> getTrackedFiles(Git git) throws Exception {
		Repository repo = git.getRepository();
		Ref head = repo.findRef("HEAD");
		RevWalk revWalk = new RevWalk(repo);
		RevCommit commit = revWalk.parseCommit(head.getObjectId());
		revWalk.close();
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(repo);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(true);
		ArrayList<String> results = new ArrayList<String>();
		while(treeWalk.next()) {
			results.add(treeWalk.getPathString());
		}
		treeWalk.close();
		return results;
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	protected TestCommitRevertExtension extension() {
		return getProject().getExtensions()
		.getByType(TestCommitRevertExtension.class);
	}
}
