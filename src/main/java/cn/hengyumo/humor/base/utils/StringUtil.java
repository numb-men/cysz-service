package cn.hengyumo.humor.base.utils;

import java.util.List;
import java.util.Random;

/**
 * StringUtil
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/8/2
 */
public class StringUtil {

    /**
     * 将List<String>使用分隔符分隔并合并
     * @author hengyumo
     * @since 2019/4/23
     *
     * @param list 要处理的List<String>
     * @param separator 分隔符
     * @return String 处理好的字符串
     */
    public static String listToString(List<String> list, char separator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append(s).append(separator);
        }
        return stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
    }

    /**
     * 返回由大小写字母和数字组成的随机字符串
     * @author hengyumo
     * @since 2019/4/26
     *
     * @param length 所需随机字符串的长度
     * @return String 生成的字符串
     */
    public static String getRandomString(int length){
        String str = "1234567890zxcvbnmasdfghjklqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM" +
                "`~!@#$%^&*()_+-=<>?:\"'\\;.,/|{}\n\t ";
        return getRandomString(str, length);
    }

    /**
     * 根据传入的字符串、长度生成随机字符串
     *
     * @param str 母字符串
     * @param length 需要的随机字符串的长度
     * @return 生成的随机字符串
     */
    public static String getRandomString(String str, int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < length; i++){
            int number = random.nextInt(str.length());
            stringBuilder.append(str.charAt(number));
        }
        return stringBuilder.toString();
    }

    /**
     * 根据要求的类型和长度生成对应的随机字符串
     *
     * @param length 长度
     * @param type 类型
     *
     * normal       大小写字母、数字、加其他字符
     * letter       大小写字母
     * lowercase    小写字母
     * uppercase    大写字母
     * digit        数字
     * param        符合变量名规则
     *
     * @return 随机字符串
     */
    public static String getRandomString(int length, String type) {
        String str;
        switch (type) {
            case "normal":
                return getRandomString(length);
            case "letter":
                str = "zxcvbnmasdfghjklqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM";
                return getRandomString(str, length);
            case "lowercase":
                str = "zxcvbnmasdfghjklqwertyuiop";
                return getRandomString(str, length);
            case "uppercase":
                str = "QWERTYUIOPASDFGHJKLZXCVBNM";
                return getRandomString(str, length);
            case "digit":
                str = "1234567890";
                return getRandomString(str, length);
            case "param":
                return getRandomParamName(length);
            default:
                return null;
        }
    }

    /**
     * 生成一个符合长度要求的随机变量名
     *
     * @param length 长度
     * @return 变量名
     */
    public static String getRandomParamName(int length) {
        String startStr = "zxcvbnmasdfghjklqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM_$";
        String bodyStr = "1234567890zxcvbnmasdfghjklqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM_";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startStr.charAt(random.nextInt(startStr.length())));
        for (int i = 0; i < length - 1; i++) {
            int number = random.nextInt(bodyStr.length());
            stringBuilder.append(bodyStr.charAt(number));
        }
        return stringBuilder.toString();
    }

    /**
     * 判断字符串是否符合变量名类型
     *
     * @param str 字符串
     * @return 是否符合变量名类型
     */
    public static boolean matchParamName(String str) {
        return str.matches("^[a-zA-Z\\$_][a-zA-Z\\d_]*$");
    }

    /**
     * 校验密码
     * 含且仅含大小写字母及数字且长度在8-255位
     * @author hengyumo
     * @since 2019/4/29
     *
     * @param str 待判断的字符串
     * @return boolean
     */
    public static boolean isGoodPassword(String str) {
        // 定义一个boolean值，用来表示是否包含数字
        boolean isDigit = false;
        // 定义一个boolean值，用来表示是否包含字母
        boolean isLetter = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                // 用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLetter(str.charAt(i))) {
                // 用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]{8,255}$";

        return isDigit && isLetter && str.matches(regex);
    }

    /**
     * 返回首字母大写的字符串
     *
     * @param s 字符串
     * @return 首字母大写
     */
    public static String upperFirst(String s) {
        return String.valueOf(s.charAt(0)).toUpperCase() + s.substring(1);
    }

    /**
     * 将驼峰字符串改成指定分隔符分隔的字符串
     *
     * @param s 字符串
     * @param symbol 分隔符
     * @return 字符串
     */
    public static String camelCaseToSymbol(String s, String symbol) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Character c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                stringBuilder.append(symbol);
                stringBuilder.append(Character.toLowerCase(c));
            }
            else {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
