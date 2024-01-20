package ru.syn.quotes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.syn.quotes.models.Quote;
import ru.syn.quotes.repositories.QuoteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class QuoteService {

    @Autowired
    BashParser parser;

    @Autowired
    QuoteRepository repository;

    public List<Quote> getPage(int page) {
        List<Quote> res = new ArrayList<>();
        Map<Integer, String> map = parser.getPage(page);
        for (Map.Entry<Integer, String> e : map.entrySet()) {
            Quote rawQuote = new Quote();
            rawQuote.setQuoteId(e.getKey());
            rawQuote.setText(e.getValue());
            Optional<Quote> existed = repository.findByQuoteIdEquals(rawQuote.getQuoteId());
            if (existed.isEmpty()) {
                res.add(repository.save(rawQuote));
            } else {
                res.add(existed.get());
            }
        }
        return res;
    }

    public Quote getById(int id) {
        Optional<Quote> existingQuote = repository.findByQuoteIdEquals(id);
        if (existingQuote.isPresent()) return existingQuote.get();
        Map.Entry<Integer, String> quoteEntry = parser.getById(id);
        if (quoteEntry == null) return null;
        Quote newQuote = new Quote();
        newQuote.setQuoteId(quoteEntry.getKey());
        newQuote.setText(quoteEntry.getValue());
        return repository.save(newQuote);
    }

    public Quote getRandom() {
        Map.Entry<Integer, String> quoteEntry = parser.getRandom();
        if (quoteEntry == null) return null;

        Optional<Quote> existingQuote = repository.findByQuoteIdEquals(quoteEntry.getKey());
        if (existingQuote.isPresent()) return existingQuote.get();

        Quote newQuote = new Quote();
        newQuote.setQuoteId(quoteEntry.getKey());
        newQuote.setText(quoteEntry.getValue());
        return repository.save(newQuote);
    }

}
