package edu.bbte.idde.scim2304.spring.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
public class Advert extends BaseEntity {
    private String name;
    private String brand;
    private int year;
    private float price;
    private Date uploadDate;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Comment> comments = new ArrayList<>();
}
