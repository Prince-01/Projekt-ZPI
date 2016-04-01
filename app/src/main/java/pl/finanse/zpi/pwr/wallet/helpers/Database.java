package pl.finanse.zpi.pwr.wallet.helpers;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import pl.finanse.zpi.pwr.wallet.model.Position;

/**
 * Created by Kamil on 01.04.2016.
 */
public class Database {
    private static SQLiteOpenHelper dbConn;//trzyma polaczenie z baza danych
    private static SQLiteDatabase db;
    public static final String DATABASE_NAME = "pl.finanse.zpi.pwr.wallet - database";

    /**
     * otwiera polaczenie z baza danych, musi byc wywolane na samym poczatku
     *
     * @param context kontekst aplikacji
     * @return true gdy sie udalo uzyskac polaczenie, flase gdy nie
     */
    public static boolean Open(Context context) {
        dbConn = new DatabaseHelper(context, DATABASE_NAME, null, 1);
        try {
            db = dbConn.getWritableDatabase();
            return true;
        } catch (SQLException e) {
            db = dbConn.getReadableDatabase();
        }
        return false;
    }

    /**
     * zamyka polaczenie z baza danych
     */
    public static void Close() {
        dbConn.close();
    }

    public static void AddNewPosition(Position position) {

    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        /**
         * tworzy nam baze danych jesli nie istnieje
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            String create = "CREATE TABLE IF NOT EXISTS Kategorie (Nazwa varchar(255) NOT NULL, NazwaNadkategorii varchar(255), PRIMARY KEY (Nazwa), FOREIGN KEY(NazwaNadkategorii) REFERENCES Kategorie(Nazwa));" +
                    "CREATE TABLE ListyZakupow (IdListy  INTEGER NOT NULL PRIMARY KEY, Nazwa varchar(255) NOT NULL UNIQUE, Pozycje varchar(4095) NOT NULL, CzyKupiono varchar(1023) NOT NULL, CzyUkryte blob, PozycjeIdPozycji integer(10) NOT NULL, FOREIGN KEY(PozycjeIdPozycji) REFERENCES Pozycje(IdPozycji));" +
                    "CREATE TABLE Porady (IdPorady  INTEGER NOT NULL PRIMARY KEY, Nazwa varchar(255) NOT NULL, Link varchar(255) NOT NULL);" +
                    "CREATE TABLE Portfele (Nazwa varchar(255) NOT NULL, Stan double(10) NOT NULL, Waluta varchar(3) DEFAULT 'PLN' NOT NULL, PRIMARY KEY (Nazwa));" +
                    "CREATE TABLE Pozycje (IdPozycji  INTEGER NOT NULL PRIMARY KEY, Nazwa varchar(255), Wartosc double(20), Data date, Komentarz integer(511), CzyPrzychod blob NOT NULL, CzyUlubiona blob NOT NULL, CzyStale blob NOT NULL, KategorieNazwa varchar(255) NOT NULL, PortfeleNazwa varchar(255) NOT NULL, ListyZakupowIdListy integer(10), FOREIGN KEY(KategorieNazwa) REFERENCES Kategorie(Nazwa), FOREIGN KEY(PortfeleNazwa) REFERENCES Portfele(Nazwa));" +
                    "CREATE TABLE ZleceniaStale (Nazwa varchar(255) NOT NULL, DataOd date NOT NULL, DataDo date, PozycjeIdPozycji integer(10) NOT NULL, Cyklicznosc integer(10) NOT NULL, Czestotliwosc integer(10) NOT NULL, DniCyklicznosci varchar(127), PRIMARY KEY (Nazwa), FOREIGN KEY(PozycjeIdPozycji) REFERENCES Pozycje(IdPozycji));" +
                    "CREATE UNIQUE INDEX ListyZakupow_IdListy ON ListyZakupow (IdListy);" +
                    "CREATE UNIQUE INDEX Porady_IdPorady ON Porady (IdPorady);" +
                    "CREATE UNIQUE INDEX Pozycje_IdPozycji ON Pozycje (IdPozycji);";
            db.execSQL(create);
        }

        /**
         * nieuzywana bo nasza baza ma tylko 1 wersje i nie przewidujemy zmian
         *
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }

    }
}
