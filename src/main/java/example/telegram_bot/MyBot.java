package example.telegram_bot;

import example.google_sheets.GoogleSheersWrapper;
import example.models.UsersCategory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MyBot extends TelegramLongPollingBot {
    @Override
    public String getBotToken() {
        return "1047436354:AAFim116PUp8SKQ5J2evJJ14rU20boYk0zY";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // If this was a command
            if (update.getMessage().getText().startsWith("/")) {
                System.out.println("Command received: " + update.getMessage().getText());
                handleCommand(update);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "bot1047436354";
    }

    private void handleCommand(Update update) {
        try {
            List<UsersCategory> usersCategoryList = GoogleSheersWrapper.getUserCategories();
            List<String> usersCategoryCommands = usersCategoryList.stream().map(s -> s.getCommand().toLowerCase()).collect(Collectors.toList());

            // Image
            if (update.getMessage().getText().equalsIgnoreCase("/image")) {
                //String imgUrl = "https://lh3.googleusercontent.com/WCLVFWBDcar3VkydUCJu3qobWJJnxw_X8H_wGvU-I9FGpA2CSNghlOA5LoJl-DjSvYr1SEpKL4CnHcxtPREjYC_8bBCpqLtFzIX70C1H3YGy39JVWhOxU1N9GwVdo0E6f-mJ4sFPE2ijBuh6o1ftU62bKBVqEh6VzokWnw10rfmIOKtJrUm42E5mgAmm4cq7P008G736V-ftT42Fg9x44QHfw5R9hJpy5zRPuKNHWjmoXbWNvcUxlhOV_zJjoaSs35NAw5Jk2fBa7qqWuBxbrg9DSQ6EyZtAiSK9iQKpamHn4ob5b4TwlwfJbCe5G6WOvPx8Nzo7JxDEyaLARleDU1pyPX8-LKK2-_R0wl7WSl_7mdTIp3CHnndGGAQfV5slkERzli_3r8bqvbzaxKPxwivkqEmBCZcFQBhGDLzlqaDNpRz8DOUHV15Itm4-0FiWe23eoRm2VFszeqOoOa0aaihuLGwns9X_mYAm6aCOiVFmi9XYI480EEQ0aIzloV51614naKgw2KtEI3tz35QyoCnrWRmoPIrWOaP29z45lwe2VnXwr_WqMCQP8CIPgFd7x5iwP53FO9wM66RiHbD94oiPjs5fqdLm7Iyr0zi4k0C3SuNhxN54G_vryR7TNXG4KJ7pyTPy8mbv9yxjNhBSYaBFFV_dHd2FGzi6Car5JChjsGE27sjE8xsYydJ9_ok=s759-no";
                String imgUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Infiniti_Q50_S_HYBRID_%28V37%29_%E2%80%93_Frontansicht%2C_14._Juni_2014%2C_D%C3%BCsseldorf.jpg/2880px-Infiniti_Q50_S_HYBRID_%28V37%29_%E2%80%93_Frontansicht%2C_14._Juni_2014%2C_D%C3%BCsseldorf.jpg";
                InputStream is = new URL(imgUrl).openStream();
                SendPhoto message = new SendPhoto().setChatId(update.getMessage().getChatId()).setPhoto("SomeText", is);

                execute(message);
            }

            // If this is a help command
            else if (update.getMessage().getText().equalsIgnoreCase("/help")) {
                String text = "Possible commands: ";
                for (UsersCategory usersCategory : usersCategoryList)
                    text = text + "\n" + usersCategory.getCommand();
                SendMessage message = new SendMessage(update.getMessage().getChatId(), text);
                execute(message);
            }


            // If this is a total command
            else if (update.getMessage().getText().equalsIgnoreCase("/total")) {
                int sum = usersCategoryList.stream().mapToInt(c -> c.getCount()).sum();
                String text = "Total: " + sum;
                SendMessage message = new SendMessage(update.getMessage().getChatId(), text);
                execute(message);
            }


            // If this might be a category command
            else if (usersCategoryCommands.contains(update.getMessage().getText().toLowerCase())) {
                for (UsersCategory usersCategory : usersCategoryList) {
                    // If this is a command for this category
                    if (update.getMessage().getText().equalsIgnoreCase(usersCategory.getCommand())) {
                        String text = usersCategory.getName() + " -> " + usersCategory.getPercentage() + "% (" + usersCategory.getCount() + ")";
                        SendMessage message = new SendMessage(update.getMessage().getChatId(), text);
                        execute(message);
                    }
                }
            }

            // Treat as column command
            else {
                // /{sheetName}:{cellName}
                String sheetName = update.getMessage().getText().split(":")[0].replace("/", "");
                String cellName = update.getMessage().getText().split(":")[1];
                String cell = GoogleSheersWrapper.getCell(sheetName, cellName);
                SendMessage message = new SendMessage(update.getMessage().getChatId(), cell);
                execute(message);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
