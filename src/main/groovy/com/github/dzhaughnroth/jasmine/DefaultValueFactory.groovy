package com.github.dzhaughnroth.jasmine;

import org.gradle.api.Project

interface DefaultValueFactory {

    void setProject( Project p );
    boolean isFailBuildOnSpecFailure();
    boolean isFailBuildOnJslintFailure();
    String getBuildDirName();
    List<String> getSourceIncludes();
    List<String> getTestIncludes();
    List<String> getSourceExcludes();
    List<String> getTestExcludes();
    List<String> getRunnerIncludes();
    List<String> getRunnerExcludes();
    String getGeneratorTargetDir();

    String getParentDir();
}