package com.example.hieudeptrai.ontapcuoikyfinish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class XemChiTiet extends AppCompatActivity {

    EditText editid, edithotenmoi,editngaysinhmoi,edisodtmoi;
    RadioButton rbnammoi,rbnumoi;
    Button btnTroVe, btnSua;
    private DBManager database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_chi_tiet);
        database = new DBManager(this);
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("DATA");
        final Contact contact = (Contact) b.getSerializable("CONTACT");
        editid = (EditText) findViewById(R.id.editid);
        edithotenmoi = (EditText) findViewById(R.id.edithotensua);
        editngaysinhmoi = (EditText) findViewById(R.id.editngaysinhmoi);
        rbnammoi=(RadioButton) findViewById(R.id.rbnammoi);
        rbnumoi = (RadioButton) findViewById(R.id.rbnumoi);
        btnSua = (Button) findViewById(R.id.btnsuamoi);
        btnTroVe = (Button) findViewById(R.id.btntrove);
        edisodtmoi = (EditText) findViewById(R.id.editsdtsua) ;

        editid.setText(String.valueOf(contact.getId()));
        edithotenmoi.setText(contact.getHoTen());
        edisodtmoi.setText(String.valueOf(contact.getSdt()));
        editngaysinhmoi.setText(contact.getNgaySinh());
        if(contact.getGioiTinh().equals("Nam"))
        {
            rbnammoi.performClick();
        }
        else
        {
            rbnumoi.performClick();
        }
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sua(contact);
            }
        });
    }

    public void sua(Contact contact){
        contact.setHoTen(edithotenmoi.getText().toString());
        contact.setSdt(Integer.parseInt(edisodtmoi.getText().toString()));
        contact.setNgaySinh(editngaysinhmoi.getText().toString());
        String gioiTinh ="";
        if(rbnammoi.isChecked())
        {
            gioiTinh="Nam";
        }else
        {
            gioiTinh="Nữ";
        }
        contact.setGioiTinh(gioiTinh);
        database.Update(contact);
        Toast.makeText(this, "Sua Thanh Cong", Toast.LENGTH_SHORT).show();

    }
  
}
