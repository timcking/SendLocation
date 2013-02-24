package com.timothyking.sendlocation;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendLocation extends Activity implements LocationListener {
    /** Called when the activity is first created. */
    private LocationManager myManager;
    private EditText tv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // get a handle to our TextView so we can write to it later
        tv = (EditText) findViewById(R.id.EditText01);

        // set up the LocationManager
        myManager = (LocationManager) getSystemService(LOCATION_SERVICE); 
    }
    
    public void myClickHandler(View view) {
        String s = (tv.getText().toString());
        String strDate = null;
        
        Calendar cal = Calendar.getInstance(); 

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm z");

        if (cal != null) {
            strDate = sdf.format(cal.getTime());
        }
        else {
            strDate = "Date Unknown";
        }
        
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"timcking@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Location " + strDate);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
        startActivity(Intent.createChooser(emailIntent, "Send location..."));
    }    
    
    @Override
    protected void onDestroy() {
        stopListening();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        stopListening();
        super.onPause();
    }

    @Override
    protected void onResume() {
        startListening();
        super.onResume();
    }

    /**********************************************************************
     * helpers for starting/stopping monitoring of GPS changes below 
     **********************************************************************/
    private void startListening() {
        myManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                5000, 
                5, 
                this
        );
    }

    private void stopListening() {
        if (myManager != null)
                myManager.removeUpdates(this);
    }

    /**********************************************************************
     * LocationListener overrides below 
     **********************************************************************/
    // @Override
    public void onLocationChanged(Location location) {
        // we got new location info. lets display it in the textview
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        String short_lat = lat.substring(0, (Integer) lat.indexOf(".") + 7);
        String short_lon = lon.substring(0, (Integer) lon.indexOf(".") + 7);

        tv.setText(short_lat + "," + short_lon);
    }    

    // @Override
    public void onProviderDisabled(String provider) {}    

    // @Override
    public void onProviderEnabled(String provider) {}    

    // @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}