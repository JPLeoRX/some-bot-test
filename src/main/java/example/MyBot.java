package example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyBot extends TelegramLongPollingBot {
    // Load google sheets wrapper
    private GoogleSheetsWrapper googleSheetsWrapper = new GoogleSheetsWrapper("1yR2EjdHD1mkhYlIwQgHEAJSll2c_JDWtIUFeAYjRWWM", "/sheets-service-account.json");

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
            // Load user categories from sheets, and map them to bot commands
            List<UsersCategory> usersCategoryList = googleSheetsWrapper.getUserCategories();
            Set<String> usersCategoryCommands = usersCategoryList.stream().map(s -> s.getCommand().toLowerCase()).collect(Collectors.toSet());

            // Special case 1 - File command
            if (update.getMessage().getText().equalsIgnoreCase("/file")) {
                String imgUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Infiniti_Q50_S_HYBRID_%28V37%29_%E2%80%93_Frontansicht%2C_14._Juni_2014%2C_D%C3%BCsseldorf.jpg/2880px-Infiniti_Q50_S_HYBRID_%28V37%29_%E2%80%93_Frontansicht%2C_14._Juni_2014%2C_D%C3%BCsseldorf.jpg";
                InputStream is = new URL(imgUrl).openStream();
                SendDocument message = new SendDocument().setChatId(update.getMessage().getChatId()).setDocument("sample.jpg", is).setCaption("File caption");
                execute(message);
            }

            // Special case 2 - Image command
            else if (update.getMessage().getText().equalsIgnoreCase("/image")) {
                String imgUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Infiniti_Q50_S_HYBRID_%28V37%29_%E2%80%93_Frontansicht%2C_14._Juni_2014%2C_D%C3%BCsseldorf.jpg/2880px-Infiniti_Q50_S_HYBRID_%28V37%29_%E2%80%93_Frontansicht%2C_14._Juni_2014%2C_D%C3%BCsseldorf.jpg";
                InputStream is = new URL(imgUrl).openStream();
                SendPhoto message = new SendPhoto().setChatId(update.getMessage().getChatId()).setPhoto("SomeText", is);
                execute(message);
            }

            // Special case 3 - Help command
            else if (update.getMessage().getText().equalsIgnoreCase("/help")) {
                String text = "Possible commands: ";
                for (UsersCategory usersCategory : usersCategoryList)
                    text = text + "\n" + usersCategory.getCommand();
                text = text + "\n/File";
                text = text + "\n/Image";
                SendMessage message = new SendMessage(update.getMessage().getChatId(), text);
                execute(message);
            }

            // Special case 3 - Total command
            else if (update.getMessage().getText().equalsIgnoreCase("/total")) {
                // Sum all users
                int sum = usersCategoryList.stream().mapToInt(c -> c.getCount()).sum();
                String text = "Total: " + sum;
                SendMessage message = new SendMessage(update.getMessage().getChatId(), text);
                execute(message);
            }

            // If this might be a category command
            else if (usersCategoryCommands.contains(update.getMessage().getText().toLowerCase())) {
                // Go through each user category
                for (UsersCategory usersCategory : usersCategoryList) {
                    // If this is a command for this category
                    if (update.getMessage().getText().equalsIgnoreCase(usersCategory.getCommand())) {
                        String text = usersCategory.getName() + " -> " + usersCategory.getPercentage() + "% (" + usersCategory.getCount() + ")";
                        SendMessage message = new SendMessage(update.getMessage().getChatId(), text);
                        execute(message);
                    }
                }
            }

            // If don't match any known commands - treat as column command /{pageName}:{cellName}
            else {
                String pageName = update.getMessage().getText().split(":")[0].replace("/", "");
                String cellName = update.getMessage().getText().split(":")[1];
                String cell = googleSheetsWrapper.getCell(pageName, cellName);
                SendMessage message = new SendMessage(update.getMessage().getChatId(), cell);
                execute(message);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
