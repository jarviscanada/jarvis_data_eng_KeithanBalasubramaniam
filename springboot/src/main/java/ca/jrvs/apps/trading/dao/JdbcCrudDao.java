package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class JdbcCrudDao<T extends Entity<Integer>> implements CrudRepository<T, Integer>{

    private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

    abstract public JdbcTemplate getJdbcTemplate();

    abstract public SimpleJdbcInsert getSimpleJdbcInsert();

    abstract public String getTableName();

    abstract public String getIdColumnName();

    abstract Class<T> getEntityClass();

    /**
     * Save an entity and update auto-generated integer ID
     * @param entity to be saved
     * @return saved entity
     */
    @Override
    public <S extends T>S save(S entity) {
        if (existsById(entity.getID())) {
            if (updateOne(entity) != 1) {
                throw new DataRetrievalFailureException("Unable to update quote");
            }
        } else {
            addOne(entity);
        }
        return entity;
    }

    private <S extends T> void addOne(S entity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);

        Number newId = getSimpleJdbcInsert().executeAndReturnKey(parameterSource);
        entity.setID(newId.intValue());
    }

    abstract public int updateOne(T entity);

    @Override
    public Optional<T> findById(Integer id) {
        Optional<T> entity = Optional.empty();
        String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";
        try {
            entity = Optional.ofNullable((T)getJdbcTemplate()
                    .queryForObject(selectSql, BeanPropertyRowMapper.newInstance(getEntityClass()), id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            logger.debug("Can't find trader id:" + id, ex);
        }
        return entity;
    }

    @Override
    public boolean existsById(Integer id) {
        T t = null;
        String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";
        try {
            t = getJdbcTemplate()
                    .queryForObject(selectSql,
                            BeanPropertyRowMapper.newInstance(getEntityClass()), id);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("Can't find entity id:" + id, e);
        }
        if (t != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<T> findAll() {
        String selectSql = "SELECT * FROM " + getTableName();
        return getJdbcTemplate()
                .query(selectSql, BeanPropertyRowMapper.newInstance(getEntityClass()));
    }

    @Override
    public List<T> findAllById(Iterable<Integer> ids) {
        List<T> allValues = new ArrayList<>();
        ids.forEach(element -> allValues.add(findById(element).get()));
        return allValues;
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        String deleteSql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";
        getJdbcTemplate().update(deleteSql, id);
    }

    @Override
    public long count() {
        String countSql = "SELECT COUNT(*) FROM " + getTableName();
        return getJdbcTemplate().queryForObject(countSql, long.class);
    }

    @Override
    public void deleteAll() {
        String deleteSql = "DELETE FROM " + getTableName();
        getJdbcTemplate().update(deleteSql);
    }
}
