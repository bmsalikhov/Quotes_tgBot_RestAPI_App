package ru.syn.quotes.controllers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.syn.quotes.models.Chat;
import ru.syn.quotes.models.Quote;
import ru.syn.quotes.repositories.ChatRepository;
import ru.syn.quotes.services.QuoteService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

@Service
public class BotController {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    QuoteService quoteService;

    private final TelegramBot bot;

    public BotController() {
        bot = new TelegramBot(getBotToken());

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                handleUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
        String text = update.message().text();
        Long chatId = update.message().chat().id();
        Optional<Chat> rawChat = chatRepository.findByChatIdEquals(chatId);
        Chat chat;
        if (rawChat.isPresent()) {
            chat = rawChat.get();
        } else {
            Chat _chat = new Chat();
            _chat.setChatId(chatId);
            _chat.setLastId(0);
            chat = chatRepository.save(_chat);
        }
        switch (text) {
            case "/start":
            case "/next":
                sendNextQuote(chat);
                break;
            case "/prev":
                sendPrevQuote(chat);
                break;
            case "/rand":
                sendRandomQuote(chat);
                break;
        }
    }

    private void sendNextQuote(Chat chat) {
        Quote quote = null;
        int newId = chat.getLastId();
        while (quote == null) {
            newId++;
            quote = quoteService.getById(newId);
        }
        chat.setLastId(quote.getQuoteId());
        chatRepository.save(chat);
        sendText(chat.getChatId(), quote.getText());
    }

    private void sendPrevQuote(Chat chat) {
        Quote quote = null;
        int newId = chat.getLastId();
        while (quote == null) {
            newId--;
            if (newId < 2) newId = 2;
            quote = quoteService.getById(newId);
        }
        chat.setLastId(quote.getQuoteId());
        chatRepository.save(chat);
        sendText(chat.getChatId(), quote.getText());
    }

    private void sendRandomQuote(Chat chat) {
        Quote quote = quoteService.getRandom();
        sendText(chat.getChatId(), quote.getText());
    }

    private void sendText(long chatId, String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    private static String getBotToken() {
        String token;
        try {
            Properties properties = new Properties();
            String fileName = "config.properties";
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = loader.getResourceAsStream(fileName)) {
                properties.load(inputStream);
            } catch (IOException e) {
                throw new IOException(String.format("Error loading properties file '%s'", fileName));
            }
            token = properties.getProperty("token");
            if (token == null) {
                throw new RuntimeException("Token value is null");
            }
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException("bot.Bot initialization error: " + e.getMessage());
        }
        return token;
    }

}
