package at.kaindorf.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
        @NamedQuery(name = "Country.countAll", query = "SELECT COUNT(c) FROM Country c"),
        @NamedQuery(name = "Country.findByName", query = "SELECT c FROM Country c WHERE c.country_name = :country_name"),
        @NamedQuery(name = "Country.findAll", query = "SELECT c FROM Country c")
})
public class Country {
    @Id
    @GeneratedValue
    private Long country_id;
    @XmlElement(name = "country")
    @NonNull
    private String country_name;
    @NonNull
    private String country_code;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    @XmlTransient
    private List<Address> addresses = new ArrayList<>();

    public void addAddress (Address address) {
        addresses.add(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (!country_name.equals(country.country_name)) return false;
        return country_code.equals(country.country_code);
    }

    @Override
    public int hashCode() {
        int result = country_name.hashCode();
        result = 31 * result + country_code.hashCode();
        return result;
    }
}