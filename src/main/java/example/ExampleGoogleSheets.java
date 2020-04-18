package example;

import example.google_sheets.GoogleSheetsWrapper;

public class ExampleGoogleSheets {
    public static void main(String[] args) throws Exception {
        GoogleSheetsWrapper.getCell("imageSheet", "A1");
        GoogleSheetsWrapper.getCell("sheetA", "A1");
        GoogleSheetsWrapper.getCell("sheetA", "A3");

//        List<UsersCategory> usersCategories = GoogleSheersWrapper.getUserCategories();
//        for (UsersCategory usersCategory : usersCategories) {
//            System.out.println(usersCategory);
//        }
    }
}