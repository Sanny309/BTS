package com.example.sanny.loader;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClint;
    Location mLastLocation;
    LocationRequest mlocationRequest;
    private Button mLogout,mrequest;
    private LatLng pickuplocation;
    private  Boolean requestBol=false;
    private Marker pickupmarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLogout=(Button)findViewById(R.id.logout);
        mrequest=(Button)findViewById(R.id.request);
        mLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(CustomerMapActivity.this,CutomerMenu_Activity.class);
                        startActivity(intent);
                        finish();
                        return;
            }
        });
        mrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestBol){
                    requestBol=false;
                    geoQuery.removeAllListeners();
                    if (driverFoundID!=null){
                        DatabaseReference driverRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID);
                        driverRef.setValue(true);
                        driverFoundID=null;

                    }
                    driverFound=false;
                    radius=1;

                    driverLoacationRef.removeEventListener(driverLoacationRefListener);
                    String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("customerRequest");
                    GeoFire geoFire= new GeoFire(ref);
                    geoFire.removeLocation(userId);
                    if (pickupmarker!=null){
                        pickupmarker.remove();
                    }
                    mrequest.setText("call Loader");
                }else{
                    requestBol=true;
                    String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("customerRequest");
                    GeoFire geoFire= new GeoFire(ref);
                    geoFire.setLocation(userId,new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
                    pickuplocation=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                    pickupmarker=mMap.addMarker(new MarkerOptions().position(pickuplocation).title("pickup here"));
                    mrequest.setText("Getting your Driver.....");

                    getClosestDriver();

                }
                pickupmarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));


            }
        });
    }
    private int radius=1;
    private Boolean driverFound=false;
    private String driverFoundID;

    GeoQuery geoQuery;
private  void getClosestDriver(){
    DatabaseReference  driverLocation=FirebaseDatabase.getInstance().getReference().child("DriversAvialable");
    GeoFire geoFire=new GeoFire(driverLocation);
    geoQuery=geoFire.queryAtLocation(new GeoLocation(pickuplocation.latitude,pickuplocation.longitude),radius);
    geoQuery.removeAllListeners();
    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
        @Override
        public void onKeyEntered(String key, GeoLocation location) {
            if (!driverFound && requestBol){
                driverFound=true;
                driverFoundID=key;

                DatabaseReference driverRef=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID);
                String customerId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                HashMap map=new HashMap();
                map.put("cudtomerRideId",customerId);
                driverRef.updateChildren(map);
                getDriverLocation();
                mrequest.setText("Looking for Driver Location......");
            }

        }

        @Override
        public void onKeyExited(String key) {

        }

        @Override
        public void onKeyMoved(String key, GeoLocation location) {

        }

        @Override
        public void onGeoQueryReady() {
          if (!driverFound)
          {
              radius++;
              getClosestDriver();

          }
        }

        @Override
        public void onGeoQueryError(DatabaseError error) {


        }
    });
}
    private Marker mDrivermarker;
    private DatabaseReference driverLoacationRef;
    private ValueEventListener driverLoacationRefListener;
    private void  getDriverLocation(){
        driverLoacationRef=FirebaseDatabase.getInstance().getReference().child("driverWorking").child(driverFoundID).child("l");
        driverLoacationRefListener=driverLoacationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&& requestBol){
                    List<Object>map=(List<Object>)dataSnapshot.getValue();
                    double  locationLat=0;
                    double locationLng=0;
                    mrequest.setText("Driver Found");
                    if (map.get(0)!=null){
                        locationLat=Double.parseDouble(map.get(0).toString());

                    }
                    if (map.get(1)!=null){
                        locationLng=Double.parseDouble(map.get(1).toString());

                    }
                    LatLng driverLatLng=new LatLng(locationLat,locationLng);
                    if (mDrivermarker !=null){
                        mDrivermarker.remove();
                    }
                    Location locl=new Location("");
                    locl.setLatitude(pickuplocation.latitude);
                    locl.setLongitude(pickuplocation.longitude);
                     Location loc2=new Location("");
                     loc2.setLatitude(driverLatLng.latitude);
                     loc2.setLongitude(driverLatLng.longitude);
                    float distance=locl.distanceTo(loc2);
                    if (distance<100) {

                        mrequest.setText("driver is here  ");
                    }else{
                    mrequest.setText("driver found "+String.valueOf(distance));
                    }


                     mDrivermarker=mMap.addMarker(new MarkerOptions().position(driverLatLng).title("your driver"));


                }
                mDrivermarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClint=new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClint.connect();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = new LocationRequest();
        mlocationRequest.setInterval(1000);
        mlocationRequest.setFastestInterval(1000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClint, mlocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;

        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));




    }

    @Override
    protected void onStop() {
        super.onStop();


    }
}

