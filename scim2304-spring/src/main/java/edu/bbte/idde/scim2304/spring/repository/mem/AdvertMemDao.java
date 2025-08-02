package edu.bbte.idde.scim2304.spring.repository.mem;

import edu.bbte.idde.scim2304.spring.model.Advert;
import edu.bbte.idde.scim2304.spring.repository.AdvertDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Repository
@Profile("mem")
@Slf4j
public class AdvertMemDao extends MemDao<Advert> implements AdvertDao {
    public AdvertMemDao() {
        super();
        setModelClassType(Advert.class);
    }

    @Override
    public Collection<Advert> findByBrand(String brand) {
        var result = entities.values().stream()
            .filter(advert -> advert.getBrand().equals(brand))
            .collect(Collectors.toList());
        log.info("Found {} advert(s) by brand {}", result.size(), brand);
        return result;
    }

    @Override
    public Collection<Advert> findByName(String name) {
        var result = entities.values().stream()
            .filter(advert -> advert.getName().equals(name))
            .collect(Collectors.toList());
        log.info("Found {} advert(s) by name {}", result.size(), name);
        return result;
    }

    @Override
    public Collection<Advert> findByYear(int year) {
        var result = entities.values().stream()
            .filter(advert -> advert.getYear() == year)
            .collect(Collectors.toList());
        log.info("Found {} advert(s) by year {}", result.size(), year);
        return result;
    }

    @Override
    public Collection<Advert> findUploadedBeforeDate(Date date) {
        var result = entities.values().stream()
            .filter(advert -> advert.getUploadDate().before(date))
            .collect(Collectors.toList());
        log.info("Found {} advert(s) uploaded before {}", result.size(), date);
        return result;
    }

    @Override
    public Collection<Advert> findUploadedAfterDate(Date date) {
        var result = entities.values().stream()
            .filter(advert -> advert.getUploadDate().after(date))
            .collect(Collectors.toList());
        log.info("Found {} advert(s) uploaded after {}", result.size(), date);
        return result;
    }
}
