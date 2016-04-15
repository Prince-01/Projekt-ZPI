package pl.finanse.zpi.pwr.wallet.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import java.util.LinkedList;
import java.util.List;

import pl.finanse.zpi.pwr.wallet.model.Position;
import pl.finanse.zpi.pwr.wallet.view.Category;

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

    /**
     * pobiera tablice kategorii z bazy danych na podstawie ich nadkategorii
     * zeby uzyskac wszystkie glowne kategorie, jako parametr podajemy nulla
     * @param catName
     * @return tablica kategorii z bazy danych
     */
    public static Category[] GetCategories(Context context,String catName){


        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
//        String query = "SELECT Nazwa FROM Kategorie WHERE NazwaNadkategorii = ?";
//        String[] arr= {catName};
//        Cursor c = db.rawQuery(query,arr);
        String query = null;
        String[] arr = null;
        if(catName == null){
            query = "SELECT Nazwa FROM Kategorie WHERE NazwaNadkategorii IS NULL";
            arr = new String[0];
        }else{
            query = "SELECT Nazwa FROM Kategorie WHERE NazwaNadkategorii = ?";
            arr = new String[]{catName};

        }
        String[] col = {"Nazwa"};
        //Cursor c = db.query("Kategorie",col,"NazwaNadkategorii = '"+catName+"'",null,null,null,null);
        Cursor c = db.rawQuery(query,arr);
        int index = c.getColumnIndex("Nazwa");
        Category[] cat = new Category[ c.getCount()];
        Toast.makeText(context,""+c.getCount(),Toast.LENGTH_SHORT).show();
        int i=0;
        while(c.moveToNext()){
            cat[i++] = new Category(c.getString(index),0);
        }
        Close();
        return cat;
    }
    public static void AddNewCategory(Context context, Category cat,String nadKategoria) {
        if(nadKategoria == null)
            nadKategoria = "NULL";
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = "INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii','IdObrazka') VALUES (?,?,?)";
        String[] arr= {cat.categoryName,nadKategoria,Integer.toString(cat.icon)};
        db.rawQuery(query,arr);
        Close();
    }
    public static void RemoveCategory(Context context, Category kategoria) {
        if(kategoria == null)
            throw new RuntimeException("Spoko takie usuwanie NULLa");
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = "UPDATE Kategorie SET CzyUsunieto=1 WHERE Nazwa = ?;";
        String[] arr= {kategoria.categoryName};
        db.rawQuery(query,arr);
        Close();
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {

        private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        //wywolyane tylko raz, podczas tworzenia bazy danych
        private void CreateDataForFirstUse(SQLiteDatabase db){
            //tutaj tworzenie kategorii wbudowanych
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Spozywka',NULL); ");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Samochod',NULL); ");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Inne',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Testowa','Inne');");
        }

        /**
         * tworzy nam baze danych jesli nie istnieje
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            String create =
                    "CREATE TABLE IF NOT EXISTS Kategorie (Nazwa varchar(255) NOT NULL, NazwaNadkategorii varchar(255), CzyUsuniete blob DEFAULT '0' NOT NULL, IdObrazka integer(20), PRIMARY KEY (Nazwa), FOREIGN KEY(NazwaNadkategorii) REFERENCES Kategorie(Nazwa));" +
                    "CREATE TABLE IF NOT EXISTS ListyZakupow (IdListy  INTEGER NOT NULL PRIMARY KEY, Nazwa varchar(255) NOT NULL UNIQUE, Pozycje varchar(4095) NOT NULL, CzyKupiono varchar(1023) NOT NULL, CzyUkryte blob, PozycjeIdPozycji integer(10) NOT NULL, FOREIGN KEY(PozycjeIdPozycji) REFERENCES Pozycje(IdPozycji));" +
                    "CREATE TABLE IF NOT EXISTS Porady (IdPorady  INTEGER NOT NULL PRIMARY KEY, Nazwa varchar(255) NOT NULL, Link varchar(255) NOT NULL);" +
                    "CREATE TABLE IF NOT EXISTS Portfele (Nazwa varchar(255) NOT NULL, Stan double(10) NOT NULL, Waluta varchar(3) DEFAULT 'PLN' NOT NULL, PRIMARY KEY (Nazwa));" +
                    "CREATE TABLE IF NOT EXISTS Pozycje (IdPozycji  INTEGER NOT NULL PRIMARY KEY, Nazwa varchar(255), Wartosc double(20), Data date, Komentarz integer(511), CzyPrzychod blob NOT NULL, CzyUlubiona blob NOT NULL, CzyStale blob NOT NULL, KategorieNazwa varchar(255) NOT NULL, PortfeleNazwa varchar(255) NOT NULL, ListyZakupowIdListy integer(10), FOREIGN KEY(KategorieNazwa) REFERENCES Kategorie(Nazwa), FOREIGN KEY(PortfeleNazwa) REFERENCES Portfele(Nazwa));" +
                    "CREATE TABLE IF NOT EXISTS ZleceniaStale (Nazwa varchar(255) NOT NULL, DataOd date NOT NULL, DataDo date, PozycjeIdPozycji integer(10) NOT NULL, Cyklicznosc integer(10) NOT NULL, Czestotliwosc integer(10) NOT NULL, DniCyklicznosci varchar(127), PRIMARY KEY (Nazwa), FOREIGN KEY(PozycjeIdPozycji) REFERENCES Pozycje(IdPozycji));" +
                    "CREATE UNIQUE INDEX ListyZakupow_IdListy ON ListyZakupow (IdListy);" +
                    "CREATE UNIQUE INDEX Porady_IdPorady ON Porady (IdPorady);" +
                    "CREATE UNIQUE INDEX Pozycje_IdPozycji ON Pozycje (IdPozycji);";
            db.execSQL(create);
            CreateDataForFirstUse(db);
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
