package edu.bbte.idde.scim2304.spring.controller.dto.outgoing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentOutDto {
    private Long id;
    private String text;
    private String author;
}
