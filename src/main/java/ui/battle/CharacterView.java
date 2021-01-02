package ui.battle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import manager.Database;
import manager.LanguageUtility;
import manager.Utility;
import model.Spell;
import model.item.Armor;
import model.item.Jewellery;
import model.item.Weapon;
import model.member.ExtendedBattleMember;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import model.member.generation.Talent;
import org.apache.commons.configuration2.Configuration;
import ui.View;

import java.util.*;
import java.util.stream.Collectors;

import static ui.ViewFactory.labelShortTextField;

public class CharacterView extends View {

    protected ExtendedBattleMember character;

    public CharacterView(ExtendedBattleMember character) {
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

        for (PrimaryAttribute attribute : PrimaryAttribute.values()) {
            HBox attr = labelShortTextField(attribute.toStringProperty(), character.getAttribute(attribute));
            primAttr.getChildren().add(attr);
        }

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        attributes.getChildren().add(separator);

        VBox secAttr = new VBox(4);
        attributes.getChildren().add(secAttr);

        for (SecondaryAttribute attribute : SecondaryAttribute.values()) {
            HBox attr = labelShortTextField(attribute.toStringProperty(),
                    character.getAttribute(attribute), character.getModifier(attribute));
            secAttr.getChildren().add(attr);
        }

        VBox weaponBox = new VBox();
        info.getChildren().add(weaponBox);

        HBox weaponHeadline = new HBox();
        weaponBox.getChildren().add(weaponHeadline);

        weaponHeadline.getChildren().add(createEquipmentHeadline(weaponHeadline, "character.weapon.headline.name", 100, false));
        weaponHeadline.getChildren().add(createEquipmentHeadline(weaponHeadline, "character.weapon.headline.typ", 90));
        weaponHeadline.getChildren().add(createEquipmentHeadline(weaponHeadline, "character.weapon.headline.initiative", 55));
        weaponHeadline.getChildren().add(createEquipmentHeadline(weaponHeadline, "character.weapon.headline.dice", 55));
        weaponHeadline.getChildren().add(createEquipmentHeadline(weaponHeadline, "character.weapon.headline.damage", 55));
        weaponHeadline.getChildren().add(createEquipmentHeadline(weaponHeadline, "character.weapon.headline.hit", 55));
        weaponHeadline.getChildren().add(createEquipmentHeadline(weaponHeadline, "character.weapon.headline.effect", 150));

        for (Weapon weapon : character.getWeapons()) {
            HBox weaponLine = new HBox();
            weaponBox.getChildren().add(weaponLine);

            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getName(), 100, false, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getSubTyp(), 90, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getInitiative(), 55, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getDice(), 55, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, String.valueOf(weapon.getDamage()), 55, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, String.valueOf(weapon.getHit()), 55, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getEffect(), 150, true, false));

