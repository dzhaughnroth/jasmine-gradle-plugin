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
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import groovy.xml.MarkupBuilder


class MultiRunnerMain {

    public static String FAILURES_FILE_NAME = "jasmine.failures";
    public static String JSLINT_FAILURES_FILE_NAME = "jasmine.jslint.failures";

    /** 
     * First argument is project name, second is build directory name, 
     * rest are paths (relative to project root) to the MultiRunner.html 
     * files to check.
     */
	public static void main( String[] args ) throws Exception {
		def main = new MultiRunnerMain();
        main.run( args );
	}

    String name;
    String buildDirName;
	def summaries = [];
	File buildDir;

    def run( argv ) {
        def args = [];
        args.addAll( argv );
        def name = args.remove(0);
        buildDirName = args.remove(0);
        buildDir = new File( buildDirName );
        HttpFileServer server = new HttpFileServer( buildDir );
        try {
            runAndReport args;
        }
        finally {
            server.destroy();
        }
    }
    
	def runAndReport( args ) {
		Date start = new Date();
        File failuresFile = new File( buildDir, FAILURES_FILE_NAME );
        File jslintFailuresFile = new File( buildDir, JSLINT_FAILURES_FILE_NAME );
	failuresFile.delete();
	jslintFailuresFile.delete();
	args.each( { 
            def path =  it.substring( buildDir.getAbsolutePath().length() + 1 );
            println( "Running ${it} with ${path}." )
			runHtmlUnitOnMultiRunner( "http://localhost:36018/", path ) 
		} );
		long elapsed = System.currentTimeMillis() - start.time;
		File summaryOut = new File( buildDir, "jasmine-summary.html" );
		String absBuildPath = buildDir.absolutePath;
		File jasmineDir = new File( buildDir, "jasmine" );
		String jasmineDirPath = jasmineDir.absolutePath;	
		def writer = new StringWriter()
		def html = new MarkupBuilder(writer)
		html.html {
			head {
				title {
					mkp.yield( "${name}: Jasmine Spec Result Summary" )
				}
			}
			body {
				h1 { mkp.yield( "Jasmine Spec Results for project ${name}" ); }
				p { mkp.yield( "${summaries.size()} SpecRunners, run in ${elapsed} ms" )}
				ul {
		summaries.each { x->
					li {
						mkp.yield( "${x.passed ? 'Passed' : 'FAILED'} page " );
						def relPath = x.path;
						def pathToSrc = "../src/" + x.path; //x.file.absolutePath.substring( jasmineDirPath.length() + 1 );
						a( href:"../${pathToSrc}" ) { 
							mkp.yield( "${pathToSrc}" ) }
                        a( href:relPath ) { mkp.yield( "(Build Copy)" ) }
                        mkp.yield( " ${x.message} (in ${x.elapsed} ms)}" )
                        if ( !x.passed ) {
                            textarea( cols:"80", rows:"10" ) {
                                mkp.yield( x.longMessage );
                            }
                        }  

					}
        }
				}
			}
		}
		summaryOut.write( writer.toString() );
		
        if ( summaries.any( { ! it.passed } ) ) {
            failuresFile.write( "Jasmine Specs failed on ${start}." );
        }
        if ( summaries.any( { ! it.jsLintPassed } ) ) {
            jslintFailuresFile.write( "Some JSLint failures occurred on ${start}." );
        }

	}

	String runHtmlUnitOnMultiRunner( String prefix, String path ) {
		WebClient webClient = new WebClient();
		HtmlPage page = webClient.getPage( prefix + path );//uri );
        long now = System.currentTimeMillis();
        webClient.waitForBackgroundJavaScriptStartingBefore( 30000L );
        long elapsed = System.currentTimeMillis() - now;
        println( "Took ${elapsed} to run ${prefix}${path}" );
        report( path, page, elapsed );
		return page;
	}

	def report( String path, HtmlPage page, long elapsed ) {
        ScriptResult sr = page.executeJavaScript( "jasmineGradle.getStatus()" );
        println( "Status: " + sr.getJavaScriptResult() );
        boolean passed = String.valueOf(sr.getJavaScriptResult() ).contains( "passed" );
        boolean failed = String.valueOf(sr.getJavaScriptResult() ).contains( "failed" );
        String veryLongMsg = "";
        String longMsg = "";
        try {
            sr = page.executeJavaScript( "jasmineGradle.getResultsAsText(true)" );
            veryLongMsg = sr.getJavaScriptResult();
            sr = page.executeJavaScript( "jasmineGradle.getResultsAsText(false)" );
            longMsg = sr.getJavaScriptResult();
        }
        catch( Exception e ) {
            passed = false;
            longMsg = "Could not retrieve results as text from ${sr}:" + e;
        }
        println( "VLM: " +  veryLongMsg );
        File out = new File( buildDir, path + ".out" );
        out.write( longMsg );
        out.write( veryLongMsg );
        
        boolean jsLintPassed = false;
        String jsLintMessage = "";
        try {
            sr = page.executeJavaScript( "jasmineGradle.getJsLintResultsAsText()" );
            jsLintMessage += sr.getJavaScriptResult();
            jsLintPassed = jsLintMessage.startsWith( "JSLint: 0" );
        }
        catch( Exception e ) {
            e.printStackTrace();
            jsLintMessage = "Problem getting JsLint results: " + e;
        }
        File lintOut = new File( buildDir, path + ".jslint" );
        lintOut.write( jsLintMessage );

        def summary = [ path:path,
				passed:passed,
				page:page,
                elapsed:elapsed,
                message:"All passed",
                longMessage:longMsg,
                jslintMessage: jsLintMessage,
                jsLintPassed: jsLintPassed ];
		summaries.add( summary );
        if ( failed ) {
			summary.message = "Spec failures in ${path}";
		}
        else if ( !passed) {
            summary.message = "Errors in runing ${path}: ${longMsg}}";
        }
	}

}