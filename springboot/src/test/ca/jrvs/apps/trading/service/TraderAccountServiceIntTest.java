package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import java.util.Date;
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
public class TraderAccountServiceIntTest {

    private TraderAccountView savedView;

    @Autowired
    private TraderAccountService traderAccountService;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private AccountDao accountDao;

    @Before
    public void addOne() {
        Trader savedTrader = new Trader();
        savedTrader.setFirstName("Jane");
        savedTrader.setLastName("Doe");
        savedTrader.setDob(new Date(2020, 7, 23));
        savedTrader.setCountry("Canada");
        savedTrader.setEmail("jane.doe@gmail.ca");
        savedView = traderAccountService.createTraderAndAccount(savedTrader);
    }

    @After
    public void deleteOne() {
        accountDao.deleteById(savedView.getAccount().getID());
        traderDao.deleteById(savedView.getTrader().getID());
    }

    @Test
    public void deleteTraderById() {
        try {
            traderAccountService.deleteTraderById(savedView.getTrader().getID());
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }

        try {
            traderAccountService.deleteTraderById(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deposit() {
        traderAccountService.deposit(savedView.getTrader().getID(), 100.0);
        assertEquals((Double)100.0, accountDao.findById(savedView.getAccount().getID()).get().getAmount());
        try {
            traderAccountService.deposit(null, 100.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            traderAccountService.deposit(savedView.getTrader().getID(), 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            traderAccountService.deposit(-100, 100.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void withdraw() {
        traderAccountService.withdraw(savedView.getTrader().getID(), 100.0);
        assertEquals((Double)(-100.0), accountDao.findById(savedView.getAccount().getID()).get().getAmount());
        try {
            traderAccountService.withdraw(null, 100.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            traderAccountService.withdraw(savedView.getTrader().getID(), 0.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            traderAccountService.withdraw(-100, 100.0);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

}
