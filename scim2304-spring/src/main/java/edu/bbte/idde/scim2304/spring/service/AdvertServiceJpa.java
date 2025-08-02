package edu.bbte.idde.scim2304.spring.service;

import edu.bbte.idde.scim2304.spring.model.Advert;
import edu.bbte.idde.scim2304.spring.repository.jpa.AdvertJpaDao;
import edu.bbte.idde.scim2304.spring.service.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Slf4j
@Service
@Profile("jpa")
public class AdvertServiceJpa implements AdvertService {
    @Autowired
    private AdvertJpaDao advertDao;

    @Override
    public Advert createAdvert(Advert advert) {
        return advertDao.save(advert);
    }

    @Override
    public Advert findAdvertById(Long id) throws NotFoundException {
        var advert = advertDao.findById(id);
        if (advert.isPresent()) {
            return advert.get();
        }
        log.error("Advert does not exist");
        throw new NotFoundException("Advert not found");
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
        return advertDao.findByUploadDateBefore(date);
    }

    @Override
    public Collection<Advert> findAdvertsUploadedAfterDate(Date date) {
        return advertDao.findByUploadDateAfter(date);
    }

    @Override
    @Transactional
    public Advert updateAdvert(Long id, Advert newAdvert) throws NotFoundException {
        this.findAdvertById(id);  // Check if the advert exists
        newAdvert.setId(id);
        return advertDao.save(newAdvert);
    }

    @Override
    public Advert deleteAdvert(Long id) throws NotFoundException {
        var advert = this.findAdvertById(id);  // Find the advert to be deleted
        advertDao.deleteById(id);
        return advert;
    }
}
