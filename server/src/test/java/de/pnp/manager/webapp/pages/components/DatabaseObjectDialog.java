package de.pnp.manager.webapp.pages.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.character.Talent;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.component.upgrade.Upgrade;
import de.pnp.manager.webapp.utils.WebTestUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.assertj.core.api.Assertions;
import org.springframework.util.ReflectionUtils;

/**
 * Describes the database object dialog
 */
public class DatabaseObjectDialog {

    /**
     * The locator of the component
     */
    protected final Locator locator;

    private DatabaseObjectDialog(Locator locator) {
        this.locator = locator;
    }

    /**
     * Returns the currently open dialog.
     */
    public static DatabaseObjectDialog getDialog(Page page) {
        return new DatabaseObjectDialog(page.getByTestId("database-object-dialog"));
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void set(String attribute, String value) {
        locator.getByTestId(attribute).getByRole(AriaRole.TEXTBOX).fill(value);
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void set(String attribute, Number value) {
        locator.getByTestId(attribute).getByRole(AriaRole.TEXTBOX).fill(String.valueOf(value));
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void set(String attribute, boolean value) {
        Locator checkbox = locator.getByTestId(attribute);
        if (value) {
            checkbox.check();
        } else {
            checkbox.uncheck();
        }
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void setAutoComplete(String attribute, String value) {
        WebTestUtils.selectAutoComplete(locator.getByTestId(attribute), value, true);
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void setSelect(String attribute, String value) {
        WebTestUtils.select(locator.getByTestId(attribute), value);
    }

    /**
     * Fills all input fields, so they match the given object.
     */
    public void fillOut(DatabaseObject object) {
        fillOut(object, "");
    }

    private void fillOut(Object object, String idPrefix) {
        if (object == null) {
            return;
        }

        getSubTypeAnnotation(object.getClass()).ifPresent(
            subTypeAnnotation -> {
                String typePrefix = idPrefix + "@type";
                if (locator.getByTestId(typePrefix).isVisible()) {
                    setSelect(typePrefix, subTypeAnnotation);
                }
            });

        ReflectionUtils.doWithFields(object.getClass(), field -> {
            field.setAccessible(true);
            Object property = field.get(object);

            String id = idPrefix + field.getName();

            if (!locator.getByTestId(id).isVisible()) {
                return;
            }

            if (property instanceof String s) {
                set(id, s);
            } else if (property instanceof Number n) {
                set(id, n);
            } else if (property instanceof Boolean b) {
                set(id, b);
            } else if (property instanceof Collection<?> collection) {
                fillOutCollection(collection, id);
            } else if (property instanceof Enum<?> e) {
                setSelect(id, e.name());
            } else if (property instanceof IUniquelyNamedDataObject named) {
                setAutoComplete(id, named.getName());
            } else if (property instanceof Talent t) {
                setAutoComplete(id, t.getName());
            } else if (property instanceof Upgrade u) {
                setAutoComplete(id, u.getName());
            } else if (property instanceof BinaryExpressionTree tree) {
                set(id, tree.asHumanReadableString());
            } else {
                fillOut(property, id + ".");
            }
        });
    }

    private void fillOutCollection(Collection<?> collection, String id) {
        if (locator.getByTestId(id + "-add").isVisible()) {
            // This means we have a complex list

            // Remove all currently open entries
            locator.getByTestId(Pattern.compile(id + "-sub-\\d+")).all().stream()
                // We need to remove the last button first, so the data-testids won't change
                .sorted(Comparator.comparing(l -> l.getAttribute("data-testid"), Comparator.reverseOrder()))
                .forEach(Locator::click);

            List<?> list = collection.stream().toList();
            for (int i = 0; i < list.size(); i++) {
                locator.getByTestId(id + "-add").click();
                fillOut(list.get(i), id + "[" + i + "].");
            }
        } else {
            // This means it is an autocomplete with multiselect

            WebTestUtils.clearAutoComplete(locator.getByTestId(id));
            for (Object o : collection) {
                if (o instanceof IUniquelyNamedDataObject named) {
                    setAutoComplete(id, named.getName());
                } else if (o instanceof Talent t) {
                    setAutoComplete(id, t.getName());
                } else if (o instanceof Upgrade u) {
                    setAutoComplete(id, u.getName());
                }
            }
        }
    }

    private Optional<String> getSubTypeAnnotation(Class<?> clazz) {
        List<JsonSubTypes> subTypes = new ArrayList<>();
        Class<?> currentClass = clazz;
        do {
            subTypes.addAll(List.of(currentClass.getDeclaredAnnotationsByType(JsonSubTypes.class)));
            currentClass = currentClass.getSuperclass();
        } while (currentClass != null);

        for (JsonSubTypes subType : subTypes) {
            for (Type type : subType.value()) {
                if (clazz.equals(type.value())) {
                    return Optional.of(type.name());
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Asserts that the given attribute has an error hint.
     */
    public void assertError(String attribute) {
        Assertions.assertThat(
                locator.getByTestId(attribute)
                    .locator("p").textContent())
            .isNotBlank();
    }

    /**
     * Tries to add the object to the universe.
     */
    public void add() {
        locator.getByTestId("dialog-action").click();
    }

    /**
     * Tries to edit the object to the universe.
     */
    public void edit() {
        locator.getByTestId("dialog-action").click();
    }
}
