package com.revosleap.text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.onegravity.contactpicker.ContactElement;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.group.Group;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String EXTRA_DARK_THEME = "EXTRA_DARK_THEME";
    private static final String EXTRA_GROUPS = "EXTRA_GROUPS";
    private static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";
    private static final  int PERMISSIONS_SEND_SMS=100;

    private static final int REQUEST_CONTACT = 0;

    private boolean mDarkTheme;
    private List<Contact> mContacts;
    private List<Group> mGroups;
    TextView mssge;

    Button btnsend;

    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // read parameters either from the Intent or from the Bundle
        if (savedInstanceState != null) {
            mDarkTheme = savedInstanceState.getBoolean(EXTRA_DARK_THEME);
            mGroups = (List<Group>) savedInstanceState.getSerializable(EXTRA_GROUPS);
            mContacts = (List<Contact>) savedInstanceState.getSerializable(EXTRA_CONTACTS);
        }
        else {
            Intent intent = getIntent();
            mDarkTheme = intent.getBooleanExtra(EXTRA_DARK_THEME, false);
            mGroups = (List<Group>) intent.getSerializableExtra(EXTRA_GROUPS);
            mContacts = (List<Contact>) intent.getSerializableExtra(EXTRA_CONTACTS);
        }

       // setTheme(mDarkTheme ? R.style.Theme_Dark : R.style.Theme_Light);

        // set layout
        setContentView(R.layout.activity_main);
        btnsend= findViewById(R.id.buttonsend);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateContactList();
            }
        });


        msg = getIntent().getStringExtra("Value");
        mssge= findViewById(R.id.messagetype);
        mssge.setText(msg);

        // configure "pick contact(s)" button
        Button button = findViewById(R.id.pick_contact);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ContactPickerActivity.class)
                           // .putExtra(ContactPickerActivity.EXTRA_THEME, mDarkTheme ?
                                   // R.style.Theme_Dark : R.style.Theme_Light)

                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE,
                                    ContactPictureType.ROUND.name())

                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION,
                                    ContactDescription.ADDRESS.name())
                            .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                            .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT, 0)
                            .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, false)
                            //.putExtra(ContactPickerActivity.EXTRA_WITH_GROUP_TAB, false)

                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE,
                                    ContactsContract.CommonDataKinds.Email.TYPE_WORK)

                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER,
                                    ContactSortOrder.AUTOMATIC.name());

                    startActivityForResult(intent, REQUEST_CONTACT);
                }
            });
        }
        else {
            finish();
        }

        // populate contact list
//        populateContactList(mGroups, mContacts);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(EXTRA_DARK_THEME, mDarkTheme);
        if (mGroups != null) {
            outState.putSerializable(EXTRA_GROUPS, (Serializable) mGroups);
        }
        if (mContacts != null) {
            outState.putSerializable(EXTRA_CONTACTS, (Serializable) mContacts);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK && data != null &&
                (data.hasExtra(ContactPickerActivity.RESULT_GROUP_DATA) ||
                        data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA))) {

            // we got a result from the contact picker --> show the picked contacts
            mGroups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
            mContacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
//            populateContactList(mGroups, mContacts);
        }
    }

    private void sendSMS(String number){

        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(number,null,msg,null,null);
        Toast.makeText(MainActivity.this, "Sent to: "+number, Toast.LENGTH_SHORT).show();
    }

    private void populateContactList(/*List<Group> groups, final List<Contact> contacts*/) {
        // we got a result from the contact picker --> show the picked contacts
       // TextView contactsView = (TextView) findViewById(R.id.contacts);
        SpannableStringBuilder result = new SpannableStringBuilder();

        try { for ( int i = 0; i<mContacts.size(); i++) {
            if (mGroups != null && !mGroups.isEmpty()) {
                result.append("GROUPS\n");
                for (Group group : mGroups) {
                    populateContact(result, group, "");
                    for (Contact contact : group.getContacts()) {
                        populateContact(result, contact, "    ");
                    }
                }
            }
            if (mContacts != null && !mContacts.isEmpty()) {
                result.append("CONTACTS\n");
                for (Contact contact : mContacts) {
                    populateContact(result, contact, "");
                }
            }
             String hoe= mContacts.get(i).getPhone(i);

            sendSMS(hoe);
        }

        }

        catch (Exception e) {
            result.append(e.getMessage());
        }

    }

    private void populateContact(SpannableStringBuilder result, ContactElement element, String prefix) {
        //int start = result.length();
        String displayName = element.getDisplayName();
        result.append(prefix);
        result.append(displayName + "\n");
        //result.setSpan(new BulletSpan(15), start, result.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        getMenuInflater().inflate(R.menu.contact_picker_demo, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//
//        int textId = mDarkTheme ? R.string.light_theme : R.string.dark_theme;
//        menu.findItem(R.id.action_theme).setTitle(textId);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_theme) {
//            mDarkTheme = ! mDarkTheme;
//            Intent intent = new Intent(this, this.getClass())
//                    .putExtra(EXTRA_DARK_THEME, mDarkTheme);
//            if (mGroups != null) {
//                intent.putExtra(EXTRA_GROUPS, (Serializable) mGroups);
//            }
//            if (mContacts != null) {
//                intent.putExtra(EXTRA_CONTACTS, (Serializable) mContacts);
//            }
//            startActivity(intent);
//            finish();
//            return true;
//        }
//
//        return false;
//    }
}