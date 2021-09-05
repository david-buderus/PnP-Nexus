package manager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class CharacterSheetParser {

    // TODO get from config
    final static int PARAMETER_COL = 0;
    final static int VALUE_COL = 1;

    public static CharacterSheetParameterMap parseCharacterSheet(Workbook wb) {
        // TODO: localize
        Sheet sheet = wb.getSheet("manager_parse");
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
                    switch(valueCell.getCachedFormulaResultType()) {
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
