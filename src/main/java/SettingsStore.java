import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
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
    private HashMap<String, List> galleryList;

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

    public HashMap<String, List> getGalleryList() {
        return galleryList;
    }

    public void updateGalleryList(String absoluteDiskPath) {
        HashMap<String, List> galleryList = new HashMap<>();

        ArrayList<String> detailsList = new ArrayList<>();
        File folder = new File(absoluteDiskPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (!file.isFile()) {
                    galleryList.put(file.getName(), detailsList);
                }

            }
        } else {
            galleryList.put("No items", detailsList);
        }
        this.galleryList = galleryList;
    }
}