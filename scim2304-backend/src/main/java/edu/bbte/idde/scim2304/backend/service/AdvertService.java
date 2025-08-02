package edu.bbte.idde.scim2304.backend.service;


import edu.bbte.idde.scim2304.backend.service.exceptions.FailedCreateException;
import edu.bbte.idde.scim2304.backend.model.Advert;
import edu.bbte.idde.scim2304.backend.service.exceptions.NotFoundException;

import java.util.Collection;
import java.util.Date;

public interface AdvertService {
    Advert createAdvert(Advert advert) throws FailedCreateException;

    Advert findAdvertById(Long id) throws NotFoundException;

    Collection<Advert> findAllAdverts();

    Collection<Advert> findAdvertsByName(String name);

    Collection<Advert> findAdvertsByBrand(String brand);

    Collection<Advert> findAdvertsByYear(int year);

    Collection<Advert> findAdvertsUploadedBeforeDate(Date date);

    Collection<Advert> findAdvertsUploadedAfterDate(Date date);

    Advert updateAdvert(Long id, Advert newAdvert) throws NotFoundException;

    Advert deleteAdvert(Long id) throws NotFoundException;

    void onInit();

    void onDestroy();
}
