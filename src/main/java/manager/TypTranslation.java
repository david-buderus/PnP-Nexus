package manager;

import java.util.*;

public abstract class TypTranslation {

    private static final Map<String, HashSet<String>> translationMap = new HashMap<>();

    public static void clear(){
        translationMap.clear();
    }

    public static void addStandards(){
        add("Rüstung", "Beliebig");
        add("Waffe", "Beliebig");
        add("Magische Waffe", "Beliebig");
        add("Schmuck", "Beliebig");
        add("Nahkampfwaffe", "Waffe");
        add("Fernkampfwaffe", "Waffe");
        add("Einhandwaffe", "Waffe");
        add("Zweihandwaffe", "Waffe");

        add("Klingenwaffe", "Nahkampfwaffe");
        add("Stumpfe Waffe", "Nahkampfwaffe");
        add("Wurfwaffe", "Fernkampfwaffe");
        add("Bogen", "Fernkampfwaffe", "Zweihandwaffe");
        add("Armbrust", "Fernkampfwaffe", "Zweihandwaffe");
        add("Gewehr", "Fernkampfwaffe", "Zweihandwaffe");

        add("Dolch", "Klingenwaffe", "Einhandwaffe");
        add("Einhand-Axt", "Klingenwaffe", "Einhandwaffe");
        add("Einhand-Keule", "Stumpfe Waffe", "Einhandwaffe");
        add("Einhand-Schwert", "Klingenwaffe", "Einhandwaffe");
        add("Faustwaffe", "Stumpfe Waffe", "Einhandwaffe");
        add("Klaue", "Klingenwaffe", "Einhandwaffe");
        add("Hammer", "Stumpfe Waffe", "Einhandwaffe");
        add("Kriegshammer", "Stumpfe Waffe", "Zweihandwaffe");
        add("Schleuder", "Fernkampfwaffe");
        add("Stab", "Magische Waffe");
        add("Stangenwaffe", "Klingenwaffe");
        add("Wurfmesser", "Klingenwaffe");
        add("Zauberbuch", "Magische Waffe");
        add("Großes Zauberbuch", "Magische Waffe");
        add("Zweihand-Axt", "Klingenwaffe", "Zweihandwaffe");
        add("Zweihand-Keule", "Stumpfe Waffe", "Zweihandwaffe");
        add("Zweihand-Schwert", "Klingenwaffe", "Zweihandwaffe");

        add("Kopf", "Rüstung");
        add("Oberkörper", "Rüstung");
        add("Arme", "Rüstung");
        add("Beine", "Rüstung");
        add("Rücken", "Rüstung");
        add("Schild", "Rüstung");
        add("Großschild", "Schild");

        add("Elixier", "Verbrauchsgegenstand");
        add("Medizin", "Verbrauchsgegenstand");
        add("Munition", "Verbrauchsgegenstand");
        add("Nahrung", "Verbrauchsgegenstand");

        add("Bolzen", "Munition");
        add("Gewehrkugel", "Munition");
        add("Pfeil", "Munition");

    }

    public static void add(String key, String... typ){
        if(translationMap.containsKey(key)){
            translationMap.get(key).addAll(Arrays.asList(typ));
        } else {
            translationMap.put(key, new HashSet<>(Arrays.asList(typ)));
        }
    }

    public static Collection<String> getAllTypes(String typ){
        HashSet<String> typSet = new HashSet<>();
        typSet.add(typ);
        int oldSize;

        do{
            oldSize = typSet.size();
            for(String x : new HashSet<>(typSet)){
                if(translationMap.containsKey(x)) {
                    typSet.addAll(translationMap.get(x));
                }
            }
        } while (oldSize != typSet.size());

        return typSet;
    }
}
