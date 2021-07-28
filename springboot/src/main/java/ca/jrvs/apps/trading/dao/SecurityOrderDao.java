package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.SecurityOrder;

import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {

    private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);

    private final String TABLE_NAME = "security_order";
    private final String ID_COLUMN = "id";
    private final String ACCOUNT_ID = "account_id";


    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public SecurityOrderDao(DataSource dataSource) {
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
    Class<SecurityOrder> getEntityClass() {
        return SecurityOrder.class;
    }

    @Override
    public int updateOne(SecurityOrder entity) {
        String updateSql = "UPDATE " + TABLE_NAME + " SET account_id=?, status=?, ticker=?, size=?, "
                + "price=?, notes=? WHERE " + ID_COLUMN + "=?";
        return jdbcTemplate.update(updateSql, getUpdateValues(entity));
    }

    private Object getUpdateValues(SecurityOrder entity) {
        Object[] updateValues = new Object[7];
        updateValues[0] = entity.getAccountId();
        updateValues[1] = entity.getStatus();
        updateValues[2] = entity.getTicker();
        updateValues[3] = entity.getSize();
        updateValues[4] = entity.getPrice();
        updateValues[5] = entity.getNotes();
        updateValues[6] = entity.getID();
        return updateValues;
    }

    @Override
    public <S extends SecurityOrder> Iterable<S> saveAll(Iterable<S> iterable) {
        iterable.forEach(this::save);
        return iterable;
    }

    @Override
    public void delete(SecurityOrder securityOrder) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends SecurityOrder> iterable) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<SecurityOrder> findAllByAccountId(Integer accountId) {
        String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ACCOUNT_ID + "=?";
        List<SecurityOrder> orders = jdbcTemplate
                .query(selectSql, BeanPropertyRowMapper.newInstance(SecurityOrder.class), accountId);
        return orders;
    }
}