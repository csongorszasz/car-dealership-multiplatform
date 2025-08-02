package edu.bbte.idde.scim2304.backend.repository.mysql;

import edu.bbte.idde.scim2304.backend.model.Advert;
import edu.bbte.idde.scim2304.backend.repository.AdvertDao;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class AdvertMySQLDao extends MySQLDao<Advert> implements AdvertDao {
    public AdvertMySQLDao(Class<Advert> modelClassType) {
        super(modelClassType);
    }

    @Override
    public Collection<Advert> findByName(String name) {
        return findByField(Objects.requireNonNull(MySQLHelper.getFieldByName(this, "name")), name);
    }

    @Override
    public Collection<Advert> findByBrand(String brand) {
        return findByField(Objects.requireNonNull(MySQLHelper.getFieldByName(this, "brand")), brand);
    }

    @Override
    public Collection<Advert> findByYear(int year) {
        return findByField(Objects.requireNonNull(MySQLHelper.getFieldByName(this, "year")), year);
    }

    @Override
    public Collection<Advert> findUploadedBeforeDate(Date date) {
        return findLessThanField(Objects.requireNonNull(MySQLHelper.getFieldByName(this, "uploadDate")), date);
    }

    @Override
    public Collection<Advert> findUploadedAfterDate(Date date) {
        return findGreaterThanField(Objects.requireNonNull(MySQLHelper.getFieldByName(this, "uploadDate")), date);
    }
}
