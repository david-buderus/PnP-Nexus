package model.member;

import manager.CharacterSheetParameterMap;
import manager.CharacterSheetParser;
import org.apache.poi.ss.usermodel.Workbook;

public class PlayerBattleMember extends ExtendedBattleMember {

    public PlayerBattleMember(Workbook wb) {
        this(CharacterSheetParser.parseCharacterSheet(wb));
    }

    public PlayerBattleMember(CharacterSheetParameterMap parameterMap) {
        super(parameterMap);

        // load occupation
        // load age

        // load inventory
        // load history


    }

    // todo set loot dirferent
}
