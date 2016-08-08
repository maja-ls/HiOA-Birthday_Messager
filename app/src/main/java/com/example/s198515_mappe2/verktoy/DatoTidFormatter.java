package com.example.s198515_mappe2.verktoy;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Verktøyklasse som formatterer datoer og tider
 */
public class DatoTidFormatter {

    private static final String MYDEBUG = "DatoTidFormatter.java";



    private static final SimpleDateFormat DATO_FORMATTER_FOR_MASKIN = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private static final SimpleDateFormat DATO_UTEN_AAR_FORMATTER_FOR_MASKIN = new SimpleDateFormat("MM-dd", Locale.getDefault());

    private static final SimpleDateFormat DATO_UTEN_AAR_FORMATTER_FOR_MENNESKE = new SimpleDateFormat("dd-MM", Locale.getDefault());

    private static final SimpleDateFormat DATO_FORMATTER_FOR_MENNESKE = new SimpleDateFormat("dd. MMM yyyy", Locale.getDefault());

    private static final SimpleDateFormat TID_FORMATTER = new SimpleDateFormat("HH:mm", Locale.getDefault());



    public static SimpleDateFormat getDatoFormatterForMaskin() {
        return DATO_FORMATTER_FOR_MASKIN;
    }

    public static SimpleDateFormat getDatoFormatterForMenneske() {
        return DATO_FORMATTER_FOR_MENNESKE;
    }

    public static SimpleDateFormat getTidFormatter() {
        return TID_FORMATTER;
    }

    // Returnerer en dato i form av string som er formatert for lettest å brukes av en maskin
    public static String getStringDatoForMaskin(Calendar cal) {
        String dato;
        dato = DatoTidFormatter.DATO_FORMATTER_FOR_MASKIN.format(cal.getTime());

        return dato;
    }

    // Returnerer en dato uten år i form av string som er formatert for lettest å brukes av en maskin
    public static String getStringDatoUtenAArForMaskin(Calendar cal) {
        String dato;
        dato = DatoTidFormatter.DATO_UTEN_AAR_FORMATTER_FOR_MASKIN.format(cal.getTime());

        return dato;
    }

    // Returnerer en dato uten år i form av string som er formatert for lettest å brukes av en maskin
    public static String getStringDatoUtenAArForMenneske(Calendar cal) {
        String dato;
        dato = DatoTidFormatter.DATO_UTEN_AAR_FORMATTER_FOR_MENNESKE.format(cal.getTime());

        return dato;
    }

    // Returnerer en  dato i form av string som er formatert for å være lettlest for et menneske
    public static String getStringDatoForMenneske(Calendar cal) {
        String dato;
        dato = DatoTidFormatter.DATO_FORMATTER_FOR_MENNESKE.format(cal.getTime());

        return dato;
    }

    // Returnerer en  dato i form av  calendar object fra en string
    public static Calendar getCalendarDatoFraStringMaskin(String dato) {

        Calendar cal = Calendar.getInstance();
        //Har en try her fordi DATO_FORMATTER_FOR_MASKIN.parse krever det
        try {
            cal.setTime(DatoTidFormatter.DATO_FORMATTER_FOR_MASKIN.parse(dato));
        }
        catch (Exception e) {
            Log.d(MYDEBUG, "\n********\n\n Det skjedde en feil ved konvertering fra String til Calendar *******");
            return null;
        }

        return cal;
    }


    // Returnerer et tidspunkt i form av string som er formatert for å være lettlest for et menneske
    public static String getStringTidFraCal (Calendar cal) {
        String tid;
        tid = DatoTidFormatter.TID_FORMATTER.format(cal.getTime());

        return tid;
    }


    // Returnerer et tidspunkt i form av calendar object fra en string
    public static Calendar getCalTidFraString(String tidspunkt) {

        Calendar cal = Calendar.getInstance();
        //Har en try her fordi TID_FORMATTER.parse krever det
        try {
            cal.setTime(DatoTidFormatter.TID_FORMATTER.parse(tidspunkt));
        }
        catch (Exception e) {
            Log.d(MYDEBUG, "\n********\n\n Det skjedde en feil ved konvertering fra String til Calendar *******");
            return null;
        }

        return cal;
    }

}
