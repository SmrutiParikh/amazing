package forbes.utils;

import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forbes.services.impl.PromotionProcessorService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVFileReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVFileReader.class);

    public static List<List<String>> read(String filePath) {
        List<List<String>> records = new ArrayList<>();
        URL resource = PromotionProcessorService.class.getClassLoader().getResource(filePath);
        if (null == resource) {
            return null;
        }
        try (CSVReader csvReader = new CSVReader(new FileReader(new File(resource.getFile())))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            LOGGER.error("Error while reading CSV file, filePath : {}, Exception : ", filePath, e);
        }
        return records;
    }
}
