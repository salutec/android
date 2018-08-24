package com.example.srini.sabsesmartidcard;


import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;

import android.app.PendingIntent;
import android.widget.Button;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.net.Uri;
import android.os.Bundle;

import java.lang.Object;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ToggleButton tb1, tb2, tb3, tb4, tb5, tb6, tb7;
    SwitchCompat switchCompat;

    FloatingActionButton fab, info, videos;


    //USB control commands
    //USB control commands
    private static final int COMMAND_LITTLE_TOGGLE = 1;
    private static final int COMMAND_RING_TOGGLE = 2;
    private static final int COMMAND_MIDDLE_TOGGLE = 3;
    private static final int COMMAND_FORE_TOGGLE = 4;
    private static final int COMMAND_THUMB_TOGGLE = 5;
    private static final int COMMAND_ALL_TOGGLE = 6;
    private static final int COMMAND_SWITCH_HAND = 20;
    private static final int COMMAND_STATUS = 9;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";


    // private TextView voiceInput;
    // private TextView speakButton;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    Snackbar snackbar;
    StringBuilder result = new StringBuilder();

    Context context;
    UsbManager usbManager;
    UsbDevice usbDevice;
    UsbInterface intf = null;
    UsbEndpoint input, output;
    UsbDeviceConnection connection;
    Boolean connectionEstablished=false;
    PendingIntent permissionIntent;


    byte[] readBytes = new byte[64];


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        tb1 = (ToggleButton) findViewById(R.id.tb1);
        tb2 = (ToggleButton) findViewById(R.id.tb2);
        tb3 = (ToggleButton) findViewById(R.id.tb3);
        tb4 = (ToggleButton) findViewById(R.id.tb4);
        tb5 = (ToggleButton) findViewById(R.id.tb5);
        tb6 = (ToggleButton) findViewById(R.id.tb6);
        tb7 = (ToggleButton) findViewById(R.id.tb7);
        switchCompat = (SwitchCompat) findViewById(R.id.switchButton);

//        tb2.setBackgroundColor(Color.WHITE);
//        tb2.setTextColor(Color.BLACK);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        info = (FloatingActionButton) findViewById(R.id.info);
        videos = (FloatingActionButton) findViewById(R.id.videos);

        fab.setImageResource(R.drawable.lwl);
        info.setImageResource(R.drawable.speechcommand);
        videos.setImageResource(R.drawable.movie);

        fab.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{0}));
        info.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{0}));
        videos.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{0}));


