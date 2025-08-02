package edu.bbte.idde.scim2304.spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Comment extends BaseEntity {
    private String text;
    private String author;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Advert advert;
}
