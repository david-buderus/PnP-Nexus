package de.pnp.manager.webapp.pages.species;

import com.microsoft.playwright.Page;
import de.pnp.manager.component.character.traits.ICharacterTrait;
import de.pnp.manager.webapp.pages.PageBase;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Base for the species/nation detail page
 */
public abstract class DetailPageBase extends PageBase {

    private final static Pattern HTML_PATTERN = Pattern.compile("<.*>");

    public DetailPageBase(Page page) {
        super(page);
    }

    /**
     * Asserts the name on the page.
     */
    public void assertName(String expectedName) {
        assertThat(page.getByTestId("name")).containsText(expectedName);
    }

    /**
     * Asserts the description on the page.
     */
    public void assertDescription(String expectedDescription) {
        assertThat(page.getByTestId("description"))
                .containsText(HTML_PATTERN.matcher(expectedDescription).replaceAll(""));
    }

    /**
     * Asserts the advantage trait on the page.
     */
    public void assertAdvantageTrait(ICharacterTrait trait) {
        assertThat(page.getByTestId("advantageTraits")).containsText(trait.getDescription());
    }

    /**
     * Asserts the advantage trait on the page.
     */
    public void assertDisadvantageTrait(ICharacterTrait trait) {
        assertThat(page.getByTestId("disadvantageTraits")).containsText(trait.getDescription());
    }
}
