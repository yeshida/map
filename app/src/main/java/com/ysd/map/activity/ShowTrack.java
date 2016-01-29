package com.ysd.map.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.ysd.map.R;
import com.ysd.map.db.LocalDbAdapter;
import com.ysd.map.db.TrackDbAdapter;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/1/25.
 */
public class ShowTrack extends MapActivity{
    private static final String TAG="ShowTrack";
    //定义菜单需要的常量
    private static final int MENU_NEW= Menu.FIRST+1;
    private static final int MENU_CON= MENU_NEW+1;
    private static final int MENU_DEL= MENU_CON+1;
    private static final int MENU_MAIN= MENU_DEL+1;
    private TrackDbAdapter mDbHelper;
    private LocalDbAdapter mlcDbHelper;
    private static MapView mMapView;
    private MapController mc;
    protected MyLocationOverlay mOverlayController;
    private Button mZin;
    private Button mZout;
    private Button mPanN;
    private Button mPanE;
    private Button mPanW;
    private Button mPanS;
    private Button mGps;
    private Button mSat;
    private Button mTraffic;
    private Button mStreetView;
    private String  mDefCaption="";
    private GeoPoint mDefPoint;

    private LocationManager lm;
    private LocationListener locationListener;

    private int track_id;
    private Long rowId;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.show_track);
        findViews();
        centerOnGPSPosition();
        revArgs();
        paintLocates();
        startTrackService();
    }

    private void startTrackService() {
        Intent i = new Intent("com.iceskysl.iTracks.START_TRACK_SERVICE");
        i.putExtra(LocalDbAdapter.TRACKID, track_id);
        startActivity(i);
    }
    private void stopTrackService() {
        stopService(new Intent("om.iceskysl.iTracks.START_TRACK_SERVICE"));
    }
    private void paintLocates() {
        mlcDbHelper = new  LocalDbAdapter(this);
        try {
            mlcDbHelper.open();
            Cursor mLocatesCursor = mlcDbHelper.getTrackAllLocates(track_id);
            startManagingCursor(mLocatesCursor);
            Resources resources = getResources();
            Overlay overlays = new MyLocationOverlay(resources.getDrawable(R.drawable.icon),mLocatesCursor);
            mMapView.getOverlays().add(overlays);
            mlcDbHelper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void revArgs() {
        Log.d(TAG, "revArgs");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString(TrackDbAdapter.NAME);
            rowId = extras.getLong(TrackDbAdapter.KEY_ROWID);
            track_id = rowId.intValue();
            Log.d(TAG, "rowId=" + rowId);
            if (name != null) {
                setTitle(name);
            }
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private void findViews() {
        Log.d(TAG, "find views");
        mMapView = (MapView) findViewById(R.id.mv);
        mc = mMapView.getController();
        SharedPreferences settings = getSharedPreferences(Setting.SETTING_INFOS,0);
        String setting_gps = settings.getString(Setting.SETTING_MAP, "10");
        mc.getZoom(Integer.parseInt(setting_gps));

        //set up the button for "Zoom In"
        mZin = (Button) findViewById(R.id.zin);
        mZin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoomIn();
            }
        });
        //set up the button for "Zoom out"
        mZout = (Button) findViewById(R.id.zout);
        mZout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZoomOut();
            }
        });
        //set up the button for "pan North"
        mPanN = (Button) findViewById(R.id.pann);
        mPanN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panNorth();
            }
        });
        //set up the button for "pan east"
        mPanE = (Button) findViewById(R.id.pane);
        mPanE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panEast();
            }
        });
        //set p the button for "pan west"
        mPanW = (Button) findViewById(R.id.panw);
        mPanW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panWest();
            }
        });
        //set p the button for "pan south"
        mPanS = (Button) findViewById(R.id.pans);
        mPanS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panSouth();
            }
        });
        //set p the button for "GPS"
        mGps = (Button) findViewById(R.id.sat);
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerOnGPSPosition();
            }
        });
        //set up the button for "Satelite toggle"
        mSat = (Button) findViewById(R.id.sat);
        mSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSatellite();
            }
        });
        //set up the button for "traffic toggle"
        mTraffic = (Button) findViewById(R.id.traffic);
        mTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTraffic();
            }
        });
        //set up the button for "street toggle"
        mStreetView = (Button) findViewById(R.id.streetview);
        mStreetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleStreetView();
            }
        });
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener= new MyLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onkeydown");
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            panWest();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            panEast();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            panNorth();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            panSouth();
            return true;
        }
        return false;
    }

    private void panSouth() {
        GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6() - mMapView.getLatitudeSpan() / 4, mMapView.getMapCenter().getLongitudeE6());
        mc.setCenter(pt);
    }

    private void panWest() {
        GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6(),mMapView.getMapCenter().getLongitudeE6()- mMapView.getLatitudeSpan() / 4);
        mc.setCenter(pt);
    }

    private void panEast() {
        GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6(),mMapView.getMapCenter().getLongitudeE6()+ mMapView.getLatitudeSpan() / 4);
        mc.setCenter(pt);
    }

    private void panNorth() {
        GeoPoint pt = new GeoPoint(mMapView.getMapCenter().getLatitudeE6()+ mMapView.getLatitudeSpan() / 4,mMapView.getMapCenter().getLongitudeE6());
        mc.setCenter(pt);
    }

    private void ZoomOut() {
        mc.zoomIn();
    }

    private void ZoomIn() {
        mc.zoomOut();
    }
    private void toggleStreetView() {
        mMapView.setSatellite(false);
        mMapView.setStreetView(true);
        mMapView.setTraffic(false);
    }

    private void toggleTraffic() {
        mMapView.setSatellite(false);
        mMapView.setStreetView(false);
        mMapView.setTraffic(true);
    }

    private void toggleSatellite() {
        mMapView.setSatellite(true);
        mMapView.setStreetView(false);
        mMapView.setTraffic(false);
    }

    private void centerOnGPSPosition() {
        Log.d(TAG, "centerongpsposition");
        String provider = "gps";
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Location loc = lm.getLastKnownLocation(provider);
        mDefPoint = new GeoPoint((int) (loc.getLatitude() * 1000000), (int) (loc.getLongitude() * 1000000));
        mDefCaption ="Im Here.";
        mc.animateTo(mDefPoint);
        mc.setCenter(mDefPoint);
        //show Overlay on map
        MyOverlay mo =new MyOverlay();
        mo.onTab(mDefPoint, mMapView);
        mMapView.getOverlays().add(mo);
    }
