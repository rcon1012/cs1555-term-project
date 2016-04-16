import java.lang.StringBuilder;

public class PrettyPrinter {
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final String hr = "------------------------------------------------------------------------";
    private String divToken = "-";
    public PrettyPrinter(){}

    public PrettyPrinter(String newToken){
        divToken = newToken;
    }

    public String buildLine(String token, int maxLen) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxLen / token.length(); i++) {
            sb.append(token);
        }
        return sb.toString();
    }

    /*
    Method derived from SO thread:
    http://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
 */
    private static String fromBytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void displayDivider() {
        System.out.println("\n" + hr + "\n");
    }

    public void displayUnderlined(String input) {
        System.out.println(input);
        System.out.println(buildLine("-", input.length()) + "\n");
    }

    public void displayBoxed(String input) {
        System.out.println("\n" + hr + "\n" + input + "\n" + hr + "\n");
    }

    public void displayError(String input) {
        System.out.println("ERROR: " + input);
    }

    public void displaySuccess(String input) {
        System.out.println("SUCCESS: " + input);
    }
}
