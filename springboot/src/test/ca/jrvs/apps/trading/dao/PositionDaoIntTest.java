package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class PositionDaoIntTest {

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private SecurityOrderDao securityOrderDao;

    @Autowired
    private QuoteDao quoteDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    private SecurityOrder savedSecurityOrder;
    private Quote savedQuote;
    private Account savedAccount;
    private Trader savedTrader;

    @Before
    public void addOne() {
        savedQuote = new Quote();
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize(10);
        savedQuote.setBidPrice(10d);
        savedQuote.setBidSize(10);
        savedQuote.setID("AAPL");
        savedQuote.setLastPrice(10.1d);
        quoteDao.save(savedQuote);

        savedTrader = new Trader();
        savedTrader.setFirstName("Jane");
        savedTrader.setLastName("Doe");
        savedTrader.setDob(new Date(2020, 7, 23));
        savedTrader.setCountry("Canada");
        savedTrader.setEmail("jane.doe@gmail.ca");
        savedTrader = traderDao.save(savedTrader);

        savedAccount = new Account();
        savedAccount.setTraderId(savedTrader.getID());
        savedAccount.setAmount(100.0);
        savedAccount = accountDao.save(savedAccount);

        savedSecurityOrder = new SecurityOrder();
        savedSecurityOrder.setAccountId(savedAccount.getID());
        savedSecurityOrder.setStatus("FILLED");
        savedSecurityOrder.setTicker(savedQuote.getTicker());
        savedSecurityOrder.setSize(10);
        savedSecurityOrder.setPrice(20d);
        savedSecurityOrder.setNotes("Note");
        savedSecurityOrder = securityOrderDao.save(savedSecurityOrder);
    }

    @After
    public void deleteOne() {
        securityOrderDao.deleteById(savedSecurityOrder.getID());
        quoteDao.deleteById(savedQuote.getTicker());
        accountDao.deleteById(savedAccount.getID());
        traderDao.deleteById(savedTrader.getID());
    }

    @Test
    public void findById() {
        Position position = positionDao.findById(savedSecurityOrder.getAccountId(), savedSecurityOrder.getTicker()).get();
        assertEquals(savedSecurityOrder.getSize(), position.getPosition());
    }

    @Test
    public void findAll() {
        List<Position> testList = positionDao.findAll();
        assertEquals(savedSecurityOrder.getSize(), testList.get(0).getPosition());
    }

    @Test
    public void count() {
        assertEquals(1, positionDao.count());
    }

    @Test
    public void findAllByAccountId() {
        List<Position> testList = positionDao.findAllByAccountId(savedAccount.getID());
        assertEquals(savedSecurityOrder.getSize(), testList.get(0).getPosition());
    }


}