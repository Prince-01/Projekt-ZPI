package pl.finanse.zpi.pwr.wallet.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import pl.finanse.zpi.pwr.wallet.MainActivity;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kamil on 01.04.2016.
 * NIE WYWOŁYWAĆ KURWA ŻADEJ METODY Z DATABASE W METODACH Z DATABASE, BO TO ŚMIERĆ!!!!!!!!!!!!
 * nie dotyczy to open'a i close'a, ktore musza byc wywolane na poczatku i koncu kazdej metody z database
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
            query = "SELECT Nazwa FROM Kategorie WHERE NazwaNadkategorii = ? ";
            arr = new String[]{catName};

        }
        String[] col = {"Nazwa"};
        //Cursor c = db.query("Kategorie",col,"NazwaNadkategorii = '"+catName+"'",null,null,null,null);
        Cursor c = db.rawQuery(query, arr);
        int index = c.getColumnIndex("Nazwa");
        Category[] cat = new Category[ c.getCount()];
        //Toast.makeText(context,""+c.getCount(),Toast.LENGTH_SHORT).show();
        int i=0;
        while(c.moveToNext()){
            cat[i++] = new Category(c.getString(index),0, arr.length == 0 ? "" : arr[0]);
        }
        Close();
        return cat;
    }

    public static Category[] GetAllCategories(Context context){
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = null;
        query = "SELECT Nazwa, NazwaNadkategorii FROM Kategorie";
        Cursor c = db.rawQuery(query,null);
        int nazIndex = c.getColumnIndex("Nazwa");
        int nazNadkatInd = c.getColumnIndex("NazwaNadkategorii");
        Category[] cat = new Category[ c.getCount()];
        int i=0;
        while(c.moveToNext()){
            cat[i++] = new Category(c.getString(nazIndex),0,c.getString(nazNadkatInd));
        }
        Close();
        return cat;
    }
    public static Wallet[] GetAllWallets(Context context){
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = null;
        query = "SELECT Nazwa, Stan, Waluta FROM Portfele";
        Cursor c = db.rawQuery(query,null);
        int nazIndex = c.getColumnIndex("Nazwa");
        int stIndex = c.getColumnIndex("Stan");
        int walIndex = c.getColumnIndex("Waluta");

        Wallet[] wallets = new Wallet[ c.getCount()];
        int i=0;
        while(c.moveToNext()){
            wallets[i++] = new Wallet(c.getString(nazIndex),c.getFloat(stIndex),c.getString(walIndex));
        }
        Close();
        return wallets;
    }

    /**
     * pobiera pozycje dla alktulanego portfela
     * @param context
     * @return
     */
    public static Operation[] GetAllPositions(Context context){
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = null;
        query = "SELECT IdPozycji, Nazwa, Wartosc, Data, CzyPrzychod, KategorieNazwa, PortfeleNazwa FROM Pozycje WHERE PortfeleNazwa = ? ORDER BY Data DESC, IdPozycji DESC";
        String[] arr = {Wallet.GetActiveWallet(context).getName()};
        Cursor c = db.rawQuery(query,arr);
        int idIndex = c.getColumnIndex("IdPozycji");
        int nazIndex = c.getColumnIndex("Nazwa");
        int wartIndex = c.getColumnIndex("Wartosc");
        int datIndex = c.getColumnIndex("Data");
        int przIndex = c.getColumnIndex("CzyPrzychod");
        int knazIndex = c.getColumnIndex("KategorieNazwa");
        int portIndex = c.getColumnIndex("PortfeleNazwa");
        Operation[] operations = new Operation[ c.getCount()];
//        Toast.makeText(context,"Liczba pozycji: "+operations.length,Toast.LENGTH_SHORT).show();
        int i=0;
        while(c.moveToNext()){
            try {
                operations[i++] = new Operation(c.getInt(idIndex),c.getString(portIndex), c.getString(nazIndex),c.getFloat(wartIndex),(new SimpleDateFormat("yyyy-MM-dd")).parse(c.getString(datIndex)), c.getInt(przIndex) == 0 ? false : true, c.getString(knazIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Close();
        return operations;
    }

    /**
     * dodaje nowy portfle do bazy danych
     * @param context
     * @param wallet
     */
    public static void AddNewWallet(Context context, Wallet wallet) {

        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        ContentValues values = new ContentValues();
        values.put("Nazwa", wallet.getName());
        values.put("Stan", wallet.getValue());
        values.put("Waluta", wallet.getCurrency());
        db.insert("Portfele", null, values);
        Close();
    }

    /**
     * dodaje nowa kategorie do bazy danych, w przekazanej kategorii, musi byc usupelnoine pole superCategory
     * @param context
     * @param category
     */
    public static void AddNewCategory(Context context, Category category) {

        if(!Open(context))
        throw new RuntimeException("Blad podczas polaczenia z baza");
        ContentValues values = new ContentValues();
        values.put("Nazwa", category.categoryName);
        values.put("NazwaNadkategorii", category.superCategory);
        values.put("IdObrazka", category.icon);
        db.insert("Kategorie", null, values);
        Close();
    }

    /**
     * zwraca nadkategoire dla podanej kategorii
     * @param cat
     * @return
     */
    public static String GetParentCategory(Context context, String cat){
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = null;
        String[] arr = null;
        if(cat == null){
            throw new NullPointerException("Wrzucasz nulla do wyszukiania śmieciu");
        }else{
            query = "SELECT NazwaNadkategorii FROM Kategorie WHERE Nazwa = ?";
            arr = new String[]{cat};

        }
        String[] col = {"NazwaNadkategorii"};
        //Cursor c = db.query("Kategorie",col,"NazwaNadkategorii = '"+catName+"'",null,null,null,null);
        Cursor c = db.rawQuery(query,arr);
        int index = c.getColumnIndex("NazwaNadkategorii");
        //Toast.makeText(context,""+c.getCount(),Toast.LENGTH_SHORT).show();
        while(c.moveToNext()){
            Close();
            return c.getString(index);
        }
       return null;
    }

    public static void AddQuickNewPosition(Context context, Operation operacja) {

        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        ContentValues values = new ContentValues();
        values.put("Nazwa", operacja.operationName);
        values.put("Wartosc", operacja.cost);
        values.put("Data", (new SimpleDateFormat("yyyy-MM-dd")).format(operacja.date));
        values.put("CzyPrzychod", operacja.isIncome ? 1 : 0);
        values.put("KategorieNazwa", operacja.category);
        values.put("PortfeleNazwa", operacja.wallet);
        db.insert("Pozycje", null, values);
        Close();
    }

    /**
     * Funkcja, która dodaje do stanu wybranego portfela wartość ostatniej operacji
     * @param context
     * @param walletName nazawa portfela
     * @param money wartość ostatniej operacji
     */

    public static void UpdateWalletState(Context context, String walletName, float money) {
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = "UPDATE Portfele SET Stan = Stan + ? WHERE Nazwa = ?;";
        String[] arr= {String.valueOf(money), walletName};
        db.execSQL(query, arr);
        Close();

        Wallet current = Wallet.GetActiveWallet(context);
        if(current.getName().equals(walletName))
            current.UpdateValueBy(money);
    }

    /**
     * "usuowa" podajna kategorie z bazy danych, tak naprawde nie usuow, tylko ja ukrywa polem pomocniczym
     * @param context
     * @param kategoria
     */
    public static void RemoveCategory(Context context, Category kategoria) {
        //TODO dorobic "triggera", ktory bedzie zmienial kategorie dla pozycji, jesli sie bedzie usuwalo kategoire
        Toast.makeText(context, "RemoveCategory", Toast.LENGTH_SHORT).show();
        if(kategoria == null)
            throw new RuntimeException("Spoko takie usuwanie NULLa");
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = "UPDATE Kategorie SET CzyUsunieto=1 WHERE Nazwa = ?;";
        String[] arr= {kategoria.categoryName};
        db.execSQL(query, arr);
        Close();
    }

    /**
     * przywraca widocznosc dla wszystkich usunietych kategorii
     * @param context
     * @return
     */
    public static void RestoreAllCategories(Context context){
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = "UPDATE Kategorie SET CzyUsunieto=0;";
        db.rawQuery(query, new String[0]);
        Close();
    }

    public static Wallet GetWallet(Context context, String wname) {
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = null;
        query = "SELECT Stan, Waluta FROM Portfele WHERE Nazwa = ?";
        String[] arr = new String[] {wname};
        Cursor c = db.rawQuery(query,arr);

        int stIndex = c.getColumnIndex("Stan");
        int walIndex = c.getColumnIndex("Waluta");

        Wallet[] wallets = new Wallet[ c.getCount()];
        int i=0;
        while(c.moveToNext()){
            wallets[i++] = new Wallet(wname, c.getFloat(stIndex), c.getString(walIndex));
        }
        Close();
        return wallets[0];
    }

    /**
     * usuwa wybrana operacje z bazy danych
     */
    public static void RemoveOperation(Context context, Operation operation){
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = "DELETE FROM Pozycje WHERE IdPozycji= ?";
        String[] arr = {Integer.toString(operation.id)};
        db.execSQL(query, arr);
        Close();
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {

        private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        //wywolyane tylko raz, podczas tworzenia bazy danych
        private void CreateDataForFirstUse(SQLiteDatabase db){
            //tutaj tworzenie kategorii wbudowanych
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Artykuły spożywcze',NULL); ");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Samochód',NULL); ");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Przychody',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Wynagrodzenie','Przychody');");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Roszczenia żydowskie','Przychody');");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Lapowki','Wynagrodzenie');");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Jedzenie poza domem',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Transport',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Rozrywka',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Odzież',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Osobiste',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Dzieci',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Zwierzęta',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Dom',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Telefon i internet',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Rachunki',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Wakacje',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Alkohol',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Inne',NULL);");

            db.execSQL("INSERT INTO Portfele ('Nazwa', 'Stan', 'Waluta') VALUES ('Moj portfel', 1280.0, 'PLN');");
          //  db.execSQL("INSERT INTO Pozycje ('Nazwa', 'Wartosc', 'Data', 'Komentarz', 'CzyPrzychod','CzyUlubiona','CzyStale','KategorieNazwa','PortfeleNazwa','ListyZakupowIdListy') VALUES ('',12.5,'2015')");
        }

        /**
         * tworzy nam baze danych jesli nie istnieje
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            String[] create = new String[] {
                    "CREATE TABLE IF NOT EXISTS Kategorie (Nazwa varchar(255) NOT NULL, NazwaNadkategorii varchar(255), CzyUsunieto INTEGER DEFAULT 0  NOT NULL, IdObrazka integer(20), PRIMARY KEY (Nazwa), FOREIGN KEY(NazwaNadkategorii) REFERENCES Kategorie(Nazwa));",
                    "CREATE TABLE IF NOT EXISTS Portfele (Nazwa varchar(255) NOT NULL, Stan double(10) DEFAULT 0 NOT NULL, Waluta varchar(3) DEFAULT 'PLN' NOT NULL, PRIMARY KEY (Nazwa));",
                    "CREATE TABLE IF NOT EXISTS Pozycje (IdPozycji INTEGER PRIMARY KEY AUTOINCREMENT, Nazwa varchar(255), Wartosc double(10) NOT NULL, Data date, Komentarz integer(511), CzyPrzychod INTEGER DEFAULT 0 NOT NULL, CzyUlubiona INTEGER DEFAULT 0 NOT NULL, CzyStale INTEGER DEFAULT 0 NOT NULL, KategorieNazwa varchar(255) NOT NULL, PortfeleNazwa varchar(255) NOT NULL, ListyZakupowIdListy integer(10), FOREIGN KEY(KategorieNazwa) REFERENCES Kategorie(Nazwa), FOREIGN KEY(PortfeleNazwa) REFERENCES Portfele(Nazwa));",
                    "CREATE TABLE IF NOT EXISTS ListyZakupow (IdListy INTEGER PRIMARY KEY AUTOINCREMENT, Nazwa varchar(255) NOT NULL UNIQUE, Pozycje varchar(4095) NOT NULL, CzyKupiono varchar(1023) NOT NULL, CzyUkryte INTEGER DEFAULT 0, PozycjeIdPozycji integer(10) NOT NULL, FOREIGN KEY(PozycjeIdPozycji) REFERENCES Pozycje(IdPozycji));",
                    "CREATE TABLE IF NOT EXISTS Porady (IdPorady INTEGER PRIMARY KEY AUTOINCREMENT, Nazwa varchar(255) NOT NULL, Link varchar(255) NOT NULL);",
                    "CREATE TABLE IF NOT EXISTS ZleceniaStale (Nazwa varchar(255) NOT NULL, DataOd date NOT NULL, DataDo date, PozycjeIdPozycji integer(10) NOT NULL, Cyklicznosc integer(10) NOT NULL, Czestotliwosc integer(10) NOT NULL, DniCyklicznosci varchar(127), PRIMARY KEY (Nazwa), FOREIGN KEY(PozycjeIdPozycji) REFERENCES Pozycje(IdPozycji));",
                    "CREATE UNIQUE INDEX ListyZakupow_IdListy ON ListyZakupow (IdListy);",
                    "CREATE UNIQUE INDEX Porady_IdPorady ON Porady (IdPorady);",
                    "CREATE UNIQUE INDEX Pozycje_IdPozycji ON Pozycje (IdPozycji);" };
            for(String c : create)
                db.execSQL(c);

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
