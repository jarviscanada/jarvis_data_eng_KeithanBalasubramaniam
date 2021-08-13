package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
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
public class SecurityOrderDaoIntTest {

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
        savedAccount.setAmount(30.0);
        savedAccount = accountDao.save(savedAccount);

        savedSecurityOrder = new SecurityOrder();
        savedSecurityOrder.setAccountId(savedAccount.getID());
        savedSecurityOrder.setStatus("Sold");
        savedSecurityOrder.setTicker(savedQuote.getTicker());
        savedSecurityOrder.setSize(10);
        savedSecurityOrder.setPrice(20d);
        savedSecurityOrder.setNotes("Note");
        savedSecurityOrder = securityOrderDao.save(savedSecurityOrder);
    }

    @After
    public void deleteAll() {
        securityOrderDao.deleteAll();
        quoteDao.deleteById(savedQuote.getTicker());
        accountDao.deleteById(savedAccount.getID());
        traderDao.deleteById(savedTrader.getID());
    }

    @Test
    public void findAllById() {
        List<SecurityOrder> testList = Lists.newArrayList(securityOrderDao.findAllById(
                Arrays.asList(savedSecurityOrder.getID())));
        assertEquals(1, testList.size());
        assertEquals(savedSecurityOrder, testList.get(0));
    }

    @Test
    public void saveAll() {
        List<SecurityOrder> testList = new ArrayList<>();
        testList.add(new SecurityOrder());
        testList.get(0).setAccountId(savedAccount.getID());
        testList.get(0).setStatus("Completed");
        testList.get(0).setTicker(savedQuote.getTicker());
        testList.get(0).setSize(10);
        testList.get(0).setPrice(11d);
        testList.get(0).setNotes("Test note");
        testList.add(new SecurityOrder());
        testList.get(1).setAccountId(savedAccount.getID());
        testList.get(1).setStatus("In progress");
        testList.get(1).setTicker(savedQuote.getTicker());
        testList.get(1).setSize(16);
        testList.get(1).setPrice(14.0);
        testList.get(1).setNotes("Test Note 2");
        securityOrderDao.saveAll(testList);
        assertEquals(testList.get(0), securityOrderDao.findById(testList.get(0).getID()).get());
        assertEquals(testList.get(1), securityOrderDao.findById(testList.get(1).getID()).get());
    }

}