package es.ivan.cifrador.utils;

public class Caesar {

    public String encrypt(String text, int offSet) {
        final StringBuilder cifrado = new StringBuilder();

        offSet = offSet % 26;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) >= 'a' && text.charAt(i) <= 'z') {
                if ((text.charAt(i) + offSet) > 'z') {
                    cifrado.append((char) (text.charAt(i) + offSet - 26));
                } else {
                    cifrado.append((char) (text.charAt(i) + offSet));
                }
            } else if (text.charAt(i) >= 'A' && text.charAt(i) <= 'Z') {
                if ((text.charAt(i) + offSet) > 'Z') {
                    cifrado.append((char) (text.charAt(i) + offSet - 26));
                } else {
                    cifrado.append((char) (text.charAt(i) + offSet));
                }
            }
        }
        return cifrado.toString();
    }

    public String decrypt(String text, int offSet) {
        final StringBuilder cifrado = new StringBuilder();

        offSet = offSet % 26;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) >= 'a' && text.charAt(i) <= 'z') {
                if ((text.charAt(i) - offSet) < 'a') {
                    cifrado.append((char) (text.charAt(i) - offSet + 26));
                } else {
                    cifrado.append((char) (text.charAt(i) - offSet));
                }
            } else if (text.charAt(i) >= 'A' && text.charAt(i) <= 'Z') {
                if ((text.charAt(i) - offSet) < 'A') {
                    cifrado.append((char) (text.charAt(i) - offSet + 26));
                } else {
                    cifrado.append((char) (text.charAt(i) - offSet));
                }
            }
        }
        return cifrado.toString();
    }
}
