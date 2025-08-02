package edu.bbte.idde.scim2304.spring.service;

import edu.bbte.idde.scim2304.spring.model.Advert;
import edu.bbte.idde.scim2304.spring.repository.AdvertDao;
import edu.bbte.idde.scim2304.spring.repository.exceptions.EntityNotFoundException;
import edu.bbte.idde.scim2304.spring.repository.exceptions.FailedCreateEntityException;
import edu.bbte.idde.scim2304.spring.service.exceptions.FailedCreateException;
import edu.bbte.idde.scim2304.spring.service.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Slf4j
@Service
@Profile({"jdbc", "mem"})
public class AdvertServiceJdbcMem implements AdvertService {
    @Autowired
    private AdvertDao advertDao;

    @Override
    public Advert createAdvert(Advert advert) throws FailedCreateException {
        try {
            return advertDao.create(advert);
        } catch (FailedCreateEntityException e) {
            log.error("Failed to create advert", e);
            throw new FailedCreateException("Failed to create advert", e);
        }
    }

    @Override
    public Advert findAdvertById(Long id) throws NotFoundException {
        try {
            return advertDao.findById(id);
        } catch (EntityNotFoundException e) {
            log.error("Advert does not exist", e);
            throw new NotFoundException("Advert not found", e);
        }
    }

    @Override
    public Collection<Advert> findAllAdverts() {
        return advertDao.findAll();
    }

    @Override
    public Collection<Advert> findAdvertsByName(String name) {
        return advertDao.findByName(name);
    }

    @Override
    public Collection<Advert> findAdvertsByBrand(String brand) {
        return advertDao.findByBrand(brand);
    }

    @Override
    public Collection<Advert> findAdvertsByYear(int year) {
        return advertDao.findByYear(year);
    }

    @Override
    public Collection<Advert> findAdvertsUploadedBeforeDate(Date date) {
        return advertDao.findUploadedBeforeDate(date);
    }

    @Override
    public Collection<Advert> findAdvertsUploadedAfterDate(Date date) {
        return advertDao.findUploadedAfterDate(date);
    }

    @Override
    public Advert updateAdvert(Long id, Advert newAdvert) throws NotFoundException {
        try {
            return advertDao.update(id, newAdvert);
        } catch (EntityNotFoundException e) {
            log.error("Advert does not exist", e);
            throw new NotFoundException("Advert not found", e);
        }
    }

    @Override
    public Advert deleteAdvert(Long id) throws NotFoundException {
        try {
            return advertDao.delete(id);
        } catch (EntityNotFoundException e) {
            log.error("Advert does not exist", e);
            throw new NotFoundException("Advert not found", e);
        }
    }
}
