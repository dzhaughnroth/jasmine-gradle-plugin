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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.testng.TestNGOptions;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.logging.LogLevel;

class JasmineGradlePlugin implements Plugin<Project> {

    def config;

    public void apply(Project p) {
        config = new JasmineGradlePluginConvention();
        p.convention.plugins.jasmineGradlePlugin = config;
        def props = new Properties();
        props.load( JasmineGradlePlugin.class.getResourceAsStream( "jasmine-gradle.properties" ));
        def version = props["version"];
        
        p.configurations.add("jasmineGradlePlugin");
        p.dependencies.add("jasmineGradlePlugin",
                "com.github.dzhaughnroth:jasmine-gradle-plugin:${version}");
        JasmineGeneratorTask jgentask = p.tasks.add( "jasmineGenerate",
            JasmineGeneratorTask.class );
        jgentask.defaultsFactory = config;

        JasmineCopyTask jcopytask = p.tasks.add("jasmineCopy",
                JasmineCopyTask.class);
        jcopytask.defaultsFactory = config;

        JasmineRunnerTask jrunnertask = p.tasks.add("jasmineRun",
                JasmineRunnerTask.class);
        jrunnertask.defaultsFactory = config;
        jrunnertask.dependsOn(jcopytask);

        if (p.tasks.findByName("check") != null) {
            p.check.dependsOn(jrunnertask);
        }

        p.logger.log(LogLevel.INFO, "Applied JasminePlugin");
    }
}

class JasmineGradlePluginConvention extends ProjectValueFactory {

}
