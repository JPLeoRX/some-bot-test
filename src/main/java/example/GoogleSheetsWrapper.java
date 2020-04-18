package example;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class GoogleSheetsWrapper {
    private String sheetId;
    private String accountJsonLocation;
    private Credential credential;
    private Sheets sheets;

    //------------------------------------------------------------------------------------------------------------------
    public GoogleSheetsWrapper(String sheetId, String accountJsonLocation) {
        this.sheetId = sheetId;
        this.accountJsonLocation = accountJsonLocation;

        try {
            this.credential = loadCredentialFromJsonInResources(accountJsonLocation);
            this.sheets = getSheets(this.credential);
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }
    //------------------------------------------------------------------------------------------------------------------



    // Static helper methods
    //------------------------------------------------------------------------------------------------------------------
    public static Credential loadCredentialFromJsonInResources(String jsonLocation) throws IOException {
        InputStream is = GoogleSheetsWrapper.class.getResourceAsStream(jsonLocation);
        return GoogleCredential.fromStream(is).createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    }

    public static Sheets getSheets(Credential credential) throws GeneralSecurityException, IOException {
        Sheets.Builder builder = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential);
        builder.setApplicationName("Google Sheets Client");
        return builder.build();
    }

    public static ValueRange getRange(Sheets sheets, String sheetId, String range) throws IOException {
        return sheets.spreadsheets().values().get(sheetId, range).execute();
    }
    //------------------------------------------------------------------------------------------------------------------



    // Wrapped methods
    //------------------------------------------------------------------------------------------------------------------
    public String getCell(String pageName, String cellName) throws Exception {
        ValueRange range = getRange(this.sheets, this.sheetId, pageName + "!" + cellName);
        return (String) (range.getValues().get(0).get(0));
    }

    public List<UsersCategory> getUserCategories() throws Exception {
        List<UsersCategory> list = new ArrayList<>();
        ValueRange range = getRange(this.sheets, this.sheetId, "users!A3:D20");
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
    //------------------------------------------------------------------------------------------------------------------
}