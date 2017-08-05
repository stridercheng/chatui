package com.rance.chatui.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rance.chatui.R;
import com.rance.chatui.adapter.ContactAdapter;
import com.rance.chatui.enity.IMContact;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.util.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class ContactActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {
    RecyclerView rvContact;

    private List<IMContact> imContactList;
    private final static int CODE_REQUEST_CONTACT = 0x222;
    private ContactAdapter mContactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        rvContact = (RecyclerView) findViewById(R.id.rv_contact);
        setup();
        checkPermission();
    }

    private void setup() {
        rvContact.setLayoutManager(new LinearLayoutManager(this));
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}
                    , CODE_REQUEST_CONTACT);
        } else {
            readContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_REQUEST_CONTACT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(this, "未设置读取联系人权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void readContacts() {
        imContactList = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri contactUri =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            cursor = this.getContentResolver().query(contactUri,
                    new String[]{"display_name", "sort_key", "contact_id","data1"},
                    null, null, "sort_key");
            String contactName;
            String contactNumber;
            while (cursor.moveToNext()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                IMContact imContact = new IMContact(contactName, contactNumber);
                imContact.setSurname(contactName.substring(0, 1));
                if (contactName!=null)
                    imContactList.add(imContact);
            }
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }

        loadData();
    }

    private void loadData() {
        mContactAdapter = new ContactAdapter(imContactList);
        mContactAdapter.setOnContactClickListener(this);
        rvContact.setAdapter(mContactAdapter);
    }

    @Override
    public void onContactClick(View view, final IMContact imContact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send to John Snow");

        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_contact, null);

        TextView tvSurname = (TextView) contentView.findViewById(R.id.tv_surname);
        tvSurname.setText(imContact.getSurname());
//        ((TextView)contentView.findViewById(R.id.tv_surname)).setText(imContact.getName().charAt(0));
        ((TextView)contentView.findViewById(R.id.tv_name)).setText(imContact.getName());
        ((TextView)contentView.findViewById(R.id.tv_phone)).setText("电话：" + imContact.getPhonenumber());

        builder.setView(contentView);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setFileType(Constants.CHAT_FILE_TYPE_CONTACT);
                messageInfo.setObject(imContact);
                EventBus.getDefault().post(messageInfo);
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
