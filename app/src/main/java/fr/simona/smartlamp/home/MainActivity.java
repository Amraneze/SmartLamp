package fr.simona.smartlamp.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.simona.smartlamp.R;
import fr.simona.smartlamp.common.ui.CommonButton;
import fr.simona.smartlamp.common.ui.CustomSnackBar;
import fr.simona.smartlamp.common.ui.FontManager;
import fr.simona.smartlamp.device.AddDeviceActivity;

/**
 * Created by Amrane Ait Zeouay on 26-Nov-17.
 */

public class MainActivity extends AppCompatActivity implements HomeView  {

    private static final int PERMISSIONS_REQUEST_CODE = 1;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_menu_filter)
    LinearLayout llBtnMenuFilter;
    @BindView(R.id.btn_menu_filter)
    ImageButton btnMenuFilter;
    @BindView(R.id.ll_btn_scan)
    LinearLayout llBtnRefreshMeasurement;
    @BindView(R.id.btn_scan_devices)
    ImageButton btnRefreshMeasurement;
    @BindView(R.id.nv_view)
    NavigationView nvView;
    @BindView(R.id.rl_no_measurement)
    RelativeLayout rlNoMeasurement;
    @BindView(R.id.btn_add_device)
    CommonButton btnAddDevice;

    private ActionBarDrawerToggle drawerToggle;

    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        drawerToggle = setupDrawerToggle();
        presenter = new HomePresenter(this);
        setUpToolbar();
        setUpPermissions();
        setupDrawerContent();
    }

    private void setUpToolbar() {
        llBtnMenuFilter.setVisibility(View.VISIBLE);
        llBtnRefreshMeasurement.setVisibility(View.VISIBLE);
        //btnMenuFilter.setVisibility(View.VISIBLE);
        //btnRefreshMeasurement.setVisibility(View.VISIBLE);
    }

    private void setUpPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_CODE);
            } else {
                afterPermissionRequests();
            }
        } else {
            afterPermissionRequests();
        }
    }

    private void afterPermissionRequests() {
        startScanService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length > 0) {
            boolean localisationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (localisationPermission) {
                afterPermissionRequests();
            } else {
                setUpPermissions();
            }
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent() {
        nvView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @OnClick(R.id.btn_menu_filter)
    public void displayFilterMenu() {
        //filterMenu.show();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        menuItem.setChecked(false);
        Intent intent;
        switch(menuItem.getItemId()) {
            case R.id.paired_devices:
                startActivity(new Intent(this, AddDeviceActivity.class));
                menuItem.setChecked(true);
                break;
            default:
                break;
        }
        drawerLayout.closeDrawers();
    }

    /*@OnClick(R.id.btn_add_device2)
    public void sendLightOn() {
        presenter.setLight(true);
    }*/

    @OnClick(R.id.btn_add_device)
    public void sendLightOff() {
        presenter.setLight(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListeners() {
        drawerLayout.addDrawerListener(drawerToggle);
        //btnAddDevice.setVisibility(presenter.isPairedDeviceListEmpty() ? View.VISIBLE : View.GONE);
        btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(HomeActivity.this, AddDeviceActivity.class));
            }
        });
        /*llBtnRefreshMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBar(CustomSnackBar.newError(getString(R.string.no_internet_connection))
                        .setDuration(CustomSnackBar.SNACKBAR_DURATION_LONG));
            }
        });*/

    }

    private void startScanService() {
        presenter.searchBluetoothDevices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //presenter.onResume();
        setListeners();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void showSnackBar(final CustomSnackBar options) {
        final Snackbar snackbar = Snackbar.make(rlNoMeasurement, options.getMessage(), options.getDuration());
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, options.getBackgroundColor()));
        TextView txt = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextSize(20);
        txt.setTypeface(FontManager.INSTANCE.getTypeFace(this, FontManager.Style.O_BOLD));
        txt.setTextColor(ContextCompat.getColor(this, options.getTextColor()));
        snackbar.show();
    }
}

