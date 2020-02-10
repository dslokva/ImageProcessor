import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SettingsStore {
    private static volatile SettingsStore instance;
    private Integer blurRatio;
    private Integer compressionRatio;
    private String compressEnabled;
    private String blurEnabled;
    private String histogramUpEnabled;
    private String lightUpEnabled;
    private static Ini iniFile;
    private HashMap<String, HashMap> galleryList;

    public SettingsStore() {
        initSavedSettings();
    }

    private void initSavedSettings() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String filePath = Objects.requireNonNull(classLoader.getResource("../../resources/main.ini")).getFile();
            iniFile = new Ini(new File(filePath));

            lightUpEnabled = checkOnStr(iniFile.get("ProcessingSettings", "lightUpEnabled"));
            setLightUpEnabledEnabled(lightUpEnabled);

            histogramUpEnabled = checkOnStr(iniFile.get("ProcessingSettings", "histogramUpEnabled"));
            setHistogramUpEnabled(histogramUpEnabled);

            blurEnabled = checkOnStr(iniFile.get("ProcessingSettings", "blurEnabled"));
            setBlurEnabled(blurEnabled);

            compressEnabled = checkOnStr(iniFile.get("ProcessingSettings", "compressEnabled"));
            setCompressEnabled(compressEnabled);

            blurRatio = checkInt(iniFile.get("ProcessingSettings", "blurRatio"));
            setBlurRatio(blurRatio);

            compressionRatio = checkInt(iniFile.get("ProcessingSettings", "compressRatio"));
            setCompressionRatio(compressionRatio);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Integer checkInt(String a) {
        Integer result = 0;
        try {
            if (a != null)
                result = Integer.valueOf(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String checkOnStr(String a) {
        String result = "";
        if (a != null && a.equals("checked"))
            result = "checked";

        return result;
    }

    public String getHistogramUpEnabled() {
        return histogramUpEnabled;
    }

    public String getLightUpEnabled() {
        return lightUpEnabled;
    }

    public String getCompressEnabled() {
        return compressEnabled;
    }

    public String getBlurEnabled() {
        return blurEnabled;
    }


    public void setCompressEnabled(String enabled) {
        compressEnabled = "";
        if (enabled != null && (enabled.equals("on") || enabled.equals("checked")))
            compressEnabled = "checked";
        saveToIni("compressEnabled", compressEnabled);
    }

    public Integer getBlurRatio() {
        return blurRatio;
    }

    public void setBlurRatio(Integer blurRatio) {
        this.blurRatio = blurRatio;
        saveToIni("blurRatio", blurRatio);
    }

    public void setBlurEnabled(String enabled) {
        blurEnabled = "";
        if (enabled != null && (enabled.equals("on") || enabled.equals("checked")))
            blurEnabled = "checked";
        saveToIni("blurEnabled", blurEnabled);
    }

    public void setHistogramUpEnabled(String enabled) {
        histogramUpEnabled = "";
        if (enabled != null && (enabled.equals("on") || enabled.equals("checked")))
            histogramUpEnabled = "checked";
        saveToIni("histogramUpEnabled", histogramUpEnabled);
    }

    public void setLightUpEnabledEnabled(String enabled) {
        lightUpEnabled = "";
        if (enabled != null && (enabled.equals("on") || enabled.equals("checked")))
            lightUpEnabled = "checked";
        saveToIni("lightUpEnabled", lightUpEnabled);
    }


    private void saveToIni(String key, Object value) {
        try {
            if (iniFile != null) {
                iniFile.put("ProcessingSettings", key, value);
                iniFile.store();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Integer getCompressionRatio() {
        return compressionRatio;
    }

    public void setCompressionRatio(Integer compressionRatio) {
        this.compressionRatio = compressionRatio;
        saveToIni("compressRatio", compressionRatio);
    }

    public static SettingsStore getInstance() {
        SettingsStore localInstance = instance;
        if (localInstance == null) {
            synchronized (SettingsStore.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SettingsStore();
                }
            }
        }
        return localInstance;
    }

    public HashMap<String, HashMap> getGalleryList() {
        return galleryList;
    }

    public void updateGalleryList(String absoluteDiskPath) {
        HashMap<String, HashMap> galleryList = new HashMap<>();

        File outputDir = new File(absoluteDiskPath);
        File[] listOfFolders = outputDir.listFiles();

        if (listOfFolders != null) {
            for (File folder : listOfFolders) {
                if (!folder.isFile()) { // folder loop
                    File[] listOfFiles = folder.listFiles();

                    if (listOfFiles != null) { //files loop for each folder
                        HashMap<String, HashMap> filesMap = new HashMap<>();
                        HashMap<String, String> detailsMap = new HashMap<>();

                        for (File file : listOfFiles) { //create map with file detail info, size, path, etc
                            if (file.getName().endsWith("original.jpg")) {
                                detailsMap.put("size", getFileSizeKiloBytes(file));
                                detailsMap.put("imgLink", "/ImageProcessor_war/output/" + folder.getName() + "/" + file.getName());
                                detailsMap.put("createDateTime", getFileCreateDate(file));
                                detailsMap.put("compressedSize", getFileSizeKiloBytes(file));
                            } else {
                                detailsMap.put("size", getFileSizeKiloBytes(file));
                                detailsMap.put("imgLink", "/ImageProcessor_war/output/" + folder.getName() + "/" + file.getName());
                            }
                            filesMap.put(file.getName(), detailsMap);
                        }
                        galleryList.put(folder.getName(), filesMap);
                    }
                }
            }
        } else {
            galleryList.put("No items", null);
        }
        this.galleryList = galleryList;
    }

    private String getFileCreateDate(File file) {
        try {
            Path p = Paths.get(file.getAbsoluteFile().toURI());
            BasicFileAttributes attr = null;
            attr = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh-mm");

            return df.format(attr.creationTime().toMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getFileSizeKiloBytes(File file) {
        if (file != null) {
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);
            return df.format(file.length() / 1024) + "  kb";
        } else return "0 kb";
    }
}