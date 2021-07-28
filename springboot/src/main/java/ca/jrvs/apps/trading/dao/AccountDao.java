package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class AccountDao extends JdbcCrudDao<Account>{

    private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

    private static String TABLE_NAME = "account";
    private static String ID_COLUMN = "id";
    private static String TRADER_ID = "trader_id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public AccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID_COLUMN);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return simpleJdbcInsert;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnName() {
        return ID_COLUMN;
    }

    @Override
    Class<Account> getEntityClass() {
        return Account.class;
    }

    /**
     * Helper function that updates one quote
     *
     * @param entity
     */
    @Override
    public int updateOne(Account entity) {
        String updateSql = "UPDATE " + TABLE_NAME + " SET amount=? WHERE " +ID_COLUMN + "=?";
        return jdbcTemplate.update(updateSql, getUpdateValues(entity));
    }

    private Object[] getUpdateValues(Account entity) {
        Object[] updateValues = new Object[2];
        updateValues[0] = entity.getAmount();
        updateValues[1] = entity.getID();
        return updateValues;
    }

    @Override
    public <S extends Account> Iterable<S> saveAll(Iterable<S> iterable) {
        iterable.forEach(this::save);
        return iterable;
    }

    @Override
    public void delete(Account account) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends Account> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public Optional<Account> findByTraderId(int traderId) {
        Optional<Account> account = Optional.empty();
        String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + TRADER_ID + "=?";
        try {
            account = Optional.ofNullable(getJdbcTemplate().queryForObject(selectSql,
                    BeanPropertyRowMapper.newInstance(getEntityClass()), traderId));
        } catch (IncorrectResultSizeDataAccessException ex) {
            logger.error("Cannot find account with trader ID: " + traderId, ex);
        }
        return account;
    }

}
