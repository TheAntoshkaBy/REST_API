public class Utils {
    public static boolean isAllPositiveNumbers(String... str){

        for(int i = 0; i < str.length; i++){
            if(!StringUtils.isPositiveNumber(str[i])){
                return false;
            }
        }
        return true;
    }
}
