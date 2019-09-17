package forbes.utils;

public class Constants {
    public static final String OUTPUT_FILE_PATH = "output.file.path";
    public static final String APP_PROPERTIES = "app.properties";
    public static final String PRODUCTS_URL = "products.url";
    public static final String CONVERSION_URL = "conversion.url";
    public static final String BASE_CURRENCY = "base.currency";
    public static final String PROMOTION_SETS = "promotion.sets";
    public static final String SPECIAL_PROMOTION = "special.promotion.set";
    public static final String PROMOTIONS_ARRAY_SEPARATOR = "_";
    public static final int DISCOUNT_VALUE_INDEX = 2;
    public static final int DISCOUNT_METRIC_INDEX = 1;


    public enum OPERATOR {
        EQ ("EQ", "String"),
        GT("GT", "Float"),
        LT ("LT", "Float"),
        LTE("LTE", "Float"),
        GTE("GTE", "Float"),
        IN("IN", "Array"),
        AND("AND", "Boolean"),
        OR ("OR", "Boolean"),
        NEQ("NEQ", "String"),
        BETWEEN("BETWEEN", "Range"),
        NONEOF("NONEOF", "Array"),
        ALLOF("ALLOF", "Array"),
        ANYOF("ANYOF", "Array");

        private String operator;
        private String type;
        OPERATOR(String operator, String type) {
            this.operator =operator;
            this.type = type;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public enum DISCOUNT_METRIC {
        PERCENTAGE, INR
    }
}
