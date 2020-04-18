// ^(38)(067|097|068|096|098|039|050|095|066|099|063|073|093|091|094|044)

// 1047436354:AAFim116PUp8SKQ5J2evJJ14rU20boYk0zY
// https://api.telegram.org/bot1047436354:AAFim116PUp8SKQ5J2evJJ14rU20boYk0zY
// /sendMessage?chat_id=<chat_id>&text=<текст_отправляемого_сообщения>

function SendTelegram(){
    var ss = SpreadsheetApp.getActiveSpreadsheet();
    var activeCell = ss.getActiveSheet().getActiveCell();
    var sheet = ss.getSheets()[1]; // 2 лист!
    var renge = sheet.getRange("A:A").getValues();
    var rowNum = activeCell.getRow();
    var lastRowInCol = renge.filter(String).length;
    var namesManagers = sheet.getRange(2,1,lastRowInCol - 1, 1).getValues();
    var nameManager = activeCell.getValue();
    var namesManagersArr = namesManagers.map(function(row){return row[0]})
    var rowManager = namesManagersArr.indexOf(nameManager) + 2;

    var chatID = sheet.getRange(rowManager, 2).getValue(); // сюда можно напрямую задать id чата если он один.

    var orderName = ss.getActiveSheet().getRange(rowNum, 2).getValue();
    var orderPhone = ss.getActiveSheet().getRange(rowNum, 3).getValue();
    var orderInfo = ss.getActiveSheet().getRange(rowNum, 4).getValue();
    var orderAdress = ss.getActiveSheet().getRange(rowNum, 5).getValue();

    var tokken =  "1047436354:AAFim116PUp8SKQ5J2evJJ14rU20boYk0zY";

    var text = encodeURIComponent("\n" + "NAME :"+ orderName + "\n" + "☎️ :" + " +" + orderPhone + "\n" + "ℹ️ : " + orderInfo + "\n" + "Key Words :" + orderAdress) ;
    var url = "https://api.telegram.org/bot" + tokken + "/sendMessage?chat_id=" + chatID + "&text="+ text;

    messBox(url)


}

function messBox(url) {
    var ss = SpreadsheetApp.getActiveSpreadsheet();
    var ui = SpreadsheetApp.getUi();

    var columManager = ss.getSheets()[1].getActiveCell().getColumn()
    var rowManager = ss.getSheets()[1].getActiveCell().getRow()

    if (columManager == 6 && rowManager > 1)
    {
        var response = ui.alert('Send Message?', ui.ButtonSet.YES_NO);
        if (response == ui.Button.YES) {
            var openUrl = UrlFetchApp.fetch(url).getContentText();


        }
    }
}


