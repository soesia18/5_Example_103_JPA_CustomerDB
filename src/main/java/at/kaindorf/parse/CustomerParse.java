package at.kaindorf.parse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerParse {
    private String country;
    private String country_code;
    private String city;
    private String postal_code;
    private String streetname;
    private int streetnumber;
    private String firstname;
    private String lastname;
    @XmlJavaTypeAdapter(CharacterAdapter.class)
    private Character gender;
    private boolean active;
    private String email;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate since;
}


