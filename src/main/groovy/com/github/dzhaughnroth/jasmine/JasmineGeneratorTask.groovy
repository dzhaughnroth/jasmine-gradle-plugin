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

import javax.sound.sampled.TargetDataLine;

import org.apache.tools.ant.util.LayoutPreservingProperties.LogicalLine;
import org.gradle.api.logging.LogLevel;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.DefaultTask;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

/** If you need to custom configure this task, call initDefaults() first. */

class JasmineGeneratorTask extends DefaultTask {

    DefaultValueFactory defaultsFactory = new ProjectValueFactory();

    boolean defaultsInited = false;

    def initDefaults() {
        defaultsFactory.setProject(project);
        targetDir = new File( project.projectDir, defaultsFactory.generatorTargetDir );
     }

    File targetDir;

    static noclobber = [ "specRunners.js" ];
    static resources = [ "SampleSpecRunner.html", "SampleSpec.js" ];
    static libResources = [ "jasmine.css", "jasmine.js", "jasmine-html.js", "jasmine-reporters.js", 
            "MIT.LICENSE", "jasmine-gradle.js", "jasmine-gradle.css", "webjslint.js", "jquery-1.6.1.min.js", "MultiRunner.html" ];

    @TaskAction
	def copyResources() {
        if ( ! defaultsInited ) {
            initDefaults();
        }
        targetDir.mkdirs();
        noclobber.each( { copyResource( [], it, true ) } );
        resources.each { copyResource( [], it, false ) };
        libResources.each { copyResource( ["lib"], it, false ) };
	}
    
    def copyResource( List<String> pathElements, String resourceName, boolean noclobber ) {
        File outdir = targetDir;
        String s = "";
        pathElements.each( {
            outdir = new File( outdir, it );
            s += it + "/";
        } );
        outdir.mkdirs();
        outfile = new File( outdir, resourceName );
        if (outfile.exists() && noclobber ) {
            project.logger.log( LogLevel.INFO, "Not clobbering " + outfile );
            return;
        }
        else {
            if ( outfile.exists() ) { outfile.delete(); }
            outfile.createNewFile();
            outfile.append( JasmineGeneratorTask.class.getClassLoader().getResourceAsStream( "jasmineGradle/" + s + resourceName ));
            project.logger.log( LogLevel.INFO, "Copied to " + outfile );
        }
    }
}
