package com.smartx.xpass.xpass;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import android.net.Uri;

import java.util.ArrayList;

import android.speech.SpeechRecognizer;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import java.util.Timer;

import android.view.View.OnTouchListener;
import android.view.InputEvent;
import android.widget.Button;
import android.os.Handler;
import android.view.MotionEvent;
import android.os.SystemClock;
import android.graphics.Color;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.os.Build.VERSION;
import android.content.Intent;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    FloatingActionButton email, talk, phone, bell, fire, police, medical;
    Snackbar snackbar;
    StringBuilder result = new StringBuilder();
    private int PRESS_DOWN_TIMER = 3000;
    long fire_duration, police_duration, medical_duration, bell_duration = 0L;
    boolean alarmBellInitiated = false;
    boolean alarmFireInitiated = false;
    boolean alarmPoliceInitiated = false;
    boolean alarmMedicalInitiated = false;

    private LocationManager locationManager;
    private LocationListener listener;
    private StringBuilder locationString = new StringBuilder();


    @SuppressLint({"MissingPermission", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        email = (FloatingActionButton) findViewById(R.id.email);
        talk = (FloatingActionButton) findViewById(R.id.talk);
        phone = (FloatingActionButton) findViewById(R.id.phone);
        bell = (FloatingActionButton) findViewById(R.id.bell);
        fire = (FloatingActionButton) findViewById(R.id.fire);
        police = (FloatingActionButton) findViewById(R.id.police);
        medical = (FloatingActionButton) findViewById(R.id.medical);

        email.setImageResource(R.drawable.email);
        talk.setImageResource(R.drawable.mic);
        phone.setImageResource(R.drawable.phone);
        bell.setImageResource(R.drawable.bell);
        fire.setImageResource(R.drawable.fire);
        police.setImageResource(R.drawable.police);
        medical.setImageResource(R.drawable.medical);



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                locationString.setLength(0);
                locationString.append("Location : " + location.getLongitude() + " " + location.getLatitude());
                snackbar = Snackbar.make(bell, locationString, Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };



        final FloatingActionButton bell = (FloatingActionButton) findViewById(R.id.bell);
        bell.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        bell_duration = SystemClock.elapsedRealtime();
                        if (alarmBellInitiated == true) {
                            alarmBellInitiated = false;
                            snackbar = Snackbar.make(bell, "Alarm sound terminated.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            stopPositionLocation();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        bell_duration = SystemClock.elapsedRealtime() - bell_duration;
                        if (bell_duration > PRESS_DOWN_TIMER) {
                            alarmBellInitiated = true;
                            getPositionLocation();
                            snackbar = Snackbar.make(bell, "Alarm sound initiated.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                        break;
                    default:
                        break;
                }


                return true; // or whatever you want to return.
            }
        });



        final FloatingActionButton fire = (FloatingActionButton) findViewById(R.id.fire);
        fire.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        fire_duration = SystemClock.elapsedRealtime();
                        if (alarmFireInitiated == true) {
                            alarmFireInitiated = false;
                            snackbar = Snackbar.make(fire, "Alarm fire terminated.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            stopPositionLocation();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        fire_duration = SystemClock.elapsedRealtime() - fire_duration;
                        if (fire_duration > PRESS_DOWN_TIMER) {
                            alarmFireInitiated = true;
                            getPositionLocation();
                            snackbar = Snackbar.make(fire, "Alarm fire initiated.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                        break;
                    default:
                        break;
                }


                return true;
            }
        });

        final FloatingActionButton police = (FloatingActionButton) findViewById(R.id.police);
        police.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        police_duration = SystemClock.elapsedRealtime();
                        if (alarmPoliceInitiated == true) {
                            alarmPoliceInitiated = false;
                            snackbar = Snackbar.make(fire, "Alarm police terminated.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            stopPositionLocation();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        police_duration = SystemClock.elapsedRealtime() - police_duration;
                        if (police_duration > PRESS_DOWN_TIMER) {
                            alarmPoliceInitiated = true;
                            getPositionLocation();
                            snackbar = Snackbar.make(fire, "Alarm police initiated.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                        break;
                    default:
                        break;
                }


                return true;
            }
        });



        final FloatingActionButton medical = (FloatingActionButton) findViewById(R.id.medical);
        medical.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        medical_duration = SystemClock.elapsedRealtime();
                        if (alarmMedicalInitiated == true) {
                            alarmMedicalInitiated = false;
                            snackbar = Snackbar.make(fire, "Alarm medical terminated.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            stopPositionLocation();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        medical_duration = SystemClock.elapsedRealtime() - medical_duration;
                        if (medical_duration > 4000) {
                            alarmMedicalInitiated = true;
                            getPositionLocation();
                            snackbar = Snackbar.make(fire, "Alarm medical initiated.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                        break;
                    default:
                        break;
                }
                return true;
            }
        });





        FloatingActionButton email = (FloatingActionButton) findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        FloatingActionButton phone = (FloatingActionButton) findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                String p = "tel:" + getString(R.string.phone_number);
                i.setData(Uri.parse(p));
                startActivity(i);            }
        });

        talk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                getPositionLocation();
                break;
            default:
                break;
        }
    }


    void getPositionLocation() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        else {
            locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            snackbar = Snackbar.make(fire, "Location started.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    void stopPositionLocation() {

        if ((alarmBellInitiated == true) ||
                (alarmFireInitiated == true) ||
                (alarmPoliceInitiated == true) ||
                (alarmMedicalInitiated == true)) {
            return;
        }

        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        else {
            locationManager.removeUpdates(listener);
            snackbar = Snackbar.make(fire, "Location stoped.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public static String locationStringFromLocation(final Location location) {
        return Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + " " + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Showing google speech input dialog
    private void askSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
        }
    }


    // Receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speachInput = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (speachInput.get(0).equalsIgnoreCase("call security")){
                        Uri uri = Uri.parse("http://www.ucsd.edu"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }


                    snackbar = Snackbar.make(bell, speachInput.get(0), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                break;
            }
        }
    }

    protected void sendEmail() {

        String[] TO = {"someone@gmail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request to campus security");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello Campus Security, \n\n <include your request here>");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            snackbar = Snackbar.make(bell, "Launching email client ...", Snackbar.LENGTH_LONG);
            snackbar.show();

        } catch (android.content.ActivityNotFoundException ex) {
            snackbar = Snackbar.make(bell, "There is no email client installed.", Snackbar.LENGTH_LONG);
            snackbar.show();

        }
    }

}
