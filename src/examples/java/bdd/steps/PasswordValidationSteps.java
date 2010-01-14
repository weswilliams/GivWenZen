package bdd.steps;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class PasswordValidationSteps {

    private static final String START_OF_LINE = "^";
    private static final String ANY_CHARS_AND_END_OF_LINE = ".*$";
    private static final String AT_LEAST_ONE_ALPHA_CHAR = "(?=.*[a-zA-Z])";
    private static final String AT_LEAST_ONE_SPECIAL_CHAR = "(?=.*\\W)";
    private static final String AT_LEAST_ONE_DIGIT = "(?=.*\\d)";
    private static final String NO_MORE_THAN_16_CHARS = "(?!.{17,}$)";
    private static final String AT_LEAST_6_CHARS = "(?=.{6,}$)";
    private static final String DOES_NOT_CONTAIN_SPACES = "(?!.* )";
    private String passwordToVerify = "";

    @DomainStep("a new account is created with a password of (.*)")
    public String createAccountWithPassword(String password) {
        changePasswordForExistingAccount(password);
        return password;
    }
    
    @DomainStep("the password is changed to (.*)")
    public void changePasswordForExistingAccount(String newPassword) {
        this.passwordToVerify = newPassword == null ? "" : newPassword;
    }
    
    @DomainStep("the password is accepted because it meets the security requirements")
    public boolean passwordMeetsSecurityRequirements() {
      return isValidPassword();
    }
    
    @DomainStep("the password is rejected (?:.*)")
    public boolean isInvalidPassword() {
        return !isValidPassword();
    }
    
    @DomainStep("the password is valid (?:.*)")
    public boolean isValidPassword() {
        System.out.println(passwordToVerify);
        return passwordToVerify.matches( 
                        START_OF_LINE +
                        AT_LEAST_6_CHARS + 
                        NO_MORE_THAN_16_CHARS + 
                        AT_LEAST_ONE_DIGIT + 
                        AT_LEAST_ONE_SPECIAL_CHAR + 
                        AT_LEAST_ONE_ALPHA_CHAR + 
                        DOES_NOT_CONTAIN_SPACES +
                        ANY_CHARS_AND_END_OF_LINE);
    }

    @DomainStep("the new account is not created")
    public boolean newAccountIsNotCreated() {
        return true;
    }
}
