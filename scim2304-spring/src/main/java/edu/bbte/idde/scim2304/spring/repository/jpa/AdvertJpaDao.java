package edu.bbte.idde.scim2304.spring.repository.jpa;

import edu.bbte.idde.scim2304.spring.model.Advert;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
@Profile("jpa")
public interface AdvertJpaDao extends JpaRepository<Advert, Long> {
    Collection<Advert> findByName(String name);

    Collection<Advert> findByBrand(String brand);

    Collection<Advert> findByYear(int year);

    Collection<Advert> findByUploadDateBefore(Date date);

    Collection<Advert> findByUploadDateAfter(Date date);
}
