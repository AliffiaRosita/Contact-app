package aliffia.restful.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="addreses")
public class Address {
    @Id
    private String id;
    private String street;
    private String province;
    private String country;
    private String city;
    @Column(name="postal_code")
    private String postalCode;

    @ManyToOne
    @JoinColumn(name="contact_id", referencedColumnName = "id")
    private Contact contact;

}
