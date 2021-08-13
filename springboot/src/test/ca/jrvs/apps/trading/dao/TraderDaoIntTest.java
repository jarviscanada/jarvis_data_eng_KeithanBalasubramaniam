package ca.jrvs.apps.trading.dao;
import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
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
public class TraderDaoIntTest{

    @Autowired
    private TraderDao traderDao;

    private Trader savedTrader;

    @Before
    public void insertOne(){
        savedTrader = new Trader();
        savedTrader.setFirstName("Keith");
        savedTrader.setLastName("Bala");
        savedTrader.setDob(new Date(1999, 3, 31));
        savedTrader.setCountry("Canada");
        savedTrader.setEmail("keith@gmail.ca");
        traderDao.save(savedTrader);
    }

    @After
    public void deleteOne(){
        traderDao.deleteAll();
    }

    @Test
    public void findAllById(){
        List<Trader> traders = Lists.newArrayList(traderDao.findAllById(Arrays.asList(savedTrader.getID())));
        assertEquals(1, traders.size());
        assertEquals(savedTrader.getCountry(), traders.get(0).getCountry());
    }

    @Test
    public void saveAll() {
        List<Trader> savedTraders = new ArrayList<>();
        Trader savedTraderA = new Trader();
        savedTraderA.setFirstName("John");
        savedTraderA.setLastName("Doe");
        savedTraderA.setDob(new Date(2020, 1, 1));
        savedTraderA.setCountry("Canada");
        savedTraderA.setEmail("john.doe@gmail.ca");
        savedTraders.add(savedTraderA);

        traderDao.saveAll(savedTraders);
        assertEquals(2, traderDao.findAll().size());
        assertEquals(savedTraders.get(0), traderDao.findById(savedTraderA.getID()).get());
    }

}