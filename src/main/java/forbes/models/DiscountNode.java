package forbes.models;

public class DiscountNode {
    private String amount = "0";
    private String discountTag = "No discounts";

    public DiscountNode() {
    }

    public DiscountNode(String amount, String discountTag) {
        this.amount = amount;
        this.discountTag = discountTag;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscountTag() {
        return discountTag;
    }

    public void setDiscountTag(String discountTag) {
        this.discountTag = discountTag;
    }


    @Override
    public String toString() {
        return "{" +
                "amount='" + amount + '\"' +
                ", discountTag='" + discountTag + '\'' +
                '}';
    }
}
