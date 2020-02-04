import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SettingsStore {
    private static volatile SettingsStore instance;
    private Integer blurRatio;
    private Integer compressionRatio;
    private Ini iniFile;

    public SettingsStore() {
        initSavedSettings();
    }

    private void initSavedSettings() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String filePath = Objects.requireNonNull(classLoader.getResource("../../resources/main.ini")).getFile();
            iniFile = new Ini(new File(filePath));

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

    public Integer getBlurRatio() {
        return blurRatio;
    }

    public void setBlurRatio(Integer blurRatio) {
        this.blurRatio = blurRatio;
        saveIntToIni("blurRatio", blurRatio);
    }

    private void saveIntToIni(String key, Integer value) {
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
        saveIntToIni("compressRatio", compressionRatio);
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
}