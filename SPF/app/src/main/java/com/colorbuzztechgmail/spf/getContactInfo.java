package com.colorbuzztechgmail.spf;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by PC01 on 18/02/2018.
 */

public class getContactInfo {

    String id,name,phone,email,adress,company;
    Context context;


    public getContactInfo(String contactId,String contactName,Context context) {
        super();

        this.id=contactId;
        this.name=contactName;
        this.context=context;


    }
    public String getName() {

        return name;
    }

    public String getPhone() {

        Cursor phoneCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{id},
                null);

         if(phoneCur.getCount()>0){

             if (phoneCur.moveToFirst()) {
                phone = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
             }

             phoneCur.close();

             Log.i("Movile", "Contact Phone Number: " + phone);

            }else {

             phone = context.getResources().getString(R.string.importNoAsigned_Cat);
           }
        return phone;
    }

    public String getEmail() {

        Cursor emailCur = context.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
        if(emailCur.getCount()>0) {
            while (emailCur.moveToNext()) {
                email= emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                Log.i("Email", getName() + " " + email);
            }
            emailCur.close();
        }else{

            email=context.getResources().getString(R.string.importNoAsigned_Cat);
        }
        return email;
    }

    public String getAdress() {

        Cursor postal_cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null,

                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE + " = " +
                        ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME,
                new String[]{id}, null);

        if(postal_cursor.getCount()>0){
            while (postal_cursor.moveToNext()) {
                adress = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));

            }
            postal_cursor.close();
        }else{

            adress=context.getResources().getString(R.string.importNoAsigned_Cat);
        }
        return adress;
    }

    public String getCompany() {

        Cursor companyCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ? ",
                new String[]{id}, null);

        if(companyCursor.getCount()>0){
            while (companyCursor.moveToNext()) {

                company = companyCursor.getString(companyCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA1));
                String MIMETYPE = companyCursor.getString(companyCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.MIMETYPE));



                if ((company != null)&&(MIMETYPE.equals("vnd.android.cursor.item/organization")))
                {
                   return company;
                }else{

                    company=context.getResources().getString(R.string.importNoAsigned_Cat);
                }

            }
            companyCursor.close();
        }
        return company;
    }
}
