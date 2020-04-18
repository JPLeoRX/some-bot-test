function doPost(e) {
    // получаем сигнал от бота
    var update = JSON.parse(e.postData.contents);

    // проверяем тип полученного, нам нужен только тип "сообщение"
    if (update.hasOwnProperty('message')) {
        var msg = update.message;
        var chatId = msg.chat.id;

        // проверяем, является ли сообщение командой к боту
        if (msg.hasOwnProperty('entities') && msg.entities[0].type == 'bot_command') {

            // проверяем на название команды - /lastpost
            if (msg.text == '/lastpost') {

                // если все проверки пройдены - запускаем код, который ниже,
                // открываем оглавление нашего канала
                var sheet =  SpreadsheetApp.openById('1u0aRlwb0rQHbjE9avbgCqIJSQ3EvPfTxT-h54jTQh8I').getSheets()[0]

                // достает последний пост
                var lastpost = sheet.getRange(sheet.getLastRow(), 1, 1,  3).getValues()[0]
                var message = ' <strong>'+lastpost[1] + '</strong> \n' + lastpost[2]

                //формируем с ним сообщение
                var payload = {
                    'method': 'sendMessage',
                    'chat_id': String(chatId),
                    'text': message,
                    'parse_mode': 'HTML'
                }
                var data = {
                    "method": "post",
                    "payload": payload
                }

                // и отправляем его боту (замените API на свой)
                var API_TOKEN = '684873979:AAFWxHSV8QWoZUpiA9Z18EAyDHgLvrpjdkA'
                UrlFetchApp.fetch('https://api.telegram.org/bot' + API_TOKEN + '/', data);
            }
        }
    }
}