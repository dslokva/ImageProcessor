import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SettingsStore {
    private static volatile SettingsStore instance;
    private Integer blurRatio;
    private Integer compressionRatio;
    private String compressEnabled;
    private String blurEnabled;
    private static Ini iniFile;

    public SettingsStore() {
        initSavedSettings();
    }

    private void initSavedSettings() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String filePath = Objects.requireNonNull(classLoader.getResource("../../resources/main.ini")).getFile();
            iniFile = new Ini(new File(filePath));

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
        String result = "off";
        if (a != null && a.equals("on"))
            result = "on";

        return result;
    }

    public String getCompressEnabled() {
        return compressEnabled;
    }

    public String getBlurEnabled() {
        return blurEnabled;
    }


    public void setCompressEnabled(String enabled) {
        compressEnabled = "";
        if (enabled != null && enabled.equals("on"))
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
        if (enabled != null && enabled.equals("on"))
            blurEnabled = "checked";
        saveToIni("blurEnabled", blurEnabled);
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
//        try {
//            iniFile.
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return localInstance;
    }
}