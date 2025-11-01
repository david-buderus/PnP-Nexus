package de.pnp.manager.webapp.pages.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import de.pnp.manager.Tag;
import de.pnp.manager.component.DatabaseObject;
import de.pnp.manager.component.Dice;
import de.pnp.manager.component.IUniquelyNamedDataObject;
import de.pnp.manager.component.math.BinaryExpressionTree;
import de.pnp.manager.webapp.utils.WebTestUtils;
import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.springframework.util.ReflectionUtils;

import java.util.*;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

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
        return new DatabaseObjectDialog(page.getByRole(AriaRole.DIALOG));
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void set(String attribute, String value) {
        getByDataPath(attribute).fill(value);
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void set(String attribute, Number value) {
        getByDataPath(attribute).fill(String.valueOf(value));
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void set(String attribute, boolean value) {
        Locator checkbox = getByDataPath(attribute);
        if (value) {
            checkbox.check();
        } else {
            checkbox.uncheck();
        }
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void setSelect(String attribute, DatabaseObject object) {
        Select.from(getByDataPath(attribute)).select(object);
    }

    /**
     * Sets the value of the corresponding attribute.
     */
    public void setSelect(String attribute, IUniquelyNamedDataObject object) {
        Select.from(getByDataPath(attribute)).select(object);
    }


    /**
     * Sets the value of the corresponding attribute.
     */
    public void setSelect(String attribute, String value) {
        Select.from(getByDataPath(attribute)).select(value);
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
                    if (getByDataPath(typePrefix).isVisible()) {
                        setSelect(typePrefix, subTypeAnnotation);
                    }
                });

        ReflectionUtils.doWithFields(object.getClass(), field -> {
            if (field.getType().equals(ObjectId.class)) {
                // ObjectIds don't need to be filled out
                return;
            }

            field.setAccessible(true);
            Object property = field.get(object);

            String id = idPrefix + field.getName();

            switch (property) {
                case String s -> set(id, s);
                case Number n -> set(id, n);
                case Boolean b -> set(id, b);
                case Collection<?> collection -> fillOutCollection(collection, id);
                case Enum<?> e -> Select.from(getByDataPath(id)).select(e.name());
                case DatabaseObject databaseObject -> setSelect(id, databaseObject);
                case BinaryExpressionTree tree -> set(id, tree.toHumanReadableString());
                case Dice dice -> set(id, dice.toHumandReadableString());
                case null, default -> fillOut(property, id + ".");
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

            WebTestUtils.clearMultiSelect(getByDataPath(id));
            for (Object o : collection) {
                if (o instanceof DatabaseObject databaseObject) {
                    setSelect(id, databaseObject);
                } else if (o instanceof Tag(String name)) {
                    set(id, name);
                } else {
                    fail("Unsupported object: " + o);
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
                        getByDataPath(attribute)
                                .locator("..")
                                .locator("..")
                                .locator("p").textContent())
                .isNotBlank();
    }

    /**
     * Tries to add the object to the universe.
     */
    public void add() {
        locator.locator("[type=submit]").click();
    }

    /**
     * Tries to edit the object to the universe.
     */
    public void edit() {
        locator.locator("[type=submit]").click();
    }

    private Locator getByDataPath(String path) {
        Locator input = locator.locator("[data-path=\"" + path + "\"]");
        assertThat(input).isVisible();
        assertThat(input).isEnabled();
        return input;
    }
}
