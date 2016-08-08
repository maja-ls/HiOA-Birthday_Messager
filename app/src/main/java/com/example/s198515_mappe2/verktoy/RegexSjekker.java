package com.example.s198515_mappe2.verktoy;

/**
 * Verktøyklasse som sjekker stringer den mottar
 */
public class RegexSjekker {

    // Regex som skal funke på internasjonale telefonnummer, med eller uten + eller 00 foran
    private final static String telefonPattern = "(\\+|00)?(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$";



    public static String getTelefonPattern() {
        return telefonPattern;
    }


    public static boolean sjekkTlfRegex (String tlf) {
        boolean matcher = tlf.matches(telefonPattern);

        return matcher;
    }

    public static boolean sjekkStringHarSynligChars (String string) {
        boolean harSynligChars = true;

        if (string == null) {
            harSynligChars = false;
        }
        else {
            string = string.trim();

            if (string.isEmpty())
                harSynligChars = false;
        }

        return harSynligChars;
    }
}
