package forbes.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forbes.services.ISink;

import java.io.FileWriter;
import java.util.List;

public class FileWriterSink implements ISink {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileWriterSink.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void writer(List<JsonNode> products, String filePath) {
        LOGGER.info("Writing {} products to file : {}", products.size(), filePath);

        try (FileWriter writer = new FileWriter(filePath)) {
            mapper.writeValue(writer, products);
        } catch (Exception e) {
            LOGGER.error("Exception at writer : {}", e.getMessage(), e);
        }

    }
}