//        voiceInput = (TextView) findViewById(R.id.voiceInput);
//        speakButton = (TextView) findViewById(R.id.btnSpeak);

        info.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });


        usbManager =  (UsbManager) getSystemService(Context.USB_SERVICE);

        permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter);



        tb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                result.setLength(0);
                if (tb1.isChecked()) {
                    // The toggle is enabled
                    result.append("Closing LITTLE");
                    int tmpInt = 11;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                } else {
                    // The toggle is disabled
                    result.append("Opening LITTLE");
                    int tmpInt = 21;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                }
                snackbar = Snackbar.make(compoundButton, result.toString(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        });

        tb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                result.setLength(0);
                if (tb2.isChecked()) {
                    // The toggle is enabled
                    result.append("Closing RING");
                    int tmpInt = 12;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                } else {
                    // The toggle is disabled
                    result.append("Opening RING");
                    int tmpInt = 22;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                }
                snackbar = Snackbar.make(compoundButton, result.toString(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        });

        tb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                result.setLength(0);
                if (tb3.isChecked()) {
                    // The toggle is enabled
                    result.append("Closing MIDDLE");
                    int tmpInt = 13;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                } else {
                    // The toggle is disabled
                    result.append("Opening MIDDLE");
                    int tmpInt = 23;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                }
                snackbar = Snackbar.make(compoundButton, result.toString(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        });

        tb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                result.setLength(0);
                if (tb4.isChecked()) {
                    // The toggle is enabled
                    result.append("Closing FORE");
                    int tmpInt = 14;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                } else {
                    // The toggle is disabled
                    result.append("Opening FORE");
                    int tmpInt = 24;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                }
                snackbar = Snackbar.make(compoundButton, result.toString(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        });

        tb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                result.setLength(0);
                if (tb5.isChecked()) {
                    // The toggle is enabled
                    result.append("Closing THUMB");
                    int tmpInt = 10;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                } else {
                    // The toggle is disabled
                    result.append("Opening THUMB");
                    int tmpInt = 20;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);
                }
                snackbar = Snackbar.make(compoundButton, result.toString(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        });

        tb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                result.setLength(0);
                if (tb6.isChecked()) {
                    // The toggle is enabled
                    result.append("Closing ALL FINGERS");
                    int tmpInt = 15;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);

                    if (!tb1.isChecked()) {
                        tb1.toggle();
                    }
                    if (!tb2.isChecked()) {
                        tb2.toggle();
                    }
                    if (!tb3.isChecked()) {
                        tb3.toggle();
                    }
                    if (!tb4.isChecked()) {
                        tb4.toggle();
                    }
                    if (!tb5.isChecked()) {
                        tb5.toggle();
                    }

                } else {
                    // The toggle is disabled
                    result.append("Opening ALL FINGERS");
                    int tmpInt = 25;
                    String tmpStr = String.valueOf(tmpInt);
                    send(tmpStr);

                    if (tb1.isChecked()) {
                        tb1.toggle();
                    }
                    if (tb2.isChecked()) {
                        tb2.toggle();
                    }
                    if (tb3.isChecked()) {
                        tb3.toggle();
                    }
                    if (tb4.isChecked()) {
                        tb4.toggle();
                    }
                    if (tb5.isChecked()) {
                        tb5.toggle();
                    }

                }
                snackbar = Snackbar.make(compoundButton, result.toString(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        });

        tb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                result.setLength(0);
                if (tb7.isChecked()) {
                    // The toggle is enabled
                    result.append("Switching to RIGHT HAND");
                } else {
                    // The toggle is disabled
                    result.append("Switching to LEFT HAND");
                }
                snackbar = Snackbar.make(compoundButton, result.toString(), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.GREEN);
                snackbar.show();
            }
        });

        //fab.setImageDrawable(Drawable.createFromPath("\"C:\\Users\\balasubr\\Documents\\Personal\\Suraj\\Suraj LWL\\Limbs-With-Love.png\""));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.limbswithlove.org"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });




 /*       info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "** pressed **", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.BLUE);
                snackbar.show();


                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                result.setLength(0);
                result.append("").append(tb1.getText());
                result.append(":").append(tb2.getText());
                result.append(":").append(tb3.getText());
                result.append(":").append(tb4.getText());
                result.append(":").append(tb5.getText());
                result.append(":").append(tb6.getText());
                result.append("Hand : ").append(tb7.getText());


                snackbar = Snackbar.make(view, result.toString(), Snackbar.LENGTH_LONG)
                        .setAction("SWITCH ENABLE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switchCompat.setChecked(true);
                            }
                        });

                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
            }
        });
*/

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//                Snackbar.make(buttonView, "Controls activation set to " + isChecked, Snackbar.LENGTH_LONG)
//                        .setAction("ACTION", null).show();

                if(isChecked) {
                    connect();

                    snackbar = Snackbar.make(buttonView, listUsbDevices(), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();
                }
                else {
                    stop();
                }
            }
        });

    }


    // Showing google speech input dialog
    private void askSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
        }
    }

    // Receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> speachInput = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    // voiceInput.setText(speachInput.get(0));
                    if (speachInput.get(0).equalsIgnoreCase("little open")){
                        result.append("Opening LITTLE");
                        int tmpInt = 11;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb1.setChecked (false);
                    }
                    if (speachInput.get(0).equalsIgnoreCase("little close")){
                        result.append("Closing LITTLE");
                        int tmpInt = 21;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb1.setChecked (true);
                    }

                    if (speachInput.get(0).equalsIgnoreCase("ring open")){
                        result.append("Opening RING");
                        int tmpInt = 12;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb2.setChecked (false);
                    }
                    if (speachInput.get(0).equalsIgnoreCase("ring close")){
                        result.append("Closing RING");
                        int tmpInt = 22;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb2.setChecked (true);
                    }

                    if (speachInput.get(0).equalsIgnoreCase("middle open")){
                        result.append("Opening MIDDLE");
                        int tmpInt = 13;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb3.setChecked (false);
                    }
                    if (speachInput.get(0).equalsIgnoreCase("middle close")){
                        result.append("Closing MIDDLE");
                        int tmpInt = 23;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb3.setChecked (true);
                    }

                    if (speachInput.get(0).equalsIgnoreCase("for open")){
                        result.append("Opening FORE");
                        int tmpInt = 14;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb4.setChecked (false);
                    }
                    if (speachInput.get(0).equalsIgnoreCase("for close")){
                        result.append("Closing FORE");
                        int tmpInt = 24;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb4.setChecked (true);
                    }

                    if (speachInput.get(0).equalsIgnoreCase("thumb open")){
                        result.append("Opening THUMB");
                        int tmpInt = 10;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb5.setChecked (false);
                    }
                    if (speachInput.get(0).equalsIgnoreCase("thumb close")){
                        result.append("Closing THUMB");
                        int tmpInt = 20;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb5.setChecked (true);
                    }

                    if (speachInput.get(0).equalsIgnoreCase("all open")){
                        result.append("Opening ALL");
                        int tmpInt = 15;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb6.setChecked (false);
                    }
                    if (speachInput.get(0).equalsIgnoreCase("all close")){
                        result.append("Closing ALL");
                        int tmpInt = 25;
                        String tmpStr = String.valueOf(tmpInt);
                        send(tmpStr);
                        tb6.setChecked (true);
                    }

                    if (speachInput.get(0).equalsIgnoreCase("activate control")){
                        result.append("Controls Activated");
                        connect();
                        switchCompat.setChecked(true);
                    }
                    if (speachInput.get(0).equalsIgnoreCase("deactivate control")){
                        result.append("Controls Deactivated");
                        stop();
                        switchCompat.setChecked(false);
                    }

                    if (speachInput.get(0).equalsIgnoreCase("left hand")){
                        result.append("Switching to RIGHT HAND");
                        if (!tb7.isChecked()) {
                            tb7.toggle();
                        }
                    }
                    if (speachInput.get(0).equalsIgnoreCase("right hand")){
                        result.append("Switching to LEFT HAND");
                        if (tb7.isChecked()) {
                            tb7.toggle();
                        }
                    }

                    if (speachInput.get(0).equalsIgnoreCase("open limbs with love")){
                        Uri uri = Uri.parse("http://www.limbswithlove.org"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                    if (speachInput.get(0).equalsIgnoreCase("open tutorial")){
                        Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                    snackbar = Snackbar.make(tb1, result.toString(), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.GREEN);
                    snackbar.show();
                    result.delete(0, 100);
                }
                break;
            }
        }
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


