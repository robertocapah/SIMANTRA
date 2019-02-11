package shp.template;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import shp.template.BL.clsActivity;
import shp.template.BL.clsHelperBL;
import shp.template.BL.clsMainBL;
import shp.template.Common.clsPhotoProfile;
import shp.template.Common.clsPushData;
import shp.template.Common.clsStatusMenuStart;
import shp.template.Common.clsToken;
import shp.template.Common.mMenuData;
import shp.template.Common.mUserLogin;
import shp.template.Common.tNotification;
import shp.template.Common.tProgramVisitSubActivity;
import shp.template.Common.tRealisasiVisitPlan;
import shp.template.Data.DatabaseHelper;
import shp.template.Data.DatabaseManager;
import shp.template.Data.VolleyResponseListener;
import shp.template.Data.VolleyUtils;
import shp.template.Data.clsHardCode;
import shp.template.Fragment.FragementInfoProgram;
import shp.template.Fragment.FragementMaintenance;
import shp.template.Fragment.FragmentAkuisisi;
import shp.template.Fragment.FragmentDownloadData;
import shp.template.Fragment.FragmentHeaderCallPlan;
import shp.template.Fragment.FragmentNotification;
import shp.template.Fragment.FragmentPushData;
import shp.template.Fragment.FragmentSetting;
import shp.template.Repo.clsPhotoProfilRepo;
import shp.template.Repo.clsTokenRepo;
import shp.template.Repo.enumStatusMenuStart;
import shp.template.Repo.mConfigRepo;
import shp.template.Repo.mMenuRepo;
import shp.template.Repo.mUserLoginRepo;
import shp.template.Repo.tNotificationRepo;
import shp.template.Repo.tProgramVisitSubActivityRepo;
import shp.template.Repo.tRealisasiVisitPlanRepo;
import shp.template.ResponseDataJson.loginMobileApps.LoginMobileApps;
import shp.template.ResponseDataJson.responsePushData.ResponsePushData;
import shp.template.Service.MyServiceNative;
import shp.template.Utils.AuthenticatorUtil;
import shp.template.Utils.CircularTextView;
import shp.template.Utils.IOBackPressed;

import com.kalbe.mobiledevknlibs.PickImageAndFile.PickImage;
import com.kalbe.mobiledevknlibs.ToastAndSnackBar.ToastCustom;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.leolin.shortcutbadger.ShortcutBadgeException;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Rian Andrivani on 11/22/2017.
 */

