package dev.gianniosapostolos.passwordgenerator;


import java.security.SecureRandom;
import java.util.Set;

public class PasswordGenerator {

    int passwordLength;
    String passwordCharacters;
    Set<String> excludedCharacters;
    SecureRandom secureRandom = new SecureRandom();
    StringBuilder myPassword;


    public PasswordGenerator(int passwordLength, String passwordCharacters, Set<String> excludedCharacters) {
        this.passwordLength = passwordLength;
        this.passwordCharacters = passwordCharacters;
        this.excludedCharacters = excludedCharacters;

    }


    public String GeneratePassword() {
        myPassword = new StringBuilder(passwordLength);
        int index;
        char chosenChar;

        for (int i = 0; i < passwordLength; i++) {
            index = secureRandom.nextInt(passwordCharacters.length());
            chosenChar = passwordCharacters.charAt(index);
            if (excludedCharacters.contains(Character.toString(chosenChar)))
                i--;
            else
                myPassword.append(chosenChar);
        }
        return myPassword.toString();
    }
}