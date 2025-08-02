package edu.bbte.idde.scim2304.spring.controller;

import edu.bbte.idde.scim2304.spring.controller.dto.incoming.CommentInDto;
import edu.bbte.idde.scim2304.spring.controller.dto.outgoing.CommentOutDto;
import edu.bbte.idde.scim2304.spring.model.Comment;
import edu.bbte.idde.scim2304.spring.model.mapper.CommentMapper;
import edu.bbte.idde.scim2304.spring.service.AdvertService;
import edu.bbte.idde.scim2304.spring.service.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequestMapping("/adverts/{id}/comments")
@RestController
@Profile("jpa")
public class CombinedController {
    @Autowired
    private AdvertService service;

    @Autowired
    private CommentMapper mapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentOutDto> findAll(@PathVariable("id") Long id) throws NotFoundException {
        var advert = service.findAdvertById(id);
        return mapper.commentsToDtos(advert.getComments());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public CommentOutDto create(@PathVariable("id") Long id, @RequestBody CommentInDto commentInDto)
            throws NotFoundException {
        var advert = service.findAdvertById(id);
        Comment comment = mapper.dtoToComment(commentInDto);
        comment.setAdvert(advert);
        advert.getComments().add(comment);
        service.updateAdvert(id, advert);
        return mapper.commentToDto(comment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public CommentOutDto delete(@PathVariable("id") Long advertId, @PathVariable("commentId") Long commentId)
            throws NotFoundException {
        var advert = service.findAdvertById(advertId);
        List<Comment> comments = new ArrayList<>(advert.getComments());
        Comment commentToDelete = comments.stream()
                .filter(comment -> comment.getId()
                .equals(commentId))
                .findFirst()
                .orElseThrow(NotFoundException::new);
        comments.remove(commentToDelete);
        advert.setComments(comments);
        service.updateAdvert(advertId, advert);
        return mapper.commentToDto(commentToDelete);
    }
}
