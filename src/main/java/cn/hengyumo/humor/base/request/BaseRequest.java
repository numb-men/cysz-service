package cn.hengyumo.humor.base.request;

/**
 * BaseRequest
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
public interface BaseRequest {

    Integer MISSING_PARAM = 601;
    Integer MISSING_REQUEST_PART = 602;
    Integer PARAM_VALIDATE_FAIL = 603;
    Integer FORM_VALIDATE_FAIL = 604;
    Integer HTTP_REQUEST_METHOD_NOT_SUPPORTED = 605;
    Integer METHOD_ARGUMENT_TYPE_MISMATCH = 606;
    Integer MULTIPART_EXCEPTION = 607;
    Integer JSON_VALIDATE_FAIL = 608;
}
