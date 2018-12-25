package com.example.hieudeptrai.ontapcuoikyfinish;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MySMSActivity extends AppCompatActivity {

    Button btnSenSMS;
    TextView txtSendTo;
    EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sms);

        //lay thong tin tu intent
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("DATA");
        final  Contact contact = (Contact) b.getSerializable("CONTACT");

        btnSenSMS = (Button) findViewById(R.id.btnsensms);
        txtSendTo = (TextView) findViewById(R.id.txtsento);
        editContent = (EditText) findViewById(R.id.editcontent);

        txtSendTo.setText(String.valueOf(contact.getSdt()));

        btnSenSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(contact);
                }
        });
    }
    public void sendSMS(Contact contact){
        //lấy mặc định sms
        final SmsManager sms = SmsManager.getDefault();
        Intent msgSent = new Intent("ACTION_MSG_SENT");
        //khai báo intent pendding de kiem tra ket qua
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,msgSent,0);
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                String msg="Send OK";
                if (result != Activity.RESULT_OK) { msg="Send failed"; }
                Toast.makeText(MySMSActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }, new IntentFilter("ACTION_MSG_SENT"));
        //Gọi hàm gửi tin nhắn đi
        sms.sendTextMessage(String.valueOf(contact.getSdt()), null, editContent.getText()+"",
                pendingIntent, null);
        finish();
    }
}
