package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest{

    @Autowired
    private QuoteDao quoteDao;

    private Quote savedQuote;

    @Before
    public void insertOne(){
        quoteDao.deleteAll();
        savedQuote = new Quote();
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize(10);
        savedQuote.setBidPrice(10.2d);
        savedQuote.setBidSize(10);
        savedQuote.setID("AAPL");
        savedQuote.setLastPrice(10.1d);
        quoteDao.save(savedQuote);
    }

    @After
    public void deleteOne(){
        quoteDao.deleteById(savedQuote.getID());
    }

    @Test
    public void testSave() {
        Quote testQuote = new Quote();
        testQuote.setAskPrice(11d);
        testQuote.setAskSize(11);
        testQuote.setBidPrice(11d);
        testQuote.setBidSize(11);
        testQuote.setID("GOOGL");
        testQuote.setLastPrice(11.1d);
        quoteDao.save(testQuote);
        Quote retrievedQuote = quoteDao.findById("GOOGL").get();
        assertEquals(testQuote, retrievedQuote);
    }

    @Test
    public void testSaveAll() {
        Quote testQuoteA = new Quote();
        testQuoteA.setAskPrice(11d);
        testQuoteA.setAskSize(11);
        testQuoteA.setBidPrice(11d);
        testQuoteA.setBidSize(11);
        testQuoteA.setID("GOOGL");
        testQuoteA.setLastPrice(11.1d);

        Quote testQuoteB = new Quote();
        testQuoteB.setAskPrice(11d);
        testQuoteB.setAskSize(11);
        testQuoteB.setBidPrice(11d);
        testQuoteB.setBidSize(11);
        testQuoteB.setID("FB");
        testQuoteB.setLastPrice(11.1d);
        quoteDao.saveAll(Arrays.asList(testQuoteA, testQuoteB));

        Quote retrievedQuoteA = quoteDao.findById("GOOGL").get();
        Quote retrievedQuoteB = quoteDao.findById("FB").get();
        assertEquals(testQuoteA, retrievedQuoteA);
        assertEquals(testQuoteB, retrievedQuoteB);
    }

    @Test
    public void findByIdTest() {
        assertEquals(savedQuote, quoteDao.findById(savedQuote.getID()).get());
    }

    @Test
    public void existsByIdTest() {
        assertEquals(true, quoteDao.existsById(savedQuote.getID()));
    }

    @Test
    public void findAllTest() {
        List<Quote> retrievedQuotes = (List<Quote>) quoteDao.findAll();
        assertEquals(1, retrievedQuotes.size());
        assertEquals(savedQuote, retrievedQuotes.get(0));
    }

    @Test
    public void countTest() {
        assertEquals(1, quoteDao.count());
    }

    @Test
    public void deleteByIdTest() {
        quoteDao.deleteById(savedQuote.getID());
        assertEquals(0, quoteDao.count());
    }

    @Test
    public void deleteAllTest() {
        quoteDao.deleteAll();
        assertEquals(0, quoteDao.count());
    }

}