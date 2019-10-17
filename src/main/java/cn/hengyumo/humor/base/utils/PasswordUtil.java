package cn.hengyumo.humor.base.utils;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * PasswordUtil
 *
 * 密码生成
 * 读取用户信息文件(用户名，密码)>后端sha1(sha1(密码)+用户名)>后端随机生产16位salt>后端将16位salt按规则插入32位密码中生成48位密码
 *
 * 前端校验时生成的sha1密码
 * sha1(sha1(sha1(密码)+用户名)+验证码)
 * * 密码校验
 * 前端sha1密码>后端从48位密码按逆规则2取盐和32位密码>sha1(32位密码+验证码)>sha1密码按规则放入盐>两个32位密码判断是否相等
 *
 * 优点（用户信息(顶级权限)通过代码写死在文件里，数据库被黑黑客也拿不到后台权限；
 * 不需要单独存salt；
 * 数据库被爆了，黑客不知道代码中的规则也搞不出原密码；
 * 每次登录都要生成一个验证码，黑客不知道验证码获取的密码只能用一次，下一次登录这个验证码就会过期）
 *
 * 缺点（前端加密比较耗时；
 * 前端sha1密码可能会被拦截，黑客直接使用该sha1密码登录，使用https会安全一些）
 *
 * 注意（事先还是要做好防xss，csrf，sql注入）
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/7/29
 */
public class PasswordUtil {

    /**
     * md5加密
     *
     * @param plainText 明文
     * @return cipherText 密文
     */
    public static String md5(String plainText) {

        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] bytes = md.digest(plainText.getBytes());
            return new String(new Hex().encode(bytes));
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * sha1加密
     *
     * @param plainText 明文
     * @return cipherText 密文
     */
    public static String sha1(String plainText) {

        try {
            MessageDigest md = MessageDigest.getInstance("sha1");
            byte[] bytes = md.digest(plainText.getBytes());
            return new String(new Hex().encode(bytes));
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密工具
     * 如algorithm 为sha1
     * 则加密过程为 sha1(sha1(密码明文)+用户名)
     *
     * @param userName  用户名
     * @param passwordPlainText 密码明文
     * @param algorithm 算法 sha1、md5
     *
     * @return cipherText 密码密文
     * @throws NoSuchAlgorithmException 算法不存在
     */
    public static String createCipherText(String userName, String passwordPlainText, String algorithm)
            throws NoSuchAlgorithmException {

        String cipherText;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] bytes = md.digest(passwordPlainText.getBytes());
        cipherText = new String(new Hex().encode(bytes)) + userName;
        bytes = md.digest(cipherText.getBytes());
        cipherText = new String(new Hex().encode(bytes));

        return cipherText;
    }

    /**
     * 生成盐，随机字符串
     *
     * @param length 长度
     * @return salt
     */
    public static String createSalt(int length) {
        final String RANDOM_STRINGS = "1234567890zxcvbnmasdfghjklqwertyuiop1234567890" +
                "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();
        Supplier<Integer> supplier = () -> random.nextInt(RANDOM_STRINGS.length());

        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < length; i++) {
            salt.append(RANDOM_STRINGS.charAt(supplier.get()));
        }

        return salt.toString();
    }

    /**
     * 创建一个随机的加盐规则
     *
     * @param cipherTextLength 密文长度
     *
     * @return String rules
     */
    public static String createRule(int cipherTextLength) {

        final int SALT_LENGTH = 16;
        Random random = new Random();
        Supplier<Integer> supplier = () -> {
            // 2 -> cipherTextLength - 1
            return random.nextInt(cipherTextLength - 3) + 2;
        };
        // 规则长度 2 - > SALT_LENGTH - 1
        int rulesSize = random.nextInt(SALT_LENGTH - 3) + 2;
        List<String> rules = new ArrayList<>();

        for (int i = 0; i < rulesSize; i++) {
            String num = Integer.toString(supplier.get());
            if (! rules.contains(num)) {
                rules.add(num);
            }
        }

        return String.join(",", rules);
    }

