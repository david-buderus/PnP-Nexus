package manager;

public enum Language {
    german, english;

    public String getConfigPath() {
        switch (this) {
            case german:
                return "config/german.properties";
            case english:
                return "config/english.properties";
        }
        return english.getConfigPath();
    }
}
