![](https://raw.githubusercontent.com/weswilliams/GivWenZen/master/givwenzen.png)

[Downloads](https://drive.google.com/folderview?id=0B-8PJf6aZshNUW1yRUtidXhoY3c&usp=sharing)

[GivWenZen](https://github.com/weswilliams/GivWenZen) allows a user to use the [BDD](http://behaviour-driven.org/) [Given When Then](http://wiki.github.com/aslakhellesoy/cucumber/given-when-then) vocabulary and plain text sentences to help a team [get the words right](http://behaviour-driven.org/GettingTheWordsRight) and create a [ubiquitous language](http://behaviour-driven.org/UbiquitousLanguage) to describe and test a business domain.

#The Idea

The idea was taken from [Cucumber](http://cukes.info/) and my desire to create [Cucumber](http://cukes.info/) like specifications and tests in [FitNesse](http://fitnesse.org/). Like [Cucumber](http://cukes.info/), [GivWenZen](https://github.com/weswilliams/GivWenZen) does not distinguish between [Given When Then](http://wiki.github.com/aslakhellesoy/cucumber/given-when-then) or and but you should. Regular expression parsing is used to determine what step method should be executed for each given when then step in a test. Regular expression parsing is also used to find parameters to the step method. Also see how other ideas from [Cucumber](http://cukes.info/) map to [FitNesse](http://fitnesse.org/).

#Example
#### 1) Start with an example [fixture](https://github.com/weswilliams/GivWenZen/blob/master/src/main/java/org/givwenzen/GivWenZenForSlim.java) actual class found in the givwenzen_test.jar.

#### 2) In a [FitNesse](http://fitnesse.org/) table it could look like this.

##### import and start should go in [SetUp](http://fitnesse.org/FitNesse.UserGuide.SpecialPages) or [SuiteSetUp](http://fitnesse.org/FitNesse.UserGuide.SpecialPages)

```
|import|
|org.givwenzen|

|script|
|start|giv wen zen for slim|
```
##### this is your test 
```
|script|
| given| a ToDo item is due tomorrow |
| when | the date changes to tomorrow  |
| then | a notification exists indicating the ToDo is due |
```
#### 3) The following is an example step class and test step method ===
```
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
```
-----

#More Examples
More example can be found in the [FitNesseRoot](https://github.com/weswilliams/GivWenZen/tree/master/src/examples) in source.  It is easy to [get started](https://github.com/weswilliams/GivWenZen/wiki).

[Simple scenarios](https://github.com/weswilliams/GivWenZen/tree/master/FitNesseRoot/GivWenZenTests/SimpleStepScenario)
[More complex scenarios](https://github.com/weswilliams/GivWenZen/tree/master/FitNesseRoot/GivWenZenTests/ComplexStepScenario)

##Groups

http://groups.google.com/group/givwenzen_user

http://groups.google.com/group/givwenzen_developer

<p style="margin-top:10px; margin-bottom:0; padding-bottom:0; text-align:center; line-height:0"><a target="_blank" href="http://feeds.feedburner.com/~r/weswilliams/~6/1"><img src="http://feeds.feedburner.com/weswilliams.1.gif" alt="WesWilliams" style="border:0">

<a href="http://www.atlassian.com/software/clover" title="Atlassian Clover"><img border="0" width="180" height="60" alt="Atlassian Clover" src="http://www.atlassian.com/software/clover/images/badges/v1/lg_clover.png">

#Running
```
java -jar ./lib/fitnesse.jar -p 8080
```
Open http://localhost:8080/ in the browser of your choice.

Here you will find an example test and a user guide for GivWenZen.

##License
```
License: MIT.

Libraries required for main classes:

commons-logging-1.2.jar - Apache License 2.0.
guava-27.1-jre.jar - Apache License 2.0.
javassist.jar - Apache License 2.0., LGPL 2.1, Mozilla Public License 1.1. 
log4j-1.2.17.jar - Apache License 2.0.
```
