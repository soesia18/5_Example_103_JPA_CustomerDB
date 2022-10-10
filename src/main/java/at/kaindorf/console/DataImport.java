package at.kaindorf.console;

import at.kaindorf.parse.CustomerParse;
import at.kaindorf.parse.Customers;
import at.kaindorf.pojo.Address;
import at.kaindorf.pojo.Country;
import at.kaindorf.pojo.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBContextFactory;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class DataImport {
    private EntityManagerFactory emf;
    private EntityManager em;

    private Scanner scanner;

    private final List<Country> countries = new ArrayList<>();

    public static void main(String[] args) {
        DataImport dataImport = new DataImport();
        dataImport.open();

        dataImport.importXML();
        //dataImport.importJSON();
        dataImport.countAllImports();

        dataImport.findFromCountry();
        dataImport.findByYear();

        dataImport.close();
    }

    public void open() {
        emf = Persistence.createEntityManagerFactory("PU_customerdb");
        em = emf.createEntityManager();

        scanner = new Scanner(System.in);

        em.getTransaction().begin();
        em.getTransaction().commit();
    }

    public void importXML() {
        File file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "customers.xml").toFile();
        Customers customers = JAXB.unmarshal(file, Customers.class);
        importData(customers.getCustomers());

        System.out.println(customers.getCustomers());
    }

    public void importJSON() {
        ObjectMapper mapper = new ObjectMapper();
        File file = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "customers.json").toFile();

        try {
            CustomerParse[] customers = mapper.readValue(file, CustomerParse[].class);
            importData(Arrays.stream(customers).collect(Collectors.toList()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void importData (List<CustomerParse> customerParses) {
        customerParses.forEach(customerParse -> {
            Country country = new Country(customerParse.getCountry(), customerParse.getCountry_code());
            Address address = new Address(customerParse.getStreetname(), customerParse.getStreetnumber(), customerParse.getPostal_code(), customerParse.getCity());

            if (!countries.contains(country)) {
                countries.add(country);
            }

            country = countries.get(countries.indexOf(country));
            address.setCountry(country);
            country.addAddress(address);

            Customer customer = new Customer(customerParse.getFirstname(), customerParse.getLastname(), customerParse.getGender(), customerParse.isActive(), customerParse.getEmail(), customerParse.getSince());
            customer.setAddress(address);
            address.addCustomer(customer);
        });

        countries.forEach(em::persist);
        em.getTransaction().begin();
        em.getTransaction().commit();
    }

    public void countAllImports () {
        Long countCustomer = em.createNamedQuery("Customer.countAll", Long.class).getSingleResult();
        Long countAddresses = em.createNamedQuery("Address.countAll", Long.class).getSingleResult();
        Long countCountries = em.createNamedQuery("Country.countAll", Long.class).getSingleResult();

        System.out.println("Customers imported: " + countCustomer);
        System.out.println("Addresses imported: " + countAddresses);
        System.out.println("Countries imported: " + countCountries);
    }

    public void findFromCountry () {
        System.out.println("------------------------------------------");
        System.out.println("Geben Sie ein Land ein: ");
        System.out.println("------------------------------------------");
        String countryName = scanner.nextLine();
        List<Customer> customers = em.createNamedQuery("Customer.findFromCountry", Customer.class).setParameter("country", countryName).getResultList();
        customers.forEach(System.out::println);
    }

    public void findByYear () {
        System.out.println("------------------------------------------");
        System.out.println("Get all Years: ");
        System.out.println("------------------------------------------");
        List<BigDecimal> allYears = em.createNamedQuery("Customer.findYears", BigDecimal.class).getResultList();
        allYears.forEach(System.out::println);
    }


    public void close() {
        scanner.close();
        em.close();
        emf.close();
    }
}
