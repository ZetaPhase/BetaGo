package com.zetaphase.betago;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

class ReloadThread extends Thread {
    private BetaGo activity;

    ReloadThread(BetaGo activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        HttpClient client = new DefaultHttpClient();
        TelephonyManager tMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        Location location = activity.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String zipCode = addresses.get(0).getPostalCode().toString();
            String urlString = "http://192.168.1.65/getTitle?zipCode=" + zipCode;
            Log.d("SOMETHING", urlString);
            HttpGet httpGet = new HttpGet(urlString);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseString = null;
            Log.d("INRESPONSETHREAD", "responsethread");
            try {
                responseString = client.execute(httpGet, responseHandler);
                Log.d("RESPONSESTRING", responseString);
                //String lines[] = responseString.split("\\r?\\n");
                //Log.d("LINES", String.valueOf(lines.length));
                //Log.d("LINES", String.valueOf(lines));
                Intent intent = new Intent(activity, ReloadListActivity.class);
                String message = responseString;
                intent.putExtra(activity.EXTRA_MESSAGE, message);
                activity.startActivityForResult(intent, activity.MY_REQUEST_CODE);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("IOEXCEPTION", "IOEXCEPTION");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class MarkerThread extends Thread {
    private BetaGo activity;

    MarkerThread(BetaGo activity) {
        this.activity = activity;
    }

    private static HashMap<String, String> helperMap(String t) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);

        }

        return map;
    }

    public static HashMap<String, HashMap<String, String>> jsonToMap(String t) throws JSONException {

        HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            HashMap<String, String> value = helperMap(jObject.getString(key));
            map.put(key, value);

        }

