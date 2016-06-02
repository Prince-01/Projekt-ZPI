package pl.finanse.zpi.pwr.wallet.helpers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


import pl.finanse.zpi.pwr.wallet.MainActivity;
import pl.finanse.zpi.pwr.wallet.model.Category;
import pl.finanse.zpi.pwr.wallet.model.Operation;
import pl.finanse.zpi.pwr.wallet.model.StandingOperation;
import pl.finanse.zpi.pwr.wallet.model.Wallet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    /**
     * daje wszystkie wszystkie wszystkie wszystkie kurwa kategorie, bez wyjatku
     * @param context
     * @return
     */
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

    /**
     * daje wszystkie portfele
     * @param context
     * @return
     */
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
        query = "SELECT IdPozycji, Nazwa, Wartosc, Data, CzyPrzychod, KategorieNazwa, PortfeleNazwa FROM Pozycje WHERE PortfeleNazwa = ? AND CzyStale = 0 ORDER BY Data DESC, IdPozycji DESC";
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

    public static StandingOperation[] GetAllStandingOperations(Context context){
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = null;
        query = "SELECT P.IdPozycji, P.Nazwa, P.Wartosc, P.Data, P.CzyPrzychod, P.KategorieNazwa, P.PortfeleNazwa, ZS.DataOd, ZS.DataDo, ZS.Interwal FROM Pozycje P JOIN ZleceniaStale ZS ON P.IdPozycji = ZS.PozycjeIdPozycji WHERE PortfeleNazwa = ? AND CzyStale = 1 ORDER BY Data DESC, IdPozycji DESC";
        String[] arr = {Wallet.GetActiveWallet(context).getName()};
        Cursor c = db.rawQuery(query,arr);
        int idIndex = c.getColumnIndex("IdPozycji");
        int nazIndex = c.getColumnIndex("Nazwa");
        int wartIndex = c.getColumnIndex("Wartosc");
        int datIndex = c.getColumnIndex("Data");
        int przIndex = c.getColumnIndex("CzyPrzychod");
        int knazIndex = c.getColumnIndex("KategorieNazwa");
        int portIndex = c.getColumnIndex("PortfeleNazwa");
        int odIndex = c.getColumnIndex("DataOd");
        int doIndex = c.getColumnIndex("DataDo");
        int interwalIndex = c.getColumnIndex("Interwal");
        StandingOperation[] standingOperations = new StandingOperation[ c.getCount()];
//        Toast.makeText(context,"Liczba pozycji: "+operations.length,Toast.LENGTH_SHORT).show();
        int i=0;
        while(c.moveToNext()){
            try {
                standingOperations[i++] = new StandingOperation(c.getInt(idIndex),c.getString(portIndex), c.getString(nazIndex),c.getFloat(wartIndex),(new SimpleDateFormat("yyyy-MM-dd")).parse(c.getString(odIndex)), c.getString (doIndex) != null ? (new SimpleDateFormat("yyyy-MM-dd")).parse(c.getString(doIndex)) : null, Enum.valueOf(StandingOperation.INTERVAL.class, c.getString(interwalIndex)), c.getInt(przIndex) == 0 ? false : true, c.getString(knazIndex));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Close();
        return standingOperations;
    }

    public static void CheckIfStandingOperationsUpdateIsNeededAndUpdate(Context context) {
        SharedPreferences sp = MainActivity.GetGlobalSharedPreferences(context);
        Calendar lastCheck = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        lastCheck.setTimeInMillis(sp.getLong("lastCheck", 0));

        boolean sameDay = lastCheck.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                lastCheck.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);

        if(sameDay) return;

        SharedPreferences.Editor spe = MainActivity.GetGlobalSharedPreferences(context).edit();
        spe.putLong("lastCheck", today.getTimeInMillis());
        spe.commit();

        StandingOperation[] standingOperations = GetAllStandingOperations(context);

        lastCheck.add(Calendar.DATE, 1);

        for (StandingOperation so : standingOperations)
            UpdateStandingOperationFromDateToDate(context, so, lastCheck, today);
    }

    public static void UpdateStandingOperationFromDateToDate(Context context, StandingOperation so, Calendar c1, Calendar end) {
        Calendar begin = Calendar.getInstance();
        begin.setTimeInMillis(c1.getTimeInMillis());

        while(begin.getTimeInMillis() / (1000 * 60 * 60 * 24) <= end.getTimeInMillis() / (1000 * 60 * 60 * 24)) {
            if (CheckIfAddInDay(so, begin)) {
                Operation op = new Operation(-1, so.wallet, so.operationName, so.cost, begin.getTime(), so.isIncome, so.category);
                AddQuickNewPosition(context, op);
            }
            begin.add(Calendar.DATE, 1);
        }
    }

    private static boolean CheckIfAddInDay(StandingOperation so, Calendar day) {
        Calendar c = day;
        Calendar soc = Calendar.getInstance();
        soc.setTimeInMillis(so.begin.getTime());

        if(so.end != null && c.getTimeInMillis() > so.end.getTime())
            return false;

        long differenceInDaysFromBeginning = (c.getTimeInMillis() - soc.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        long differenceInYearsFromBeginning = (c.get(Calendar.YEAR) - soc.get(Calendar.YEAR));
        long differenceInMonthsFromBeginning = differenceInYearsFromBeginning * 12 + ((c.get(Calendar.MONTH) - soc.get(Calendar.MONTH)));

        switch (so.interval) {
            case week:
                if(differenceInDaysFromBeginning % 7 == 0)
                    return true;
                break;
            case twoweeks:
                if(differenceInDaysFromBeginning % 14 == 0)
                    return true;
                break;
            case month:
                if(c.get(Calendar.DAY_OF_MONTH) == soc.get(Calendar.DAY_OF_MONTH) || (soc.get(Calendar.DAY_OF_MONTH) > c.getActualMaximum(Calendar.DAY_OF_MONTH) && c.getActualMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)))
                    return true;
                break;
            case twomonths:
                if(differenceInMonthsFromBeginning % 2 == 0 && c.get(Calendar.DAY_OF_MONTH) == soc.get(Calendar.DAY_OF_MONTH) || (soc.get(Calendar.DAY_OF_MONTH) > c.getActualMaximum(Calendar.DAY_OF_MONTH) && c.getActualMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)))
                    return true;
                break;
            case quarter:
                if(differenceInMonthsFromBeginning % 3 == 0 && c.get(Calendar.DAY_OF_MONTH) == soc.get(Calendar.DAY_OF_MONTH) || (soc.get(Calendar.DAY_OF_MONTH) > c.getActualMaximum(Calendar.DAY_OF_MONTH) && c.getActualMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)))
                    return true;
                break;
            case halfyear:
                if(differenceInMonthsFromBeginning % 6 == 0 && c.get(Calendar.DAY_OF_MONTH) == soc.get(Calendar.DAY_OF_MONTH) || (soc.get(Calendar.DAY_OF_MONTH) > c.getActualMaximum(Calendar.DAY_OF_MONTH) && c.getActualMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)))
                    return true;
                break;
            case year:
                if(differenceInMonthsFromBeginning % 12 == 0 && c.get(Calendar.DAY_OF_MONTH) == soc.get(Calendar.DAY_OF_MONTH) || (soc.get(Calendar.DAY_OF_MONTH) > c.getActualMaximum(Calendar.DAY_OF_MONTH) && c.getActualMaximum(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)))
                    return true;
                break;
        }
        return false;
    }

    public static Operation[] GetChosenPositions(Context context, Date from, Date to){
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query = null;

        query = "SELECT IdPozycji, Nazwa, Wartosc, Data, CzyPrzychod, KategorieNazwa, PortfeleNazwa FROM Pozycje WHERE PortfeleNazwa = ? AND Data >= ? AND Data <= ? ORDER BY Data DESC, IdPozycji DESC";
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        (new SimpleDateFormat("yyyy-MM-dd")).format(to);
        String[] arr = {Wallet.GetActiveWallet(context).getName(), sdf.format(from), sdf.format(to)};
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

    /**
     * dodaje SZYBKO nowa pozycje, albo moze nowa operacje, albo cos tam
     * @param context
     * @param operacja
     */
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
     * edytuje podana operacje, na podstawie jej ID
     * @param context
     * @param operacja
     */
    public static void EditPosition(Context context, Operation operacja) {

        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        ContentValues values = new ContentValues();
        values.put("Nazwa", operacja.operationName);
        values.put("Wartosc", operacja.cost);
        values.put("Data", (new SimpleDateFormat("yyyy-MM-dd")).format(operacja.date));
        values.put("CzyPrzychod", operacja.isIncome ? 1 : 0);
        values.put("KategorieNazwa", operacja.category);
        values.put("PortfeleNazwa", operacja.wallet);
        db.update("Pozycje", values, "IdPozycji = ?", new String[]{Integer.toString(operacja.id)});
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
     * usuowa podajna kategorie z bazy danych,
     * musimy miec ustawiona superkategorie
     * @param context
     * @param kategoria
     */
    public static void RemoveCategory(Context context, Category kategoria) {
        Category[] cats = GetCategories(context,kategoria.toString());
        for(int i=0;i<cats.length;i++)
            RemoveCategory(context,cats[i]);//usuwanie rekurencyjne wyszstkich podkategorii
       // Toast.makeText(context, "RemoveCategory", Toast.LENGTH_SHORT).show();
        if(kategoria == null)
            throw new RuntimeException("Spoko takie usuwanie NULLa");
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        String query2 = "UPDATE Pozycje SET KategorieNazwa = ? WHERE KategorieNazwa = ?";
        String[] arr2={kategoria.superCategory,kategoria.categoryName};
        db.execSQL(query2, arr2);
        String query = "DELETE FROM Kategorie WHERE Nazwa = ?;";
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

    public static void RemoveWallet(Context context, Wallet pornfel) {
               // Toast.makeText(context, "RemoveCategory", Toast.LENGTH_SHORT).show();
                if(pornfel == null)
                    throw new RuntimeException("Spoko takie usuwanie NULLa");
                if(!Open(context))
                    throw new RuntimeException("Blad podczas polaczenia z baza");
                String query = "DELETE FROM Portfele WHERE Nazwa = ?;";
                String[] arr= {pornfel.getName()};
                db.execSQL(query, arr);
                Close();
    }

    public static int GetNumberOfPositionsForWallet(Context context, Wallet wallet) {
                if(!Open(context))
                    throw new RuntimeException("Blad podczas polaczenia z baza");
                String query = null;
                query = "SELECT COUNT(*) FROM Pozycje WHERE PortfeleNazwa = ?";
                String[] arr = {wallet.getName()};
                Cursor c = db.rawQuery(query,arr);
                int w = -1;
                c.moveToFirst();
                w = c.getInt(0);
                Close();
                return w;
            }

    /**
     * dodaje nowa operacje zlecena stalego

     */
    public static void AddNewStandingOperation(Context context, StandingOperation operacja) {
        if(!Open(context))
            throw new RuntimeException("Blad podczas polaczenia z baza");
        ContentValues values = new ContentValues();
        values.put("Nazwa", operacja.operationName);
        values.put("Wartosc", operacja.cost);
        values.put("Data", (new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime()));
        values.put("CzyPrzychod", operacja.isIncome ? 1 : 0);
        values.put("KategorieNazwa", operacja.category);
        values.put("PortfeleNazwa", operacja.wallet);
        values.put("CzyStale", 1);
        long idPoz = db.insert("Pozycje", null, values);//dodanie do tablicy pozycji
        values = new ContentValues();
        values.put("Nazwa",operacja.operationName);
        values.put("DataOd",(new SimpleDateFormat("yyyy-MM-dd")).format(operacja.begin));
        values.put("DataDo",operacja.end != null ? (new SimpleDateFormat("yyyy-MM-dd")).format(operacja.end) : null);
        values.put("Interwal",operacja.interval.toString());
        values.put("PozycjeIdPozycji",idPoz);
        db.insert("ZleceniaStale",null,values);//dodanie do zlecen stalych
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
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Jedzenie poza domem','Artykuły spożywcze');");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Transport',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Rozrywka',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Odzież',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Osobiste',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Dzieci',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Zwierzęta',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Rachunki',NULL);");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Dom','Rachunki');");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Telefon i internet','Rachunki');");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Wakacje','Rozrywka');");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Alkohol','Rozrywka');");
            db.execSQL("INSERT INTO Kategorie ('Nazwa','NazwaNadkategorii') VALUES ('Inne',NULL);");

            db.execSQL("INSERT INTO Portfele ('Nazwa', 'Stan', 'Waluta') VALUES ('Moj portfel', 0, 'PLN');");
            db.execSQL("INSERT INTO Portfele ('Nazwa', 'Stan', 'Waluta') VALUES ('Moja firma', 0, 'PLN');");

            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Wypłata', 'Przychody', 'Moj portfel', '2016-04-05', 20000, 0)");

            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Chleb', 'Artykuły spożywcze', 'Moj portfel', '2016-04-21', 200, 0)");
            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Pies-karma', 'Zwierzęta', 'Moj portfel', '2016-03-12', 100, 0)");
            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Ziemia', 'Dom', 'Moj portfel', '2016-04-12', 20000, 0)");
            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Numizmaty', 'Inne', 'Moj portfel', '2016-04-21', 1500, 0)");
            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Grenlandia 2190', 'Wakacje', 'Moj portfel', '2016-04-21', 15000, 0)");
            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Cypr 2301', 'Wakacje', 'Moj portfel', '2016-04-21', 12000, 0)");
            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Wino', 'Alkohol', 'Moj portfel', '2016-04-21', 345, 0)");
            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Piwo', 'Alkohol', 'Moj portfel', '2016-04-21', 56, 0)");
            db.execSQL("INSERT INTO Pozycje ('Nazwa', 'KategorieNazwa', 'PortfeleNazwa', 'Data', 'Wartosc', 'CzyPrzychod') VALUES ('Wódka', 'Alkohol', 'Moj portfel', '2016-04-21', 178, 0)");

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
                    "CREATE TABLE IF NOT EXISTS Pozycje (IdPozycji INTEGER PRIMARY KEY AUTOINCREMENT, Nazwa varchar(255), Wartosc double(10) NOT NULL, Data date, Komentarz integer(511), CzyPrzychod INTEGER DEFAULT 0 NOT NULL, CzySzablon INTEGER DEFAULT 0 NOT NULL, CzyStale INTEGER DEFAULT 0 NOT NULL, KategorieNazwa varchar(255) NOT NULL, PortfeleNazwa varchar(255) NOT NULL, ListyZakupowIdListy integer(10), FOREIGN KEY(KategorieNazwa) REFERENCES Kategorie(Nazwa), FOREIGN KEY(PortfeleNazwa) REFERENCES Portfele(Nazwa));",
                    "CREATE TABLE IF NOT EXISTS ListyZakupow (IdListy INTEGER PRIMARY KEY AUTOINCREMENT, Nazwa varchar(255) NOT NULL UNIQUE, Pozycje varchar(4095) NOT NULL, CzyKupiono varchar(1023) NOT NULL, CzyUkryte INTEGER DEFAULT 0, PozycjeIdPozycji integer(10) NOT NULL, FOREIGN KEY(PozycjeIdPozycji) REFERENCES Pozycje(IdPozycji));",
                    "CREATE TABLE IF NOT EXISTS Porady (IdPorady INTEGER PRIMARY KEY AUTOINCREMENT, Nazwa varchar(255) NOT NULL, Link varchar(255) NOT NULL);",
                    "CREATE TABLE IF NOT EXISTS ZleceniaStale (Nazwa varchar(255) NOT NULL, DataOd date NOT NULL, DataDo date, PozycjeIdPozycji integer(10) NOT NULL, Interwal varchar(20), PRIMARY KEY (Nazwa), FOREIGN KEY(PozycjeIdPozycji) REFERENCES Pozycje(IdPozycji));",
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
