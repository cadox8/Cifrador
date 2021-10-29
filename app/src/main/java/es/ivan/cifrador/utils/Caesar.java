package es.ivan.cifrador.utils;

public class Caesar {

    private final String alphabet = "abcdefghijklmnñopqrstuvwxyz";

    public String encrypt(String text, int moves) {
        final StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {
            final boolean isUpper = Character.isUpperCase(c);
            c = Character.toLowerCase(c);
            final int index = this.alphabet.indexOf(c);
            char newLetter;

            if (index + moves >= this.alphabet.length()) {
                final int offSet = -this.alphabet.length() + (index + moves);
                newLetter = this.alphabet.charAt(offSet);
            } else {
                newLetter = this.alphabet.charAt(index + moves);
            }
            sb.append(isUpper ? Character.toUpperCase(newLetter) : newLetter);
        }
        return sb.toString();
    }

    public String decrypt(String text, int moves) {
        final StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {
            final boolean isUpper = Character.isUpperCase(c);
            c = Character.toLowerCase(c);
            final int index = this.alphabet.indexOf(c);
            char newLetter;

            if (index - moves < 0) {
                final int offSet = this.alphabet.length() + (index - moves);
                newLetter = this.alphabet.charAt(offSet);
            } else {
                newLetter = this.alphabet.charAt(index - moves);
            }
            sb.append(isUpper ? Character.toUpperCase(newLetter) : newLetter);
        }
        return sb.toString();
    }
}
