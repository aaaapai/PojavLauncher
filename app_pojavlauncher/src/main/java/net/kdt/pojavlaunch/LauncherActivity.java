package net.kdt.pojavlaunch;

import static net.kdt.pojavlaunch.MainActivity.INTENT_MINECRAFT_VERSION;
import static net.kdt.pojavlaunch.PojavApplication.sExecutorService;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.kdt.mcgui.ProgressLayout;
import com.kdt.mcgui.mcAccountSpinner;
import com.mio.MioUtils;
import com.mio.fragments.ModManageFragment;

import net.kdt.pojavlaunch.fragments.MainMenuFragment;
import net.kdt.pojavlaunch.fragments.MicrosoftLoginFragment;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.extra.ExtraListener;

import net.kdt.pojavlaunch.fragments.SelectAuthFragment;
import net.kdt.pojavlaunch.multirt.MultiRTConfigDialog;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import net.kdt.pojavlaunch.prefs.screens.LauncherPreferenceFragment;
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper;
import net.kdt.pojavlaunch.services.ProgressServiceKeeper;
import net.kdt.pojavlaunch.tasks.AsyncMinecraftDownloader;
import net.kdt.pojavlaunch.tasks.AsyncVersionList;
import net.kdt.pojavlaunch.value.launcherprofiles.LauncherProfiles;
import net.kdt.pojavlaunch.value.launcherprofiles.MinecraftProfile;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LauncherActivity extends BaseActivity {
    public static final String SETTING_FRAGMENT_TAG = "SETTINGS_FRAGMENT";

    private final int REQUEST_STORAGE_REQUEST_CODE = 1;
    private final Object mLockStoragePerm = new Object();


    private mcAccountSpinner mAccountSpinner;
    private FragmentContainerView mFragmentView;
    private ImageButton mSettingsButton, mDeleteAccountButton;
    private ProgressLayout mProgressLayout;
    private ProgressServiceKeeper mProgressServiceKeeper;

    /* Allows to switch from one button "type" to another */
    private final FragmentManager.FragmentLifecycleCallbacks mFragmentCallbackListener = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
            mSettingsButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), f instanceof MainMenuFragment
                    ? R.drawable.ic_menu_settings : R.drawable.ic_menu_home));
        }
    };

    /* Listener for the back button in settings */
    private final ExtraListener<String> mBackPreferenceListener = (key, value) -> {
        if(value.equals("true")) onBackPressed();
        return false;
    };

    /* Listener for the auth method selection screen */
    private final ExtraListener<Boolean> mSelectAuthMethod = (key, value) -> {
        Fragment fragment = getSupportFragmentManager().findFragmentById(mFragmentView.getId());
        // Allow starting the add account only from the main menu, should it be moved to fragment itself ?
        if(!(fragment instanceof MainMenuFragment)) return false;

        Tools.swapFragment(this, SelectAuthFragment.class, SelectAuthFragment.TAG, true, null);
        return false;
    };

    /* Listener for the settings fragment */
    private final View.OnClickListener mSettingButtonListener = v -> {
        Fragment fragment = getSupportFragmentManager().findFragmentById(mFragmentView.getId());
        if(fragment instanceof MainMenuFragment){
            Tools.swapFragment(this, LauncherPreferenceFragment.class, SETTING_FRAGMENT_TAG, true, null);
        } else{
            // The setting button doubles as a home button now
            while(!(getSupportFragmentManager().findFragmentById(mFragmentView.getId()) instanceof MainMenuFragment)){
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
    };

    /* Listener for account deletion */
    private final View.OnClickListener mAccountDeleteButtonListener = v -> new AlertDialog.Builder(this)
            .setMessage(R.string.warning_remove_account)
            .setPositiveButton(android.R.string.cancel, null)
            .setNeutralButton(R.string.global_delete, (dialog, which) -> mAccountSpinner.removeCurrentAccount())
            .show();

    private final ExtraListener<Boolean> mLaunchGameListener = (key, value) -> {
        if(mProgressLayout.hasProcesses()){
            Toast.makeText(this, R.string.tasks_ongoing, Toast.LENGTH_LONG).show();
            return false;
        }

        String selectedProfile = LauncherPreferences.DEFAULT_PREF.getString(LauncherPreferences.PREF_KEY_CURRENT_PROFILE,"");
        if (LauncherProfiles.mainProfileJson == null || !LauncherProfiles.mainProfileJson.profiles.containsKey(selectedProfile)){
            Toast.makeText(this, R.string.error_no_version, Toast.LENGTH_LONG).show();
            return false;
        }
        MinecraftProfile prof = LauncherProfiles.mainProfileJson.profiles.get(selectedProfile);
        if (prof == null || prof.lastVersionId == null || "Unknown".equals(prof.lastVersionId)){
            Toast.makeText(this, R.string.error_no_version, Toast.LENGTH_LONG).show();
            return false;
        }

        if(mAccountSpinner.getSelectedAccount() == null){
            Toast.makeText(this, R.string.no_saved_accounts, Toast.LENGTH_LONG).show();
            ExtraCore.setValue(ExtraConstants.SELECT_AUTH_METHOD, true);
            return false;
        }
        String normalizedVersionId = AsyncMinecraftDownloader.normalizeVersionId(prof.lastVersionId);
        JMinecraftVersionList.Version mcVersion = AsyncMinecraftDownloader.getListedVersion(normalizedVersionId);
        new AsyncMinecraftDownloader(this, mcVersion, normalizedVersionId, new AsyncMinecraftDownloader.DoneListener() {
            @Override
            public void onDownloadDone() {
                ProgressKeeper.waitUntilDone(()-> runOnUiThread(() -> {
                    try {
                        Intent mainIntent = new Intent(getBaseContext(), MainActivity.class);
                        mainIntent.putExtra(INTENT_MINECRAFT_VERSION, normalizedVersionId);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(mainIntent);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid()); //You should kill yourself, NOW!
                    } catch (Throwable e) {
                        Tools.showError(getBaseContext(), e);
                    }
                }));
            }

            @Override
            public void onDownloadFailed(Throwable th) {
                if(th != null) Tools.showError(LauncherActivity.this, R.string.mc_download_failed, th);
            }
        });
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pojav_launcher);
        getWindow().setBackgroundDrawable(null);
        bindViews();
        ProgressKeeper.addTaskCountListener((mProgressServiceKeeper = new ProgressServiceKeeper(this)));
        askForStoragePermission(); // Will wait here

        mSettingsButton.setOnClickListener(mSettingButtonListener);
        mDeleteAccountButton.setOnClickListener(mAccountDeleteButtonListener);
        ProgressKeeper.addTaskCountListener(mProgressLayout);
        ExtraCore.addExtraListener(ExtraConstants.BACK_PREFERENCE, mBackPreferenceListener);
        ExtraCore.addExtraListener(ExtraConstants.SELECT_AUTH_METHOD, mSelectAuthMethod);

        ExtraCore.addExtraListener(ExtraConstants.LAUNCH_GAME, mLaunchGameListener);

        new AsyncVersionList().getVersionList(versions -> ExtraCore.setValue(ExtraConstants.RELEASE_TABLE, versions), false);

        mProgressLayout.observe(ProgressLayout.DOWNLOAD_MINECRAFT);
        mProgressLayout.observe(ProgressLayout.UNPACK_RUNTIME);
        mProgressLayout.observe(ProgressLayout.INSTALL_MODPACK);
        mProgressLayout.observe(ProgressLayout.AUTHENTICATE_MICROSOFT);
        mProgressLayout.observe(ProgressLayout.DOWNLOAD_VERSION_LIST);
    }

    @Override
    public boolean setFullscreen() {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentCallbackListener, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressLayout.cleanUpObservers();
        ProgressKeeper.removeTaskCountListener(mProgressLayout);
        ProgressKeeper.removeTaskCountListener(mProgressServiceKeeper);
        ExtraCore.removeExtraListenerFromValue(ExtraConstants.BACK_PREFERENCE, mBackPreferenceListener);
        ExtraCore.removeExtraListenerFromValue(ExtraConstants.SELECT_AUTH_METHOD, mSelectAuthMethod);
        ExtraCore.removeExtraListenerFromValue(ExtraConstants.LAUNCH_GAME, mLaunchGameListener);

        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(mFragmentCallbackListener);
    }
    private ProgressDialog dialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        if(requestCode == Tools.RUN_MOD_INSTALLER && data != null){
            Tools.launchModInstaller(this, data);
            return;
        }
        if(requestCode == MultiRTConfigDialog.MULTIRT_PICK_RUNTIME && data != null){
            Tools.installRuntimeFromUri(this, data.getData());
        }
        if(requestCode == 114514 && data != null){
            ProgressDialog dialog=new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            sExecutorService.execute(() -> {
                try {
                    Uri uri=data.getData();
                    final String name = Tools.getFileName(LauncherActivity.this, uri);
                    final File modFile = new File(ModManageFragment.path, name);
                    if (!modFile.getParentFile().exists()){
                        modFile.getParentFile().mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(modFile);
                    InputStream input = LauncherActivity.this.getContentResolver().openInputStream(uri);
                    IOUtils.copy(input, fos);
                    input.close();
                    fos.close();
                    LauncherActivity.this.runOnUiThread(() -> {
                        dialog.dismiss();
                        ModManageFragment.addModToList(modFile);
                        Toast.makeText(LauncherActivity.this,"Mod安装完成",Toast.LENGTH_LONG).show();
                    });
                }catch(IOException e) {
                    LauncherActivity.this.runOnUiThread(() -> {
                        dialog.dismiss();
                    });
                    Tools.showError(LauncherActivity.this, e);
                }
            });
        }
        if(requestCode == 1919810 && data != null){
            ProgressDialog dialog=new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            PojavApplication.sExecutorService.execute(() -> {
                try {
                    Uri uri=data.getData();
                    final String name = Tools.getFileName(this, uri);
                    if (!name.endsWith(".zip")){
                        runOnUiThread(() -> {
                            dialog.dismiss();
                            Toast.makeText(this,"所选择文件不是zip格式的整合包，请重新选择。",Toast.LENGTH_LONG).show();
                        });
                        return;
                    }
                    final File modPackFile = new File(Tools.DIR_GAME_HOME+"/整合包", name);
                    if (!modPackFile.getParentFile().exists()){
                        modPackFile.getParentFile().mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(modPackFile);
                    InputStream input = getContentResolver().openInputStream(uri);
                    IOUtils.copy(input, fos);
                    input.close();
                    fos.close();
                    runOnUiThread(dialog::dismiss);
                    runOnUiThread(()->MioUtils.installModPack(LauncherActivity.this,modPackFile.getAbsolutePath()));
                }catch (Exception e){
                    runOnUiThread(dialog::dismiss);
                    Tools.showError(LauncherActivity.this, e);
                }
            });

        }
    }

    /** Custom implementation to feel more natural when a backstack isn't present */
    @Override
    public void onBackPressed() {
        MicrosoftLoginFragment fragment = (MicrosoftLoginFragment) getVisibleFragment(MicrosoftLoginFragment.TAG);
        if(fragment != null){
            if(fragment.canGoBack()){
                fragment.goBack();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public void onAttachedToWindow() {
        LauncherPreferences.computeNotchSize(this);
    }

    @SuppressWarnings("SameParameterValue")
    private Fragment getVisibleFragment(String tag){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if(fragment != null && fragment.isVisible()) {
            return fragment;
        }
        return null;
    }

    @SuppressWarnings("unused")
    private Fragment getVisibleFragment(int id){
        Fragment fragment = getSupportFragmentManager().findFragmentById(id);
        if(fragment != null && fragment.isVisible()) {
            return fragment;
        }
        return null;
    }

    private void askForStoragePermission(){
        int revokeCount = 0;
        while (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 29 && !isStorageAllowed()) { //Do not ask for storage at all on Android 10+
            try {
                revokeCount++;
                if (revokeCount >= 3) {
                    Toast.makeText(this, R.string.toast_permission_denied, Toast.LENGTH_LONG).show();
                    finish();
                }
                requestStoragePermission();

                synchronized (mLockStoragePerm) {
                    mLockStoragePerm.wait();
                }
            } catch (InterruptedException e) {
                Log.e("LauncherActivity", e.toString());
            }
        }
    }

    private boolean isStorageAllowed() {
        //Getting the permission status
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);


        //If permission is granted returning true
        return result1 == PackageManager.PERMISSION_GRANTED &&
                result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_REQUEST_CODE){
            synchronized (mLockStoragePerm) {
                mLockStoragePerm.notifyAll();
            }
        }
    }

    /** Stuff all the view boilerplate here */
    private void bindViews(){
        mFragmentView = findViewById(R.id.container_fragment);
        mSettingsButton = findViewById(R.id.setting_button);
        mDeleteAccountButton = findViewById(R.id.delete_account_button);
        mAccountSpinner = findViewById(R.id.account_spinner);
        mProgressLayout = findViewById(R.id.progress_layout);
    }




}
