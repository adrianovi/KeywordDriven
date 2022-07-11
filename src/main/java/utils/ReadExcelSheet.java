package utils;

import framework.Environment;

import io.restassured.response.Response;
import jdk.javadoc.doclet.Reporter;
import keywordDriven.ActionKeyword;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadExcelSheet {
    private static final Environment environment = Environment.getInstance();
    private static final LoggerManager log = LoggerManager.getInstance();
    private static ActionKeyword actionKeyword = new ActionKeyword();
    private Response response;
    public void readExcelData(int columnNumber) {
        String filePath = environment.getExcelFilePath();
        File file = new File(System.getProperty("user.dir") + filePath);
        ArrayList arrayList = new ArrayList();

        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workBook = new XSSFWorkbook(fis);

            XSSFSheet sheet = workBook.getSheet("Test_Case_Example");
            Iterator row = sheet.rowIterator();
            row.next();

            while (row.hasNext()) {
                Row nextRow = (Row) row.next();
                Cell cell = nextRow.getCell(columnNumber);
                String data = cell.getStringCellValue();
                arrayList.add(data);
                performStep(data);
            }
            System.out.println("Steps to be executed: " + arrayList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void performStep(String step) {
        switch (step) {
            case "setCredentials" : actionKeyword.setCredentials();
            case "getRequest": response = actionKeyword.getRequest();
            default: log.error("Unknown step, " + step);
        }
    }

    public Response getResponse() {
        return response;
    }
}
