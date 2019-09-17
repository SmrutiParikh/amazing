package forbes.utils;

import org.apache.commons.lang3.StringUtils;

public class PromotionUtils {

    public static String getDiscountTag(float discount, String metric) {
        if (Constants.DISCOUNT_METRIC.PERCENTAGE.name().equalsIgnoreCase(metric)) {
            return Math.round(discount)+"%";
        }
        if (Constants.DISCOUNT_METRIC.INR.name().equalsIgnoreCase(metric)) {
            return "Rs "+ Math.round(discount);
        }
        return StringUtils.EMPTY;
    }

    public static float calculateDiscount(float price, float discount, String metric) {
        if (Constants.DISCOUNT_METRIC.PERCENTAGE.name().equalsIgnoreCase(metric)) {
            return (price * discount) / 100;
        }
        if (Constants.DISCOUNT_METRIC.INR.name().equalsIgnoreCase(metric)) {
            return discount;
        }
        return 0F;
    }
}
