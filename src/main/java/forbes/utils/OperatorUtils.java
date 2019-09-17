package forbes.utils;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.math.NumberUtils;

public final class OperatorUtils {

    public static boolean evalExpression(List<String> expression, JsonNode jsonNode, int evalIndex) {
        Object operandVal = jsonNode.get(expression.get(evalIndex));
        return evalOperation(expression.get(evalIndex + 1),
                Objects.toString(operandVal).toLowerCase(),
                expression.get(evalIndex + 2).toLowerCase());
    }

    public static boolean evalOperation(String stringOperator, String operand1, String operand2){
        try {
            Constants.OPERATOR operator = Constants.OPERATOR.valueOf(stringOperator);
            operand1 = operand1.replace("\"", "").replace("\'", "");
            operand2 = operand2.replace("\"", "").replace("\'", "");

            switch (operator) {
                case AND:
                    return evaluateAndOperator(operand1, operand2);
                case OR:
                    return evaluateOrOperator(operand1, operand2);
                case EQ:
                    return evaluateEqualsOperator(operand1, operand2);
                case GT:
                    return evaluateGTOperator(operand1, operand2, false);
                case GTE:
                    return evaluateGTOperator(operand1, operand2, true);
                case LT:
                    return evaluateLTOperator(operand1, operand2, false);
                case LTE:
                    return evaluateLTOperator(operand1, operand2, true);
                case NEQ:
                    return !evaluateEqualsOperator(operand1, operand2);
                case BETWEEN:
                    return evaluateBetweenOperator(operand1, operand2);
                case NONEOF:
                    return evaluateNoneOfOperator(operand1, operand2);
                case ALLOF:
                    return evaluateAllOfOperator(operand1, operand2);
                case ANYOF:
                case IN:
                    return evaluateAnyOfOperator(operand1, operand2);

                default:
                    return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    private static boolean evaluateAnyOfOperator(String operand1, String operand2) {
        if(!NumberUtils.isParsable(operand1)){
            List<String> array = parseList(operand2);
            return array.contains(operand1.trim().toLowerCase());
        }
        List<Float> array = parseList(operand2).stream().map(Float::parseFloat).collect(Collectors.toList());
        float var1 = Float.parseFloat(operand1);
        return array.contains(var1);
    }

    private static boolean evaluateAllOfOperator(String operand1, String operand2) {
        List<String> array = parseList(operand2);
        List<String> value = parseList(operand1);
        if(!NumberUtils.isParsable(array.get(0))) {
            return array.containsAll(value);
        }
        List<Float> array1 = array.stream().map(Float::parseFloat).collect(Collectors.toList());
        List<Float> value1 = value.stream().map(Float::parseFloat).collect(Collectors.toList());
        return array1.containsAll(value1);
    }

    private static boolean evaluateNoneOfOperator(String operand1, String operand2) {
        List<String> array = parseList(operand2);
        List<String> value = parseList(operand1);
        if(NumberUtils.isParsable(array.get(0)) || NumberUtils.isParsable(value.get(0)) ) {
            List<Float> array1 = array.stream().map(Float::parseFloat).collect(Collectors.toList());
            List<Float> value1 = value.stream().map(Float::parseFloat).collect(Collectors.toList());
            for(Float val : value1){
                if(array1.contains(val)){
                    return false;
                }
            }
            return true;
        }
        else {
            for (String val : value) {
                if (array.contains(val)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static boolean evaluateBetweenOperator(String operand1, String operand2) {
        List<String> array = parseList(operand2);
        if( array.size() > 2){
            return false;
        }
        Float startRange = Float.parseFloat(array.get(0));
        Float endRange = Float.parseFloat(array.get(1));
        Float value = Float.parseFloat(operand1);
        return value > startRange && value < endRange;
    }

    private static List<String> parseList(String operand){
        String[] split = operand
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "")
                .replace("\"", "")
                .replace("\'", "")
                .toLowerCase()
                .split(Constants.PROMOTIONS_ARRAY_SEPARATOR);
        if (split.length == 0) {
            return new ArrayList<>(0);
        }
        return new ArrayList<>(Arrays.asList(split));
    }
    private static boolean evaluateGTOperator(String operand1, String operand2, boolean isEqualCheck) {
        Float var1 = Float.parseFloat(operand1);
        Float var2 = Float.parseFloat(operand2);
        if(isEqualCheck){
            return Objects.equals(var1, var2) || var1 > var2;
        }
        return var1 > var2;
    }

    private static boolean evaluateLTOperator(String operand1, String operand2, boolean isEqualCheck) {
        Float var1 = Float.parseFloat(operand1);
        Float var2 = Float.parseFloat(operand2);
        if(isEqualCheck){
            return Objects.equals(var1, var2) || var1 < var2;
        }
        return var1 < var2;
    }

    private static boolean evaluateEqualsOperator(String operand1, String operand2) {
        if(NumberUtils.isParsable(operand1)){
            float var1 = Float.parseFloat(operand1);
            float var2 = Float.parseFloat(operand2);
            return Objects.equals(var1, var2);
        }
        return Objects.equals(operand1, operand2);
    }

    private static boolean evaluateOrOperator(String operand1, String operand2) {
        return Boolean.valueOf(operand1) || Boolean.valueOf(operand2);
    }

    private static boolean evaluateAndOperator(String operand1, String operand2) {
        return Boolean.valueOf(operand1) && Boolean.valueOf(operand2);
    }
}
