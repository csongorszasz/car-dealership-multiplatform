package edu.bbte.idde.scim2304.spring.controller.dto.outgoing;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertOutDto {
    private Long id;
    private String name;
    private String brand;
    private int year;
    private float price;
    private Date uploadDate;
}
