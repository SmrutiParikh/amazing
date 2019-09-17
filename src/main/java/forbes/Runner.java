package forbes;

import com.fasterxml.jackson.databind.JsonNode;
import forbes.models.Product;
import forbes.services.ISink;
import forbes.services.impl.FileWriterSink;
import forbes.services.impl.ProductsProcessorService;
import forbes.services.impl.PromotionProcessorService;
import forbes.utils.Constants;
import forbes.utils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Runner {

    public static void main(String[] args) {
        //Load Properties
        PropertyUtils.initProperties();

        //Fetch Products
        List<Product> products = ProductsProcessorService.processProducts();

        //Apply Promotions
        String promotionSet = ArrayUtils.getLength(args) > 0 ? args[0] : "";
        String disableSplPromo = ArrayUtils.getLength(args) > 1 ? args[1] : "";
        promotionSet = StringUtils.isNotBlank(promotionSet) && !promotionSet.contains(".csv") ? promotionSet + ".csv" : promotionSet;
        List<JsonNode> jsonNodes = PromotionProcessorService.processPromotions(products, promotionSet, disableSplPromo);

        //Write Output to a file
        ISink sink = new FileWriterSink();
        sink.writer(jsonNodes, PropertyUtils.getProperty(Constants.OUTPUT_FILE_PATH));
    }
}
