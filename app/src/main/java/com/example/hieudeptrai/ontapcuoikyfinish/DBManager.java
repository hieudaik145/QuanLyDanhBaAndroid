package com.example.hieudeptrai.ontapcuoikyfinish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;


public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="data";
    private static final String TABLE_NAME = "contact";
    private static final String ID="id";
    private static final String NAME = "name";
    private static final String SDT = "sdt";
    private static final String GT = "gioitinh";
    private static final String NGAYSINH="ngaysinh";
    private Context context;

    public DBManager(Context context){
        super(context,DATABASE_NAME,null,1);
        Log.d("DBManager","DBManager");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,"+
        "%s TEXT,%s INTEGER, %s TEXT, %s TEXT)",TABLE_NAME,ID,NAME,SDT,GT,NGAYSINH);
        db.execSQL(sqlQuery);
        Toast.makeText(context, "Create successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
        Toast.makeText(context, "Drop successfylly", Toast.LENGTH_SHORT).show();
    }

    //thêm mới một contact
    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(NAME,contact.getHoTen());
        values.put(SDT,contact.getSdt());
        values.put(GT,contact.getGioiTinh());
        values.put(NGAYSINH,contact.getNgaySinh());
        //Neu de null thi khi value bang null thi loi
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
     /*
    Select a contact by Name
     */
     public Contact getContactByName(int id){
         Contact contact = new Contact();
         SQLiteDatabase db = this.getReadableDatabase();
         String sql = "SELECT * FROM contact where id = '"+id+"'";
         Cursor cursor = null;
         cursor = db.rawQuery(sql,null);
         int sl = cursor.getCount();
         if (sl>0)
         {
             contact.setId(cursor.getInt(0));
             contact.setHoTen(cursor.getString(1));
             contact.setSdt(cursor.getInt(2));
             contact.setGioiTinh(cursor.getString(3));
             contact.setNgaySinh(cursor.getString(4));
         }
         cursor.close();
         db.close();
         return contact;
     }

     public boolean checkContact(Contact contact)
     {
         SQLiteDatabase db = this.getReadableDatabase();
         String sql = "select * from contact where name = '"+contact.getHoTen()+"' and sdt = '"+contact.getSdt()+"'";
         Cursor cursor = null;
         cursor = db.rawQuery(sql,null);
         int sl = cursor.getCount();
         cursor.close();
         db.close();
         return  sl >0;
     }

      /*
    Update name of contact
     */
      public void Update(Contact contact){
          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues values = new ContentValues();
          values.put(NAME,contact.getHoTen());
          values.put(SDT,contact.getSdt());
          values.put(GT,contact.getGioiTinh());
          values.put(NGAYSINH,contact.getNgaySinh());
          db.update(TABLE_NAME,values,ID + "=?",new String[]{String.valueOf(contact.getId())});
          db.close();
      }
     /*
     Getting All Contact
      */
     public ArrayList<Contact> getAllContact(){
         ArrayList<Contact> listContact = new ArrayList<>();
         String sql = "select * from " +TABLE_NAME;
         SQLiteDatabase db = this.getWritableDatabase();
         Cursor cursor = db.rawQuery(sql,null);
         if (cursor.moveToFirst()){
             do {
                 Contact contact = new Contact();
                 contact.setId(cursor.getInt(0));
                 contact.setHoTen(cursor.getString(1));
                 contact.setSdt(cursor.getInt(2));
                 contact.setGioiTinh(cursor.getString(3));
                 contact.setNgaySinh(cursor.getString(4));
                 listContact.add(contact);
             }while (cursor.moveToNext());
         }
         cursor.close();
         db.close();
         return  listContact;
     }
    /*
   Delete a contact by id
    */
     public void deleteContact(Contact contact){
         SQLiteDatabase db = this.getWritableDatabase();
         db.delete(TABLE_NAME, ID + "=?",new String[]{String.valueOf(contact.getId())});
         db.close();
     }

       /*
    Get Count contact in Table Contact
     */
       public int getContactCount(){
           String sql = "select * from " +TABLE_NAME;
           SQLiteDatabase db = this.getReadableDatabase();
           Cursor cursor = db.rawQuery(sql,null);
           cursor.close();
           return cursor.getCount();
       }

      //Tìm kiếm theo tên
    public ArrayList<Contact> getListContactWithTen(String hoTen){
           ArrayList<Contact> list = new ArrayList<>();
           String sql = "select * from contact where name = '"+hoTen+"'";
           SQLiteDatabase db = this.getWritableDatabase();
           Cursor cursor = null;
           cursor = db.rawQuery(sql,null);
           if(cursor.moveToFirst())
           {
               do {
                   Contact contact = new Contact();
                   contact.setId(cursor.getInt(0));
                   contact.setHoTen(cursor.getString(1));
                   contact.setSdt(cursor.getInt(2));
                   contact.setGioiTinh(cursor.getString(3));
                   contact.setNgaySinh(cursor.getString(4));
                   list.add(contact);

               }while (cursor.moveToNext());
           }

           return  list;
    }


}
