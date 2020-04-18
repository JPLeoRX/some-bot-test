package example.google_sheets;

import com.google.api.services.sheets.v4.model.ValueRange;
import example.models.UsersCategory;

import java.util.ArrayList;
import java.util.List;

public class GoogleSheetsWrapper {
    public static final String SPREADSHEET_ID = "1yR2EjdHD1mkhYlIwQgHEAJSll2c_JDWtIUFeAYjRWWM";
    public static final String SERVICE_ACCOUNT_EMAIL = "sheets-service-account@sheets-test-274612.iam.gserviceaccount.com";

    public static String getCell(String sheetName, String cellName) throws Exception {
        GoogleSheetsService service = new GoogleSheetsService(GoogleSheetsUtil.getSheetsService());
        ValueRange range = service.getRange(SPREADSHEET_ID, sheetName + "!" + cellName);
        System.out.println(range.getValues());
        return (String) (range.getValues().get(0).get(0));
    }

    public static List<UsersCategory> getUserCategories() throws Exception {
        List<UsersCategory> list = new ArrayList<>();

        GoogleSheetsService service = new GoogleSheetsService(GoogleSheetsUtil.getSheetsService());
        ValueRange range = service.getRange(SPREADSHEET_ID, "users!A3:D20");
        for (List<Object> row : range.getValues()) {
            if (row.size() == 4) {
                String row1 = (String) row.get(0);
                String row2 = (String) row.get(1);
                String row3 = (String) row.get(2);
                String row4 = (String) row.get(3);

                if (row1.isEmpty() || row2.isEmpty() || row3.isEmpty() || row4.isEmpty()) {
                    continue;
                }

                else {
                    UsersCategory category = new UsersCategory(
                            row1,
                            Integer.parseInt(row2),
                            Double.parseDouble(row3.replace("%", "")),
                            row4
                    );

                    list.add(category);
                }
            }
        }

        return list;
    }
}