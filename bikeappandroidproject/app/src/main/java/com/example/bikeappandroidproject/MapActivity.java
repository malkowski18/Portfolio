package com.example.bikeappandroidproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

// classes needed to initialize map
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add the location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;

// classes needed to add a marker
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshot;
import com.mapbox.mapboxsdk.snapshotter.MapSnapshotter;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

// classes to calculate a route
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import okhttp3.internal.http2.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;

    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private Button button;

//SNAPSHOT//
    private MapSnapshotter mapSnapshotter;
    private FloatingActionButton cameraFab;
    private boolean hasStartedSnapshotGeneration;
//SNAPSHOT//

    //Recycler View//
    private static final String TAG1 = "RVDirectionsActivity";
    private static final String SYMBOL_ICON_ID = "SYMBOL_ICON_ID";
    private static final String PERSON_ICON_ID = "PERSON_ICON_ID";
    private static final String MARKER_SOURCE_ID = "MARKER_SOURCE_ID";
    private static final String PERSON_SOURCE_ID = "PERSON_SOURCE_ID";
    private static final String DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID = "DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String PERSON_LAYER_ID = "PERSON_LAYER_ID";
    private static final String DASHED_DIRECTIONS_LINE_LAYER_ID = "DASHED_DIRECTIONS_LINE_LAYER_ID";

    private static final Point directionsOriginPoint = Point.fromLngLat(16.630199,
            49.215841);
    private static final  LatLng[] possibleDestinations = new LatLng[]{
            new LatLng(49.213442, 16.620121),
            new LatLng(49.203456, 16.606771),
            new LatLng(49.189967, 16.608209),
            new LatLng(49.191547, 16.620342),
            new LatLng(49.200745, 16.600511),
            new LatLng(49.202662, 16.581157),
            new LatLng(49.211025, 16.583737),
            new LatLng(49.235862, 16.589667),
            new LatLng(49.172325, 16.625115)
    };
    private final List<DirectionsRoute> directionsRouteList = new ArrayList<>();
    private FeatureCollection dashedLineDirectionsFeatureCollection;

    //Recycler View//

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map_activity);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        cameraFab = findViewById(R.id.camera_share_snapshot_image_fab);
        cameraFab.setImageResource(R.drawable.ic_photo_camera_black_24dp);

        hasStartedSnapshotGeneration = false;
//Drawer Layout//
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        NavigationView nvDrawer = findViewById(R.id.navView);
        mToggle.syncState();
        setupDrawerContent(nvDrawer);
    }

@Override
public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
}
    Fragment myFragment = null;
    Class fragmentClass;

    public void selectItemDrawer(MenuItem menuItem){
        boolean openFragment = true;
    switch (menuItem.getItemId()){
        case R.id.home:
            fragmentClass = Home.class;
        break;
        case R.id.news:
            fragmentClass = NewsActivity.class;
            Intent intent0 = new Intent(MapActivity.this,
                    NewsActivity.class);
            startActivity(intent0);
            openFragment = false;            break;
        case R.id.FAQ:
            fragmentClass = com.example.bikeappandroidproject.CardView.class;
            Intent intent1 = new Intent(MapActivity.this,
                    com.example.bikeappandroidproject.CardView.class);
            startActivity(intent1);
            openFragment = false;
            break;
        case R.id.settings:
            fragmentClass = SettingsActivity.class;
            Intent intent2 = new Intent(MapActivity.this,
                    SettingsActivity.class);
            startActivity(intent2);
            openFragment = false;
            break;
        case R.id.profile_change:
            fragmentClass = UserProfile.class;
            Intent intent3 = new Intent(MapActivity.this,
                    UserProfile.class);
            startActivity(intent3);
            openFragment = false;
            break;
            case R.id.logout:
                fragmentClass = Login.class;
                Intent intent4 = new Intent(MapActivity.this,
                        Login.class);
                startActivity(intent4);
                openFragment = false;
            break;
        default:
            fragmentClass = Home.class;
    }
        if (openFragment) {
            try {
                myFragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentl, myFragment).commit();
            menuItem.setChecked(true);
        }
    setTitle(menuItem.getTitle());
    mDrawerLayout.closeDrawers();
}

private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
}

private void showHome(){
        myFragment = new Home();
        if(fragmentClass!=null){
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.contentl,myFragment,myFragment.getTag()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(myFragment instanceof Home){
            super.onBackPressed();
        }
        else {
           showHome();
        }
    }

//Drawer Layout//

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri(Style.MAPBOX_STREETS)
                .withImage(PERSON_ICON_ID, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                        getResources().getDrawable(R.drawable.ic_my_location_m))))
                .withSource(new GeoJsonSource(PERSON_SOURCE_ID,
                        Feature.fromGeometry(directionsOriginPoint)))
                .withLayer(new SymbolLayer(PERSON_LAYER_ID, PERSON_SOURCE_ID).withProperties(
                        iconImage(PERSON_ICON_ID),
                        iconSize(0.2f), //2
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                ))

