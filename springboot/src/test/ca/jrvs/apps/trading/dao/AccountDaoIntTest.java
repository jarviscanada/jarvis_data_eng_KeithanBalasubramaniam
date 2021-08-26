package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
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
public class AccountDaoIntTest {

    @Autowired
    AccountDao accountDao;

    @Autowired
    TraderDao traderDao;

    private Account savedAccount;
    private Trader savedTrader;

    @Before
    public void insertOne() {
        savedTrader = new Trader();
        savedTrader.setFirstName("Jane");
        savedTrader.setLastName("Doe");
        savedTrader.setDob(new Date(2020, 7, 22));
        savedTrader.setCountry("Canada");
        savedTrader.setEmail("jane.doe@gmail.ca");
        traderDao.save(savedTrader);

        savedAccount = new Account();
        savedAccount.setAmount(20d);
        savedAccount.setTraderId(savedTrader.getID());
        accountDao.save(savedAccount);
    }

    @After
    public void deleteAll() {
        accountDao.deleteAll();
        traderDao.deleteById(savedTrader.getID());
    }

    @Test
    public void saveAll() {
        List<Account> savedAccounts = new ArrayList<>();

        Account accountA = new Account();
        accountA.setAmount(10d);
        accountA.setTraderId(savedTrader.getID());
        savedAccounts.add(accountA);

        Account accountB = new Account();
        accountB.setAmount(30d);
        accountB.setTraderId(savedTrader.getID());
        savedAccounts.add(accountB);

        accountDao.saveAll(Arrays.asList(accountA, accountB));
        assertEquals(savedAccounts.get(0), accountDao.findAllById(Arrays.asList(savedAccounts.get(0).getID())).get(0));
        assertEquals(savedAccounts.get(1), accountDao.findAllById(Arrays.asList(savedAccounts.get(1).getID())).get(0));
    }

    @Test
    public void findAllById() {
        List<Account> accounts = Lists.newArrayList(accountDao.findAllById(Arrays.asList(savedAccount.getID())));
        assertEquals(1,accounts.size());
        assertEquals(savedAccount.getAmount(),accounts.get(0).getAmount());
    }

}
