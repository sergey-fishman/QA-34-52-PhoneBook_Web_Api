package data_providers;

import dto.ContactDto;
import dto.UserLombok;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactDataProvider {
    @DataProvider
    public Iterator<ContactDto> dataProviderWrongNumber() {
        List<ContactDto> list = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader
                ("src/test/resources/wrong_number.csv"))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                list.add(ContactDto.builder()
                        .name("John")
                        .lastName("Cina")
                        .phone(line)
                        .address("Address")
                        .build());
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("created exception");
        }
        return list.listIterator();
    }
}
