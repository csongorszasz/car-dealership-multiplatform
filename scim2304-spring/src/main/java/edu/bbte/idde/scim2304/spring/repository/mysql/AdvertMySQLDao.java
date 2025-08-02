package edu.bbte.idde.scim2304.spring.repository.mysql;


import edu.bbte.idde.scim2304.spring.model.Advert;
import edu.bbte.idde.scim2304.spring.repository.AdvertDao;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Repository
@Profile("jdbc")
public class AdvertMySQLDao extends MySQLDao<Advert> implements AdvertDao {
    public AdvertMySQLDao() {
        super();
        setModelClass(Advert.class);

        // only keep fields that are not collections, as a table can't have a collection column
        var fields = Arrays.stream(modelClass.getDeclaredFields())
            .filter(field -> !Collection.class.isAssignableFrom(field.getType()))
            .toList();
        setFields(fields);
    }

    @PostConstruct
    public void init() {
        mySQLHelper.initializeTable(this, connectionPool, mySqlConfiguration);
    }

    @Override
    public Collection<Advert> findByName(String name) {
        return findByField(Objects.requireNonNull(mySQLHelper.getFieldByName(this, "name")), name);
    }

    @Override
    public Collection<Advert> findByBrand(String brand) {
        return findByField(Objects.requireNonNull(mySQLHelper.getFieldByName(this, "brand")), brand);
    }

    @Override
    public Collection<Advert> findByYear(int year) {
        return findByField(Objects.requireNonNull(mySQLHelper.getFieldByName(this, "year")), year);
    }

    @Override
    public Collection<Advert> findUploadedBeforeDate(Date date) {
        return findLessThanField(Objects.requireNonNull(mySQLHelper.getFieldByName(this, "uploadDate")), date);
    }

    @Override
    public Collection<Advert> findUploadedAfterDate(Date date) {
        return findGreaterThanField(Objects.requireNonNull(mySQLHelper.getFieldByName(this, "uploadDate")), date);
    }
}
