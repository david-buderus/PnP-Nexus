package de.pnp.manager.main;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.util.ArrayList;
import java.util.List;

public class WorkbookUtility {
    public static String getValue(Row row, int i) {
        try {
            return row.getCell(i).getStringCellValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static int getIntegerInCell(Sheet sheet, String pos) {
        try {
            return (int) getCell(sheet, pos).getNumericCellValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getStringInCell(Sheet sheet, String pos) {
        try {
            return getCell(sheet, pos).getStringCellValue();
        } catch (Exception e) {
            return "";
        }
    }

    public static Cell getCell(Sheet sheet, String pos) {
        List<Character> input = new ArrayList<>();
        for (char c : pos.toCharArray()) {
            input.add(c);
        }

        String letter = Utility.consumeString(input);
        int x = Utility.consumeNumber(input);

        return sheet.getRow(x - 1).getCell(CellReference.convertColStringToIndex(letter));
    }
}
