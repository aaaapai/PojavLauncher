package net.kdt.pojavlaunch.fragments;

import static net.kdt.pojavlaunch.Tools.shareLog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.mio.fragments.MioPlusFragment;

import net.kdt.pojavlaunch.CustomControlsActivity;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper;

public class MainMenuFragment extends Fragment {
    public static final String TAG = "MainMenuFragment";

    public MainMenuFragment(){
        super(R.layout.fragment_launcher);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button mNewsButton = view.findViewById(R.id.news_button);
        Button mCustomControlButton = view.findViewById(R.id.custom_control_button);
        Button mInstallJarButton = view.findViewById(R.id.install_jar_button);
        Button mShareLogsButton = view.findViewById(R.id.share_logs_button);
        Button mMioPlusButton = view.findViewById(R.id.mio_plus_button);
        Button mMioPlusUpdate = view.findViewById(R.id.mio_plus_update_button);

        ImageButton mEditProfileButton = view.findViewById(R.id.edit_profile_button);
        Button mPlayButton = view.findViewById(R.id.play_button);

        mNewsButton.setOnClickListener(v -> Tools.openURL(requireActivity(), Tools.URL_HOME));
        mCustomControlButton.setOnClickListener(v -> startActivity(new Intent(requireContext(), CustomControlsActivity.class)));
        mInstallJarButton.setOnClickListener(v -> runInstallerWithConfirmation(false));
        mInstallJarButton.setOnLongClickListener(v->{
            runInstallerWithConfirmation(true);
            return true;
        });
        mEditProfileButton.setOnClickListener(v -> Tools.swapFragment(requireActivity(), ProfileEditorFragment.class, ProfileEditorFragment.TAG, true, null));

        mPlayButton.setOnClickListener(v -> ExtraCore.setValue(ExtraConstants.LAUNCH_GAME, true));

        mShareLogsButton.setOnClickListener((v) -> shareLog(requireContext()));

        try{
            mMioPlusButton.setOnClickListener(v -> Tools.swapFragment(requireActivity(), MioPlusFragment.class, MioPlusFragment.TAG, true, null));
        }catch (Exception ignored){

        }

        mMioPlusUpdate.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri url = Uri.parse("https://space.bilibili.com/35801833");
            intent.setData(url);
            startActivity(intent);
        });
        if (LauncherPreferences.DEFAULT_PREF.getBoolean("mio",true)){
            AlertDialog dialog=new AlertDialog.Builder(requireContext())
                    .setTitle("提示")
                    .setMessage("当前Pojav版本为@ShirosakiMio修改版，喜欢的话可以关注B站ShirosakiMio获取相关内容更新或是提出意见或者建议。点击”是“立即跳转。")
                    .setPositiveButton("是", (dialogInterface, i) -> {
                        SharedPreferences.Editor editor = LauncherPreferences.DEFAULT_PREF.edit();
                        editor.putBoolean("mio",false);
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri url = Uri.parse("https://space.bilibili.com/35801833");
                        intent.setData(url);
                        startActivity(intent);
                    })
                    .setNegativeButton("取消",null)
                    .create();
            dialog.show();
        }
        mNewsButton.setOnLongClickListener((v)->{
            Tools.swapFragment(requireActivity(), FabricInstallFragment.class, FabricInstallFragment.TAG, true, null);
            return true;
        });
    }
    private void runInstallerWithConfirmation(boolean isCustomArgs) {
        if (ProgressKeeper.getTaskCount() == 0)
            Tools.installMod(requireActivity(), isCustomArgs);
        else
            Toast.makeText(requireContext(), R.string.tasks_ongoing, Toast.LENGTH_LONG).show();
    }
}
