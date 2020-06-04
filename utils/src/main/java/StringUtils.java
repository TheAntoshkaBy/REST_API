import org.apache.commons.lang3.math.NumberUtils;

public class StringUtils {
    public static boolean isPositiveNumber(String str){
        int number = NumberUtils.toInt(str);
        return number >= 0;
    }

}
