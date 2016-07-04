package com.kerchin.demo.providerandbroadcast;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        queryContacts();
//        addContacts();
        IntentFilter i = new IntentFilter("BC_one");
        //动态注册优先级稍高 且在manifest中不需要写明 但是与作用域相关
        BC3 bc3 = new BC3();
        registerReceiver(bc3, i);
    }

    public void NormalBroad(View v){
        Intent i = new Intent();
        i.putExtra("msg", "normal");
        i.setAction("BC_one");
        sendBroadcast(i);
    }

    public void OrderedBroad(View v){
        Intent i = new Intent();
        i.putExtra("msg", "ordered");
        i.setAction("BC_two");
        sendOrderedBroadcast(i, "");
    }

    public void StickyBroad(View v){
        Intent i = new Intent();
        i.putExtra("msg", "sticky");
        i.setAction("BC_three");
        sendStickyBroadcast(i);
    }

    private void addContacts() {
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        Uri uri = cr.insert(RawContacts.CONTENT_URI, values);
        Long raw_contact_id = ContentUris.parseId(uri);
        values.clear();
        //插入人名
        values.put(StructuredName.RAW_CONTACT_ID, raw_contact_id);
        values.put(StructuredName.DISPLAY_NAME, "#张三");
        values.put(StructuredName.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        uri = cr.insert(Data.CONTENT_URI, values);
        //插入电话
        values.clear();
        values.put(Phone.RAW_CONTACT_ID, raw_contact_id);
        values.put(Phone.NUMBER, "13333333333");
        values.put(Phone.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        uri = cr.insert(Data.CONTENT_URI, values);
    }

    private void queryContacts() {
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(Contacts.CONTENT_URI
                , new String[]{Contacts._ID, Contacts.DISPLAY_NAME}
                , null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndex("_id"));
                Log.d("queryinfo", "id:" + id);
                Log.d("queryinfo", "name:" + c.getString(c.getColumnIndex("display_name")));
                Cursor c1 = cr.query(Phone.CONTENT_URI, new String[]{
                        Phone.NUMBER, Phone.TYPE
                }, Phone.CONTACT_ID + "=" + id, null, null);
                if (c1 != null) {
                    while (c1.moveToNext()) {
                        int type = c1.getInt(c1.getColumnIndex(Phone.TYPE));
                        if (type == Phone.TYPE_HOME)
                            Log.d("queryinfo", "home" + c1.getString(c1.getColumnIndex(Phone.NUMBER)));
                        else if (type == Phone.TYPE_MOBILE)
                            Log.d("queryinfo", "mobile" + c1.getString(c1.getColumnIndex(Phone.NUMBER)));
                    }
                    c1.close();
                }
                Cursor c2 = cr.query(Email.CONTENT_URI, new String[]{
                        Email.DATA, Email.TYPE
                }, Email.CONTACT_ID + "=" + id, null, null);
                if (c2 != null) {
                    while (c2.moveToNext()) {
                        int type = c2.getInt(c2.getColumnIndex(Email.DATA));
                        if (type == Email.TYPE_WORK)
                            Log.d("queryinfo", "home" + c2.getString(c2.getColumnIndex(Email.DATA)));
                        else if (type == Email.TYPE_HOME)
                            Log.d("queryinfo", "home" + c2.getString(c2.getColumnIndex(Email.DATA)));
                    }
                    c2.close();
                }
            }
            c.close();
        }
    }
}
