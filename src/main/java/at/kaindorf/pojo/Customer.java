package at.kaindorf.pojo;

import at.kaindorf.parse.CharacterAdapter;
import at.kaindorf.parse.LocalDateAdapter;
import at.kaindorf.parse.LocalDateDeserializer;
import at.kaindorf.parse.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Address.class, Country.class})
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
        @NamedQuery(name = "Customer.countAll", query = "SELECT COUNT(c) FROM Customer c"),
        @NamedQuery(name = "Customer.findYears", query = "SELECT DISTINCT EXTRACT(YEAR FROM c.since) FROM Customer c"),
        @NamedQuery(name = "Customer.findFromCountry", query = "SELECT c FROM Customer c WHERE c.address.country.country_name = :country")
})
public class Customer {
    @Id
    @GeneratedValue
    private Long customer_id;
    @NonNull
    private String firstname;
    @NonNull
    private String lastname;
    @XmlJavaTypeAdapter(CharacterAdapter.class)
    @NonNull
    private char gender;
    @NonNull
    private boolean active;
    @NonNull
    private String email;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NonNull
    private LocalDate since;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @XmlElements({
            @XmlElement(name = "streetname"),
            @XmlElement(name = "streetnumber"),
            @XmlElement(name = "postal_code"),
            @XmlElement(name = "city"),
            @XmlElement(name = "country"),
            @XmlElement(name = "country_code")
    })
    private Address address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (gender != customer.gender) return false;
        if (active != customer.active) return false;
        if (!firstname.equals(customer.firstname)) return false;
        if (!lastname.equals(customer.lastname)) return false;
        if (!email.equals(customer.email)) return false;
        if (!since.equals(customer.since)) return false;
        return Objects.equals(address, customer.address);
    }

    @Override
    public int hashCode() {
        int result = firstname.hashCode();
        result = 31 * result + lastname.hashCode();
        result = 31 * result + (int) gender;
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + email.hashCode();
        result = 31 * result + since.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}