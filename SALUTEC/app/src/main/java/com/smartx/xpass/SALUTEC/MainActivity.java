package com.smartx.xpass.SALUTEC;

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

import android.view.MenuInflater;

import com.smartx.xpass.xpass.R;

import org.altbeacon.beacon.startup.BootstrapNotifier;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.smartx.xpass.xpass.R;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.smartx.xpass.xpass.R;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

public class MainActivity extends AppCompatActivity implements BootstrapNotifier {

    public FloatingActionButton email, talk, phone, bell, fire, police, medical;
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


    private static final String TAG = "BeaconReferenceApp";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MonitoringActivity monitoringActivity = null;






    @SuppressLint({"MissingPermission", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
        // find a different type of beacon, you must specify the byte layout for that beacon's
        // advertisement with a line like below.  The example shows how to find a beacon with the
        // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb.  To find the proper
        // layout expression for other beacon types, do a web search for "setBeaconLayout"
        // including the quotes.

        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        beaconManager.setRegionStatePersistenceEnabled(false);

        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));


//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-1=5900,i:2-2,i:3-4,p:5-5"));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT));

        // Uncomment the code below to use a foreground service to scan for beacons. This unlocks
        // the ability to continually scan for long periods of time in the background on Andorid 8+
        // in exchange for showing an icon at the top of the screen and a always-on notification to
        // communicate to users that your app is using resources in the background.

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Scanning for Beacons");
        Intent intent = new Intent(this, MonitoringActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pendingIntent);
        beaconManager.enableForegroundServiceScanning(builder.build(), 456);

        // For the above foreground scanning service to be useful, you need to disable
        // JobScheduler-based scans (used on Android 8+) and set a fast background scan
        // cycle that would otherwise be disallowed by the operating system.

        beaconManager.setEnableScheduledScanJobs(false);
        beaconManager.setBackgroundBetweenScanPeriod(0);
        beaconManager.setBackgroundScanPeriod(1100);



        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        // If you wish to test beacon detection in the Android Emulator, you can use code like this:
//        BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
//        ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();






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
    public void didEnterRegion(Region arg0) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.


//        Log.d(TAG, "did enter region.");
        if (!haveDetectedBeaconsSinceBoot) {
//            Log.d(TAG, "auto launching MainActivity");
            snackbar = Snackbar.make(fire, "SALUTEC : did enter region first time.", Snackbar.LENGTH_LONG);
            snackbar.show();

            // The very first time since boot that we detect an beacon, we launch the
            // MainActivity

            Intent intent = new Intent(this, MonitoringActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Important:  make sure to add android:launchMode="singleInstance" in the manifest
            // to keep multiple copies of this activity from getting created if the user has
            // already manually launched the app.

            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
            snackbar = Snackbar.make(fire, "SALUTEC : did enter region subsequent times.", Snackbar.LENGTH_LONG);
            snackbar.show();

            if (monitoringActivity != null) {
                // If the Monitoring Activity is visible, we log info about the beacons we have
                // seen on its display
                monitoringActivity.logToDisplay("I see a beacon again" );
            } else {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.
                Log.d(TAG, "Sending notification.");
                sendNotification();
            }
        }


    }

    @Override
    public void didExitRegion(Region region) {
        if (monitoringActivity != null) {
            monitoringActivity.logToDisplay("I no longer see a beacon.");
        }
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        if (monitoringActivity != null) {
            monitoringActivity.logToDisplay("I have just switched from seeing/not seeing beacons: " + state);
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Beacon Reference Application")
                        .setContentText("An beacon is nearby.")
                        .setSmallIcon(R.drawable.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MonitoringActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void setMonitoringActivity(MonitoringActivity activity) {
        this.monitoringActivity = activity;
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
        if (id == R.id.action_emergency_contacts) {
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_pair) {
            if(monitoringActivity != null) {
                monitoringActivity.onRangingClicked();
            }
//            snackbar = Snackbar.make(fire, "SALUTEC card pairing initiated.", Snackbar.LENGTH_LONG);
//            snackbar.show();

            return true;
        }
        if (id == R.id.action_about) {
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
