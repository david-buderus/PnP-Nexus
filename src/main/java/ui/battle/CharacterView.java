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
import model.Spell;
import model.item.Armor;
import model.item.Jewellery;
import model.item.Weapon;
import model.member.ExtendedBattleMember;
import model.member.generation.PrimaryAttribute;
import model.member.generation.SecondaryAttribute;
import ui.View;

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
            HBox attr = labelShortTextField(attribute.toString(), character.getAttribute(attribute));
            primAttr.getChildren().add(attr);
        }

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        attributes.getChildren().add(separator);

        VBox secAttr = new VBox(4);
        attributes.getChildren().add(secAttr);

        for (SecondaryAttribute attribute : SecondaryAttribute.values()) {
            HBox attr = labelShortTextField(attribute.toString(),
                    character.getAttribute(attribute), character.getModifier(attribute));
            secAttr.getChildren().add(attr);
        }

        VBox weaponBox = new VBox();
        info.getChildren().add(weaponBox);

        HBox weaponHeadline = new HBox();
        weaponBox.getChildren().add(weaponHeadline);

        weaponHeadline.getChildren().add(createEquipmentLabel(weaponHeadline, "Waffe", 100, Color.LIGHTGRAY, false, true));
        weaponHeadline.getChildren().add(createEquipmentLabel(weaponHeadline, "Typ", 90));
        weaponHeadline.getChildren().add(createEquipmentLabel(weaponHeadline, "Init", 55));
        weaponHeadline.getChildren().add(createEquipmentLabel(weaponHeadline, "Würfel", 55));
        weaponHeadline.getChildren().add(createEquipmentLabel(weaponHeadline, "Schaden", 55));
        weaponHeadline.getChildren().add(createEquipmentLabel(weaponHeadline, "Treffer", 55));
        weaponHeadline.getChildren().add(createEquipmentLabel(weaponHeadline, "Spezial", 150));

        for (Weapon weapon : character.getWeapons()) {
            HBox weaponLine = new HBox();
            weaponBox.getChildren().add(weaponLine);

            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getName(), 100, Color.TRANSPARENT, false, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getSubTyp(), 90, Color.TRANSPARENT, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getInitiative(), 55, Color.TRANSPARENT, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getDice(), 55, Color.TRANSPARENT, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, String.valueOf(weapon.getDamage()), 55, Color.TRANSPARENT, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, String.valueOf(weapon.getHit()), 55, Color.TRANSPARENT, true, false));
            weaponLine.getChildren().add(createEquipmentLabel(weaponLine, weapon.getEffect(), 150, Color.TRANSPARENT, true, false));

            weaponBox.getChildren().add(createEquipmentLabel(weaponLine, weapon.upgradesAsString(), 560, Color.LIGHTGRAY, false, true, false));
        }

        HBox armorNotes = new HBox(5);
        info.getChildren().add(armorNotes);

        VBox armorBox = new VBox();
        armorNotes.getChildren().add(armorBox);

        HBox armorHeadline = new HBox();
        armorBox.getChildren().add(armorHeadline);

        armorHeadline.getChildren().add(createEquipmentLabel(armorHeadline, "Rüstung", 100, Color.LIGHTGRAY, false, true));
        armorHeadline.getChildren().add(createEquipmentLabel(armorHeadline, "Name", 130));
        armorHeadline.getChildren().add(createEquipmentLabel(armorHeadline, "Schutz", 60));
        armorHeadline.getChildren().add(createEquipmentLabel(armorHeadline, "Belastung", 60));

        for (Armor armor : character.getArmor()) {
            HBox armorLine = new HBox();
            armorBox.getChildren().add(armorLine);

            armorLine.getChildren().add(createEquipmentLabel(armorLine, armor.getSubTyp(), 100, Color.TRANSPARENT, false, false));
            armorLine.getChildren().add(createEquipmentLabel(armorLine, armor.getName(), 130, Color.TRANSPARENT, true, false));
            armorLine.getChildren().add(createEquipmentLabel(armorLine, String.valueOf(armor.getProtection()), 60, Color.TRANSPARENT, true, false));
            armorLine.getChildren().add(createEquipmentLabel(armorLine, String.valueOf(armor.getWeight()), 60, Color.TRANSPARENT, true, false));

            String upgradeInfo;

            if (armor.getEffect().equals("")) {
                upgradeInfo = armor.upgradesAsString();
            } else {
                upgradeInfo = armor.getEffect() + " " + armor.upgradesAsString();
            }

            armorBox.getChildren().add(createEquipmentLabel(armorLine, upgradeInfo, 350, Color.LIGHTGRAY, false, true, false));
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

            jewelleryHeadline.getChildren().add(createEquipmentLabel(jewelleryHeadline, "Name", 100, Color.LIGHTGRAY, false, true));
            jewelleryHeadline.getChildren().add(createEquipmentLabel(jewelleryHeadline, "Effekt", 460));

            for (Jewellery jewellery : character.getJewelleries()) {
                HBox jewelleryLine = new HBox();
                jewelleryBox.getChildren().add(jewelleryLine);

                jewelleryLine.getChildren().add(createEquipmentLabel(jewelleryLine, jewellery.getName(), 100, Color.TRANSPARENT, false, false));
                jewelleryLine.getChildren().add(createEquipmentLabel(jewelleryLine, jewellery.getEffect(), 460, Color.TRANSPARENT, true, false));

                jewelleryBox.getChildren().add(createEquipmentLabel(jewelleryLine, jewellery.upgradesAsString(), 560, Color.LIGHTGRAY, false, true, false));
            }
        }

        TabPane talentTabs = new TabPane();
        talentTabs.setPadding(new Insets(20));
        root.getChildren().add(talentTabs);

        Tab fightTab = new Tab("Kampf");
        fightTab.setClosable(false);
        talentTabs.getTabs().add(fightTab);

        GridPane talents = new GridPane();
        talents.setVgap(1);
        talents.setHgap(2);
        talents.setGridLinesVisible(true);
        fightTab.setContent(talents);

        talents.add(new TalentPane(character, Database.getTalent("Faustkampf")), 0, 0);
        talents.add(new TalentPane(character, Database.getTalent("Stangenwaffen")), 1, 0);
        talents.add(new TalentPane(character, Database.getTalent("Klingenwaffen Einhändig")), 0, 1);
        talents.add(new TalentPane(character, Database.getTalent("Stumpfe Waffen Einhändig")), 1, 1);
        talents.add(new TalentPane(character, Database.getTalent("Klingenwaffen Zweihändig")), 0, 2);
        talents.add(new TalentPane(character, Database.getTalent("Stumpfe Waffen Zweihändig")), 1, 2);
        talents.add(new TalentPane(character, Database.getTalent("Kleine Wurfwaffen")), 0, 3);
        talents.add(new TalentPane(character, Database.getTalent("Große Wurfwaffen")), 1, 3);
        talents.add(new TalentPane(character, Database.getTalent("Bogen / Armbrust")), 0, 4);
        talents.add(new TalentPane(character, Database.getTalent("Gewehr")), 1, 4);

        talents.add(createSpring(), 0, 6);

        talents.add(new TalentPane(character, Database.getTalent("Ausweichen")), 0, 7);
        talents.add(new TalentPane(character, Database.getTalent("Gefahreninstinkt (Normal)")), 1, 7);
        talents.add(new TalentPane(character, Database.getTalent("Entwaffnen")), 0, 8);
        talents.add(new TalentPane(character, Database.getTalent("Parieren")), 1, 8);
        talents.add(new TalentPane(character, Database.getTalent("Blocken")), 0, 9);

        talents.add(new TalentPane(character, Database.getTalent("Laufen")), 0, 10);
        talents.add(new TalentPane(character, Database.getTalent("Abrollen")), 1, 10);
        talents.add(new TalentPane(character, Database.getTalent("Springen")), 0, 11);
        talents.add(new TalentPane(character, Database.getTalent("Schwimmen")), 1, 11);
        talents.add(new TalentPane(character, Database.getTalent("Fliegen")), 0, 12);
        talents.add(new TalentPane(character, Database.getTalent("Reiten")), 1, 12);
        talents.add(new TalentPane(character, Database.getTalent("Werfen")), 0, 13);
        talents.add(new TalentPane(character, Database.getTalent("Zielen")), 1, 13);
        talents.add(new TalentPane(character, Database.getTalent("Körperliche Arbeit")), 0, 14);
        talents.add(new TalentPane(character, Database.getTalent("Entfesseln")), 1, 14);


        Tab magicTab = new Tab("Magie");
        magicTab.setClosable(false);
        talentTabs.getTabs().add(magicTab);

        GridPane magicTalents = new GridPane();
        magicTalents.setVgap(1);
        magicTalents.setHgap(2);
        magicTalents.setGridLinesVisible(true);
        magicTab.setContent(magicTalents);

        magicTalents.add(new TalentPane(character, Database.getTalent("Magisches Wissen")), 0, 0);

        magicTalents.add(createSpring(), 0, 1);

        magicTalents.add(new TalentPane(character, Database.getTalent("Arkanmagie")), 0, 2);
        magicTalents.add(new TalentPane(character, Database.getTalent("Illusionsmagie")), 1, 2);
        magicTalents.add(new TalentPane(character, Database.getTalent("Lichtmagie")), 0, 3);
        magicTalents.add(new TalentPane(character, Database.getTalent("Finstermagie")), 1, 3);
        magicTalents.add(new TalentPane(character, Database.getTalent("Feuermagie")), 0, 4);
        magicTalents.add(new TalentPane(character, Database.getTalent("Wassermagie")), 1, 4);
        magicTalents.add(new TalentPane(character, Database.getTalent("Luftmagie")), 0, 5);
        magicTalents.add(new TalentPane(character, Database.getTalent("Erdmagie")), 1, 5);
        magicTalents.add(new TalentPane(character, Database.getTalent("Sturmmagie")), 0, 6);
        magicTalents.add(new TalentPane(character, Database.getTalent("Frostmagie")), 1, 6);
        magicTalents.add(new TalentPane(character, Database.getTalent("Naturmagie")), 0, 7);
        magicTalents.add(new TalentPane(character, Database.getTalent("Totenmagie")), 1, 7);

        if (character.getSpells().size() > 0) {
            Tab spellTab = new Tab("Zauber");
            spellTab.setClosable(false);
            talentTabs.getTabs().add(spellTab);

            VBox spellBox = new VBox();
            spellTab.setContent(spellBox);

            HBox spellHeadline = new HBox();
            spellBox.getChildren().add(spellHeadline);

            spellHeadline.getChildren().add(createEquipmentLabel(spellHeadline, "Name", 100, Color.LIGHTGRAY, false, true));
            spellHeadline.getChildren().add(createEquipmentLabel(spellHeadline, "Effekt", 250));
            spellHeadline.getChildren().add(createEquipmentLabel(spellHeadline, "Typ", 90));
            spellHeadline.getChildren().add(createEquipmentLabel(spellHeadline, "Kosten", 51));
            spellHeadline.getChildren().add(createEquipmentLabel(spellHeadline, "Zeit", 51));

            for (Spell spell : character.getSpells()) {
                HBox spellLine = new HBox();
                spellBox.getChildren().add(spellLine);

                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getName(), 100, Color.TRANSPARENT, false, true));
                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getEffect(), 250, Color.TRANSPARENT, true, true));
                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getTyp(), 90, Color.TRANSPARENT, true, true));
                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getCost(), 51, Color.TRANSPARENT, true, true));
                spellLine.getChildren().add(createEquipmentLabel(spellLine, spell.getCastTime(), 51, Color.TRANSPARENT, true, true));
            }
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        show();
    }

    private Region createEquipmentLabel(HBox box, String name, double width) {
        return createEquipmentLabel(box, name, width, Color.LIGHTGRAY, true, true);
    }

    private Region createEquipmentLabel(HBox box, String name, double width, Color color, boolean left, boolean bot) {
        return createEquipmentLabel(box, name, width, color, left, bot, true);
    }

    private Region createEquipmentLabel(HBox box, String name, double width, Color color, boolean left, boolean bot, boolean bind) {
        Label label = new Label(name);
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