// Set up the image, source, and layer for the potential destination markers
                .withImage(SYMBOL_ICON_ID, BitmapFactory.decodeResource(
                        this.getResources(), R.mipmap.red_marker))
                .withSource(new GeoJsonSource(MARKER_SOURCE_ID, initDestinationFeatureCollection()))
                .withLayer(new SymbolLayer(LAYER_ID, MARKER_SOURCE_ID).withProperties(
                        iconImage(SYMBOL_ICON_ID),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconOffset(new Float[]{0f, -4f}) //0
                ))

// Set up the source and layer for the direction route LineLayer
                .withSource(new GeoJsonSource(DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID))
                .withLayerBelow(
                        new LineLayer(DASHED_DIRECTIONS_LINE_LAYER_ID, DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID)
                                .withProperties(
                                        lineWidth(7f),
                                        lineJoin(LINE_JOIN_ROUND),
                                        lineColor(Color.parseColor("#2096F3"))
                                ), PERSON_LAYER_ID),
                new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                getString(R.string.navigation_guidance_day);

                getRoutesToAllPoints();
                initRecyclerView();
                Toast.makeText(MapActivity.this,
                        R.string.toast_instruction, Toast.LENGTH_SHORT).show();

        /*        mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(49.197146, 16.603248))
                        .title("Station 1"));

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(49.198197, 16.613942))
                        .title("Station 2"));

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(49.192125, 16.613105))
                        .title("Station 3"));

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(49.178225, 16.621644))
                        .title("Station 4"));
*/
                enableLocationComponent(style);

                addDestinationIconSymbolLayer(style);

                cameraFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!hasStartedSnapshotGeneration) {
                            hasStartedSnapshotGeneration = true;
                            Toast.makeText(MapActivity.this, R.string.loading_snapshot_image, Toast.LENGTH_LONG).show();
                            startSnapShot(
                                    mapboxMap.getProjection().getVisibleRegion().latLngBounds,
                                    mapView.getMeasuredHeight(),
                                    mapView.getMeasuredWidth());
                        }
                    }
                });

                mapboxMap.addOnMapClickListener(MapActivity.this);
                button = findViewById(R.id.startButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean simulateRoute = true;
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)
                                .shouldSimulateRoute(simulateRoute)
                                .build();
// Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(MapActivity.this, options);


                    }
                });
                // To account for new security measures regarding file management that were released with Android Nougat.
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }
        });

    }

    //recycler view//
    private void getRoutesToAllPoints() {
        for (LatLng singleLatLng : possibleDestinations) {
            getRoute(Point.fromLngLat(singleLatLng.getLongitude(), singleLatLng.getLatitude()));
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void getRoute(Point destination) {
        MapboxDirections client = MapboxDirections.builder()
                .origin(directionsOriginPoint)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Log.d(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.d(TAG, "No routes found");
                    return;
                }
// Add the route to the list.
                directionsRouteList.add(response.body().routes().get(0));
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.d(TAG, "Error: " + throwable.getMessage());
                if (!Objects.equals(throwable.getMessage(), "Coordinate is invalid: 0,0")) {
                    Toast.makeText(MapActivity.this,
                            "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void drawNavigationPolylineRoute(final DirectionsRoute route) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    List<Feature> directionsRouteFeatureList = new ArrayList<>();
                    LineString lineString = LineString.fromPolyline(route.geometry(), PRECISION_6);
                    List<Point> lineStringCoordinates = lineString.coordinates();
                    for (int i = 0; i < lineStringCoordinates.size(); i++) {
                        directionsRouteFeatureList.add(Feature.fromGeometry(
                                LineString.fromLngLats(lineStringCoordinates)));
                    }
                    dashedLineDirectionsFeatureCollection =
                            FeatureCollection.fromFeatures(directionsRouteFeatureList);
                    GeoJsonSource source = style.getSourceAs(DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID);
                    if (source != null) {
                        source.setGeoJson(dashedLineDirectionsFeatureCollection);
                    }
                }
            });
        }
    }

    /**
     * Create a FeatureCollection to display the possible destination markers.
     *
     * @return a {@link FeatureCollection}, which represents the possible destinations.
     */
    private FeatureCollection initDestinationFeatureCollection() {
        List<Feature> featureList = new ArrayList<>();
        for (LatLng latLng : possibleDestinations) {
            featureList.add(Feature.fromGeometry(
                    Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
        }
        return FeatureCollection.fromFeatures(featureList);
    }

    /**
     * Set up the RecyclerView.
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_on_top_of_map);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new LocationRecyclerViewAdapter(this,
                createRecyclerViewLocations(), mapboxMap));
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    /**
     * Create data fro the RecyclerView.
     *
     * @return a list of {@link SingleRecyclerViewLocation} objects for the RecyclerView.
     */
    @SuppressLint("StringFormatInvalid")
    private List<SingleRecyclerViewLocation> createRecyclerViewLocations() {
        ArrayList<SingleRecyclerViewLocation> locationList = new ArrayList<>();
        for (int x = 0; x < possibleDestinations.length; x++) {
            SingleRecyclerViewLocation singleLocation = new SingleRecyclerViewLocation();
            singleLocation.setName(String.format(getString(R.string.rv_directions_route_card_name), x));
            singleLocation.setAvailableTables(String.format(getString(
                    R.string.rv_directions_route_available_table_info),
                    new Random().nextInt(possibleDestinations.length)));
            locationList.add(singleLocation);
        }
        return locationList;
    }

    /**
     * POJO model class for a single location in the RecyclerView.
     */
    class SingleRecyclerViewLocation {

        private String name;
        private String availableTables;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvailableTables() {
            return availableTables;
        }

        public void setAvailableTables(String availableTables) {
            this.availableTables = availableTables;
        }

    }

    static class LocationRecyclerViewAdapter extends
            RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder> {

        private List<SingleRecyclerViewLocation> locationList;
        private MapboxMap map;
        private WeakReference<MapActivity> weakReference;


        public LocationRecyclerViewAdapter(MapActivity activity,
                                           List<SingleRecyclerViewLocation> locationList,
                                           MapboxMap mapBoxMap) {
            this.locationList = locationList;
            this.map = mapBoxMap;
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_directions_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            SingleRecyclerViewLocation singleRecyclerViewLocation = locationList.get(position);
            holder.name.setText(singleRecyclerViewLocation.getName());
            holder.numOfAvailableTables.setText(singleRecyclerViewLocation.getAvailableTables());
            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    weakReference.get()
                            .drawNavigationPolylineRoute(weakReference.get().directionsRouteList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return locationList.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name;
            TextView numOfAvailableTables;
            CardView singleCard;
            ItemClickListener clickListener;

            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.location_title_tv);
                numOfAvailableTables = view.findViewById(R.id.location_num_of_beds_tv);
                singleCard = view.findViewById(R.id.single_location_cardview);
                singleCard.setOnClickListener(this);
            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getLayoutPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    //recycler view//


    private void addDestinationIconSymbolLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addImage("destination-icon-id",
                BitmapFactory.decodeResource(this.getResources(), R.drawable.mapbox_marker_icon_default));
        GeoJsonSource geoJsonSource = new GeoJsonSource("destination-source-id");
        loadedMapStyle.addSource(geoJsonSource);
        SymbolLayer destinationSymbolLayer = new SymbolLayer("destination-symbol-layer-id", "destination-source-id");
        destinationSymbolLayer.withProperties(
                iconImage("destination-icon-id"),
                iconAllowOverlap(true),
                iconIgnorePlacement(true)
        );
        loadedMapStyle.addLayer(destinationSymbolLayer);
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        Point destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        Point originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        GeoJsonSource source = mapboxMap.getStyle().getSourceAs("destination-source-id");
        if (source != null) {
            source.setGeoJson(Feature.fromGeometry(destinationPoint));
        }

        getRoute(originPoint, destinationPoint);
        button.setEnabled(true);
        button.setBackgroundResource(R.color.mapboxBlue);
        return true;
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
// You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

// Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
// Activate the MapboxMap LocationComponent to show user location
// Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //map snapshot//
    private void startSnapShot(final LatLngBounds latLngBounds, final int height, final int width) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (mapSnapshotter == null) {
// Initialize snapshotter with map dimensions and given bounds
                    MapSnapshotter.Options options =
                            new MapSnapshotter.Options(width, height)
                                    .withRegion(latLngBounds)
                                    .withCameraPosition(mapboxMap.getCameraPosition())
                                    .withStyle(style.getUri());

                    mapSnapshotter = new MapSnapshotter(MapActivity.this, options);
                } else {
// Reuse pre-existing MapSnapshotter instance
                    mapSnapshotter.setSize(width, height);
                    mapSnapshotter.setRegion(latLngBounds);
                    mapSnapshotter.setCameraPosition(mapboxMap.getCameraPosition());
                }

                mapSnapshotter.start(new MapSnapshotter.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(MapSnapshot snapshot) {

                        Bitmap bitmapOfMapSnapshotImage = snapshot.getBitmap();

                        Uri bmpUri = getLocalBitmapUri(bitmapOfMapSnapshotImage);

                        Intent shareIntent = new Intent();
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/png");
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(shareIntent, "Share map image"));

                        hasStartedSnapshotGeneration = false;
                    }
                });
            }
        });
    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(getExternalFilesDir(
                Environment.DIRECTORY_PICTURES),
                "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 95, out);
            try {
                out.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return bmpUri;
    }
    //map snapshot//


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        // Make sure to stop the snapshotter on pause if it exists
        if (mapSnapshotter != null) {
            mapSnapshotter.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}