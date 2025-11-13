package de.pnp.manager.component.universe;

import com.fasterxml.jackson.annotation.JsonCreator;
import de.pnp.manager.component.character.PnPCharacterSheet;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Objects;

/**
 * Defines which {@link PnPCharacterSheet} are used for players and enemies.
 */
public class CharacterSheetSettings extends SettingsBase {

    /**
     * The default settings
     */
    public static final CharacterSheetSettings DEFAULT = new CharacterSheetSettings(null, null);

    @DBRef
    private final PnPCharacterSheet playerSheet;

    @DBRef
    private final PnPCharacterSheet enemySheet;

    @JsonCreator
    @PersistenceCreator
    public CharacterSheetSettings(PnPCharacterSheet playerSheet, PnPCharacterSheet enemySheet) {
        this.playerSheet = playerSheet;
        this.enemySheet = enemySheet;
    }

    public PnPCharacterSheet getPlayerSheet() {
        return playerSheet;
    }

    public PnPCharacterSheet getEnemySheet() {
        return enemySheet;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterSheetSettings that = (CharacterSheetSettings) o;
        return Objects.equals(playerSheet, that.playerSheet) && Objects.equals(enemySheet, that.enemySheet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerSheet, enemySheet);
    }
}
