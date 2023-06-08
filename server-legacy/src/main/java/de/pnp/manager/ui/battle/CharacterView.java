package de.pnp.manager.ui.battle;

import de.pnp.manager.main.Database;
import de.pnp.manager.main.LanguageUtility;
import de.pnp.manager.main.Utility;
import de.pnp.manager.model.item.*;
import de.pnp.manager.model.character.PnPCharacter;
import de.pnp.manager.model.character.data.PrimaryAttribute;
import de.pnp.manager.model.character.data.SecondaryAttribute;
import de.pnp.manager.model.other.ISpell;
import de.pnp.manager.model.other.ITalent;
import de.pnp.manager.ui.View;
import de.pnp.manager.ui.part.CharacterTable;
import de.pnp.manager.ui.part.EquipmentTable;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import org.apache.commons.configuration2.Configuration;

import java.util.*;
import java.util.stream.Collectors;

import static de.pnp.manager.ui.ViewFactory.labelShortTextField;

public class CharacterView extends View {

    protected PnPCharacter character;

    public CharacterView(PnPCharacter character) {
        super(LanguageUtility.getMessage("character.title"), new ReadOnlyStringWrapper(" ").concat(character.nameProperty()));
        this.character = character;

        HBox root = new HBox();

        VBox info = new VBox(25);
        info.setPadding(new Insets(20));
        root.getChildren().add(info);

        HBox attributes = new HBox(10);
        attributes.setAlignment(Pos.TOP_CENTER);
        info.getChildren().add(attributes);

        VBox primAttr = new VBox(4);
        attributes.getChildren().add(primAttr);

        for (PrimaryAttribute attribute : PrimaryAttribute.getValuesWithoutDummy()) {
            HBox attr = labelShortTextField(attribute.toStringProperty(), character.getPrimaryAttributes().get(attribute));
            primAttr.getChildren().add(attr);
        }

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        attributes.getChildren().add(separator);

        VBox secAttr = new VBox(4);
        attributes.getChildren().add(secAttr);

        for (SecondaryAttribute attribute : SecondaryAttribute.values()) {
            HBox attr = labelShortTextField(attribute.toStringProperty(),
                    character.getSecondaryAttributeModifier(attribute).resultProperty(), character.getSecondaryAttributeModifier(attribute).modifierProperty());
            secAttr.getChildren().add(attr);
        }

        // TODO add initivateModifier from weapon
        EquipmentTable<IWeapon> weaponTable = new EquipmentTable<>(character.getEquippedWeapons());
        weaponTable.addStringColumn("character.weapon.headline.name", 100, IWeapon::getName);
        weaponTable.addStringColumn("character.weapon.headline.type", 90, IWeapon::getSubtype);
        weaponTable.addStringColumn("character.weapon.headline.initiative", 55, IWeapon::getInitiative);
        weaponTable.addStringColumn("character.weapon.headline.dice", 55, IWeapon::getDice);
        weaponTable.addColumn("character.weapon.headline.damage", 55, weapon -> ((Weapon) weapon).damageWithWearBinding());
        weaponTable.addStringColumn("character.weapon.headline.hit", 55, IWeapon::getHit);
        weaponTable.addStringColumn("character.weapon.headline.effect", 150, IWeapon::getEffect);
        info.getChildren().add(weaponTable);

        HBox armorNotes = new HBox(5);
        info.getChildren().add(armorNotes);

        EquipmentTable<IArmor> armorTable = new EquipmentTable<>(character.getEquippedArmorAsList());
        armorTable.addStringColumn("character.armor.headline.position", 100, IArmor::getSubtype);
        armorTable.addStringColumn("character.armor.headline.name", 130, IArmor::getName);
        armorTable.addColumn("character.armor.headline.protection", 60, armor -> ((Armor) armor).protectionWithWearBinding());
        armorTable.addStringColumn("character.armor.headline.weight", 60, IArmor::getWeight);
        armorTable.setUpgradeFactory(armor -> {
            if (armor.getEffect().isBlank()) {
                return new ReadOnlyStringWrapper(armor.upgradesAsString());
            } else {
                return new ReadOnlyStringWrapper(armor.getEffect() + " " + armor.upgradesAsString());
            }
        });
        armorNotes.getChildren().add(armorTable);

        TextArea notes = new TextArea();
        notes.setPrefWidth(100);
        HBox.setHgrow(notes, Priority.ALWAYS);
        notes.prefHeightProperty().bind(armorTable.heightProperty());
        notes.textProperty().bindBidirectional(character.notesProperty());
        armorNotes.getChildren().add(notes);

        if (character.getEquippedJewellery().size() > 0) {
            EquipmentTable<IJewellery> jewelleryTable = new EquipmentTable<>(character.getEquippedJewellery());
            jewelleryTable.addStringColumn("character.jewellery.headline.name", 100, IJewellery::getName);
            jewelleryTable.addStringColumn("character.jewellery.headline.effect", 460, IJewellery::getEffect);
            info.getChildren().add(jewelleryTable);
        }

        TabPane talentTabs = new TabPane();
        talentTabs.setPadding(new Insets(20));
        root.getChildren().add(talentTabs);

        // Load layout from the config file
        Configuration config = Utility.getConfig();
        int pageCount = config.getInt("character.talent.pages");
        int columnCount = config.getInt("character.talent.page.columns");
        String custom = config.getString("character.talent.custom.marker");
        String customs = config.getString("character.talent.customs.marker");
        String skip = config.getString("character.talent.skip.marker");
        List<List<String>> talentPages = new ArrayList<>();

        for (int i = 0; i < pageCount; i++) {
            List<String> page = new ArrayList<>();

            for (String talentKey : config.getStringArray("character.talent.page." + i)) {
                if (talentKey.equals(custom) || talentKey.equals(customs) || talentKey.equals(skip) || talentKey.isBlank()) {
                    page.add(talentKey.trim());
                } else {
                    String talent = LanguageUtility.hasMessage("talent." + talentKey) ? LanguageUtility.getMessage("talent." + talentKey) : talentKey.trim();
                    page.add(talent);
                }
            }

            talentPages.add(page);
        }

        // All talents that the character uses but are not shown in the default layout
        Queue<ITalent> notListedTalents = this.character.getTalents().entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .filter(talent -> talentPages.stream().flatMap(List::stream)
                        .noneMatch(name -> name.equalsIgnoreCase(talent.getName())))
                .collect(Collectors.toCollection(LinkedList::new));

        for (int pageNr = 0; pageNr < talentPages.size(); pageNr++) {
            List<String> page = talentPages.get(pageNr);
            Tab tab = new Tab();
            tab.textProperty().bind(LanguageUtility.getMessageProperty("character.tab" + pageNr + ".name"));
            tab.setClosable(false);
            talentTabs.getTabs().add(tab);

            GridPane talents = new GridPane();
            talents.setVgap(1);
            talents.setHgap(2);
            talents.setGridLinesVisible(true);
            tab.setContent(talents);

            int pos = 0;
            for (String talentName : page) {

                if (talentName.equals(customs) && !notListedTalents.isEmpty()) {
                    for (ITalent talent : notListedTalents) {
                        talents.add(new TalentPane(character, talent), pos % columnCount, pos / columnCount);
                        pos++;
                    }
                    notListedTalents.clear();

                } else if (talentName.equals(custom)) {
                    if (!notListedTalents.isEmpty()) {
                        talents.add(new TalentPane(character, notListedTalents.poll()), pos % columnCount, pos / columnCount);
                        pos++;
                    }

                } else if (talentName.equals(skip)) {
                    if (pos % columnCount != 0) {
                        pos = (pos / columnCount + 1) * columnCount;
                    }
                    talents.add(createEmptyCell(), pos % columnCount, pos / columnCount);
                    pos += columnCount;
                } else {
                    if (!talentName.isBlank()) {
                        talents.add(new TalentPane(character, Database.getTalent(talentName)), pos % columnCount, pos / columnCount);
                    }
                    pos++;
                }
            }
        }

        if (character.getSpells().size() > 0) {
            Tab spellTab = new Tab();
            spellTab.textProperty().bind(LanguageUtility.getMessageProperty("character.tabSpells.name"));
            spellTab.setClosable(false);
            talentTabs.getTabs().add(spellTab);

            CharacterTable<ISpell> spellTable = new CharacterTable<>(character.getSpells());
            spellTable.addStringColumn("character.spell.headline.name", 100, ISpell::getName);
            spellTable.addStringColumn("character.spell.headline.effect", 250, ISpell::getEffect);
            spellTable.addStringColumn("character.spell.headline.type", 90, ISpell::getType);
            spellTable.addStringColumn("character.spell.headline.costs", 51, ISpell::getCost);
            spellTable.addStringColumn("character.spell.headline.castTime", 51, ISpell::getCastTime);
            spellTab.setContent(spellTable);
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        show();
    }

    private Pane createEmptyCell() {
        Pane pane = new Pane();
        pane.setPrefHeight(30);
        return pane;
    }
}
