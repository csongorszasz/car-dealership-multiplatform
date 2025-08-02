package edu.bbte.idde.scim2304.backend.repository;

import edu.bbte.idde.scim2304.backend.model.Advert;

import java.util.Collection;
import java.util.Date;

public interface AdvertDao extends Dao<Advert> {
    Collection<Advert> findByName(String name);

    Collection<Advert> findByBrand(String brand);

    Collection<Advert> findByYear(int year);

    Collection<Advert> findUploadedBeforeDate(Date date);

    Collection<Advert> findUploadedAfterDate(Date date);
}
