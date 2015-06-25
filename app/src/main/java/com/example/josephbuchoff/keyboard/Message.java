package com.example.josephbuchoff.keyboard;

/**
 * Created by blazi_000 on 6/10/2015.
 */
public class Message {
    // Classwide Constants
    private static final String ALPHANUM = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 `,./;'[]\\~!@#$%^&*()}{+_\":?><-=";
    private static final int ALPHANUM_LENGTH = ALPHANUM.length();

    // Instance Fields
    private String pass;
    private String enc;
    private String key;

    // General Constructor
    public Message(String plainText, String password)
    {
//        this.key = genKey();
        this.key = "S\\\"?GW;{FdyoeHsJUc.niB^ whZtM#RrzVl)'64Yq0I_}\\8<O!+C7aL$`pk=*uPN-v5E[A%KX&>f391jx~:@/,T(g]QbmD2";  // Andrea's Key
//        this.key = "{]Nev!\"la~=dsh;Bxyk&>D/6)cP*m,iwQ gKoA$4LXUM+nF9-tGC7qbpOr:Z?H[f0J_\\^.'8<3%SI#VRuT1z}E(@Y`W5j2";  // Melissa's key
//        this.key = ":@Vm\\SNt0J/1Q#p}nlePj ;=dIBZ2RHbW`)Ty[r3saq]!w-MFkX'zOAxo&$U?Ccu.*<iG\">LK4E7~Y(%h5v9+fg6{^8D_,";  // Ramya's key
//        this.key = "beUl\\];y@29u('oTawHMYEKAI\"tSg8DFL3Cmj/+Vh[#s>-nkv`^pcR%B}7W!5J{QqfP0~,$)=1XrdN_OiZ*6x4G.&z:? <"; // Fola's key
//        this.key = "<3Z.i~p_Hvog=!@ESBCG6suU[r:Mtc;VWaXI${?mb^kf&j>]ey72JKqQ8P0%#*d\\`A '/1,\"}nNl5(+DFRLz94x)Th-YwO"; // Ferret's key

        setPass(password);
        this.enc = encode(plainText);
    }

    // Constructor without password
    public Message (String plainText)
    {
        this(plainText, "coolbeans");
    }

    // Empty constructor for initialization without values
    public Message ()
    {
        this("","coolbeans");
    }

    // Private methods

    /*  genKey
        Takes no arguments
        Returns a String
        Generates a pseudorandom assembly of most used characters and returns it as a String
     */
    private String genKey()
    {
        StringBuilder localAN = new StringBuilder(ALPHANUM);    // Modifiable copy of ALPHANUM
        StringBuilder localKey = new StringBuilder();           // StringBuilder for the Key
        int rand;

        for (int i = 0; i < ALPHANUM_LENGTH; i++) { // Take a random character in the list of chars, append it to the key and delete it from the list of chars until we have a full key
            rand = (int)(Math.random()*100)%(ALPHANUM_LENGTH-i);
            localKey.append(localAN.charAt(rand));
            localAN.deleteCharAt(rand);
        }

        return localKey.toString();
    }

    /*  encode
        Takes a String as a parameter
        Returns a String
        Encodes the String using the key and returns the encoded text
     */
    private String encode (String message)
    {
        StringBuilder encBuilder = new StringBuilder();
        int len = message.length();

        for (int i = 0; i < len; i++) {
            encBuilder.append(encodeChar(message.charAt(i)));   // Encode the current char and append it to the StringBuilder for the encoded message
        }

        return encBuilder.toString();
    }

    /*  encodeChar
        takes a char as a parameter
        returns a char
        Encodes a single char with the key and returns the encoded char
     */
    private char encodeChar (char c)
    {
        int index = ALPHANUM.indexOf(c);    // Set index to the position in the list of characters of char c ex. 'a' = 1, 'b' = 2...

        return key.charAt(index);
    }

    // Public methods

    /*  decode
        Takes a String as a parameter
        Returns a String
        Checks the password for congruence then decodes the message if it matches
     */
    public String decode (String password)
    {
        StringBuilder decBuilder = new StringBuilder();

        if (enc.equals("") || enc == null) {
            return "There was no message to be decoded.";
        }
        if (!password.equals(pass)) {
            return "Your password wasn't correct.";
        }

        int len = enc.length();    // The length of the encoded message

        for (int i = 0; i < len; i++) {
            decBuilder.append(decodeChar(enc.charAt(i)));  // Append to the builder the decoded character at i
        }

        return decBuilder.toString();
    }

    /*  decodeChar (char)
        takes a char as a parameter
        returns a char
        Decodes the char and returns it
     */
    public char decodeChar (char c)
    {
        int index = key.indexOf(c); // Set the index to the place in key where char c lies

        return ALPHANUM.charAt(index);
    }
    /*  decodeChar (String)
        takes a String as a parameter
        returns a char
        Decodes the first character in the String and returns it
     */
    public char decodeChar (String s)
    {
        return decodeChar(s.charAt(0));
    }

    /*  setMessage
        takes a String as a parameter
        returns null
        Setter for enc from plain text
    */
    public void setMessage (String message)
    {
        this.enc = encode(message);

        return;
    }

    /*  setEnc
        takes a String as a parameter
        returns null
        Setter for enc from encoded text
    */
    public void setEnc (String message)
    {
        this.enc = message;

        return;
    }

    /*  setPass
        takes a String as a parameter
        returns nothing
        Setter for Password
     */
    public void setPass (String password)
    {
        this.pass = password;

        return;
    }

    /*  append
        takes a char as a parameter
        returns a char
        Encode and append a single char and returns the encoded char
     */
    public char append (char c)
    {
        StringBuilder encBuilder = new StringBuilder(enc);

        char encC = encodeChar(c);  // Encode char c
        encBuilder.append(encC);   // Append the encoded char to the StringBuilder
        enc = encBuilder.toString();    // Set enc to the StringBuilder's contents

        return encC;
    }
    /*  append
        takes a String as a parameter
        returns a char
        Encode and append the first char of the String and returns the encoded char
     */
    public char append (String s)
    {
        return append(s.charAt(0));
    }

    // Overriding toString() to return the encoded message
    public String toString() {
        return enc;
    }
}