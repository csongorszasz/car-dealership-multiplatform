package edu.bbte.idde.scim2304.spring.controller;

import edu.bbte.idde.scim2304.spring.controller.dto.incoming.AdvertInDto;
import edu.bbte.idde.scim2304.spring.controller.dto.outgoing.AdvertOutDto;
import edu.bbte.idde.scim2304.spring.model.mapper.AdvertMapper;
import edu.bbte.idde.scim2304.spring.service.AdvertService;
import edu.bbte.idde.scim2304.spring.service.exceptions.FailedCreateException;
import edu.bbte.idde.scim2304.spring.service.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/adverts")
public class AdvertController {
    @Autowired
    private AdvertService service;

    @Autowired
    private AdvertMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<AdvertOutDto> findAll(
        @RequestParam(name = "name") @Valid Optional<String> name,
        @RequestParam(name = "brand") @Valid Optional<String> brand,
        @RequestParam(name = "year") @Valid Optional<Integer> year,
        @RequestParam(name = "beforeUploadDate") @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid Optional<Date>
                beforeUploadDate,
        @RequestParam(name = "afterUploadDate") @DateTimeFormat(pattern = "yyyy-MM-dd") @Valid Optional<Date>
                afterUploadDate
    ) {
        if (name.isPresent()) {
            return mapper.advertsToDtos(service.findAdvertsByName(name.get()));
        } else if (brand.isPresent()) {
            return mapper.advertsToDtos(service.findAdvertsByBrand(brand.get()));
        } else if (year.isPresent()) {
            return mapper.advertsToDtos(service.findAdvertsByYear(year.get()));
        } else if (beforeUploadDate.isPresent()) {
            return mapper.advertsToDtos(service.findAdvertsUploadedBeforeDate(beforeUploadDate.get()));
        } else if (afterUploadDate.isPresent()) {
            return mapper.advertsToDtos(service.findAdvertsUploadedAfterDate(afterUploadDate.get()));
        }
        return mapper.advertsToDtos(service.findAllAdverts());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdvertOutDto findById(@PathVariable Long id) throws NotFoundException {
        return mapper.advertToDto(service.findAdvertById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertOutDto create(@RequestBody @Valid AdvertInDto advert) throws FailedCreateException {
        return mapper.advertToDto(service.createAdvert(mapper.dtoToAdvert(advert)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdvertOutDto deleteById(@PathVariable Long id) throws NotFoundException {
        return mapper.advertToDto(service.deleteAdvert(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdvertOutDto update(@PathVariable Long id, @RequestBody @Valid AdvertInDto advert) throws NotFoundException {
        var updatedAdvert = service.updateAdvert(id, mapper.dtoToAdvert(advert));
        updatedAdvert.setId(id);
        return mapper.advertToDto(updatedAdvert);
    }
}
