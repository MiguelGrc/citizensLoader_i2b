package main.asw.parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by nicolas on 10/02/17.
 */
public class ApachePoiDataContainer implements CellLikeDataContainer {

    private int numberOfRows = -1, numberOfColumns = -1,
            currentRow = -1, currentSheet = 0;

    HSSFWorkbook wb;


    public ApachePoiDataContainer(String filename) throws IOException {
        this.wb = this.readFile(filename);
    }

    @Override
    public int getNumberOfRows() {
        //Needs to be calculated
        if (numberOfRows == -1) {
            calculateNumberOfRows();
        }
        return this.numberOfRows;
    }

    @Override
    public int getNumberOfColumns() {
        //NeedsToBeCalculated
        if (numberOfColumns == -1) {
            calculateNumberOfColumns();
        }
        return numberOfColumns;
    }

    private void calculateNumberOfColumns() {
        this.numberOfColumns = wb.getSheetAt(currentSheet)
                .getRow(currentRow)
                .getPhysicalNumberOfCells();
    }

    private void calculateNumberOfRows() {
        int numberOfRows = 0;
        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            numberOfColumns += wb.getSheetAt(k).getPhysicalNumberOfRows();
        }
        this.numberOfRows = numberOfRows;
    }

    @Override
    public boolean nextRow() throws IOException {
        boolean res = false;
        if (currentRow + 1 < wb.getSheetAt(currentSheet).getPhysicalNumberOfRows()) {
            currentRow++;
            res = true;
            calculateNumberOfColumns();
        } else if (currentSheet + 1 < wb.getNumberOfSheets()) {
            currentSheet++;
            currentRow = 0;
            res = true;
            calculateNumberOfColumns();
        } else {
            wb.close();
            res = false;
        }
        return res;
    }

    @Override
    public String getCell(int index) {
        if (index < getNumberOfColumns()) {
            return wb.getSheetAt(currentSheet)
                    .getRow(currentRow)
                    .getCell(index)
                    .getStringCellValue();
        } else {
            throw new IllegalArgumentException("The index is out of range");
        }
    }

    @Override
    public int getCurrentRow() {
        return this.currentRow;
    }

    @Override
    public String toString() {
        return "Sheet: " + currentSheet + "Row: " + currentRow;
    }

    private HSSFWorkbook readFile(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        try {
            return new HSSFWorkbook(fis);
        } finally {
            fis.close();
        }
    }
}
