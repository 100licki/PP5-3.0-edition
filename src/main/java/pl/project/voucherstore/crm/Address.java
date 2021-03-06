package pl.project.voucherstore.crm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@NoArgsConstructor
@Data
public class Address {
    @Column
    private String street;
    @NotNull
    private String zip;
    @NotNull
    private String city;
}
