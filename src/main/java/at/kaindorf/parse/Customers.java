package at.kaindorf.parse;

import at.kaindorf.pojo.Country;
import at.kaindorf.pojo.Customer;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Customers {
    @XmlElement(name = "customer")
    private List<CustomerParse> customers;
}
