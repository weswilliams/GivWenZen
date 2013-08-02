# GivWenZen Fork Notice

There has not been a major update to the GivWenZen main project since 2011. I have decided to build and release a fork of this project, updated to work with the latest Fitnesse. I am not the maintainer of the original project; this is a fork for my own development purposes. 

The instructions in the original README below are somewhat out-of-date; I have chosen to focus on getting GivWenZen to build as a Maven project. To that end, here is what you need to include GivWenZen in your Maven project (this assumes a working proficiency with Maven):

1. Add the following repository to your pom.xml or settings.xml:

    &lt;repositories>
        ...
        &lt;repository>
            &lt;id>givwenzen-repo&lt;/id>
            &lt;name>GivWenZen Repository&lt;/name>
            &lt;url>https://raw.github.com/bernerbits/GivWenZen.git/releases&lt;/url>
            &lt;releases>
                &lt;enabled>true&lt;/enabled>
            &lt;/releases>
            &lt;snapshots>
                &lt;enabled>false&lt;/enabled>
            &lt;/snapshots>
        &lt;/repository>
        ...
    &lt;/repositories>

2. Add the dependency to your pom project:

    &lt;dependency>
        &lt;groupId>com.googlecode.givwenzen&lt;/groupId>
        &lt;artifactId>givwenzen&lt;/artifactId>
        &lt;version>1.0.4&lt;/version>
    &lt;/dependency>

That's it! Note that if you are using GivWenZen with a testing framework you will want to include `<scope>test</scope>` in your dependency.

## Changing the Fixture package

Note that the Fitnesse instructions below assume that you want to write your GivWenZen fixtures in the default package of `bdd.steps`. You may not want to do this by default. I am working on a way to configure GivWenZen from within Fitnesse, but in the meantime, the following workaround will get you running in the package of your choosing:

    |script                                                     |
    |start                     |Giv Wen Zen Executor Creator    |
    |step class base package   |vanderbilt.voom.fitnesse.fixture|
    |$executor=                |create                          |
    |start|giv wen zen for slim|$executor                       |

This is literally just instantiating Java classes and calling methods on them. The final line does some magic in SLIM that lets you write Given/When/Then fixtures without a title indicator. I don't currently understand this mechanism but will post more as I learn more :)

### Thanks and good luck! 
Derek Berner

# Original README

= [http://code.google.com/p/givwenzen http://code.google.com/p/givwenzen/logo?logo_id=1253844639&nonsense=something_that_ends_with.png] = 

[http://code.google.com/p/givwenzen GivWenZen] allows a user to use the [http://behaviour-driven.org/ BDD] [http://wiki.github.com/aslakhellesoy/cucumber/given-when-then Given When Then] vocabulary and plain text sentences to help a team [http://behaviour-driven.org/GettingTheWordsRight get the words right] and create a [http://behaviour-driven.org/UbiquitousLanguage ubiquitous language] to describe and test a business domain.

= The Idea =

The idea was taken from [http://cukes.info/ Cucumber] and my desire to create [http://cukes.info/ Cucumber] like specifications and tests in [http://fitnesse.org/ FitNesse]. Like [http://cukes.info/ Cucumber], [http://code.google.com/p/givwenzen GivWenZen] does not distinguish between [http://wiki.github.com/aslakhellesoy/cucumber/given-when-then Given When Then] or and but you should. Regular expression parsing is used to determine what step method should be executed for each given when then step in a test. Regular expression parsing is also used to find parameters to the step method. Also see how other ideas from [http://cukes.info/ Cucumber] map to [http://fitnesse.org/ FitNesse].

= Example =
=== 1) Start with an example [http://code.google.com/p/givwenzen/wiki/ExampleSlimFixture fixture] actual class found in the givwenzen_test.jar. ===

=== 2) In a [http://fitnesse.org/ FitNesse] table it could look like this. ===

===== import and start should go in [http://fitnesse.org/FitNesse.UserGuide.SpecialPages SetUp] or [http://fitnesse.org/FitNesse.UserGuide.SpecialPages SuiteSetUp] =====

    |import|
    |org.givwenzen|

    |script|
    |start|giv wen zen for slim|

===== this is your test =====

    |script|
    | given| a ToDo item is due tomorrow |
    | when | the date changes to tomorrow  |
    | then | a notification exists indicating the ToDo is due |

=== 3) The following is an example step class and test step method ===

    package bdd.steps;

    @DomainSteps
    public class ExampleSteps {

      @DomainStep( “a ToDo item is due (.*)” )
      public void createToDoWithDueDateOf(CustomDate date) {
        // do something
      }

      @DomainStep( “the date changes to (.*)” )
      public void theDateIs(CustomDate date) {
        // do something
      }

      @DomainStep( “a notification exists indicating the ToDo is due” )
      public boolean verifyNotificationExistsForDueToDo() {
        // do something
        return false;
      }

    }

----

= More Examples =
More example can be found in the [http://code.google.com/p/givwenzen/source/browse/#svn/trunk/FitNesseRoot FitNesseRoot] in source.  It is easy to [http://code.google.com/p/givwenzen/wiki/GettingStarted get started].

[http://code.google.com/p/givwenzen/source/browse/#svn/trunk/FitNesseRoot/GivWenZenTests/SimpleStepScenario Simple scenarios]
[http://code.google.com/p/givwenzen/source/browse/#svn/trunk/FitNesseRoot/GivWenZenTests/ComplexStepScenario More complex scenarios]

==== Groups: ====

http://groups.google.com/group/givwenzen_user

http://groups.google.com/group/givwenzen_developer

<p style="margin-top:10px; margin-bottom:0; padding-bottom:0; text-align:center; line-height:0"><a target="_blank" href="http://feeds.feedburner.com/~r/weswilliams/~6/1"><img src="http://feeds.feedburner.com/weswilliams.1.gif" alt="WesWilliams" style="border:0">

<a href="http://www.atlassian.com/software/clover" title="Atlassian Clover"><img border="0" width="180" height="60" alt="Atlassian Clover" src="http://www.atlassian.com/software/clover/images/badges/v1/lg_clover.png">
h1. Running

ant
java -jar ./lib/fitnesse.jar

Open http://localhost/ in the browser of your choice.

Here you will find an example test and a user guide for GivWenZen.

h2. License

License: MIT.

Libraries required for main classes:

commons-logging.jar - Apache License 2.0.
google-collect-snapshot-20080530.jar - Apache License 2.0.
javassist.jar - Mozilla Public License 1.1. 
log4j-1.2.9.jar - Apache License 2.0.
reflections-0.9.2.jar - GNU Lesser General Public License
