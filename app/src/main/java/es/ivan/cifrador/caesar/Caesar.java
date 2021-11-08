package es.ivan.cifrador.caesar;

public class Caesar {

    private final String alphabet = "abcdefghijklmnñopqrstuvwxyz";

    /**
     * Método para cifrar el texto. Usa el algoritmo Cesar, es decir, que la letra se sustituye por otra dependiendo de los espacios movidos
     *
     * @param text  El texto a ser cifrado
     * @param moves La cantidad de posiciones que para encriptar
     * @return El texto encriptado
     */
    public String encrypt(String text, int moves) {
        final StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c == ' ') {
                // Si hay un espacio no queremos que lo encripte
                sb.append(' ');
                continue;
            }
            final boolean isUpper = Character.isUpperCase(c);
            c = Character.toLowerCase(c);
            final int index = this.alphabet.indexOf(c);
            char newLetter;

            if (index + moves >= this.alphabet.length()) {
                // En negativo para obtener si o si el resultado de la operación en positivo
                final int offSet = -this.alphabet.length() + (index + moves);
                newLetter = this.alphabet.charAt(offSet);
            } else {
                newLetter = this.alphabet.charAt(index + moves);
            }
            sb.append(isUpper ? Character.toUpperCase(newLetter) : newLetter);
        }
        return sb.toString();
    }

    /**
     * Método para descifrar el texto. Usa el algoritmo Cesar, es decir, que la letra se sustituye por otra dependiendo de los espacios movidos
     *
     * @param text El texto a ser descifrado
     * @param moves La cantidad de posiciones que para desencriptar
     * @return El texto desencriptado
     */
    public String decrypt(String text, int moves) {
        final StringBuilder sb = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (c == ' ') {
                // Si hay un espacio no queremos que lo encripte
                sb.append(' ');
                continue;
            }
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
