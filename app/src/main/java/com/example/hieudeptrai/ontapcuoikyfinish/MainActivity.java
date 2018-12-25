package com.example.hieudeptrai.ontapcuoikyfinish;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText edithoten, editsdt,editngaysinh,edittim;
    RadioButton rbnam, rbnu;
    Button btnThem, btnloadata, btnXoaTrang, btnTim;
    ListView lvData;
    private DBManager database;
    ArrayList<Contact>listContactSQLite = new ArrayList<Contact>();
    ArrayAdapter<Contact> adapterSQLite = null;
    Contact selectedContact = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        database = new DBManager(this);
        listContactSQLite = database.getAllContact();
        adapterSQLite = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listContactSQLite);
        lvData.setAdapter(adapterSQLite);
        adapterSQLite.notifyDataSetChanged();
        doAddEvent();
    }

    public void anhXa(){
        edithoten =(EditText) findViewById(R.id.edithoten);
        editsdt = (EditText) findViewById(R.id.editsdt);
        editngaysinh = (EditText) findViewById(R.id.editngaysinh);
        edittim = (EditText) findViewById(R.id.editsearch);
        rbnam = (RadioButton) findViewById(R.id.rbnam);
        rbnu = (RadioButton) findViewById(R.id.rbnu);
        btnThem = (Button) findViewById(R.id.btnthem);
        btnloadata = (Button) findViewById(R.id.btnloaddata);
        btnXoaTrang = (Button) findViewById(R.id.btnxoatrang);
        btnTim = (Button) findViewById(R.id.btnsearch);
        lvData = (ListView) findViewById(R.id.lvdata);
        registerForContextMenu(lvData);
    }

    public void doAddEvent()
    {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                them();
            }
        });
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timkiem();
            }
        });
        btnXoaTrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edithoten.setText("");
                editngaysinh.setText("");
                editsdt.setText("");
                edittim.setText("");
            }
        });
        btnloadata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listContactSQLite = database.getAllContact();
                lvData.setAdapter(adapterSQLite);
                adapterSQLite.notifyDataSetChanged();
            }
        });
        lvData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedContact = listContactSQLite.get(position);
                return false;
            }
        });
    }
    //tim theo ten
    public void timkiem(){
        String search = edittim.getText().toString();
        listContactSQLite = database.getListContactWithTen(search);
        ArrayAdapter<Contact> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listContactSQLite);
        lvData.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void them(){
        String hoTen = edithoten.getText().toString();
        String sdt = editsdt.getText().toString();
        String gioiTinh = getGioiTinh();
        String ngaySinh = editngaysinh.getText().toString();
        if (hoTen.equals("")|| sdt.equals("")||gioiTinh.equals("")||ngaySinh.equals(""))
        {
            Toast.makeText(this, "Phải Nhập Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show();
        }else
        {
            if(checkIsNumber(sdt)== false)
            {
                Toast.makeText(this, "Số đt nhập vào phải toàn là chữ số", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Contact contact = new Contact();
                contact.setHoTen(hoTen);
                contact.setSdt(Integer.parseInt(sdt));
                contact.setGioiTinh(gioiTinh);
                contact.setNgaySinh(ngaySinh);
                if(database.checkContact(contact)== true){
                    Toast.makeText(this, "Danh Bạ đã tồn tại", Toast.LENGTH_SHORT).show();
                }else{
                    database.addContact(contact);
                    adapterSQLite.add(contact);
                    lvData.setAdapter(adapterSQLite);
                    adapterSQLite.notifyDataSetChanged();
                }
            }
        }

    }
    public boolean checkIsNumber(String sdt)
    {
        for(int i =0; i<=sdt.length();i++){
            if (Character.isLetter(sdt.charAt(i))){
                return  false;
            }
            else
            {
                return true;
            }
        }
        return false;
    }
    public String getGioiTinh(){
        if (rbnam.isChecked())
        {
            return "Nam";
        }else
        {
            return "Nữ";
        }
    }

    //dang ky menu

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.contace_menu,menu);
        menu.getItem(0).setTitle("Xem Chi Tiết: "+ selectedContact.getHoTen());
        menu.getItem(1).setTitle("Xóa: "+selectedContact.getHoTen());
        menu.getItem(2).setTitle("Call: "+selectedContact.getSdt());
        menu.getItem(3).setTitle("SMS to: "+selectedContact.getSdt());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_chitiet:
                xemChiTiet();
                break;
            case R.id.menu_xoa:
                doMakeXoa();
                break;
            case  R.id.menu_call:
                doMakeCall();
                break;
            case R.id.menu_sms:
                doMakeSMS();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void xemChiTiet(){
        Intent intent = new Intent(MainActivity.this,XemChiTiet.class);
        Bundle b = new Bundle();
        b.putSerializable("CONTACT",selectedContact);
        intent.putExtra("DATA",b);
        startActivity(intent);

    }
    public void doMakeXoa(){
        database.deleteContact(selectedContact);
        adapterSQLite.remove(selectedContact);
        adapterSQLite.notifyDataSetChanged();

    }
    //de call va smm duoc thi phai khai bao trong manifest
    @SuppressLint("MissingPermission")
    public void doMakeCall(){
        Uri uri = Uri.parse("tel:"+selectedContact.getSdt());
        Intent i = new Intent(Intent.ACTION_CALL, uri);
        startActivity(i);
    }
    public void doMakeSMS(){
        Intent intent = new Intent(MainActivity.this,MySMSActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("CONTACT",selectedContact);
        intent.putExtra("DATA",b);
        startActivity(intent);
    }

//    //code show chon date
//    public void showDatePikerDiaLog(){
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                txtNgaySinh.setText((dayOfMonth)+"/"+(month) + "/" + (year));
//            }
//        },year,month,day);
//
//        datePickerDialog.show();
//
//    }
//
//    public String chonSOThich()
//    {
//        CheckBox cb[] = new CheckBox[3];
//        cb[1] = cbDocBao;
//        cb[2] = cbDocSach;
//        cb[0] = cbDocCoding;
//        for(int i = 0 ; i<cb.length;i++)
//        {
//            if (cb[i].isChecked())
//            {
//                return cb[i].getText().toString();
//            }
//        }
//        return null;
//    }
//
//    public void showAlertDialog()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Thông tin cá nhân");
//        builder.setMessage(editHoTen.getText().toString()+"\n" +
//                editCMND.getText().toString()+"\n" +
//                checkBangCap().getText().toString()+"\n"
//                +chonSOThich()+"\n--------------------\n"
//                +"Thông tin bổ sung\n"+ editBoSung.getText().toString()
//        );
//
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//    }
}
