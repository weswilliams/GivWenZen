package org.givwenzen.experimental;

import static org.junit.Assert.*;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class Steps {

    private boolean happy;
    private int cupsBought;
    private boolean coffeeDrank;

    @DomainStep("i go to the coffee shop")
    public void iGoToShop() {
        happy = true;
    }
    
    @DomainStep("i buy (.*) cups of coffee")
    public void iBuy(int cups) {
        cupsBought = cups;
    }
    
    @DomainStep("i am happy")
    public void iAmInShop() {
        assertTrue(happy);
    }
    
    @DomainStep("i drink coffee")
    public void drink() {
        coffeeDrank = true;
    }
    
    @DomainStep("i feel as if I drank (.*) cups of coffee")
    public void howIFeel(int cups) {
        assertTrue(coffeeDrank);
        assertEquals(cupsBought, cups);
    }
}