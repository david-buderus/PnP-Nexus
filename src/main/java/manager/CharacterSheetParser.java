package manager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class CharacterSheetParser {

    final static int PARAMETER_COL = Utility.getConfig().getInt("character.sheet.keyCol");
    final static int VALUE_COL = Utility.getConfig().getInt("character.sheet.valueCol");

    public static CharacterSheetParameterMap parseCharacterSheet(Workbook wb) {
        String sheetName = LanguageUtility.getMessage("character.sheet.manager_parse");
        Sheet sheet = wb.getSheet(sheetName);
        CharacterSheetParameterMap paramMap = new CharacterSheetParameterMap();

        for (Row r : sheet) {
            Cell parameterNameCell = r.getCell(PARAMETER_COL);
            Cell valueCell = r.getCell(VALUE_COL);

            switch (valueCell.getCellType()) {
                case STRING:
                    paramMap.put(parameterNameCell.getStringCellValue().trim(), valueCell.getStringCellValue());
                    break;
                case NUMERIC:
                    paramMap.put(parameterNameCell.getStringCellValue().trim(), valueCell.getNumericCellValue());
                    break;
                case FORMULA:
                    switch (valueCell.getCachedFormulaResultType()) {
                        case STRING:
                            paramMap.put(parameterNameCell.getStringCellValue().trim(), valueCell.getStringCellValue());
                            break;
                        case NUMERIC:
                            paramMap.put(parameterNameCell.getStringCellValue().trim(), valueCell.getNumericCellValue());
                            break;
                    }
                    break;
            }
        }

        return paramMap;
    }

}
