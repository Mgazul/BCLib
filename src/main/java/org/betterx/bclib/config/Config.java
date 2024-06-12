package org.betterx.bclib.config;

import org.betterx.bclib.BCLib;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public abstract class Config {
    public static final String CONFIG_SYNC_PREFIX = "CONFIG_";
    protected final ConfigKeeper keeper;
    public final String configID;

    protected abstract void registerEntries();


    protected Config(String modID, String group) {
        configID = modID + "." + group;
        this.keeper = new ConfigKeeper(modID, group);
        this.registerEntries();
    }

    public void saveChanges() {
        this.keeper.save();
    }

    public void reload() {
        this.keeper.reload();
        BCLib.LOGGER.info("Did Reload " + keeper.getConfigFile());
    }

    @Nullable
    public <T, E extends ConfigKeeper.Entry<T>> E getEntry(ConfigKey key, Class<E> type) {
        return this.keeper.getEntry(key, type);
    }

    @Nullable
    public <T, E extends ConfigKeeper.Entry<T>> T getDefault(ConfigKey key, Class<E> type) {
        ConfigKeeper.Entry<T> entry = keeper.getEntry(key, type);
        return entry != null ? entry.getDefault() : null;
    }

    protected String getString(ConfigKey key, String defaultValue) {
        String str = keeper.getValue(key, ConfigKeeper.StringEntry.class);
        if (str == null) {
            ConfigKeeper.StringEntry entry = keeper.registerEntry(key, new ConfigKeeper.StringEntry(defaultValue));
            return entry.getValue();
        }
        return str != null ? str : defaultValue;
    }

    protected String getString(ConfigKey key) {
        String str = keeper.getValue(key, ConfigKeeper.StringEntry.class);
        return str != null ? str : "";
    }

    protected boolean setString(ConfigKey key, String value) {
        try {
            ConfigKeeper.StringEntry entry = keeper.getEntry(key, ConfigKeeper.StringEntry.class);
            if (entry == null) return false;
            entry.setValue(value);
            return true;
        } catch (NullPointerException ex) {
            BCLib.LOGGER.warn("", ex);
        }
        return false;
    }

    protected int getInt(ConfigKey key, int defaultValue) {
        Integer val = keeper.getValue(key, ConfigKeeper.IntegerEntry.class);
        if (val == null) {
            ConfigKeeper.IntegerEntry entry = keeper.registerEntry(key, new ConfigKeeper.IntegerEntry(defaultValue));
            return entry.getValue();
        }
        return val != null ? val : defaultValue;
    }

    protected int getInt(ConfigKey key) {
        Integer val = keeper.getValue(key, ConfigKeeper.IntegerEntry.class);
        return val != null ? val : 0;
    }

    protected boolean setInt(ConfigKey key, int value) {
        try {
            ConfigKeeper.IntegerEntry entry = keeper.getEntry(key, ConfigKeeper.IntegerEntry.class);
            if (entry == null) return false;
            entry.setValue(value);
            return true;
        } catch (NullPointerException ex) {
            BCLib.LOGGER.warn("", ex);
        }
        return false;
    }

    protected <T extends Comparable<T>, RE extends ConfigKeeper.RangeEntry<T>> boolean setRanged(
            ConfigKey key,
            T value,
            Class<RE> type
    ) {
        try {
            ConfigKeeper.RangeEntry<T> entry = keeper.getEntry(key, type);
            if (entry == null) return false;
            entry.setValue(value);
            return true;
        } catch (NullPointerException | ClassCastException ex) {
            BCLib.LOGGER.warn("", ex);
        }
        return false;
    }

    protected float getFloat(ConfigKey key, float defaultValue) {
        Float val = keeper.getValue(key, ConfigKeeper.FloatEntry.class);
        if (val == null) {
            ConfigKeeper.FloatEntry entry = keeper.registerEntry(key, new ConfigKeeper.FloatEntry(defaultValue));
            return entry.getValue();
        }
        return val;
    }

    protected float getFloat(ConfigKey key) {
        Float val = keeper.getValue(key, ConfigKeeper.FloatEntry.class);
        return val != null ? val : 0.0F;
    }

    protected boolean setFloat(ConfigKey key, float value) {
        try {
            ConfigKeeper.FloatEntry entry = keeper.getEntry(key, ConfigKeeper.FloatEntry.class);
            if (entry == null) return false;
            entry.setValue(value);
            return true;
        } catch (NullPointerException ex) {
            BCLib.LOGGER.warn("", ex);
        }
        return false;
    }

    protected boolean getBoolean(ConfigKey key, boolean defaultValue) {
        Boolean val = keeper.getValue(key, ConfigKeeper.BooleanEntry.class);
        if (val == null) {
            ConfigKeeper.BooleanEntry entry = keeper.registerEntry(key, new ConfigKeeper.BooleanEntry(defaultValue));
            return entry.getValue();
        }
        return val;
    }

    protected boolean getBoolean(ConfigKey key) {
        Boolean val = keeper.getValue(key, ConfigKeeper.BooleanEntry.class);
        return val != null ? val : false;
    }

    protected boolean setBoolean(ConfigKey key, boolean value) {
        try {
            ConfigKeeper.BooleanEntry entry = keeper.getEntry(key, ConfigKeeper.BooleanEntry.class);

            if (entry == null) return false;
            entry.setValue(value);
            return true;
        } catch (NullPointerException ex) {
            BCLib.LOGGER.warn("", ex);
        }
        return false;
    }

    protected List<String> getStringArray(ConfigKey key, List<String> defaultValue) {
        List<String> str = keeper.getValue(key, ConfigKeeper.StringArrayEntry.class);
        if (str == null) {
            ConfigKeeper.StringArrayEntry entry = keeper.registerEntry(
                    key,
                    new ConfigKeeper.StringArrayEntry(defaultValue)
            );
            return entry.getValue();
        }
        return str != null ? str : defaultValue;
    }

    protected List<String> getStringArray(ConfigKey key) {
        List<String> str = keeper.getValue(key, ConfigKeeper.StringArrayEntry.class);
        return str != null ? str : new ArrayList<>(0);
    }

    protected boolean setStringArray(ConfigKey key, List<String> value) {
        try {
            ConfigKeeper.StringArrayEntry entry = keeper.getEntry(key, ConfigKeeper.StringArrayEntry.class);
            if (entry == null) return false;
            entry.setValue(value);
            return true;
        } catch (NullPointerException ex) {
            BCLib.LOGGER.warn("", ex);
        }
        return false;
    }
}
