package io.github.fozimus.discworkshop.audio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.SystemUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.fozimus.discworkshop.DiscWorkshop;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class AudioDownloader {
    private static final String GITHUB_DOWNLOAD_URL = "https://github.com/%s/releases/latest/download/%s";
    private static final String GITHUB_VERSION_URL = "https://api.github.com/repos/%s/releases/latest";

    private static final String YT_DLP_REPO = "yt-dlp/yt-dlp";
    private static final String YT_DLP_DOWNLOAD_NAME = String.format("yt-dlp%s", SystemUtils.IS_OS_WINDOWS ? ".exe"
                                                                     : SystemUtils.IS_OS_MAC ? "_macos"
                                                                     : "_linux");
    private static final Path YT_DLP_EXE = getExePath(YT_DLP_DOWNLOAD_NAME);

    private static final String FFMPEG_REPO = "Tyrrrz/FFmpegBin";
    private static final String FFMPEG_DOWNLOAD_NAME = String.format("ffmpeg-%s.zip", SystemUtils.IS_OS_WINDOWS ? "windows-x64"
                                                                     : SystemUtils.IS_OS_MAC ? "osx-x64"
                                                                     : "linux-x64");
    private static final Path FFMPEG_EXE = getExePath(FFMPEG_DOWNLOAD_NAME);
    private static final String FFMPEG_ZIP_FILENAME = "ffmpeg" + (SystemUtils.IS_OS_WINDOWS ? ".exe" : "");

    public static final Path DOWNLOAD_FOLDER = DiscWorkshop.MODPATH.resolve("downloads");
    
    private static final Path getExePath(String name) {
        if (name.endsWith(".zip")) {
            return DiscWorkshop.MODPATH.resolve(name.substring(0, name.length() - 4) + (SystemUtils.IS_OS_WINDOWS ? ".exe" : ""));
        }
        return DiscWorkshop.MODPATH.resolve(name);
    }

    private static String currentVersion(Path exe) {
        Path filePath = Path.of(exe.toString() + ".version");

        filePath.toFile().getParentFile().mkdirs();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String version = reader.readLine();
            if (version == null) return "";
            return version;
        }
        catch (IOException e) {
            return "";
        }
    }

    private static void writeVersionFile(Path exe, String version) throws IOException {
        Path filePath = Path.of(exe.toString() + ".version");

        filePath.toFile().getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(version);
        }
    }

    private static String getVersion(String repo) throws URISyntaxException, IOException {
        try (BufferedReader  reader =
             new BufferedReader(
              (new InputStreamReader
               (new URI(String.format(GITHUB_VERSION_URL, repo)).toURL().openStream())))) {
            JsonObject JsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            String version = JsonObject.get("tag_name").getAsString();
            if (version == null) return null;
            return version;
        }
    }

    private static void downloadIfNeeded(String repo, String downloadName, Path exe) {
        downloadIfNeeded(repo, downloadName, exe, null);
    }
    
    private static void downloadIfNeeded(String repo, String downloadName, Path exe, String zipFilename) {
        exe.toFile().getParentFile().mkdirs();
        
        try {
            String version = getVersion(repo);
            String curVersion = currentVersion(exe);
            if (!curVersion.equals(version)) {
                if (curVersion.isBlank()) {
                    DiscWorkshop.LOGGER.info("Installing {} version: {}", repo, version);
                }
                else {
                    DiscWorkshop.LOGGER.info("Updating {} version: {} -> {}", repo, curVersion, version);
                }
                
                try (InputStream inputStream =
                     new URI(String.format(GITHUB_DOWNLOAD_URL, repo, downloadName)).toURL().openStream()) {
                    if (downloadName.endsWith(".zip")) {
                        try (ZipInputStream zipInput = new ZipInputStream(inputStream)) {
                            ZipEntry zipEntry =  zipInput.getNextEntry();
                            while (zipEntry != null) {
                                if (zipEntry.getName().equals(zipFilename)) {
                                    Files.copy(zipInput, exe, StandardCopyOption.REPLACE_EXISTING);
                                    break;
                                }
                                zipEntry = zipInput.getNextEntry();
                            }

                            if (zipEntry == null) {
                                DiscWorkshop.LOGGER.error("Error downloading {}: zip {} does not contain {}", repo, downloadName, zipFilename);
                                return;
                            }
                        }
                    }
                    else {
                        Files.copy(inputStream, exe, StandardCopyOption.REPLACE_EXISTING);
                    }
                    if (SystemUtils.IS_OS_LINUX) {
                        Runtime.getRuntime().exec(new String[] {"chmod", "+x", exe.toString()});
                    }
                }
                DiscWorkshop.LOGGER.info("Downloaded {}", exe.getFileName().toString());
                writeVersionFile(exe, version);
            }
            else {
                DiscWorkshop.LOGGER.info("{} up to date! (version: {})", repo, curVersion);
            }
        }
        catch (IOException | URISyntaxException e) {
            DiscWorkshop.LOGGER.error("Error downloading {}: {}", repo, e.getMessage());
        }
    }
    
    public static void checkExecutables() throws IOException {
        downloadIfNeeded(YT_DLP_REPO, YT_DLP_DOWNLOAD_NAME, YT_DLP_EXE);
        downloadIfNeeded(FFMPEG_REPO, FFMPEG_DOWNLOAD_NAME, FFMPEG_EXE, FFMPEG_ZIP_FILENAME);
    }

    public static CompletableFuture<Boolean> downloadAudio(MinecraftClient client, String url, String fileName) throws IOException {
        DOWNLOAD_FOLDER.toFile().mkdirs();

        if (!FFMPEG_EXE.toFile().exists()) {
            throw new FileNotFoundException(FFMPEG_EXE.toString());
        }

        if (!YT_DLP_EXE.toFile().exists()) {
            throw new FileNotFoundException(YT_DLP_EXE.toString());
        }

        return CompletableFuture.supplyAsync(() -> {
                Process process;
				try {
                    client.player.sendMessage(Text.literal("Downloading..."), true);
					process = Runtime.getRuntime().exec(new String[]{
					        YT_DLP_EXE.toString(), url, "-x", "--no-progress", "--concat-playlist", "always", "--add-metadata",
					        "-P", DOWNLOAD_FOLDER.toString(), "--break-match-filter", "ext~=3gp|aac|flv|m4a|mov|mp3|mp4|ogg|wav|webm|opus",
                            "--audio-quality", "96K",
					        "--audio-format", "vorbis",
					        "--ffmpeg-location", FFMPEG_EXE.toString(),
					        "-o", String.format("%%(playlist_autonumber&{}|)s%s.%%(ext)s", fileName)
					    });

                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        for (String line; (line = errorReader.readLine()) != null;) {
                            DiscWorkshop.LOGGER.warn(line);
                        }
                    }

                    try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        for (String line; (line = outputReader.readLine()) != null;) {
                            DiscWorkshop.LOGGER.debug(line);
                        }
                    }

                    if (process.waitFor() == 0) {
                        return true;
                    } else {
                        DiscWorkshop.LOGGER.error("Command failed with exit code: {}", process.exitValue());
                        return false;
                    }
				} catch (IOException | InterruptedException e) {
                    return false;
				}
            });
    }
}
