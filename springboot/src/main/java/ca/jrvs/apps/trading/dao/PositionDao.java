package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDao  {

    private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);

    private final String TABLE_NAME = "position";
    private final String ACCOUNT_ID = "account_id";
    private final String QUOTE_ID = "ticker";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PositionDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<Position> findById(Integer accountId, String ticker) {
        Optional<Position> entity = Optional.empty();
        String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE (" + ACCOUNT_ID + " =? "
                +"AND " + QUOTE_ID + "=?)";
        try {
            entity = Optional.ofNullable((Position) jdbcTemplate
                    .queryForObject(selectSql, BeanPropertyRowMapper.newInstance(Position.class),accountId, ticker));
        }catch (IncorrectResultSizeDataAccessException e) {
            logger.debug("Unable to find account :" + accountId + " with ticker" + ticker, e);
        }
        return entity;
    }

    public List<Position> findAll() {
        String selectSql = "SELECT * FROM " + TABLE_NAME;
        return jdbcTemplate.query(selectSql, BeanPropertyRowMapper.newInstance(Position.class));
    }

    public List<Position> findAllByAccountId(Integer accountId) {
        String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ACCOUNT_ID + "=?";
        List<Position> positions = jdbcTemplate
                .query(selectSql, BeanPropertyRowMapper.newInstance(Position.class), accountId);
        return positions;
    }

    public long count() {
        String countSql = "SELECT COUNT(*) FROM " + TABLE_NAME;
        return jdbcTemplate.queryForObject(countSql, Integer.class);
    }
}
