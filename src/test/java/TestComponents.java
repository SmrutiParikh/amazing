import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forbes.models.DiscountNode;
import forbes.models.Product;
import forbes.services.impl.PromotionProcessorService;
import forbes.utils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestComponents {
    private static List<Product> products = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeClass
    public static void init() {
        PropertyUtils.initProperties();
        products.add(new Product("electronics", 10, (float) 3.8, "INR",
                2500F, "Asia", "XTP mobile", null));
        products.add(new Product("clothing", 50, (float)4.8, "INR",
                1800F, "Asia", "Jeet white shoes", null));
    }

    @Before
    public void clearPromoService(){
        PromotionProcessorService.clear();
    }

    @Test
    public void testPromoDefault() {
        List<JsonNode> jsonNodes = PromotionProcessorService.processPromotions(products, StringUtils.EMPTY, StringUtils.EMPTY);
        List<DiscountNode> discountNodes = jsonNodes.stream().map(e -> mapper.convertValue(e.get("discount"), DiscountNode.class)).collect(Collectors.toList());
        assertEquals(discountNodes.get(0).getAmount(), "100");
        assertEquals(discountNodes.get(1).getAmount(), "216");
    }

    @Test
    public void testPromoSetA() {
        List<JsonNode> jsonNodes = PromotionProcessorService.processPromotions(products, "promotionSetA.csv", StringUtils.EMPTY);
        List<DiscountNode> discountNodes = jsonNodes.stream().map(e -> mapper.convertValue(e.get("discount"), DiscountNode.class)).collect(Collectors.toList());
        assertEquals(discountNodes.get(0).getAmount(), "100");
        assertEquals(discountNodes.get(1).getAmount(), "36");
    }

    @Test
    public void testPromoSetB() {
        List<JsonNode> jsonNodes = PromotionProcessorService.processPromotions(products, "promotionSetB.csv", StringUtils.EMPTY);
        List<DiscountNode> discountNodes = jsonNodes.stream().map(e -> mapper.convertValue(e.get("discount"), DiscountNode.class)).collect(Collectors.toList());
        assertEquals(discountNodes.get(0).getAmount(), "50");
        assertEquals(discountNodes.get(1).getAmount(), "216");
    }

    @Test
    public void testPromoSetANoSpl() {
        List<JsonNode> jsonNodes = PromotionProcessorService.processPromotions(products, "promotionSetA.csv", "true");
        List<DiscountNode> discountNodes = jsonNodes.stream().map(e -> mapper.convertValue(e.get("discount"), DiscountNode.class)).collect(Collectors.toList());
        assertEquals(discountNodes.get(0).getAmount(), "100");
        assertEquals(discountNodes.get(1).getAmount(), "0");
    }

    @Test
    public void testPromoSetBNoSpl() {
        List<JsonNode> jsonNodes = PromotionProcessorService.processPromotions(products, "promotionSetB.csv", "true");
        List<DiscountNode> discountNodes = jsonNodes.stream().map(e -> mapper.convertValue(e.get("discount"), DiscountNode.class)).collect(Collectors.toList());
        assertEquals(discountNodes.get(0).getAmount(), "0");
        assertEquals(discountNodes.get(1).getAmount(), "216");
    }

}
