package edu.bbte.idde.scim2304.backend.service;

import edu.bbte.idde.scim2304.backend.repository.AdvertDao;
import edu.bbte.idde.scim2304.backend.repository.exceptions.EntityNotFoundException;
import edu.bbte.idde.scim2304.backend.repository.exceptions.FailedCreateEntityException;
import edu.bbte.idde.scim2304.backend.repository.mysql.ConnectionPool;
import edu.bbte.idde.scim2304.backend.service.exceptions.FailedCreateException;
import edu.bbte.idde.scim2304.backend.model.Advert;
import edu.bbte.idde.scim2304.backend.repository.DaoFactory;
import edu.bbte.idde.scim2304.backend.service.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

public class AdvertServiceImpl implements AdvertService {
    private static final Logger LOG = LoggerFactory.getLogger(AdvertServiceImpl.class);

    @Override
    public Advert createAdvert(Advert advert) throws FailedCreateException {
        try {
            return DaoFactory.getInstance().getAdvertDao().create(advert);
        } catch (FailedCreateEntityException e) {
            LOG.error("Failed to create advert", e);
            throw new FailedCreateException("Failed to create advert", e);
        }
    }

    @Override
    public Advert findAdvertById(Long id) throws NotFoundException {
        try {
            return DaoFactory.getInstance().getAdvertDao().findById(id);
        } catch (EntityNotFoundException e) {
            LOG.error("Advert does not exist", e);
            throw new NotFoundException("Advert not found", e);
        }
    }

    @Override
    public Collection<Advert> findAllAdverts() {
        return DaoFactory.getInstance().getAdvertDao().findAll();
    }

    @Override
    public Collection<Advert> findAdvertsByName(String name) {
        return DaoFactory.getInstance().getAdvertDao().findByName(name);
    }

    @Override
    public Collection<Advert> findAdvertsByBrand(String brand) {
        return DaoFactory.getInstance().getAdvertDao().findByBrand(brand);
    }

    @Override
    public Collection<Advert> findAdvertsByYear(int year) {
        return DaoFactory.getInstance().getAdvertDao().findByYear(year);
    }

    @Override
    public Collection<Advert> findAdvertsUploadedBeforeDate(Date date) {
        return DaoFactory.getInstance().getAdvertDao().findUploadedBeforeDate(date);
    }

    @Override
    public Collection<Advert> findAdvertsUploadedAfterDate(Date date) {
        return DaoFactory.getInstance().getAdvertDao().findUploadedAfterDate(date);
    }

    @Override
    public Advert updateAdvert(Long id, Advert newAdvert) throws NotFoundException {
        try {
            return DaoFactory.getInstance().getAdvertDao().update(id, newAdvert);
        } catch (EntityNotFoundException e) {
            LOG.error("Advert does not exist", e);
            throw new NotFoundException("Advert not found", e);
        }
    }

    @Override
    public Advert deleteAdvert(Long id) throws NotFoundException {
        try {
            return DaoFactory.getInstance().getAdvertDao().delete(id);
        } catch (EntityNotFoundException e) {
            LOG.error("Advert does not exist", e);
            throw new NotFoundException("Advert not found", e);
        }
    }

    @Override
    public void onInit() {
        DaoFactory.getInstance().getAdvertDao(); // try accessing the advert dao to initialize the database connection
        LOG.info("Advert service initialized");
    }

    @Override
    public void onDestroy() {
        if (DaoFactory.getInstance().getAdvertDao().getClass().isAssignableFrom(AdvertDao.class)) {
            ConnectionPool.getInstance().close();
            LOG.info("Connection pool closed");
        }
        LOG.info("Advert service destroyed");
    }
}
