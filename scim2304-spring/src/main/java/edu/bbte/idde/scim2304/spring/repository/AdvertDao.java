package edu.bbte.idde.scim2304.spring.repository;

import edu.bbte.idde.scim2304.spring.model.Advert;

import java.util.Collection;
import java.util.Date;

public interface AdvertDao extends Dao<Advert> {
    Collection<Advert> findByName(String name);

    Collection<Advert> findByBrand(String brand);

    Collection<Advert> findByYear(int year);

    Collection<Advert> findUploadedBeforeDate(Date date);

    Collection<Advert> findUploadedAfterDate(Date date);
}