        return map;
    }

    private StringBuffer request(String urlString, JSONObject jsonObj) {
        // TODO Auto-generated method stub

        StringBuffer chaine = new StringBuffer("");
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.connect();

            Log.d("REQUESTOUTPUT", "requesting");
            byte[] b = jsonObj.toString().getBytes();
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(b);


            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }

        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }

        return chaine;
    }

    private void post() throws IOException, JSONException {
        Log.d("POST", "posting");
        Log.d("POSTLM", String.valueOf(activity.lm));
        Location location = activity.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();
        TelephonyManager tMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();

        JSONObject markerMap = new JSONObject();
        Log.d("MARKERLAT", String.valueOf(activity.markerLat.size()));
        for (int i = 0; i < activity.markerLat.size(); i++) {
            Log.d("INLOOP", String.valueOf(i));
            JSONObject tmpMarker = new JSONObject();
            tmpMarker.put("lat", activity.markerLat.get(i));
            tmpMarker.put("lng", activity.markerLng.get(i));
            tmpMarker.put("description", activity.descriptions.get(i));

            tmpMarker.put("image", activity.imageMap.get("Snap" + String.valueOf(i + 1) + ".jpg"));

            markerMap.put("Snap" + String.valueOf(i + 1), tmpMarker);
        }

        JSONArray latList = new JSONArray();
        JSONArray lngList = new JSONArray();
        for (int i = 0; i < activity.myRecord.size(); i++) {
            latList.put(activity.myRecord.get(i).latitude);
            lngList.put(activity.myRecord.get(i).longitude);
        }

        JSONArray zipCodeList = new JSONArray();
        for (int i = 0; i < activity.myRecord.size(); i++) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(activity, Locale.getDefault());
            addresses = geocoder.getFromLocation(latList.getDouble(i), lngList.getDouble(i), 1);
            if (jsonHas(zipCodeList, addresses.get(0).getPostalCode().toString()) != true) {
                Log.d("JSONDOESNOTHAVE", addresses.get(0).getPostalCode().toString());
                zipCodeList.put(addresses.get(0).getPostalCode().toString());
            }
        }

        JSONObject j = new JSONObject();
        j.put("lat", latList);
        j.put("lng", lngList);
        j.put("markerMap", markerMap);
        j.put("phone", mPhoneNumber);
        j.put("title", activity.recordName);
        j.put("zipCodeList", zipCodeList);
        Log.d("MYRECORDSTRING", j.toString());

        StringBuffer a = request("http://192.168.1.65/json", j);


        final String response = a.toString();
        Log.d("POSTRESPONSE", response);
        Log.d("POSTRESPONSE", String.valueOf((response == "successful")));
        if (response.equals("successful")) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TOASTRUNNING", "toastrunning");
                    Toast.makeText(activity.getBaseContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public boolean jsonHas(JSONArray array, String value) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            if (array.getString(i).equals(value)) {
                Log.d("JSONEQUALS", array.getString(i) + " " + value);
                return true;
            } else {
                Log.d("NOTEQUALSJSON", array.getString(i) + " " + value);
            }
        }
        return false;
    }

    @Override
    public void run() {
        try {
            post();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


class RecordTask extends TimerTask {
    private BetaGo activity;

    RecordTask(BetaGo activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        Location location = activity.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        final LatLng currentPos = new LatLng(latitude, longitude);
        activity.myRecord.add(currentPos);
        if (activity.recordPrevious != null) {
            List<LatLng> polylist = new ArrayList<LatLng>();
            polylist.add(activity.recordPrevious);
            polylist.add(currentPos);
            final List<LatLng> tmpPolylist = polylist;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.mMap.addPolyline(new PolylineOptions()
                            .addAll(tmpPolylist)
                            .width(12)
                            .color(Color.parseColor("#05b1fb"))
                            .geodesic(true));
                    Toast.makeText(activity.getBaseContext(), "Recording...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        activity.recordPrevious = currentPos;
        Log.d("RECORDING", String.valueOf(activity.myRecord));
    }
}


class FirstTask extends TimerTask {

    private BetaGo activity;

    FirstTask(BetaGo activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        MarkerThread thread = new MarkerThread(this.activity);
        thread.start();
    }
};

public class BetaGo extends FragmentActivity implements OnMapReadyCallback {

    public static final int ARRAY_SIZE = 10;
    public static final String EXTRA_MESSAGE = "com.zetaphase.betago.MESSAGE";
    public static final int MY_REQUEST_CODE = 12345;
    public static final int CAM_REQUEST = 7850;
    public GoogleMap mMap;
    public double[] latlist = new double[ARRAY_SIZE];
    public double[] longlist = new double[ARRAY_SIZE];
    public int pointer = 0;
    public LocationManager lm;
    public Marker marker;
    public HashMap<String, Marker> markerMap = new HashMap<String, Marker>();
    public List<LatLng> myRecord = new ArrayList<LatLng>();
    public String recordName = "Untitled";
    public LatLng recordPrevious = null;
    public int snapNumber = 1;
    public List<Double> markerLat = new ArrayList<Double>();
    public List<Double> markerLng = new ArrayList<Double>();
    public List<String> descriptions = new ArrayList<String>();
    public String reloadName = "";
    public String snapName;
    public HashMap<String, String> imageMap = new HashMap<String, String>();
    public Timer recordTimer;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAM_REQUEST) {
            Log.d("CAMERAINTENT", String.valueOf(data));
            Bundle extras = data.getExtras();
            Bitmap myBitmap = (Bitmap) extras.get("data");
            Log.d("IMAGEBITMAP", String.valueOf(myBitmap));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (myBitmap != null) {
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            }
            byte[] byteImage_photo = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteImage_photo, Base64.DEFAULT);
            imageMap.put(snapName, encodedImage);
        } else if (requestCode == MY_REQUEST_CODE) {
            Log.d("ACTIVITYRESULT", data.getStringExtra("Mydata"));
            String intentResult = data.getStringExtra("Mydata");
            TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            Log.d("MPHONENUMBER", mPhoneNumber);
            final HttpClient client = new DefaultHttpClient();
            String urlString = "http://192.168.1.65/getDetail?id=" + intentResult + "&phoneNumber=" + mPhoneNumber;
            Log.d("intentURL", urlString);
            final HttpGet httpGet = new HttpGet(urlString);
            final ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String[] responseString = {null};
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        responseString[0] = client.execute(httpGet, responseHandler);
                        Log.d("GETDETAILS", responseString[0]);
                        try {
                            final JSONObject jsonObj = new JSONObject(responseString[0]);
                            final JSONObject markerMap = jsonObj.getJSONObject("markerMap");
                            List<LatLng> reloadPath = new ArrayList<LatLng>();
                            for (int i = 0; i < jsonObj.getJSONArray("lat").length(); i++) {
                                reloadPath.add(new LatLng(jsonObj.getJSONArray("lat").getDouble(i), jsonObj.getJSONArray("lng").getDouble(i)));
                            }
                            final List<LatLng> tmpReloadPath = reloadPath;
                            Random rnd = new Random();
                            final int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                            BetaGo.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMap.addPolyline(new PolylineOptions()
                                            .addAll(tmpReloadPath)
                                            .width(12)
                                            .color(color)
                                            .geodesic(true));
                                }
                            });
                            Iterator<?> keys = markerMap.keys();
                            int counter = 1;
                            while (keys.hasNext()) {
                                final String key = (String) keys.next();
                                Log.d("MARKEMAPKEY", key);
                                JSONObject jObj = markerMap.getJSONObject(key);
                                final Double mlat = jObj.getDouble("lat");
                                final Double mlng = jObj.getDouble("lng");
                                final String description = jObj.getString("description");
                                final String image = jObj.getString("image");
                                Log.d("MARKERIMAGE", markerMap.getString(key));
                                imageMap.clear();
                                imageMap.put(key + ".jpg", image);
                                BetaGo.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMap.addMarker(new MarkerOptions().position(new LatLng(mlat, mlng)).title(key).snippet(description));
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Log.d("JSONEXCEPTION", "jsonexception");
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        Log.d("IOEXCEPTION", "ioexception");
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    private void addDrawerItems() {
        String[] osArray = {"Record", "Stop", "Snap", "Upload", "Reload"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beta_go);
        mDrawerList = (ListView) findViewById(R.id.navList);
        addDrawerItems();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    private File getFile(String fileName) {
        File folder = new File("sdcard/camera_app");

        if (!folder.exists()) {
            folder.mkdir();
        }
        //File image_file = new File(folder, "snap.jpg");
        File image_file = new File(folder, fileName);
        return image_file;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);

                    TextView description = (TextView) v.findViewById(R.id.descriptionWindow);
                    ImageView snap = (ImageView) v.findViewById(R.id.snapView);

                    LatLng ll = marker.getPosition();
                    description.setText(marker.getSnippet());
                    byte[] decodedString = Base64.decode(imageMap.get(marker.getTitle() + ".jpg"), Base64.DEFAULT);
                    Bitmap myBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    snap.setImageBitmap(myBitmap);

                    return v;
                }

            });
        }
        mMap.setPadding(0, 2170, 0, 0);
        mMap.setMyLocationEnabled(true);
        this.lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = this.lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d("LM", String.valueOf(location));
        if (location != null) {
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                    Log.d("DRAWERLISTCLICK", "drawer list clicked");
                    TextView textView = (TextView) viewClicked;
                    String value = textView.getText().toString();
                    Toast.makeText(BetaGo.this, value, Toast.LENGTH_LONG).show();
                    if (value == "Record") {
                        if (recordTimer == null) {
                            final BetaGo finalActivity = BetaGo.this;
                            recordTimer = new Timer();
                            recordTimer.schedule(new RecordTask(finalActivity), 0, 5000);
                        }
                    } else if (value == "Stop") {
                        if (recordTimer != null) {
                            Toast.makeText(getBaseContext(), "Stop", Toast.LENGTH_SHORT).show();
                            recordTimer.cancel();
                            recordTimer.purge();
                            recordTimer = null;
                        }
                    } else if (value == "Snap") {
                        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = getFile("Snap" + snapNumber + ".jpg");
                        snapName = "Snap" + snapNumber + ".jpg";
                        //camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        if (camera_intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(camera_intent, CAM_REQUEST);
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(BetaGo.this);
                        builder.setTitle("New Snap!");

                        final EditText input = new EditText(BetaGo.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Location currentLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())).title("Snap" + String.valueOf(snapNumber)));
                                snapNumber += 1;
                                marker.setSnippet(input.getText().toString());
                                markerLat.add(currentLoc.getLatitude());
                                markerLng.add(currentLoc.getLongitude());
                                descriptions.add(input.getText().toString());
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    } else if (value == "Upload") {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BetaGo.this);
                        builder.setTitle("Save Record As:");

                        final EditText input = new EditText(BetaGo.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), "In progress..", Toast.LENGTH_LONG).show();
                                recordName = input.getText().toString();
                                MarkerThread thread = new MarkerThread(BetaGo.this);
                                thread.start();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    } else if (value == "Reload") {
                        Log.d("CLICKEDRELOAD", "clicked reload");
                        ReloadThread thread = new ReloadThread(BetaGo.this);
                        thread.start();
                    }
                }
            });

            final double longitude = location.getLongitude();
            final double latitude = location.getLatitude();
            LatLng loc = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }
    }

}
