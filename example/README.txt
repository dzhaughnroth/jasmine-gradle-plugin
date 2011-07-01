ABOUT THIS EXAMPLE

The Jasmine Gradle Plugin is designed to make it easy to include
Jasmine and JSLint checks in a mixed Java/Groovy and JavaScript
project build with Gradle. If you are willing to follow some mild
conventions, it will allow you to run Jasmine Specs first in a browser
directly against your source code--with no need to build--but ensure
that they are run automatically as part of your gradle build.

Let's walk through a brief example where we start using the plugin.
Our objective is to apply tests and JSLint checks to a simple library,
"format.js," in the src/main/javascript.

You will notice a lot of specs and builds failing in the discussion
below; do not be surprised. The plugin ensures that a build fails when
its tests fail, and we want to demonstrate that here. (If you
disagree, you can configure the plugin to behave differently; but
indulge us for now.)

** Getting Started

The "build.gradle" file in this directory is just a generic Java
script with a few lines to apply the plugin from the central
repository. To get started, run "gradle jasmineGenerate," which
will create a number of files in the src/test/javascript directory.
In particular:

* SampleSpec.js and a SampleSpecRunner.html, just for fun.

* In src/test/javascript, "specRunners.js", an important file that tells
the plugin where all your Jasmine SpecRunners are

* The lib directory, with versions of the jasmine, jlint, and
  jquery. The plugin depends on JQuery, but in "no conflict" mode. It
  is no problem if your project uses JQuery, even a different version.
  We expect you to leave the files in the lib directory untouched;
  jasmineGenerate will clobber them, at any rate.

Finally, in the lib directory is MultiSpecRunner.html, a "dashboard"
that you can use to see all the Jasmine SpecRunners listed in
specRunners.js at once. This file is the guts that connects your
SpecRunners to the Gradle build. We expect you to leave the files in
the lib directory untouched; running the jasmineGenerate target will
clobber any changes you make, at any rate.

Point a web brower at MultiSpecRunner.html; with luck you will see a
page with a headline indicating failurs, and a single iframe with a
single SpecRunner, that runs 4 spec with 2 failures. Check that
carefully, and then click on the link above to iframe to open
SampleSpecRunner in its own window.

** Do you need to run a local web server

We recommend it. Browsers as of 2010 generally refuse to support AJAX
over the "file:" protocol. Some let you configre this
behavior. However running "python -m SimpleHTTPServer" (or any number
of equivalent things) and using "http://localhost:8000" instead is
simple enough and more robust.

In the new window, you'll see below the usual SpecRunner restuls, plus
a "JSLint" section at the bottom. The plugin supports JSLint checks,
at your option. If you see "SampleSpec.js: 2 isses" all is well;
however if you see "No JSLint Data?" you are probably using the
"file://" protocol to access your pages.

** Your build should fail.

Run "gradle build;" the build should fail at this point, complaining
about failing specs. Here is an overview of what the plugin does in
your build: (1) Your "check" target is made to depend on jasmineCopy
and jasmineRun. (2) jasmineCopy makes a copy of you
src/main/javascript and src/test/javascript directories in your build
directory (under "jasmine"), (3) jasmineRun loads the
MultiSpecRunner.html with HtmlUnit and Rhino, and parses the results,
(4) it creates "jasmine-summary.html" to help you with details.

Generally, you can just look at MultiSpecRunner.html in your browser
to find the problem. But, of course, in the messy real world of
browsers and JavaScript, there are unusual cases where MultiSpecRunner
will work in your browser but not with HtmlUnit and Rhino;
jasmine-summary.html will help you pursue such mysteries.

** Enough of the samples, on to the examples

We have supplied a little code in format.js and a Jasmine Spec in
FormatSpec.js in the src/main and src/test directories. Also, we
tweaked the SampleSpecRunner to ExampleSpecRunner, so it runs the
FormatSpec. Add the following line to specRunners.js: 
  
  jasmineGradle.add( "../format/ExampleSpecRunner.html" );

Next, point your browser at the MultiSpecRunner; you'll now see two
iframes, one for SampleSpecRunner, and one for
ExampleSpecRunner. Comment out the SampleSpecRunner line, reload, and
see that MultiSpecRunner passes.

Next run "gradle build;" the build should succeed, because
MultiSpecRunner succeeded. 

** Options

If you do not care to apply JSLint, just remove the "lintable" class
tag from the html script tags. 

If you want to include Jasmine and/or JSLint reporting, but want the
build to pass irrespective of it, uncomment the appropriate
configuration lines in build.gradle.



** The JavaScript code/test cycle; no Gradle build required!

Now let's imagine shifting over to testing real javascript code. We've
supplied "src/main/javascript/format.js" for this example, and
FormatSpec.js and TypicalSpecRunner.html in src/test/javascript for
this purpose. Pointing a browser an TypicalSpecRunner should show 
3 specs run with zero failures, and a single JSLint complaint about 
format.js. (And a valid complaint it is.)

Edit the specRunners.js file, adding the line:

   jasmineGradle.add( "../format/ExampleTypicalSpecRunner.html" );


TypicalSpecRunner is a pretty straightforward copy of
SampleSpecRunnerLet's test "format.js" instead of the sample.

Now that the plugin set up, you would next write and run some Jasmine
specs, then some Javascript code that implements the Specs. We have
done this for you in src/main/javascript/format.js, and FormatSpec.js
and TypicalSpecRunner.html.

You need to tell the plugin about your SpecRunner; add a line to
specRunners.js as follows:



Now look at MultiSpecRunner.html again, and you will see a second
iframe with the TypicalSpecRunner.html.

Now you are ready to code and test JavaScript as usual. Uncomment the
"should handle null" spec, and check that the TypicalSpecRunner and
MultiSpecRunner register failures. Don't fix the problems just yet!

** Running Specs as part of a Gradle build

Run "gradle check." The build should fail, because the Specs fail.
The plugin generates "jasmine-summary.html" in the build directory
detailing the problems. The plugin fails because the MultiSpecRunner
fails; the plugin uses HtmlUnit to run MultiSpecRunner.html and parses
the result.

Now go and fix the problems (so that MultiSpecRunner passes), and run
the build again; the gradle build should now succeed.

While this example will pose no problems, in the messy real world, it
is possible that MultiSpecRunner will pass in your browser, and not in
the build. (One example: on a project using Dojo, the browser caches
the Dojo code, but HtmlUnit did not, so could not run the page in
reasonable time. Bad luck! Another possibility is that your browser
forgives some flaky JavaScript, but Rhino does not.) The plugin
supplies lots of details to enable you to uncover such mysteries.

** JSLint integration