//    public void UsbCommunicationManager(Context context)
//    {
//        this.context = context;
//        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//
//        // ask permission from user to use the usb device
//        permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
//        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
//        context.registerReceiver(usbReceiver, filter);
//
//        return;
//    }

    public void connect()
    {
        // check if there's a connected usb device
        if(usbManager.getDeviceList().isEmpty())
        {
            Log.d("LWL", "No connected devices");
            return;
        }
        result.append("Device found");

        // get the first (only) connected device
        usbDevice = usbManager.getDeviceList().values().iterator().next();

        // user must approve of connection
        usbManager.requestPermission(usbDevice, permissionIntent);
    }

    public void stop() {
        if (connectionEstablished == true) {
            unregisterReceiver(usbReceiver);
            connectionEstablished = false;
        }
    }

    public String send(String data)
    {
        result.setLength(0);

        if(usbDevice == null)
        {
            result.append("LWL : no usb device selected.");
            return "no usb device selected";
        }

        int sentBytes = 0;
        if(!data.equals(""))
        {
            result.append("Data Sent : ").append(data);

            synchronized(this)
            {
                // send data to usb device
                byte[] bytes = data.getBytes();

                sentBytes = connection.bulkTransfer(output, bytes, bytes.length, 1000);

                result.append(" :: Bytes Sent : ").append(sentBytes);
            }
        }

        snackbar = Snackbar.make(tb1, result.toString(), Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();

        return Integer.toString(sentBytes);
    }

    public String read()
    {
        // reinitialize read value byte array
        Arrays.fill(readBytes, (byte) 0);

        // wait for some data from the mcu
        int recvBytes = connection.bulkTransfer(input, readBytes, readBytes.length, 3000);

        if(recvBytes > 0)
        {
            Log.d("LWL", "Got some data: " + new String(readBytes));
        }
        else
        {
            Log.d("LWL", "Did not get any data: " + recvBytes);
        }

        return Integer.toString(recvBytes);
    }

    public String listUsbDevices()
    {
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

        if(deviceList.size() == 0)
        {
            return "no usb devices found";
        }

        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        String returnValue = "";
        UsbInterface usbInterface;

        while(deviceIterator.hasNext())
        {
            UsbDevice device = deviceIterator.next();
            returnValue += "Name: " + device.getDeviceName();
            returnValue += "\nID: " + device.getDeviceId();
            returnValue += "\nProtocol: " + device.getDeviceProtocol();
            returnValue += "\nClass: " + device.getDeviceClass();
            returnValue += "\nSubclass: " + device.getDeviceSubclass();
            returnValue += "\nProduct ID: " + device.getProductId();
            returnValue += "\nVendor ID: " + device.getVendorId();
            returnValue += "\nInterface count: " + device.getInterfaceCount();

            for(int i = 0; i < device.getInterfaceCount(); i++)
            {
                usbInterface = device.getInterface(i);
                returnValue += "\n  Interface " + i;
                returnValue += "\n\tInterface ID: " + usbInterface.getId();
                returnValue += "\n\tClass: " + usbInterface.getInterfaceClass();
                returnValue += "\n\tProtocol: " + usbInterface.getInterfaceProtocol();
                returnValue += "\n\tSubclass: " + usbInterface.getInterfaceSubclass();
                returnValue += "\n\tEndpoint count: " + usbInterface.getEndpointCount();

                for(int j = 0; j < usbInterface.getEndpointCount(); j++)
                {
                    returnValue += "\n\t  Endpoint " + j;
                    returnValue += "\n\t\tAddress: " + usbInterface.getEndpoint(j).getAddress();
                    returnValue += "\n\t\tAttributes: " + usbInterface.getEndpoint(j).getAttributes();
                    returnValue += "\n\t\tDirection: " + usbInterface.getEndpoint(j).getDirection();
                    returnValue += "\n\t\tNumber: " + usbInterface.getEndpoint(j).getEndpointNumber();
                    returnValue += "\n\t\tInterval: " + usbInterface.getEndpoint(j).getInterval();
                    returnValue += "\n\t\tType: " + usbInterface.getEndpoint(j).getType();
                    returnValue += "\n\t\tMax packet size: " + usbInterface.getEndpoint(j).getMaxPacketSize();
                }
            }
        }

        return returnValue;
    }

    private void setupConnection()
    {
        result.setLength(0);

        // find the right interface
        for(int i = 0; i < usbDevice.getInterfaceCount(); i++)
        {
            // communications device class (CDC) type device
            //if(usbDevice.getInterface(i).getInterfaceClass() == UsbConstants.USB_CLASS_COMM)
            //{
            intf = usbDevice.getInterface(i);

            // find the endpoints
            for(int j = 0; j < intf.getEndpointCount(); j++)
            {
                if(intf.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_OUT && intf.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
                {
                    // from android to device
                    output = intf.getEndpoint(j);

                }

                if(intf.getEndpoint(j).getDirection() == UsbConstants.USB_DIR_IN && intf.getEndpoint(j).getType() == UsbConstants.USB_ENDPOINT_XFER_BULK)
                {
                    // from device to android
                    input = intf.getEndpoint(j);

                }
            }
            // }
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(ACTION_USB_PERMISSION.equals(action))
            {
                // broadcast is like an interrupt and works asynchronously with the class, it must be synced just in case
                synchronized(this)
                {
                    if(intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                    {
                        setupConnection();

                        connection = usbManager.openDevice(usbDevice);
                        connection.claimInterface(intf, true);

                        // set flow control to 8N1 at 38400 baud
                        int baudRate = 38400;
                        byte stopBitsByte = 1;
                        byte parityBitesByte = 0;
                        byte dataBits = 8;
                        byte[] msg = {
                                (byte) (baudRate & 0xff),
                                (byte) ((baudRate >> 8) & 0xff),
                                (byte) ((baudRate >> 16) & 0xff),
                                (byte) ((baudRate >> 24) & 0xff),
                                stopBitsByte,
                                parityBitesByte,
                                (byte) dataBits
                        };

                        connectionEstablished=true;
                        snackbar = Snackbar.make(tb1, result.toString(), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.GREEN);
                        snackbar.show();


                        connection.controlTransfer(UsbConstants.USB_TYPE_CLASS | 0x01, 0x20, 0, 0, msg, msg.length, 5000);
                    }
                    else
                    {
                        Log.d("LWL", "Permission denied for USB device");
                    }
                }
            }
            else if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
            {
                Log.d("LWL", "USB device detached");
            }
        }
    };
}