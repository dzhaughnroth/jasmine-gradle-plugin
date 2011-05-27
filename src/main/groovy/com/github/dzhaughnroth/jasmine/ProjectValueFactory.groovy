package com.github.dzhaughnroth.jasmine;
import org.gradle.api.Project;

class ProjectValueFactory implements DefaultValueFactory {

	Project project;

    boolean failBuildOnSpecFailure = true;
    boolean failBuildOnJslintFailure = true;
	String getBuildDirName() {
		return project.buildDir.toString();
	}
	List<String> sourceIncludes = ["src/main/javascript/**"];
	List<String> testIncludes = ["src/test/javascript/**" ];
	List<String> sourceExcludes = [];
	List<String> testExcludes = [];
	List<String> runnerIncludes = ["**/*MultiRunner*.html"];
	List<String> runnerExcludes = [];
	String parentDir = "jasmine";
    String generatorTargetDir = "src/test/javascript";
}