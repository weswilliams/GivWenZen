# GivWenZen Fork Notice

There has not been a major update to the GivWenZen main project since 2011. I have decided to build and release a fork of this project, updated to work with the latest Fitnesse. I am not the maintainer of the original project; this is a fork for my own development purposes, which I have decided to make public. As such, feature requests, issues and so on are likely to be met at worse than a snail's pace. However, should you send me pull requests, I'm more than happy to oblige. 

I have chosen to focus on getting GivWenZen to build as a Maven project. To that end, here is what you need to include GivWenZen in your Maven project (this assumes a working proficiency with Maven):

First, add the following repository to your pom.xml or settings.xml:

    <repositories>
        ...
        <repository>
            <id>givwenzen-repo</id>
            <name>GivWenZen Repository</name>
            <url>https://raw.github.com/bernerbits/GivWenZen/releases</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
        </repository>
        ...
    </repositories>

Next, add the dependency to your pom project:

    <dependency>
        <groupId>com.googlecode.givwenzen</groupId>
        <artifactId>givwenzen</artifactId>
        <version>1.0.4</version>
    </dependency>

That's it! Note that if you are using GivWenZen with a testing framework you will want to include `<scope>test</scope>` in your dependency.

## Changing the Fixture package

Note that the Fitnesse instructions below assume that you want to write your GivWenZen fixtures in the default package of `bdd.steps`. You can override this by using:

    |script                                                 |
    |start|Giv Wen Zen For Slim|your.package.name.here      |

### Thanks and good luck! 
Derek Berner

# Original README

# [![GivWenZen](http://code.google.com/p/givwenzen/logo?logo_id=1253844639&nonsense=something_that_ends_with.png)][GivWenZen]

[GivWenZen][] allows a user to use the [BDD][] [Given When Then][] vocabulary and plain text sentences to help a team [get the words right][] and create a [ubiquitous language][]  to describe and test a business domain.

# The Idea 

The idea was taken from [Cucumber][] and my desire to create [Cucumber][] like specifications and tests in [FitNesse][]. Like [Cucumber][], [GivWenZen][] does not distinguish between ["Given", "When", "Then"][Given When Then] or "And" but you should. Regular expression parsing is used to determine what step method should be executed for each given when then step in a test. Regular expression parsing is also used to find parameters to the step method. Also see how other ideas from [Cucumber][] map to [FitNesse][].

# Example 
### 1) Start with an example [fixture](http://code.google.com/p/givwenzen/wiki/ExampleSlimFixture "fixture") actual class found in the givwenzen_test.jar.

### 2) In a [FitNesse][] table it could look like this.

##### import and start should go in [SetUp](http://fitnesse.org/FitNesse.UserGuide.SpecialPages) or [SuiteSetUp](http://fitnesse.org/FitNesse.UserGuide.SpecialPages)

    |import|
    |org.givwenzen|

    |script|
    |start|giv wen zen for slim|

##### this is your test 

    |script|
    | given| a ToDo item is due tomorrow |
    | when | the date changes to tomorrow  |
    | then | a notification exists indicating the ToDo is due |

### 3) The following is an example step class and test step method

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

# More Examples 
More example can be found in the [FitNesseRoot][] in source.  It is easy to [get started][Getting Started].

[Simple scenarios](http://code.google.com/p/givwenzen/source/browse/#svn/trunk/FitNesseRoot/GivWenZenTests/SimpleStepScenario "Simple scenarios")
[More complex scenarios](http://code.google.com/p/givwenzen/source/browse/#svn/trunk/FitNesseRoot/GivWenZenTests/ComplexStepScenario "More complex scenarios")

#### Groups:

http://groups.google.com/group/givwenzen_user

http://groups.google.com/group/givwenzen_developer

# Running

    ant
    java -jar ./lib/fitnesse.jar

Open http://localhost/ in the browser of your choice.

Here you will find an example test and a user guide for GivWenZen.

## License

License: MIT.

Libraries required for main classes:

* commons-logging.jar - Apache License 2.0.
* google-collect-snapshot-20080530.jar - Apache License 2.0.
* javassist.jar - Mozilla Public License 1.1. 
* log4j-1.2.9.jar - Apache License 2.0.
* reflections-0.9.2.jar - GNU Lesser General Public License

[GivWenZen]: <http://code.google.com/p/givwenzen> "GivWenZen"
[BDD]: <http://behaviour-driven.org/> "BDD"
[Given When Then]: <http://wiki.github.com/aslakhellesoy/cucumber/given-when-then> "Given When Then"
[Cucumber]: <http://cukes.info/> "Cucumber"
[get the words right]: <http://behaviour-driven.org/GettingTheWordsRight> "Get The Words Right"
[ubiquitous language]: <http://behaviour-driven.org/UbiquitousLanguage> "Ubiquitous Language"
[Fitnesse]: <http://fitnesse.org/> "FitNesse"
[Getting Started]: <http://code.google.com/p/givwenzen/wiki/GettingStarted> "Getting Started"
[FitNesseRoot]: <FitNesseRoot/> "FitNesseRoot"
