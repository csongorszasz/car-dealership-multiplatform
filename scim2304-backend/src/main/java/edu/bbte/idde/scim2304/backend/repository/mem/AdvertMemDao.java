package edu.bbte.idde.scim2304.backend.repository.mem;

import edu.bbte.idde.scim2304.backend.model.Advert;
import edu.bbte.idde.scim2304.backend.repository.AdvertDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class AdvertMemDao extends MemDao<Advert> implements AdvertDao {
    private static final Logger LOG = LoggerFactory.getLogger(AdvertMemDao.class);

    public AdvertMemDao(Class<Advert> type) {
        super(type);
    }

    @Override
    public Collection<Advert> findByBrand(String brand) {
        var result = entities.values().stream()
            .filter(advert -> advert.getBrand().equals(brand))
            .collect(Collectors.toList());
        LOG.info("Found {} advert(s) by brand {}", result.size(), brand);
        return result;
    }

    @Override
    public Collection<Advert> findByName(String name) {
        var result = entities.values().stream()
            .filter(advert -> advert.getName().equals(name))
            .collect(Collectors.toList());
        LOG.info("Found {} advert(s) by name {}", result.size(), name);
        return result;
    }

    @Override
    public Collection<Advert> findByYear(int year) {
        var result = entities.values().stream()
            .filter(advert -> advert.getYear() == year)
            .collect(Collectors.toList());
        LOG.info("Found {} advert(s) by year {}", result.size(), year);
        return result;
    }

    @Override
    public Collection<Advert> findUploadedBeforeDate(Date date) {
        var result = entities.values().stream()
            .filter(advert -> advert.getUploadDate().before(date))
            .collect(Collectors.toList());
        LOG.info("Found {} advert(s) uploaded before {}", result.size(), date);
        return result;
    }

    @Override
    public Collection<Advert> findUploadedAfterDate(Date date) {
        var result = entities.values().stream()
            .filter(advert -> advert.getUploadDate().after(date))
            .collect(Collectors.toList());
        LOG.info("Found {} advert(s) uploaded after {}", result.size(), date);
        return result;
    }
}
