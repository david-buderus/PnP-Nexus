package manager;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

public class CharacterSheetParser {

    // TODO get from config
    final static int PARAMETER_COL = 0;
    final static int VALUE_COL = 1;

    public static Map<String, String> parseCharacterSheet(Workbook wb) {
        // TODO: localize
        Sheet sheet = wb.getSheet("manager_parse");
        Map<String, String> paramMap = new HashMap<>();

        int rowIndex = 0;
        String cellParameter = sheet.getRow(rowIndex).getCell(PARAMETER_COL).getStringCellValue();
        String cellValue = sheet.getRow(rowIndex).getCell(VALUE_COL).getStringCellValue();
        while (!cellParameter.isEmpty()) {
            paramMap.put(cellParameter.trim(), cellValue.trim());
            rowIndex++;
            cellParameter = sheet.getRow(rowIndex).getCell(VALUE_COL).getStringCellValue();
            cellValue = sheet.getRow(rowIndex).getCell(VALUE_COL).getStringCellValue();
        }

        return paramMap;
    }

}
