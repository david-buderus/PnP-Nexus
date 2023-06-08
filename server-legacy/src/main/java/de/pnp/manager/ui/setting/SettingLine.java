package de.pnp.manager.ui.setting;

public class SettingLine {

    protected final String unlocalizedName;
    protected final String key;
    protected final SettingType type;

    public SettingLine(String unlocalizedName, String key, SettingType type) {
        this.unlocalizedName = unlocalizedName;
        this.key = key;
        this.type = type;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getKey() {
        return key;
    }

    public SettingType getType() {
        return type;
    }
}
