package com.gdb.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AccountRulesPropertiesLoader {
    private Properties properties = new Properties();

    public AccountRulesPropertiesLoader(String configPath) {
        loadProperties(configPath);
    }

    private void loadProperties(String configPath) {
        InputStream in = null;
        try {
            // 1. Try the classpath first.
            in = getClass().getClassLoader().getResourceAsStream(configPath);
            // 2. Fall back to the file system if the classpath lookup found nothing.
            if (in == null && new File(configPath).exists()) {
                in = new FileInputStream(configPath);
            }
            if (in != null) {
                properties.load(in);
            }
        } catch (Exception e) {
            System.out.println("[Config] Warning: Could not load rules from " + configPath + " - " + e.getMessage());
        } finally {
            // 3. Close the stream afterwards.
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    // Ignore close failures; the rules were already loaded.
                }
            }
        }
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
