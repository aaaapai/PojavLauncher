package net.kdt.pojavlaunch.modloaders;

import android.content.Intent;

import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.modloaders.ForgeVersion.ForgeForks;
import net.kdt.pojavlaunch.utils.DownloadUtils;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ForgeUtils {
    private static final String FORGE_METADATA_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/maven-metadata.xml";
    private static final String FORGE_INSTALLER_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/%1$s/forge-%1$s-installer.jar";
    private static final String NEOFORGE_METADATA_URL = "https://maven.neoforged.net/releases/net/neoforged/forge/maven-metadata.xml";
    private static final String NEOFORGE_INSTALLER_URL = "https://maven.neoforged.net/net/neoforged/forge/%1$s/forge-%1$s-installer.jar";
    public static List<ForgeVersion> downloadForgeVersions() throws IOException {
        return downloadForgeVersions(ForgeForks.FORGE, FORGE_METADATA_URL, "forge_versions");
    }

    public static List<ForgeVersion> downloadNeoForgeVersions() throws IOException {
        return downloadForgeVersions(ForgeForks.NEOFORGE, NEOFORGE_METADATA_URL, "neoforge_versions");
    }

    public static List<ForgeVersion> downloadForgeVersions(ForgeForks fork, String metadataUrl, String cache_name) {
        SAXParser saxParser;
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            saxParser = parserFactory.newSAXParser();
        }catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
            // if we cant make a parser we might as well not even try to parse anything
            return null;
        }
        try {
            //of_test();
            return DownloadUtils.downloadStringCached(metadataUrl, cache_name, input -> {
                try {
                    ForgeVersionListHandler handler = new ForgeVersionListHandler(fork);
                    saxParser.parse(new InputSource(new StringReader(input)), handler);
                    return handler.getVersions();
                    // IOException is present here StringReader throws it only if the parser called close()
                    // sooner than needed, which is a parser issue and not an I/O one
                } catch (SAXException | IOException e) {
                    throw new DownloadUtils.ParseException(e);
                }
            });
        } catch (DownloadUtils.ParseException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ForgeVersion> downloadAllForgeVersions() throws IOException {
        List<ForgeVersion> forgeVersionList = downloadForgeVersions();
        List<ForgeVersion> neoforgeVersionList = downloadNeoForgeVersions();
        if (forgeVersionList != null && neoforgeVersionList != null) forgeVersionList.addAll(neoforgeVersionList);
        return forgeVersionList;
    }

    public static List<String> downloadAllForgeVersionsAsStrings() throws IOException {
        List<ForgeVersion> list = downloadAllForgeVersions();
        List<String> versionList = new ArrayList<>();
        for (ForgeVersion version : list) {
            versionList.add(version.toString());
        }
        return versionList;
    }

    public static String getInstallerUrl(ForgeVersion version) {
        return String.format(version.fork == ForgeForks.NEOFORGE ? NEOFORGE_INSTALLER_URL : FORGE_INSTALLER_URL, version.versionString);
    }

    public static void addAutoInstallArgs(Intent intent, File modInstallerJar, boolean createProfile) {
        intent.putExtra("javaArgs", "-javaagent:"+ Tools.DIR_DATA+"/forge_installer/forge_installer.jar"
                + (createProfile ? "=NPS" : "") + // No Profile Suppression
                " -jar "+modInstallerJar.getAbsolutePath());
        intent.putExtra("skipDetectMod", true);
    }
    public static void addAutoInstallArgs(Intent intent, File modInstallerJar, String modpackFixupId) {
        intent.putExtra("javaArgs", "-javaagent:"+ Tools.DIR_DATA+"/forge_installer/forge_installer.jar"
                + "=\"" + modpackFixupId +"\"" +
                " -jar "+modInstallerJar.getAbsolutePath());
        intent.putExtra("skipDetectMod", true);
    }
}