//this is used draw an overlay on the map
    private class MyOverlay extends Overlay{

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean b) {
        Log.d(TAG, "MyOverlay::darw..mDefCation=" + mDefCaption);
        super.draw(canvas, mapView, b);
        if (mDefCaption.length() == 0) {
            return;
        }
        Paint p = new Paint();
        int[] scoords = new int[2];
        int sz=5;
        //convert to screen coords
        Point myScreenCoords = new Point();
        mMapView.getProjection().toPixels(mDefPoint, myScreenCoords);
        scoords[0]=myScreenCoords.x;
        scoords[1]=myScreenCoords.y;
        p.setTextSize(14);
        p.setAntiAlias(true);

        int sw = (int) (p.measureText(mDefCaption) + 0.5f);
        int sh=25;
        int sx = scoords[0]-sw/2-5;
        int sy = scoords[1]-sh-sz/2-5;
        RectF rec = new RectF(sx, sy, sx + sw + 10, sy + sh);

        p.setStyle(Paint.Style.FILL);
        p.setARGB(128, 255, 0, 0);

        canvas.drawRoundRect(rec, 5, 5, p);

        p.setStyle(Paint.Style.STROKE);
        p.setARGB(255, 255, 255, 255);
        canvas.drawRoundRect(rec, 5, 5, p);

        canvas.drawText(mDefCaption, sx + 5, sy + sh - 8, p);
        //draw piint body and outer ring

        p.setStyle(Paint.Style.FILL);
        p.setARGB(88, 255, 0, 0);
        p.setStrokeWidth(1);
        RectF spot = new RectF(scoords[0] - sz, scoords[1] + sz, scoords[0]+sz,scoords[1]-sz);
        canvas.drawOval(spot,p);
        p.setARGB(255, 255, 0, 0);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(scoords[0],scoords[1],sz,p);
    }
}

    protected class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            Log.d(TAG, "mylocatioListener::onLocationChanged..");
            if (loc != null) {

            }
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
    //初始化菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    //当一个菜单被选中的时候调用

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
