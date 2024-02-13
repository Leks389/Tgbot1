package org.example;

//import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot extends TelegramLongPollingBot {

    private InlineKeyboardMarkup keyboardM1;
    private InlineKeyboardMarkup keyboardM2;
    @Override
    public String getBotUsername() {
        return "Forchatting_bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()){
            {
                SendMessage message = new SendMessage();
                message.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                message.setText(update.getCallbackQuery().getData());
                game(update.getCallbackQuery().getMessage().getChatId(),update.getCallbackQuery().getData());
            }
        }

        if(update.hasMessage()){
            var msg = update.getMessage();
            var user = msg.getFrom();
            var id = user.getId();
            var txt = msg.getText();
            var next = InlineKeyboardButton.builder()
                    .text("Next").callbackData("next")
                    .build();

            var back = InlineKeyboardButton.builder()
                    .text("Back").callbackData("back")
                    .build();

            var url = InlineKeyboardButton.builder()
                    .text("Tgbot")
                    .url("https://core.telegram.org/bots/api")
                    .build();

            keyboardM1 = InlineKeyboardMarkup.builder()
                    .keyboardRow(List.of(next)).build();
            keyboardM2 = InlineKeyboardMarkup.builder()
                    .keyboardRow(List.of(back))
                    .keyboardRow(List.of(url))
                    .build();
            if(msg.isCommand()) {
                if (txt.equals("/menu"))
                    sendMenu(id, "<b>Menu 1</b>", keyboardM1);
                else if(txt.equals("/rockpaperscissors")){
                    try {
                        execute(sendInlineKeyBoardMessage(update.getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void game(@NotNull Long who, String data){
        Random r = new Random();
        int x = Integer.parseInt(data);
        int y = r.nextInt(3) + 1;
        String s1 = "", s2 = "", s3 = "";
        // 1 - бумага, 2 - ножницы, 3 - камень
        if(x == 1){
            s2 = "\uD83D\uDCC3";
        } else if(x == 2){
            s2 = "✂\uFE0F";
        } else if(x == 3){
            s2 = "\uD83E\uDEA8";
        }
        if(y == 1){
            s3 = "\uD83D\uDCC3";
        } else if(y == 2){
            s3 = "✂\uFE0F";
        } else if(y == 3){
            s3 = "\uD83E\uDEA8";
        }
        if(x == y) {
            s1 = "Ничья!\nВы поставили: "+ s2 +"\n Противник поставил:" + s3;
        }
        if((x == 3 && y == 2) || (x == 2 && y == 1) || (x == 1 && y == 3)) {
            s1 = "Вы победили!\nВы поставили: " + s2 + "\n Противник поставил: " + s3;
        }
        if((x == 1 && y == 2) || (x == 2 && y == 3) || (x == 3 && y == 1)) {
            s1 = "Вы проиграли!\nВы поставили: " + s2 + "\n Противник поставил: " + s3;
        }

        sendText(who, s1);
    }
    public void sendText(@NotNull Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
    public static SendMessage sendInlineKeyBoardMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton11 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("\uD83D\uDCC3");
        inlineKeyboardButton1.setCallbackData("1");
        inlineKeyboardButton11.setText("✂\uFE0F");
        inlineKeyboardButton11.setCallbackData("2");
        inlineKeyboardButton2.setText("\uD83E\uDEA8");
        inlineKeyboardButton2.setCallbackData("3");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton11);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage ms = new SendMessage();
        ms.setChatId(String.valueOf(chatId));
        ms.setText("Выбери что ты поставишь:");
        ms.setReplyMarkup(inlineKeyboardMarkup);
        return ms;
    }
    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb){
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

