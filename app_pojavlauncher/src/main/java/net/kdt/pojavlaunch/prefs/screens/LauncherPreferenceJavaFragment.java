package net.kdt.pojavlaunch.prefs.screens;

import static net.kdt.pojavlaunch.Architecture.is32BitsDevice;
import static net.kdt.pojavlaunch.Tools.getTotalDeviceMemory;

import android.os.Bundle;
import android.widget.TextView;

import androidx.preference.EditTextPreference;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.prefs.CustomSeekBarPreference;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;

public class LauncherPreferenceJavaFragment extends LauncherPreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle b, String str) {
        int ramAllocation = LauncherPreferences.PREF_RAM_ALLOCATION;
        int minimumRAMAllocation = LauncherPreferences.PREF_MINIMUM_RAM_ALLOCATION;

        // Triggers a write for some reason
        addPreferencesFromResource(R.xml.pref_java);

        int maxRAM;
        int deviceRam = getTotalDeviceMemory(getContext());

        CustomSeekBarPreference seek7 = findPreference("allocation");
        CustomSeekBarPreference seek8 = findPreference("minimum_allocation");

        seek7.setMin(128);
        seek8.setMin(128);

        if(is32BitsDevice() || deviceRam < 2048) maxRAM = Math.min(1000, deviceRam);
        else maxRAM = deviceRam - (deviceRam < 3064 ? 800 : 1024); //To have a minimum for the device to breathe

        seek7.setMax(maxRAM);
        seek7.setValue(ramAllocation);
        seek7.setSuffix(" MB");

        seek8.setMax(maxRAM);
        seek8.setValue(minimumRAMAllocation);
        seek8.setSuffix(" MB");


        EditTextPreference editJVMArgs = findPreference("javaArgs");
        if (editJVMArgs != null) {
            editJVMArgs.setOnBindEditTextListener(TextView::setSingleLine);
        }

    }
}
