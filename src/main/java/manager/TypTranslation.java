package manager;

import java.util.*;

public abstract class TypTranslation {

    private static final Map<String, HashSet<String>> translationMap = new HashMap<>();

    public static void clear() {
        translationMap.clear();
    }

    public static void add(String key, String... typ) {
        if (translationMap.containsKey(key)) {
            translationMap.get(key).addAll(Arrays.asList(typ));
        } else {
            translationMap.put(key, new HashSet<>(Arrays.asList(typ)));
        }
    }

    public static Collection<String> getAllTypes(String typ) {
        HashSet<String> typSet = new HashSet<>();
        typSet.add(typ);
        int oldSize;

        do {
            oldSize = typSet.size();
            for (String x : new HashSet<>(typSet)) {
                if (translationMap.containsKey(x)) {
                    typSet.addAll(translationMap.get(x));
                }
            }
        } while (oldSize != typSet.size());

        return typSet;
    }
}
