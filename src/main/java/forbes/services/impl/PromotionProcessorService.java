package forbes.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import forbes.models.DiscountNode;
import forbes.models.Product;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forbes.utils.CSVFileReader;
import forbes.utils.PropertyUtils;

import java.util.*;
import java.util.stream.Collectors;

import static forbes.utils.Constants.*;
import static forbes.utils.OperatorUtils.evalExpression;
import static forbes.utils.OperatorUtils.evalOperation;
import static forbes.utils.PromotionUtils.calculateDiscount;
import static forbes.utils.PromotionUtils.getDiscountTag;

public class PromotionProcessorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionProcessorService.class);

    private static final Map<String, List<List<String>>> promotionSets = new HashMap<>();
    private static final List<List<String>> specialPromotion = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<JsonNode> processPromotions(List<Product> products, String promotionSet, String disableSplPromo) {
        if (CollectionUtils.isEmpty(products)) {
            LOGGER.error("Products are not available.");
            return new ArrayList<>();
        }
        init(promotionSet, disableSplPromo);
        return products.stream()
                .map(PromotionProcessorService::applyPromotionSets)
                .collect(Collectors.toList());
    }

    private static void init(String promotionSet, String disableSplPromo) {
        String[] promotionSetFiles;
        if(StringUtils.isBlank(promotionSet)) {
            String property = PropertyUtils.getProperty(PROMOTION_SETS);
            promotionSetFiles = property != null ? property.split(",") : new String[]{};
        }
        else {
            promotionSetFiles = new String[]{ promotionSet };
        }

        Arrays.stream(promotionSetFiles).forEach(filePath -> {
            List<List<String>> records = CSVFileReader.read(filePath);
            if (CollectionUtils.isEmpty(records)) {
                return;
            }
            promotionSets.put(filePath, records);
        });

        if(Boolean.valueOf(disableSplPromo)) {
            return;
        }

        List<List<String>> records = CSVFileReader.read(PropertyUtils.getProperty(SPECIAL_PROMOTION));
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        specialPromotion.addAll(records);
    }

    private static JsonNode applyPromotionSets(Product product) {
        JsonNode jsonNode = mapper.convertValue(product, JsonNode.class);
        DiscountNode maxDiscountNode = new DiscountNode();

        for (Map.Entry<String, List<List<String>>> promotionSet : promotionSets.entrySet()) {
            DiscountNode discountNode = parsePromotionSet(promotionSet.getValue(), jsonNode);
            if (isGreater(maxDiscountNode, discountNode)) {
                maxDiscountNode = discountNode;
            }
        }

        if ( Float.parseFloat(maxDiscountNode.getAmount()) == 0F) {
            parsePromotionSet(specialPromotion, jsonNode);
        }
        ((ObjectNode) jsonNode).put("discount", mapper.convertValue(maxDiscountNode, JsonNode.class));
        return jsonNode;
    }

    private static boolean isGreater(DiscountNode maxDiscountNode, DiscountNode discountNode) {
        return Float.parseFloat(discountNode.getAmount()) > Float.parseFloat(maxDiscountNode.getAmount());
    }

    private static DiscountNode parsePromotionSet(List<List<String>> value, JsonNode jsonNode) {
        DiscountNode maxDiscountNode = new DiscountNode();
        for (List<String> expression : value) {
            DiscountNode discountNode = parseExpressionAndGetDiscount(expression, jsonNode);
            if (discountNode != null && isGreater(maxDiscountNode, discountNode)) {
                maxDiscountNode = discountNode;
            }
        }
        return maxDiscountNode;
    }

    private static DiscountNode parseExpressionAndGetDiscount(List<String> expression, JsonNode jsonNode) {
        try {
            boolean eval = false;
            if (expression.contains(OPERATOR.AND.name()) || expression.contains(OPERATOR.OR.name())) {
                int frequency = Collections.frequency(expression, OPERATOR.AND.name()) +
                        Collections.frequency(expression, OPERATOR.OR.name());
                for (int i = 0; i <= frequency; i++) {
                    int evalIndex = i * 3 + i;
                    boolean localEval = evalExpression(expression, jsonNode, evalIndex);
                    if (evalIndex == 0) {
                        eval = localEval;
                        continue;
                    }
                    String boolOp = expression.get(evalIndex - 1);
                    eval = evalOperation(boolOp, Boolean.toString(eval), Boolean.toString(localEval));
                }
            } else {
                eval = evalExpression(expression, jsonNode, 0);
            }
            if (eval) {
                float promoVal = Float.parseFloat(expression.get(expression.size() - DISCOUNT_VALUE_INDEX));
                String metric = expression.get(expression.size() - DISCOUNT_METRIC_INDEX);
                float discount = calculateDiscount(jsonNode.get("price").floatValue(), promoVal, metric);
                return new DiscountNode(Objects.toString(Math.round(discount)),
                        "get " + getDiscountTag(promoVal, metric) + " off");
            }
        } catch (Exception e) {
            LOGGER.error("Error at parseExpressionAndGetDiscount, Exception : ", e);
        }
        return null;
    }


}
