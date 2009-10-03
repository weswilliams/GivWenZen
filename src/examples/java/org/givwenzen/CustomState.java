package org.givwenzen;

/**
 * Though i think it would be better to call other steps to get data you need. 
 * it can be nice to have a state shared between step classes.
 * this could also be done by simply have the state retrieved from another step:
 * 
 * @DomainStep("retrieve some value")
 * public String retrieveSomeValue() {
 *   return "some value";
 * }
 */
public class CustomState {
  public String someValue() {
    return "some value";
  }
}
