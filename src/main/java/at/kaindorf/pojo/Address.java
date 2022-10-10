package at.kaindorf.pojo;

import jakarta.persistence.*;
import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
        @NamedQuery(name = "Address.countAll", query = "SELECT COUNT(a) FROM Address a")
})
public class Address {
    @Id
    @GeneratedValue
    private Long address_id;
    @XmlElement(name = "streetname")
    @NonNull
    private String street_name;
    @Column(nullable = false)
    @XmlElement(name = "streetnumber")
    @NonNull
    private int street_number;
    @NonNull
    private String postal_code;
    @NonNull
    private String city;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "country_id")
    @XmlElements({
            @XmlElement(name = "country", type = Country.class),
            @XmlElement(name = "country_code", type = Country.class)
    })
    private Country country;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    @XmlTransient
    @ToString.Exclude
    private List<Customer> customers = new ArrayList<>();

    public void addCustomer (Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (street_number != address.street_number) return false;
        if (!street_name.equals(address.street_name)) return false;
        if (!postal_code.equals(address.postal_code)) return false;
        return city.equals(address.city);
    }

    @Override
    public int hashCode() {
        int result = street_name.hashCode();
        result = 31 * result + street_number;
        result = 31 * result + postal_code.hashCode();
        result = 31 * result + city.hashCode();
        return result;
    }
}