    /**
     * 将字符串规则解析为排序好的List<Integer>规则
     *
     * @param rule 字符串规则
     * @return List<Integer>规则
     */
    public static List<Integer> parseRule(String rule) {
        // "2,4,6" -> List<2, 4, 6>
        return Arrays.stream(rule.split(","))
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * 根据规则给密文加盐
     *
     * @param cipherText 密文
     * @param rule 规则 插入的坐标 2,4,6
     *
     * @return cipherTextWithSalt 加过盐的密文
     * @throws Exception rule 不符合要求
     */
    public static String addSalt(String cipherText, String rule) throws Exception {

        final int SALT_LENGTH = 16;
        List<Integer> rules = parseRule(rule);
        int max = rules.stream().max(Integer::compareTo).orElse(cipherText.length());
        if (SALT_LENGTH < rules.size() || max >= cipherText.length() || rules.size() < 2) {
            throw new Exception("rule 不符合要求");
        }

        String salt = createSalt(SALT_LENGTH);
        // "2,4,6".length = 3; 16 / 3 = 5 ; 16 % 3 = 1
        int insertLength = SALT_LENGTH / rules.size();

        // 2  4  6  <- index
        // 5  5  6  <- salt length
        // 2 5 2 5 2 6 26(32-6)  <- cipherTextWithSalt
        String[] strs1 = new String[rules.size() + 1];
        String[] strs2 = new String[rules.size()];
        int t1 = 0, t2 = 0;
        for (int i = 0; i < rules.size(); i++) {
            t2 = rules.get(i);
            strs1[i] = cipherText.substring(t1, t2);
            t1 = t2;
            if (i == rules.size() - 1) {
                strs1[i + 1] = cipherText.substring(t1, cipherText.length());
                strs2[i] = salt.substring(i * insertLength, SALT_LENGTH);
                break;
            }
            strs2[i] = salt.substring(i * insertLength, (i + 1) * insertLength);
        }

        StringBuilder cipherTextWithSalt = new StringBuilder();
        for (int i = 0; i < rules.size(); i++) {
            cipherTextWithSalt.append(strs1[i]);
            cipherTextWithSalt.append(strs2[i]);
            if (i == rules.size() - 1) {
                cipherTextWithSalt.append(strs1[i + 1]);
            }
        }

        return cipherTextWithSalt.toString();
    }

    /**
     * 根据规则从加过盐的密文中取出盐和原始密文
     *
     * @param cipherTextWithSalt 加过盐的密文
     * @param rule 规则
     *
     * @return Map<salt, cipherText> 盐和原始密文
     * @throws Exception rule 不符合要求
     */
    public static Map<String, String> parseCipherTextWithSalt(String cipherTextWithSalt, String rule)
            throws Exception {

        final int SALT_LENGTH = 16;
        List<Integer> rules = parseRule(rule);
        int cipherTextLength = cipherTextWithSalt.length() - SALT_LENGTH;
        int max = rules.stream().max(Integer::compareTo).orElse(cipherTextLength);
        if (SALT_LENGTH < rules.size() || max >= cipherTextLength || rules.size() < 2) {
            throw new Exception("rule 不符合要求");
        }

        // 根据规则取盐
        int insertLength = SALT_LENGTH / rules.size();
        String[] strs = new String[rules.size() * 2 + 1];
        // 2  4  6  <- index
        // 5  5  6  <- salt length
        // 2 5 2 5 2 6 26(32-6)  <- cipherTextWithSalt

        int t1 = 0, t2 = 0, t = 0, k = 0, z = 0;
        for (int i = 0; i < rules.size(); i++) {
            t2 = rules.get(i);
            k = t2 - t1; // 2 2 2
            strs[t++] = cipherTextWithSalt.substring(z, z + k); // 0-2
            z += k;
            if (i == rules.size() - 1) {
                int lastLength = insertLength + SALT_LENGTH % rules.size(); // 末尾
                strs[t++] = cipherTextWithSalt.substring(z, z + lastLength);
                z += lastLength;
                strs[t] = cipherTextWithSalt.substring(z, cipherTextWithSalt.length());
                break;
            }
            strs[t++] = cipherTextWithSalt.substring(z, z + insertLength);  // 2 - 7
            z += insertLength;

            t1 = t2;
        }

        Map<String, String> map = new HashMap<>();
        StringBuilder salt = new StringBuilder();
        StringBuilder cipherText = new StringBuilder();
        int u = 0;
        for (int i = 0; i < rules.size(); i++) {
            cipherText.append(strs[u++]);
            salt.append(strs[u++]);
            if (i == rules.size() - 1) {
                cipherText.append(strs[u]);
            }
        }

        map.put("salt", salt.toString());
        map.put("cipherText", cipherText.toString());

        return map;
    }

    /**
     * 根据加盐规则，来校验密码是否正确
     *
     * @param passwordForCheck 待校验密码
     * @param cipherTextWithSalt 加盐的实际密码
     * @param rule 规则
     *
     * @return boolean 是否正确
     * @throws Exception rule 不符合要求
     */
    public static boolean verify(String passwordForCheck, String cipherTextWithSalt, String rule)
            throws Exception {

        Map<String, String> map = parseCipherTextWithSalt(cipherTextWithSalt, rule);
        String cipherText = map.get("cipherText");

        return passwordForCheck.equals(cipherText);
    }

    /**
     * 根据校验码和加盐规则，来校验密码是否正确
     *
     * @param passwordForCheck 待校验密码
     * @param cipherTextWithSalt 加盐的实际密码
     * @param code 验证码
     * @param algorithm 前端验证码加密算法
     * @param rule 规则
     *
     * @return boolean 是否正确
     * @throws Exception rule 不符合要求
     */
    public static boolean verifyWithCode(String passwordForCheck, String cipherTextWithSalt,
                                         String code, String algorithm, String rule) throws Exception {

        Map<String, String> map = parseCipherTextWithSalt(cipherTextWithSalt, rule);
        String cipherText = map.get("cipherText") + code;
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] bytes = md.digest(cipherText.getBytes());
        cipherText = new String(new Hex().encode(bytes));

        return passwordForCheck.equals(cipherText);
    }

