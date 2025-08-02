package edu.bbte.idde.scim2304.spring.controller.dto.incoming;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertInDto {
    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String brand;

    @NotNull
    @Positive
    private int year;

    @NotNull
    @Positive
    private float price;

    @NotNull
    @Past
    private Date uploadDate;
}
