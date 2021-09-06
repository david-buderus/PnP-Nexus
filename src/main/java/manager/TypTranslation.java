package manager;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public abstract class TypTranslation {

    private static final ObservableMap<String, ObservableSet<String>> translationMap = FXCollections.observableMap(new HashMap<>());
    public static final ListProperty<String> allTypes = new SimpleListProperty<>(FXCollections.observableArrayList());

    static {
        translationMap.addListener((MapChangeListener<String, ObservableSet<String>>) mapChange -> {

            if (mapChange.wasAdded()) {

                for (String type : mapChange.getValueAdded()) {
                    if (!allTypes.contains(type)) {
                        allTypes.add(type);
                    }
                }

                mapChange.getValueAdded().addListener((SetChangeListener<String>) setChange -> {
                    if (setChange.wasAdded()) {
                        if (!allTypes.contains(setChange.getElementAdded())) {
                            allTypes.add(setChange.getElementAdded());
                        }
                    }
                    if (setChange.wasRemoved()) {
                        allTypes.remove(setChange.getElementRemoved());
                    }
                });
            }
            if (mapChange.wasRemoved()) {
                allTypes.removeAll(mapChange.getValueRemoved());
            }

        });

    }

    public static void clear() {
        translationMap.clear();
    }

    public static void add(String key, String... typ) {
        if (translationMap.containsKey(key)) {
            translationMap.get(key).addAll(Arrays.asList(typ));
        } else {
            translationMap.put(key, FXCollections.observableSet(typ));
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
