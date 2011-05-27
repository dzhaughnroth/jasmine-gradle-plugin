/*
 * Jasmine gradle plugin - http://purrpkg-gradle.sourceforge.net
 * Copyright (C) 2011 John Charles Roth
 *
 * This is free software, licensed under the terms of the GNU GPL 
 * Version 2 or, at your option, any later version. You should have 
 * received a copy of the license with this file. See the above web address
 * for more information, or contact the Free Software Foundation, Boston, MA. 
 * It is distributed WITHOUT WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.github.dzhaughnroth.jasmine;

import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.DefaultTask;

/** If you need to custom configure this task, call initDefaults() first. */

class JasmineCopyTask extends DefaultTask {

    DefaultValueFactory defaultsFactory = new ProjectValueFactory();
    boolean defaultsInited = false;

    def initDefaults() {
        defaultsFactory.project = project;
        sourceIncludes = defaultsFactory.getSourceIncludes();
        testIncludes = defaultsFactory.getTestIncludes();
        sourceExcludes = defaultsFactory.getSourceExcludes();
        testExcludes = defaultsFactory.getTestExcludes();
        parentDir = project
                .file("${project.buildDir}/${defaultsFactory.parentDir}");
        defaultsInited = true;
    }

    List<String> sourceIncludes;
    List<String> testIncludes;
    List<String> sourceExcludes;
    List<String> testExcludes;
    File parentDir;

    def clean() {
        project.delete(parentDir);
    }

    def copy() {
		parentDir.mkdirs();
		logger.log( LogLevel.INFO, "Copying from ${project.projectDir} to ${parentDir}" );
		project.copy( {
			from project.projectDir
			into parentDir
			include sourceIncludes
			include testIncludes
			exclude sourceExcludes
			exclude testExcludes

		} );
	}

    @TaskAction
    def copyStuff() {
        if (!defaultsInited)
            initDefaults();
        clean();
        copy();
    }

}
