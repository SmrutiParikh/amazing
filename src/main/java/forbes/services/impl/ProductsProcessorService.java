package forbes.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import forbes.models.ConversionRates;
import forbes.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import forbes.utils.Constants;
import forbes.utils.HttpUtils;
import forbes.utils.PropertyUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class ProductsProcessorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsProcessorService.class);

    private static String productsUrl;
    private static String conversionUrl;
    private static String baseCurrency;
    private static final ObjectMapper mapper = new ObjectMapper();

    private static void init() {
        productsUrl = PropertyUtils.getProperty(Constants.PRODUCTS_URL);
        conversionUrl = PropertyUtils.getProperty(Constants.CONVERSION_URL);
        baseCurrency = PropertyUtils.getProperty(Constants.BASE_CURRENCY);
        LOGGER.info("ProductsUrl : {}, conversionUrl : {}, baseCurrency : {}",
                productsUrl, conversionUrl, baseCurrency);
    }

    public static List<Product> processProducts() {
        init();
        try {
            List<Product> products = getProducts();

            ConversionRates conversionRates = getConversionRates();

            Map<String, Float> rates = conversionRates.getRates();

            products.forEach(product -> {
                if (!baseCurrency.equals(product.getCurrency())) {
                    float convertedPrice = getConvertedPrice(rates, product);
                    product.setPrice(convertedPrice);
                    product.setCurrency(baseCurrency);
                }
            });
            return products;

        } catch (Exception e) {
            LOGGER.error("Error at processProducts, Exception :", e);
        }
        return null;
    }

    private static ConversionRates getConversionRates() throws IOException {
        String response = HttpUtils.get(conversionUrl);
        requireNonNull(response);
        ConversionRates conversionRates = mapper.readValue(response, ConversionRates.class);
        requireNonNull(conversionRates);
        return conversionRates;
    }

    private static List<Product> getProducts() throws IOException {
        String response = HttpUtils.get(productsUrl);
        requireNonNull(response);
        List<Product> products = mapper.readValue(response, new TypeReference<List<Product>>() {
        });
        requireNonNull(products);
        return products;
    }

    private static float getConvertedPrice(Map<String, Float> rates, Product product) {
        return (rates.getOrDefault(baseCurrency, 1F) * product.getPrice()) /
                rates.getOrDefault(product.getCurrency(), 1F);
    }
}
