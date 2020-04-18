package example;

import example.google_sheets.GoogleSheersWrapper;
import example.models.UsersCategory;

import java.util.List;

public class ExampleGoogleSheets {
    public static void main(String[] args) throws Exception {
        GoogleSheersWrapper.getCell("imageSheet", "A1");
        GoogleSheersWrapper.getCell("sheetA", "A1");
        GoogleSheersWrapper.getCell("sheetA", "A3");

//        List<UsersCategory> usersCategories = GoogleSheersWrapper.getUserCategories();
//        for (UsersCategory usersCategory : usersCategories) {
//            System.out.println(usersCategory);
//        }
    }
}