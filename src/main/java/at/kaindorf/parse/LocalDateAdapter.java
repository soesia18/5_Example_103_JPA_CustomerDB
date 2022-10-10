package at.kaindorf.parse;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    //28-Jul-2000
    private final static DateTimeFormatter dtf =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("dd-MMM-yyyy")
                    .toFormatter(Locale.ENGLISH);

    @Override
    public LocalDate unmarshal(String str) {
        return LocalDate.parse(str, dtf);
    }

    @Override
    public String marshal(LocalDate ld) {
        return ld.format(dtf);
    }
}

