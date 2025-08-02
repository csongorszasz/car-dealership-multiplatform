package edu.bbte.idde.scim2304.spring.controller.dto.incoming;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentInDto {
    @NotNull
    @Size(max = 255)
    private String text;

    @NotNull
    @Size(max = 255)
    private String author;
}
