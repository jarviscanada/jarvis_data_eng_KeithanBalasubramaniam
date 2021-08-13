package ca.jrvs.apps.trading.service;


import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class QuoteService {

    public static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao){
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    /**
     *
     * @param ticker id
     * @return IexQuote Object
     * @throws IllegalArgumentException if ticker is invalid
     */
    public IexQuote findIexQuoteByTicker(String ticker){
        return marketDataDao.findById(ticker).orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid"));
    }

    /**
     * Update quote table against IEX source
     * - get all quotes from the quote table
     * - foreach ticker get iexQuote
     * - convert iexQuote to quote entity
     * - persist quote to db
     *
     * @throws //ResourceNotFoundException if ticket is not found from IEX
     * @throws org.springframework.dao.DataAccessException if unable to retrieve the data
     * @throws IllegalArgumentException for invalid input
     */
    public List<Quote> updateMarketData() {
        List<Quote> quotes = (List<Quote>) quoteDao.findAll();
        quotes.forEach(q -> {
            IexQuote iexQuote = marketDataDao.findById(q.getID()).get();
            Quote quote = buildQuoteFromIexQuote(iexQuote);
            quoteDao.save(quote);
        });
        return quotes;
    }

    /**
     * Helper method. Map an IEX quote to a Quote entity
     * Note: 'iexQuote.getLatestPrice() == null' if the stock market is closed
     * Make sure to set a default value for number field(s)
     */
    protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
        Quote quote = new Quote();
        quote.setTicker(iexQuote.getSymbol());
        quote.setAskSize((iexQuote.getIexAskSize() != null) ? iexQuote.getIexAskSize() : -1);
        quote.setAskPrice((iexQuote.getIexAskPrice() != null) ? iexQuote.getIexAskPrice() : -1);
        quote.setLastPrice((iexQuote.getLatestPrice() != null) ? iexQuote.getLatestPrice() : -1);
        quote.setBidPrice((iexQuote.getIexBidPrice() != null) ? iexQuote.getIexBidPrice() : -1);
        quote.setBidSize((iexQuote.getIexBidSize()!= null) ? iexQuote.getIexBidSize() : -1);
        return quote;
    }

    /**
     * Validate (against IEX) and save given tickers to a quote table
     *
     * - Get iexQuote(s)
     * - convert iexQuote to Quote entity
     * - persist the quote to db
     *
     * @throws IllegalArgumentException if ticker is not found from IEX
     */
    public List<Quote> saveQuotes(List<String> tickers) {
        List<Quote> quotes = new ArrayList<>();
        tickers.forEach(ticker -> {
            try {
                quotes.add(saveQuote(ticker));
            } catch (IllegalArgumentException ex) {
                logger.error("Error: Ticker not found from IEX");
            }
        });
        return quotes;
    }

    public Quote saveQuote(String ticker) {
        IexQuote iexQuote = findIexQuoteByTicker(ticker);
        Quote quote = buildQuoteFromIexQuote(iexQuote);
        return saveQuote(quote);
    }

    /**
     * Update a given quote to quote table without validation
     * @param quote entity
     */
    public Quote saveQuote(Quote quote) {
        return quoteDao.save(quote);
    }

    public List<Quote> findAllQuotes() {
        return (List<Quote>) quoteDao.findAll();
    }
}
