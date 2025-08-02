package edu.bbte.idde.scim2304.spring.model.mapper;

import edu.bbte.idde.scim2304.spring.model.Advert;
import edu.bbte.idde.scim2304.spring.controller.dto.incoming.AdvertInDto;
import edu.bbte.idde.scim2304.spring.controller.dto.outgoing.AdvertOutDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class AdvertMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    public abstract Advert dtoToAdvert(AdvertInDto advertInDto);

    public abstract AdvertOutDto advertToDto(Advert advert);

    @IterableMapping(elementTargetType = AdvertOutDto.class)
    public abstract Collection<AdvertOutDto> advertsToDtos(Collection<Advert> adverts);
}
