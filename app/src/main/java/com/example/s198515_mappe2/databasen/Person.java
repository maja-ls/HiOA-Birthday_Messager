package com.example.s198515_mappe2.databasen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

import com.example.s198515_mappe2.verktoy.DatoTidFormatter;

/**
 * Objektklasse som representerer en person
 */
public class Person implements Parcelable {

    private static final String MYDEBUG = "Person.java";


    private int id;

    private String fornavn;

    private String etternavn;

    private String melding;

    private String telefon;

    // Denne kunne muligens vært en string, men Calendar er sterkere å operere mot ved sammenligning etc
    private Calendar bursdag;


    // Tom konstruktør
    public Person() {
    }

    // Konstruktør som tar alle verdiene unntatt id og bursdag
    public Person(String fornavn, String etternavn, String melding, String telefon) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.melding = melding;
        this.telefon = telefon;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMelding() {
        return melding;
    }

    public void setMelding(String melding) {
        this.melding = melding;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public void setEtternavn(String etternavn) {
        this.etternavn = etternavn;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    // Returnerer en string som er formatert for lettest å brukes av en maskin
    public String getBursdagForMaskin() {
        String bursdagen;
        bursdagen = DatoTidFormatter.getStringDatoForMaskin(bursdag);

        return bursdagen;
    }

    // Returnerer en string som er formatert for å være lettlest for et menneske
    public String getBursdagForMenneske() {
        String bursdagen;
        bursdagen = DatoTidFormatter.getStringDatoForMenneske(bursdag);

        return bursdagen;
    }

    public Calendar getBursdagCalendar() {
        return bursdag;
    }

    public void setBursdagMedStringForMaskin(String bursdagen) {
        bursdag = DatoTidFormatter.getCalendarDatoFraStringMaskin(bursdagen);
    }

    public void setBursdagMedCal(Calendar bursdagen) {
        bursdag = bursdagen;
    }



    // Konstruktør for parcelable
    private Person(Parcel in) {
        fornavn = in.readString();
        etternavn = in.readString();
        melding = in.readString();
        telefon = in.readString();
        setBursdagMedStringForMaskin(in.readString());
    }

    // Verdiene som skal lagres i parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fornavn);
        dest.writeString(etternavn);
        dest.writeString(melding);
        dest.writeString(telefon);
        dest.writeString(getBursdagForMaskin());
    }

    // Obligatorisk metode for parcelable
    @Override
    public int describeContents() {
        return 0;
    }


    // Konstant  som brukes i opprettelsen fra parcel
    public static final Parcelable.Creator<Person> CREATOR =
            new Parcelable.Creator<Person>() {

                @Override
                public Person createFromParcel(Parcel source) {
                    return new Person(source);
                }

                @Override
                public Person[] newArray(int size) {
                    return new Person[0];
                }
            };


}