    public static void main(String[] args) throws Exception {
        // 测试生成密文
        String cipherText = createCipherText("le", "1234", "md5");
        System.out.println(cipherText);

        // 测试生成盐
        System.out.println(createSalt(16));
        System.out.println(createSalt(16));
        System.out.println(createSalt(16));

        // 测试sha1加盐
        String sha1CipherTextWithSalt = addSalt(
                createCipherText("le", "1234", "sha1"),
                "2,4,6");
        System.out.println(sha1CipherTextWithSalt);

        // 测试sha1取盐
        System.out.println(parseCipherTextWithSalt(sha1CipherTextWithSalt, "2,4,6"));

        // 测试md5加盐
        String md5CipherTextWithSalt = addSalt(
                createCipherText("le", "1234", "md5"),
                "2,6,4");
        System.out.println(md5CipherTextWithSalt);

        // 测试md5取盐
        System.out.println(parseCipherTextWithSalt(md5CipherTextWithSalt, "2,4,6"));

        // 测试二进制编码
        byte[] bytes = "hello".getBytes();
        System.out.println(new String(bytes));

        // 测试普通验证
        String cipherText2 = sha1(sha1("1234") + "le");
        String cipherText3 = createCipherText("le", "1234", "sha1");
        cipherText3 = addSalt(cipherText3, "3,6,12,35,21,19");
        assert cipherText2 != null;
        System.out.println("common verify: " + verify(cipherText2, cipherText3, "3,6,12,35,21,19"));

        // 测试验证码加密验证
        // 前端密文 sha1(sha1(sha1(密码)+用户名)+验证码)
        String frontCipherText = sha1(sha1(sha1("1234") + "le") + "code12as");
        System.out.println(frontCipherText);
        // 后端密文
        String backCipherTextWithSalt = addSalt(
                createCipherText("le", "1234", "sha1"),
                "2,4,15,29,31,35");
        System.out.println(backCipherTextWithSalt);
        // 校验
        assert frontCipherText != null;
        System.out.println("verify with code: " +
                verifyWithCode(frontCipherText, backCipherTextWithSalt,
                        "code12as", "sha1", "2,4,15,29,31,35"));

        // 测试创建随机加盐规则
        System.out.println(createRule(40));
        System.out.println(createRule(40));
        System.out.println(createRule(40));
        System.out.println(createRule(40));
        System.out.println(createRule(40));
    }
}
