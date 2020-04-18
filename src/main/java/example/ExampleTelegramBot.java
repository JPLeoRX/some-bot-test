package example;

import example.telegram_bot.MyBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

public class ExampleTelegramBot {
    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        //  Register our bot
        try {
            botsApi.registerBot(new MyBot());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
