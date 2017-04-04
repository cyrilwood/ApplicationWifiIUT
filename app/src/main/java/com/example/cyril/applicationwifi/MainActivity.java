package com.example.cyril.applicationwifi;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.List;
//import java.util.jar.Manifest;
import java.util.regex.Matcher;

// MainActivity qui est héritée de AppCompatActivity
public class MainActivity extends AppCompatActivity
{
    //Déclaration des variables
    int x;
    int y;
    ImageView ImageIUT = null;
    TextView TextIUT = null;
    TextView TextRSSI = null;
    int[] viewCoords = new int[2];

    public double calculateDistance(double levelInDb, double freqInMHZ)
    {
        double exp = (27.55 - (20 * Math.log10(freqInMHZ)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE}, 1);


        ImageIUT = (ImageView) findViewById(R.id.imageView); // Association entre l'image sur le xml avec la variable qu'on a creer
        TextIUT = (TextView) findViewById(R.id.textView); // Association entre le text sur le xml avec la variable qu'on a creer
        ImageIUT.getLocationOnScreen(viewCoords);
        ImageIUT.setOnTouchListener(event); //Appelle la fonction "event"


    }
    public View.OnTouchListener event = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            int   touchX =  (int)  event.getX(); // Cherche la postion x
            int   touchY =  (int)  event.getY(); // Cherche la position y
            int   imageX =  touchX - viewCoords[0]; // Cherche les coordonées de l'image X
            int   imageY =  touchY - viewCoords[1]; // Cherche les coordonées de l'image Y
            TextIUT.setText("X :"+imageX+"Y :"+imageY); //println

            final WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE); //On utilise le wifiManager qui va permettre de scanner les wifi qui sont proche du wifi "etudiants-Paris12"

            registerReceiver(new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context c, Intent intent)
                {
                    List<ScanResult> results = wifi.getScanResults(); // List les résultats de scan dans wifi

                    //Déclaration des variables
                    int rssi=100;
                    int chan=-1;
                    String mac="";

                    for (ScanResult s : results)
                    {
                        if(s.SSID.equals("Etudiants-Paris12")& Math.abs(s.level)<rssi)
                        {
                            //On attribue a chaque variable son terme technique
                            rssi=Math.abs(s.level);
                            mac=s.BSSID;
                            chan=s.frequency;
                        }
                    }
                    DecimalFormat df = new DecimalFormat("#.##"); //On attribue un nouveau format de type: "#.##"
                    TextIUT.setText("Etudiants-Paris12 BSSID : "+mac+" RSSI : "+rssi+" , Distance : "+ //Dans le champ text on va faire afficher l'accès wifi, l'adresse mac et la distance"
                            df.format(calculateDistance((double) rssi, chan)) + "m" +"canal: "+ chan);

                }
            }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

            wifi.startScan(); //Active le scan wifi
            return true;

        }
    };



}