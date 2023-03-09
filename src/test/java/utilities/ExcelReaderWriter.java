package utilities;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelReaderWriter {
    private static Workbook wbook;
    private static Sheet sheet;
    private static Row row;
    private static Cell cell;

    /**
     * This method will open xlfile and specified sheet
     *
     * @param filePath
     * @param sheetName
     */
    public static void openExcel(String filePath, String sheetName) {

        try {
            FileInputStream fis = new FileInputStream(filePath);

            //Comment In which works for you
            wbook = new XSSFWorkbook(fis);
            //  wbook = new XSSFWorkbook(filePath);

            sheet = wbook.getSheet(sheetName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will return number of rows
     *
     * @return
     */
    public static int rowCount() {
        return sheet.getPhysicalNumberOfRows();
    }

    /**
     * This method will return number of cols
     *
     * @return
     */
    public static int colsCount() {
        return sheet.getRow(0).getLastCellNum();
    }

    /**
     * This method will return data from specified row and column
     *
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public static String getCellData(int rowIndex, int cellIndex) {
        return sheet.getRow(rowIndex).getCell(cellIndex).toString();
    }


    public Object excelGetSingleData(String filePath, String sheetName) throws IOException {

        if(sheetName==null) {
            sheetName ="Sheet1";
        }

        openExcel(filePath, sheetName);

        row = sheet.getRow(0);
        cell = row.getCell(0);

        Object data = cell.getStringCellValue();
        wbook.close();
        return data;
    }
    /**
     * This method will read any Excel file and return data in 2D array
     *
     * @param filePath
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static Object[][] excelIntoArray(String filePath, String sheetName) throws IOException {

        openExcel(filePath, sheetName);

        Object[][] data = new Object[rowCount()][colsCount()];

        for (int i = 0; i < rowCount(); i++) {
            for (int y = 0; y < colsCount(); y++) {
                data[i][y] = getCellData(i, y);
            }
        }
        wbook.close();
        return data;
    }

    public static Map excelIntoMap(String filePath, String sheetName) throws IOException {

        openExcel(filePath, sheetName);

        Object[][] data = new Object[rowCount()][colsCount()];

        for (int i = 0; i < rowCount(); i++) {
            for (int y = 0; y < colsCount(); y++) {
                data[i][y] = getCellData(i, y);
            }
        }
        Map<Object, Object> map = new HashMap<Object, Object>();

        for (Object[] mapping : data)
        {
            map.put(mapping[0], mapping[1]);
        }

        wbook.close();
        return map;
    }


    public static List excelIntoList(String filePath, String sheetName) throws IOException {

        if(sheetName==null) {
            sheetName ="Sheet1";
        }

        List<Object> list = new ArrayList<Object>();

        openExcel(filePath, sheetName);

        for (int i = 0; i < rowCount()-1; i++) {

            Object celData = getCellData(i,0);

            list.add(celData);
        }

        wbook.close();
        return list;
    }



    public static void writeExcel(String filePath, String sheetName, String str) throws IOException {

        wbook = new XSSFWorkbook();
        sheet =wbook.createSheet("Sheet1");
        row=sheet.createRow(0);
        cell =row.createCell(0);



        cell.setCellValue(str);



        try {
            FileOutputStream fos = new FileOutputStream(filePath);

            wbook.write(fos);
            wbook.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void writeExcel(String filePath, String sheetName, Map map) throws IOException {

        Set keys = map.keySet();
        Object[] ar1 = keys.toArray();

        wbook = new XSSFWorkbook();
        sheet =wbook.createSheet("Sheet1");

        int rowSize = map.size();
        Set entry =map.entrySet();

        for(int i =0; i<rowSize;i++) {

            row=sheet.createRow(i);
            cell =row.createCell(0);

            cell.setCellValue(ar1[i].toString());

            cell =row.createCell(1);

            cell.setCellValue(map.get(ar1[i]).toString());
        }

        try {
            FileOutputStream fos = new FileOutputStream(filePath);

            wbook.write(fos);
            wbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void writeExcel(String filePath, String sheetName, List list) throws IOException {

        wbook = new XSSFWorkbook();
        sheet =wbook.createSheet("Sheet1");

        int rowSize = list.size();

        for(int i =0; i<rowSize;i++) {

            row=sheet.createRow(i);
            cell =row.createCell(0);

            cell.setCellValue(list.get(i).toString());
        }

        try {
            FileOutputStream fos = new FileOutputStream(filePath);

            wbook.write(fos);
            wbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
