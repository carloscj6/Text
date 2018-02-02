package com.revosleap.text;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Welcome extends AppCompatActivity {
    TextView greetings,user;
    Button compose;
    EditText message;
    ImageView imageView;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS=100;
    private static final  int PERMISSIONS_SEND_SMS=100;
    private static final int PERMISSION_READ_PROFILE=100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        greetings=findViewById(R.id.greetings);
        compose= findViewById(R.id.compose);
        message= findViewById(R.id.message);
        imageView = findViewById(R.id.image);
        user=findViewById(R.id.user);




        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            Cursor c =getApplication().getContentResolver().query(ContactsContract.Profile.CONTENT_URI,
                    null,null,null,null);
            c.moveToFirst();
            user.setText(c.getString(c.getColumnIndex("display_name")));
            c.close();
        }
        else {ActivityCompat.requestPermissions(Welcome.this,
                new String[]{Manifest.permission.READ_CONTACTS},
                PERMISSIONS_REQUEST_READ_CONTACTS); }

        Calendar hr = Calendar.getInstance();
        int currenthr= hr.get(Calendar.HOUR_OF_DAY);
        if (currenthr <=11){
        greetings.setText("Good Morning");
        imageView.setImageResource(R.drawable.sunrise);}
        else if (currenthr <=15){
            greetings.setText("Good Afternoon");
            imageView.setImageResource(R.drawable.afternoon);
        }
        else if (currenthr <=19){
            greetings.setText("Good Evening");
            imageView.setImageResource(R.drawable.sunset);}
            else {
            greetings.setText("Good Evening");
            imageView.setImageResource(R.drawable.night);
        }
final int perm = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.SEND_SMS);
        //if (perm== PackageManager.PERMISSION_GRANTED) {


            compose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = message.getText().toString();
                    if (msg.isEmpty()) {

                        Toast.makeText(Welcome.this, "No message typed", Toast.LENGTH_SHORT).show();
                    } else { if (perm==PackageManager.PERMISSION_GRANTED){

                        Intent i = new Intent(Welcome.this, MainActivity.class);
                        i.putExtra("Value", msg);
                        startActivity(i);
                    }
                    else { ActivityCompat.requestPermissions(Welcome.this,
                            new  String[]{Manifest.permission.SEND_SMS},
                            PERMISSIONS_SEND_SMS);}}

                }
            });
            return;

    }



}
