package forbes.utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

public final class GsonUtils {

    private static final Gson GSON = new GsonBuilder().serializeSpecialFloatingPointValues().create();

    private GsonUtils() {
    }

    public static String toString(Object o) {
        return GSON.toJson(o);
    }

    public static <E> E fromJson(String jsonString, Class<E> type) {
        return GSON.fromJson(jsonString, type);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static String toJson(Object o) {
        return GSON.toJson(o);
    }


}

