package net.kdt.pojavlaunch.modloaders.modpacks.api;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.modloaders.ModloaderDownloadListener;
import net.kdt.pojavlaunch.modloaders.modpacks.models.ModDetail;
import net.kdt.pojavlaunch.modloaders.modpacks.models.ModItem;
import net.kdt.pojavlaunch.modloaders.modpacks.models.SearchFilters;

import java.io.File;

/**
 *
 */
public interface ModpackApi {

    /**
     * @param searchFilters Filters
     * @return A list of mod items
     */
    ModItem[] searchMod(SearchFilters searchFilters);

    /**
     * Fetch the mod details
     * @param item The moditem that was selected
     * @return Detailed data about a mod(pack)
     */
    ModDetail getModDetails(ModItem item);

    /**
     * Download and install the mod(pack)
     * @param modDetail The mod detail data
     * @param selectedVersion The selected version
     */
    default void handleInstallation(Context context, ModDetail modDetail, int selectedVersion) {
        PojavApplication.sExecutorService.execute(() -> {
            ModLoader loaderInfo = installMod(modDetail, selectedVersion);
            if (loaderInfo == null) return;
            loaderInfo.getDownloadTask(new NotificationDownloadListener(context, loaderInfo)).run();
        });
    }

    /**
     * Install the mod(pack).
     * May require the download of additional files.
     * May requires launching the installation of a modloader
     * @param modDetail The mod detail data
     * @param selectedVersion The selected version
     */
    ModLoader installMod(ModDetail modDetail, int selectedVersion);
}
