package cn.hengyumo.humor.base.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * PhoneValidatorClass
 * 实现手机号验证注解
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/4/26
 */
public class PhoneValidatorClass implements ConstraintValidator<PhoneValidate, String> {

    @Override
    public void initialize(PhoneValidate constraintAnnotation) {}

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return isPhone(str);
    }

    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(166)|(17[0135678])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }
}
