package example.google_sheets;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;

public class GoogleSheetsService {
    private Sheets sheetsService;

    public GoogleSheetsService(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    public ValueRange getRange(String sheetId, String range) throws IOException {
        return sheetsService.spreadsheets().values().get(sheetId, range).execute();
    }
}