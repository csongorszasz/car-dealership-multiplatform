package edu.bbte.idde.scim2304.spring.model.mapper;

import edu.bbte.idde.scim2304.spring.controller.dto.incoming.CommentInDto;
import edu.bbte.idde.scim2304.spring.controller.dto.outgoing.CommentOutDto;
import edu.bbte.idde.scim2304.spring.model.Comment;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "advert", ignore = true)
    public abstract Comment dtoToComment(CommentInDto commentInDto);

    public abstract CommentOutDto commentToDto(Comment comment);

    @IterableMapping(elementTargetType = CommentOutDto.class)
    public abstract Collection<CommentOutDto> commentsToDtos(Collection<Comment> comments);
}