public class MainMenu extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    List<mUserLogin> dataLogin = null;
    mUserLoginRepo loginRepo;
    PackageInfo pInfo = null;
    int selectedId;
    Boolean isSubMenu = false;
    String[] linkMenu;
    MenuItem checkNavItem = null;
    private TextView tvUsername, tvEmail;
    public CircleImageView ivProfile;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    private String i_View ="Fragment";
    ProgressDialog pDialog;
    private Gson gson;
    List<clsToken> dataToken;
    clsTokenRepo tokenRepo;
    private AccountManager mAccountManager;
    boolean isOnCreate = false;

    @Override
    public void onBackPressed() {
        Fragment fragmentBack = getSupportFragmentManager().findFragmentById(R.id.frame);
        if ((fragmentBack instanceof IOBackPressed)){
            if (!((IOBackPressed)fragmentBack).onBackPressed())
                super.onBackPressed();
        } else {
            List<Fragment> dtFragment1= new ArrayList<>();
            if(getSupportFragmentManager().getFragments().size()<1){
                Fragment dtFragment=getSupportFragmentManager().getFragments().get(0);
                dtFragment1.add(dtFragment);
            }else{
                dtFragment1=getSupportFragmentManager().getFragments();
            }

            String bundleClass = getIntent().getStringExtra("FragmentClass");
            for (Fragment fragment : dtFragment1) {
                if(fragment!=null){
                    if(fragment.toString().contains("HomeFragment")){
//                    finish();
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

                        builder.setTitle("Exit");
                        builder.setMessage("Are you sure to exit?");

                        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                                System.exit(0);
                            }
                        });

                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        toolbar.setTitle("Home");
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                        fragmentTransactionHome.replace(R.id.frame, homeFragment);
                        fragmentTransactionHome.commit();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        super.onCreate(savedInstanceState);
        selectedId = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_300));
        }
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (!isMyServiceRunning(MyServiceNative.class)){
            startService(new Intent(MainMenu.this, MyServiceNative.class));
        }

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        pDialog = new ProgressDialog(MainMenu.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        mAccountManager = AccountManager.get(getBaseContext());
        isOnCreate = true;
//        if (getIntent().getStringExtra(i_View)!=null){
//            if (getIntent().getStringExtra(i_View).equals("FragmentNotification")){
//                toolbar.setTitle("Notification");
//                setSupportActionBar(toolbar);
//
//                FragmentNotification fragmentNotification = new FragmentNotification();
//                FragmentTransaction fragmentTransactionNotification = getSupportFragmentManager().beginTransaction();
//                fragmentTransactionNotification.replace(R.id.frame, fragmentNotification);
//                fragmentTransactionNotification.commit();
//                selectedId = 99;
//            }else if (getIntent().getStringExtra(i_View).equals("FragmentPushData")){
//                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//                toolbar.setTitle("Push Data");
//                setSupportActionBar(toolbar);
//
////                toolbar.setTitle("Push Data");
////                toolbar.setSubtitle(null);
//
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                Bundle bundle = new Bundle();
//                String myMessage = "notMainMenu";
//                bundle.putString("message", myMessage );
//
//                FragmentPushData fragmentPushData = new FragmentPushData();
//                fragmentPushData.setArguments(bundle);
//                FragmentTransaction fragmentTransactionPushData = getSupportFragmentManager().beginTransaction();
//                fragmentTransactionPushData.replace(R.id.frame, fragmentPushData);
//                fragmentTransactionPushData.commit();
//                selectedId = 99;
//            }
//        }else {
//            toolbar.setTitle("Home");
//            setSupportActionBar(toolbar);
//
//
//            HomeFragment homFragment = new HomeFragment();
//            FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
//            fragmentTransactionHome.replace(R.id.frame, homFragment);
//            fragmentTransactionHome.commit();
//            selectedId = 99;
//        }

//        addProductAndOrder();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View vwHeader = navigationView.getHeaderView(0);
        ivProfile = (CircleImageView) vwHeader.findViewById(R.id.profile_image);
        tvUsername = (TextView) vwHeader.findViewById(R.id.username);
        tvEmail = (TextView) vwHeader.findViewById(R.id.email);

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(AppIndex.API).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        ivProfile.setEnabled(false);


        try {
            loginRepo = new mUserLoginRepo(getApplicationContext());
            dataLogin = (List<mUserLogin>) loginRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvUsername.setText(new clsActivity().greetings() + dataLogin.get(0).getTxtUserName().toString());
        tvEmail.setText(dataLogin.get(0).getTxtEmail().toString());
        if (dataLogin.get(0).getBlobImg()!=null){
            Bitmap bitmap = new PickImage().decodeByteArrayReturnBitmap(dataLogin.get(0).getBlobImg());
            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            ivProfile.setImageBitmap(bitmap1);
        }

        String linkAPI = new mConfigRepo(getApplicationContext()).APIToken;
        try {
            URL u = new URL(linkAPI);
            linkAPI = u.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Menu header = navigationView.getMenu();
        SubMenu subMenuVersion = header.addSubMenu(R.id.groupVersion, 0, 3, "Version");
        try {
            subMenuVersion.add(getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " \u00a9 KN-IT").setIcon(R.mipmap.ic_android).setEnabled(false);
            subMenuVersion.add(linkAPI).setIcon(R.mipmap.ic_link).setEnabled(false);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                checkNavItem = menuItem;
                pDialog.setCancelable(false);
                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                pDialog.show();
                drawerLayout.closeDrawers();

                return true;
            }
        });

//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Fragment fragment = null;
                pDialog.dismiss();
                if(checkNavItem!= null){
                    switch (checkNavItem.getItemId()) {
                        case R.id.mnLogout:
                            checkNavItem = null;
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainMenu.this);

                            builder.setTitle("Confirm");
                            builder.setMessage("Logout Application?");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

//                                final ProgressDialog dialog2 = new ProgressDialog(MainMenu.this, ProgressDialog.STYLE_SPINNER);
//                                dialog2.setIndeterminate(true);
//                                dialog2.setMessage("Logging out...");
//                                dialog2.setCancelable(false);
//                                dialog2.show();
//
//                                new Handler().postDelayed(
//                                        new Runnable() {
//                                            public void run() {
//
//                                                List<Long> listId = new ArrayList<>();
////                                                unregisterReceiver(new ReceiverDownloadManager(listId).receiver);
//                                                // On complete call either onLoginSuccess or onLoginFailed
//                                                clearData();
//                                                // onLoginFailed();
//                                                dialog2.dismiss();
//                                            }
//                                        }, 3000);
                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                            break;
                        case R.id.home:
                            checkNavItem = null;
                            toolbar.setTitle("Home");

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            // fragment yang dituju
                            HomeFragment homFragment = new HomeFragment();
                            FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionHome.replace(R.id.frame, homFragment);
                            fragmentTransactionHome.commit();
                            selectedId = 99;
                            break;
                        case R.id.mnpushData:
                            checkNavItem = null;
                            toolbar.setTitle("Push Data");
                            toolbar.setSubtitle(null);

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            FragmentPushData fragmentPushData = new FragmentPushData();
                            FragmentTransaction fragmentTransactionPushData = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionPushData.replace(R.id.frame, fragmentPushData);
                            fragmentTransactionPushData.commit();
                            selectedId = 99;
                            break;
                        case R.id.mnDownloadData:
                            checkNavItem = null;
                            toolbar.setTitle("Download Data");
                            toolbar.setSubtitle(null);

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            FragmentDownloadData fragmentDownloadData = new FragmentDownloadData();
                            FragmentTransaction fragmentTransactionDownloadData = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionDownloadData.replace(R.id.frame, fragmentDownloadData);
                            fragmentTransactionDownloadData.commit();
                            selectedId = 99;
                            break;
                        case R.id.mnCallPlan:
                            checkNavItem = null;
                            toolbar.setTitle("Call Plan");
                            toolbar.setSubtitle(null);

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//                        FragmentListCallPlan fragmentCallPlan = new FragmentListCallPlan();
                            FragmentHeaderCallPlan fragmentCallPlan = new FragmentHeaderCallPlan();
                            FragmentTransaction fragmentTransactionCallPlan = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionCallPlan.replace(R.id.frame, fragmentCallPlan);
                            fragmentTransactionCallPlan.commit();
                            selectedId = 99;
                            break;
                        case R.id.mnAkusisi:
                            checkNavItem = null;
                            toolbar.setTitle("Akusisi");
                            toolbar.setSubtitle(null);

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            FragmentAkuisisi fragmentAkuisisi = new FragmentAkuisisi();
                            FragmentTransaction fragmentTransactionAkuisisi = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionAkuisisi.replace(R.id.frame, fragmentAkuisisi);
                            fragmentTransactionAkuisisi.commit();
                            selectedId = 99;
                            break;
                        case R.id.mnMaintenance:
                            checkNavItem = null;
                            toolbar.setTitle("Maintenance");
                            toolbar.setSubtitle(null);

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            FragementMaintenance fragmentMaintenance = new FragementMaintenance();
                            FragmentTransaction fragmentTransactionMaintenance = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionMaintenance.replace(R.id.frame, fragmentMaintenance);
                            fragmentTransactionMaintenance.commit();
                            selectedId = 99;
                            break;

                        case R.id.mnInfoProgram:
                            checkNavItem = null;
                            toolbar.setTitle("Info Program");
                            toolbar.setSubtitle(null);

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            FragementInfoProgram fragementInfoProgram = new FragementInfoProgram();
                            FragmentTransaction fragmentTransactionInfoProgram = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionInfoProgram.replace(R.id.frame, fragementInfoProgram);
                            fragmentTransactionInfoProgram.commit();
                            selectedId = 99;

                            break;
                        case R.id.mnNotification:
                            checkNavItem = null;
                            toolbar.setTitle("Notification");
                            toolbar.setSubtitle(null);

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            FragmentNotification fragmentNotification = new FragmentNotification();
                            FragmentTransaction fragmentTransactionNotification = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionNotification.replace(R.id.frame, fragmentNotification);
                            fragmentTransactionNotification.commit();
                            selectedId = 99;

                            break;
                        case R.id.mnSetting:
                            checkNavItem = null;
                            toolbar.setTitle("Setting");
                            toolbar.setSubtitle(null);

                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            FragmentSetting fragmentSetting = new FragmentSetting();
                            FragmentTransaction fragmentTransactionSetting = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionSetting.replace(R.id.frame, fragmentSetting);
                            fragmentTransactionSetting.commit();
                            selectedId = 99;
                            break;

                        case R.id.mnCheckOut:
                            checkNavItem = null;
                            break;
                        default:
                            checkNavItem = null;
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                            try {
                                Class<?> fragmentClass = Class.forName(linkMenu[checkNavItem.getItemId()]);
                                try {
                                    toolbar.setTitle(checkNavItem.getTitle().toString());
                                    toolbar.setSubtitle(null);

                                    fragment = (Fragment) fragmentClass.newInstance();
                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, fragment);
                                    fragmentTransaction.addToBackStack(fragment.getClass().getName());
                                    fragmentTransaction.commit();
                                    selectedId = checkNavItem.getItemId();
                                    isSubMenu = false;

                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                    }
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }

    // put image from camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 || requestCode == 130 || requestCode==150) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void clearData() {
        Intent intent = new Intent(MainMenu.this, SplashActivity.class);
        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
        helper.clearDataAfterLogout();
        finish();
        startActivity(intent);
    }


    private void logout(boolean isMustLogout) {
        String strLinkAPI = new clsHardCode().linkLogout;
        JSONObject resJson = new JSONObject();
        mUserLogin dtLogin = new clsMainBL().getUserLogin(getApplicationContext());
        JSONObject dataJson = new JSONObject();


        try {
            dataJson.put("GuiId", dtLogin.getTxtGuID() );
            tokenRepo = new clsTokenRepo(getApplicationContext());
            dataToken = (List<clsToken>) tokenRepo.findAll();
            resJson.put("data", dataJson);
            resJson.put("device_info", new clsHardCode().pDeviceInfo());
            resJson.put("txtRefreshToken", dataToken.get(0).txtRefreshToken.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String mRequestBody = resJson.toString();

        new clsHelperBL().volleyLogin(MainMenu.this, strLinkAPI, mRequestBody, "Logout....", isMustLogout, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                Intent res = null;
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        LoginMobileApps model = gson.fromJson(jsonObject.toString(), LoginMobileApps.class);
                        boolean txtStatus = model.getResult().isStatus();
                        String txtMessage = model.getResult().getMessage();
                        String txtMethode_name = model.getResult().getMethodName();

                        if (txtStatus == true){

                            stopService(new Intent(getApplicationContext(), MyServiceNative.class));
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancelAll();
                            deleteMediaStorage();
                            clearData();
                            ShortcutBadger.removeCountOrThrow(getApplicationContext());
//                            loginRepo = new mUserLoginRepo(getApplicationContext());
//                            mUserLogin data = new mUserLogin();
//                            data.setTxtGuID(model.getData().getTxtGuiID());
//                            data.setIntUserID(model.getData().getIntUserID());
//                            data.setTxtUserName(model.getData().getTxtUserName());
//                            data.setTxtNick(model.getData().getTxtNick());
//                            data.setTxtEmpID(model.getData().getTxtEmpID());
//                            data.setTxtEmail(model.getData().getTxtEmail());
//                            data.setIntDepartmentID(model.getData().getIntDepartmentID());
//                            data.setIntLOBID(model.getData().getIntLOBID());
//                            data.setTxtCompanyCode(model.getData().getTxtCompanyCode());
//                            if (model.getData().getMUserRole()!=null){
//                                data.setIntRoleID(model.getData().getMUserRole().getIntRoleID());
//                                data.setTxtRoleName(model.getData().getMUserRole().getTxtRoleName());
//                            }
//                            data.setDtLogIn(parseDate(model.getData().getDtDateLogin()));
//                            loginRepo.createOrUpdate(data);

                            Log.d("Data info", "logout Success");

                            Intent intent = new Intent(MainMenu.this, SplashActivity.class);
                            finish();
                            startActivity(intent);

                        } else {
                            new ToastCustom().showToasty(MainMenu.this,txtMessage,4);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ShortcutBadgeException e) {
                        Intent intent = new Intent(MainMenu.this, SplashActivity.class);
                        finish();
                        startActivity(intent);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void deleteMediaStorage (){
        File mediaStorageDir = new File(new clsHardCode().txtFolderData + File.separator);
        if (mediaStorageDir.exists()){
            if (mediaStorageDir.isDirectory()){
                for (File currentFile : mediaStorageDir.listFiles()){
                    currentFile.delete();
                }
            }
            mediaStorageDir.delete();
        }

        File akuisisi = new File(new clsHardCode().txtFolderAkuisisi + File.separator);
        if (akuisisi.exists()){
            if (akuisisi.isDirectory()){
                for (File currentFile : akuisisi.listFiles()){
                    currentFile.delete();
                }
            }
            akuisisi.delete();
        }

        File checkin = new File(new clsHardCode().txtFolderCheckIn + File.separator);
        if (checkin.exists()){
            if (checkin.isDirectory()){
                for (File currentFile : checkin.listFiles()){
                    currentFile.delete();
                }
            }
            checkin.delete();
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(MainMenu.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    public class Utility {
        public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>= Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        Account[] accounts = new AuthenticatorUtil().countingAccount(mAccountManager);
        if (!isOnCreate){
            try {
                clsStatusMenuStart _clsStatusMenuStart = new clsMainBL().checkUserActive(getApplicationContext());
                if (_clsStatusMenuStart.get_intStatus()!=null){
                    if (_clsStatusMenuStart.get_intStatus()== enumStatusMenuStart.FormLogin){
                        logout(true);
                    }else if (_clsStatusMenuStart.get_intStatus()==enumStatusMenuStart.PushDataMobile){
                        Intent myIntent = new Intent(getApplicationContext(), MainMenu.class);
                        myIntent.putExtra(i_View, "FragmentPushData");
                        finish();
                        startActivity(myIntent);
                    }else if (_clsStatusMenuStart.get_intStatus()==enumStatusMenuStart.UserActiveLogin){
                        if (accounts.length>0){
                            boolean isExist = false;
                            for (int i=0; i<accounts.length; i++){
                                if (accounts[i].name.equals(dataLogin.get(0).getTxtUserName().toUpperCase().toString())){
                                    isExist = true;
                                    break;
                                }
                            }
                            if (!isExist){
                                Intent myIntent = new Intent(getApplicationContext(), MainMenu.class);
                                myIntent.putExtra(i_View, "FragmentPushData");
                                finish();
                                startActivity(myIntent);
                            }
                        } else {
                            Intent myIntent = new Intent(getApplicationContext(), MainMenu.class);
                            myIntent.putExtra(i_View, "FragmentPushData");
                            finish();
                            startActivity(myIntent);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        isOnCreate = false;

        super.onResume();
    }
}