            weaponBox.getChildren().add(createEquipmentLabel(weaponLine, weapon.upgradesAsString(), 560, Color.LIGHTGRAY, false, true, false, false));
        }

        HBox armorNotes = new HBox(5);
        info.getChildren().add(armorNotes);

        VBox armorBox = new VBox();
        armorNotes.getChildren().add(armorBox);

        HBox armorHeadline = new HBox();
        armorBox.getChildren().add(armorHeadline);

        armorHeadline.getChildren().add(createEquipmentHeadline(armorHeadline, "character.armor.headline.position", 100, false));
        armorHeadline.getChildren().add(createEquipmentHeadline(armorHeadline, "character.armor.headline.name", 130));
        armorHeadline.getChildren().add(createEquipmentHeadline(armorHeadline, "character.armor.headline.protection", 60));
        armorHeadline.getChildren().add(createEquipmentHeadline(armorHeadline, "character.armor.headline.weight", 60));

        for (Armor armor : character.getArmor()) {
            HBox armorLine = new HBox();
            armorBox.getChildren().add(armorLine);

            armorLine.getChildren().add(createEquipmentLabel(armorLine, armor.getSubTyp(), 100, false, false));
            armorLine.getChildren().add(createEquipmentLabel(armorLine, armor.getName(), 130, true, false));
            armorLine.getChildren().add(createEquipmentLabel(armorLine, String.valueOf(armor.getProtection()), 60, true, false));
            armorLine.getChildren().add(createEquipmentLabel(armorLine, String.valueOf(armor.getWeight()), 60, true, false));

            String upgradeInfo;

            if (armor.getEffect().isBlank()) {
                upgradeInfo = armor.upgradesAsString();
            } else {
                upgradeInfo = armor.getEffect() + " " + armor.upgradesAsString();
            }

            armorBox.getChildren().add(createEquipmentLabel(armorLine, upgradeInfo, 350, Color.LIGHTGRAY, false, true, false, false));
        }

        TextArea notes = new TextArea();
        notes.setPrefWidth(100);
        HBox.setHgrow(notes, Priority.ALWAYS);
        notes.prefHeightProperty().bind(armorBox.heightProperty());
        notes.textProperty().bindBidirectional(character.notesProperty());
        armorNotes.getChildren().add(notes);

        if (character.getJewelleries().size() > 0) {
            VBox jewelleryBox = new VBox();
            info.getChildren().add(jewelleryBox);

            HBox jewelleryHeadline = new HBox();
            jewelleryBox.getChildren().add(jewelleryHeadline);

            jewelleryHeadline.getChildren().add(createEquipmentHeadline(jewelleryHeadline, "character.jewellery.headline.name", 100, false));
            jewelleryHeadline.getChildren().add(createEquipmentHeadline(jewelleryHeadline, "character.jewellery.headline.effect", 460));

            for (Jewellery jewellery : character.getJewelleries()) {
                HBox jewelleryLine = new HBox();
                jewelleryBox.getChildren().add(jewelleryLine);

                jewelleryLine.getChildren().add(createEquipmentLabel(jewelleryLine, jewellery.getName(), 100, false, false));
                jewelleryLine.getChildren().add(createEquipmentLabel(jewelleryLine, jewellery.getEffect(), 460, true, false));

                jewelleryBox.getChildren().add(createEquipmentLabel(jewelleryLine, jewellery.upgradesAsString(), 560, Color.LIGHTGRAY, false, true, false, false));
            }
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
        Queue<Talent> notListedTalents = this.character.getMainTalents().stream()
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
                    for (Talent talent : notListedTalents) {
                        talents.add(new TalentPane(character, talent), pos % columnCount, pos / columnCount);
                        pos++;
                    }
                    notListedTalents.clear();

                } else if (talentName.equals(custom)) {
                    if (!notListedTalents.isEmpty()) {
                        talents.add(new TalentPane(character, notListedTalents.poll()), pos % columnCount, pos / columnCount);
                        pos++;
                    }

                }  else if (talentName.equals(skip)) {
                    if (pos % columnCount != 0) {
                        pos = (pos / columnCount + 1) * columnCount;
                    }
                    talents.add(createSpring(), pos % columnCount, pos / columnCount);
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

            VBox spellBox = new VBox();
            spellTab.setContent(spellBox);

            HBox spellHeadline = new HBox();
            spellBox.getChildren().add(spellHeadline);

            spellHeadline.getChildren().add(createEquipmentHeadline(spellHeadline, "character.spell.headline.name", 100, false));
            spellHeadline.getChildren().add(createEquipmentHeadline(spellHeadline, "character.spell.headline.effect", 250));
            spellHeadline.getChildren().add(createEquipmentHeadline(spellHeadline, "character.spell.headline.typ", 90));
            spellHeadline.getChildren().add(createEquipmentHeadline(spellHeadline, "character.spell.headline.costs", 51));
            spellHeadline.getChildren().add(createEquipmentHeadline(spellHeadline, "character.spell.headline.castTime", 51));

            for (Spell spell : character.getSpells()) {
                HBox spellLine = new HBox();
                spellBox.getChildren().add(spellLine);

                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getName(), 100, false, true));
                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getEffect(), 250, true, true));
                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getTyp(), 90, true, true));
                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getCost(), 51, true, true));
                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getCastTime(), 51, true, true));
            }
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        show();
    }


    private Region createEquipmentHeadline(HBox box, String key, double width) {
        return createEquipmentHeadline(box, key, width, true);
    }

    private Region createEquipmentHeadline(HBox box, String key, double width, boolean left) {
        return createEquipmentLabel(box, key, width, Color.LIGHTGRAY, left, true, true, true);
    }

    private Region createEquipmentLabel(HBox box, String name, double width, boolean left, boolean bot) {
        return createEquipmentLabel(box, name, width, Color.TRANSPARENT, left, bot, true, false);
    }

    private Region createEquipmentLabel(HBox box, String name, double width, Color color, boolean left, boolean bot, boolean bind, boolean headline) {
        Label label = new Label();
        if (headline) {
            label.textProperty().bind(LanguageUtility.getMessageProperty(name));
        } else {
            label.setText(name);
        }
        label.setPrefWidth(width);
        label.setTextAlignment(TextAlignment.LEFT);
        label.setWrapText(true);
        label.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                bot ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
                left ? BorderStrokeStyle.SOLID : BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        label.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setPadding(new Insets(0, 0, 0, 5));

        if (bind) {
            label.minHeightProperty().bind(box.heightProperty());
        }

        return label;
    }

    private Pane createSpring() {
        Pane pane = new Pane();
        pane.setPrefHeight(30);
        return pane;
    }
}
