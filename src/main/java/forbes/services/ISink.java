package forbes.services;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface ISink {
    void writer(List<JsonNode> productList, String filePath);
}
