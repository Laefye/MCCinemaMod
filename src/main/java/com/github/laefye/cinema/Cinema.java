package com.github.laefye.cinema;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Cinema implements ModInitializer {
    public static Identifier CINEMA_CHANNEL = id("channel");
    public static final Logger LOGGER = LoggerFactory.getLogger("cinema");
    public static String VLC_FOLDER;
    public static boolean disableMod = false;
    public enum OS {
        WINDOWS,
        LINUX,
        MACOS,
        UNKNOWN
    }

    @Override
    public void onInitialize() {
        var os = getOperatingSystemType();
        LOGGER.info("Cinema - OS: " + os);
        if (os == OS.WINDOWS) {
            VLC_FOLDER = new File("mods/vlc").getAbsolutePath();
            if (Files.exists(Paths.get(VLC_FOLDER))) {
                NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLC_FOLDER);
            } else {
                disableMod = true;
            }
        }
        if (os == OS.LINUX) {
            if (!findLinuxVLCNativeLibrary()) {
                disableMod = true;
            }
        }
        if (os == OS.MACOS) {
            uk.co.caprica.vlcj.binding.lib.LibC.INSTANCE.setenv("VLC_PLUGIN_PATH", "/Applications/VLC.app/Contents/MacOS/plugins", 1);
            VLC_FOLDER = new File("/Applications/VLC.app/Contents/MacOS/lib").getAbsolutePath();
            if (Files.exists(Paths.get(VLC_FOLDER))) {
                NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), VLC_FOLDER);
            } else {
                disableMod = true;
            }
        }
    }

    public static OS getOperatingSystemType() {
        String os = System.getProperty("os.name", "generic").toLowerCase();
        LOGGER.info("Cinema - Property OS: " + os);

        if (os.contains("windows")) {
            return OS.WINDOWS;
        } else if (os.contains("nux")) {
            return OS.LINUX;
        } else if (os.contains("mac")) {
            return OS.MACOS;
        }
        return OS.UNKNOWN;
    }

    public static boolean findLinuxVLCNativeLibrary() {
        return findFileStartsWith("/usr/lib/", "libvlc.so") || findFileStartsWith("/lib/", "libvlc.so") || findFileStartsWith("/usr/lib/x86_64-linux-gnu/", "libvlc.so");
    }

    public static boolean findFileStartsWith(String directory, String startswith) {
        var list = new File(directory).listFiles();
        if (list == null) {
            return false;
        }
        for (var f : list) {
            if (!f.isDirectory()) {
                if (f.getName().startsWith(startswith)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Identifier id(String path) {
        return new Identifier("cinema", path);
    }
}
