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
package com.github.mfisch2011.gradle.tasks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.bundling.Zip;

/**
 * TODO:documentation...
 */
public class InstallDistribution extends DefaultTask {
	
	/**
	 * TODO:documentation
	 */
	public static final String TASK_NAME = "installDistribution";
	
	/**
	 * TODO:documentation...
	 * @param project
	 * @return
	 */
	public static InstallDistribution configure(Project project) {
		InstallDistribution task = project.getTasks().create(TASK_NAME,
				InstallDistribution.class);
		task.setGroup("Application");
		task.setDescription("Install application in Gradle binary directory");
		task.dependsOn("distZip");
		return task;
	}

	/**
	 * TODO:documentation...
	 * @throws IOException 
	 * @throws ZipException 
	 */
	@TaskAction
	public void install() throws ZipException, IOException {
		File source = getSource();
		Path dest = getDestination().toPath();
		getLogger().info("Installing {} into {}",source,dest);
		if(source!=null && source.exists()) {
			ZipFile zip = new ZipFile(source);
			Iterator<? extends ZipEntry> entries = zip.entries().asIterator();
			entries.next(); //throw away root dir...
			while(entries.hasNext()) {
				ZipEntry entry = entries.next();
				InputStream stream = zip.getInputStream(entry);
				String filename = entry.getName();
				int index = filename.indexOf('/');
				filename = filename.substring(index+1);
				Path filePath = dest.resolve(filename);
	            if (!entry.isDirectory()) {
	            	if(filePath.toFile().exists()) {
	            		getLogger().info("Skipping {}.",filePath);
	            	} else {
		            	getLogger().info("Copying {} to {}.",entry.getName(),filePath);
		                Files.copy(stream,filePath,StandardCopyOption.REPLACE_EXISTING);
	            	}
	            } else {
	                // if the entry is a directory, make the directory
	                File dir = filePath.toFile();
	                dir.mkdirs();
	            }
	            stream.close();
			}
			zip.close();
		}
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	@Internal
	protected File getDestination() {
		URL url = Gradle.class.getProtectionDomain()
				.getCodeSource().getLocation();
		String pathname = url.getPath();
		
		//remove file
		int index = pathname.lastIndexOf('/');
		pathname = pathname.substring(0,index);
		
		//remove directory
		index = pathname.lastIndexOf('/');
		pathname = pathname.substring(0,index);
		return new File(pathname);
	}
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	@Internal
	protected File getSource() {
		//TODO:must be a better way but getByName is puking...
		for(Task task : getProject().getTasks()) {
			System.out.println(task.getName());
			if(task.getName().equals("distZip")) {
				Zip zip = (Zip)task;
				return zip.getArchiveFile().get().getAsFile();
			}
		}
		return null;		
	}
}
