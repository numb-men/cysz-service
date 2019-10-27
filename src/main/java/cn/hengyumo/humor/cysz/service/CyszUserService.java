package cn.hengyumo.humor.cysz.service;

import cn.hengyumo.humor.base.cache.EhcacheCache;
import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.base.validators.PhoneValidatorClass;
import cn.hengyumo.humor.cysz.dao.CyszUserDao;
import cn.hengyumo.humor.cysz.dto.CyszUserDetailDto;
import cn.hengyumo.humor.cysz.dto.CyszUserLoginDto;
import cn.hengyumo.humor.cysz.dto.CyszUserRegisterDto;
import cn.hengyumo.humor.cysz.dto.CyszUserSearchDto;
import cn.hengyumo.humor.cysz.entity.CyszCurrentUser;
import cn.hengyumo.humor.cysz.entity.CyszUser;
import cn.hengyumo.humor.cysz.exception.common.CyszUserException;
import cn.hengyumo.humor.cysz.exception.enums.CyszUserResultEnum;
import cn.hengyumo.humor.utils.BaseUtil;
import cn.hengyumo.humor.utils.TokenUtil;
import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * CyszUserService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/10/12
 */
@Slf4j
@Service
public class CyszUserService extends BaseService<CyszUser, Long> {

    @Resource
    private CyszUserDao cyszUserDao;

    @Resource
    private BaseUtil baseUtil;

    @Value("${hengyumo.base.cysz.default.password}")
    private String defaultPassword;

    /**
     * 登录
     *
     * @param cyszUserLoginDto 登录表单
     * @return token
     */
    public String login(CyszUserLoginDto cyszUserLoginDto) {
        // 检查手机号
        if (! PhoneValidatorClass.isPhone(cyszUserLoginDto.getMobile())) {
            throw new CyszUserException(CyszUserResultEnum.MOBILE_INVALID);
        }
        CyszUser cyszUser = cyszUserDao.findOneByMobile(cyszUserLoginDto.getMobile());
        if (cyszUser == null) {
            throw new CyszUserException(CyszUserResultEnum.MOBILE_EXISTS);
        }
        if (! cyszUser.getPassword().equals(cyszUserLoginDto.getPassword())) {
            throw new CyszUserException(CyszUserResultEnum.PASSWORD_ERROR);
        }
        return TokenUtil.createToken(cyszUser.getId(), cyszUser.getPassword());
    }

    /**
     * 注册
     *
     * @param cyszUserRegisterDto 注册表单
     * @return 新用户信息
     */
    public CyszUser register(CyszUserRegisterDto cyszUserRegisterDto) {
        // 验证码
        String captchaText = (String) EhcacheCache.getValue(
                "captchaCache", cyszUserRegisterDto.getCaptchaToken());
        if (captchaText == null) {
            throw new BaseException(BaseResultEnum.CAPTCHA_EXPIRE);
        }
        if (! captchaText.toLowerCase().equals(cyszUserRegisterDto.getCode().toLowerCase())) {
            throw new BaseException(BaseResultEnum.CAPTCHA_ERROR);
        }
        // 检查手机号
        if (! PhoneValidatorClass.isPhone(cyszUserRegisterDto.getMobile())) {
            throw new CyszUserException(CyszUserResultEnum.MOBILE_INVALID);
        }
        if (cyszUserDao.findAllByMobile(cyszUserRegisterDto.getMobile()).size() > 0) {
            throw new CyszUserException(CyszUserResultEnum.MOBILE_EXISTS);
        }
        if (cyszUserDao.findOneByUsername(cyszUserRegisterDto.getUsername()) != null) {
            throw new CyszUserException(CyszUserResultEnum.USERNAME_EXISTS);
        }

        CyszUser cyszUser = new CyszUser();
        BeanUtils.copyProperties(cyszUserRegisterDto, cyszUser);
        cyszUser.setBalance(0D);
        baseUtil.setCyszCurrentUser(cyszUser);

        return save(cyszUser);
    }

    /**
     * 用户查找和和分页
     *
     * @param pageNumber 页数
     * @param pageSize 页大小
     * @param searchDto 查找表单
     * @return 用户分页
     */
    public Page<CyszUser> searchAndPaged(Integer pageNumber, Integer pageSize,
                                                   CyszUserSearchDto searchDto) {
        Page<CyszUser> page;
        Specification<CyszUser> specification = createSearch(searchDto);
        if (pageSize > 0) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = findAll(specification, pageable);
        }
        else {
            page = new PageImpl<>(findAll());
        }
        return page;
    }

    /**
     * 格式化like语句
     *
     * @param s 参数
     * @return 格式化后的like语句
     */
    private String likeFormat(String s) {
        return String.format("%%%s%%", s);
    }

    /**
     * 判断字符串非空
     *
     * @param s 字符串
     * @return 字符串是否为空
     */
    private boolean notBlank(String s) {
        return s != null && ! s.equals("");
    }

    /**
     * 根据查询表单生成动态查询语句
     *
     * @param searchDto 查询表单
     * @return 动态查询
     */
    private Specification<CyszUser> createSearch(CyszUserSearchDto searchDto) {
        PredicateBuilder<CyszUser> p1 = Specifications.<CyszUser>and()
                .eq("isDeleted", false);
        PredicateBuilder<CyszUser> p2 = Specifications.or();
        if (notBlank(searchDto.getUsername())) {
            p2 = p2.like("username", likeFormat(searchDto.getUsername()));
        }
        if (notBlank(searchDto.getMobile())) {
            p2 = p2.like("mobile", likeFormat(searchDto.getMobile()));
        }
        if (notBlank(searchDto.getUsername()) || notBlank(searchDto.getMobile())) {
            return p1.predicate(p2.build()).build();
        }
        return p1.build();
    }

    /**
     * 保存h或者更新用户的详细信息
     *
     * @param cyszUserDetailDto 用户详细信息
     * @return 用户详细信息
     */
    public CyszUserDetailDto saveOrUpdateDetail(CyszUserDetailDto cyszUserDetailDto) {
        // 检查手机号
        if (! PhoneValidatorClass.isPhone(cyszUserDetailDto.getMobile())) {
            throw new CyszUserException(CyszUserResultEnum.MOBILE_INVALID);
        }

        if (cyszUserDetailDto.getId() == null) {
            if (cyszUserDao.findOneByUsername(cyszUserDetailDto.getUsername()) != null) {
                throw new CyszUserException(CyszUserResultEnum.USERNAME_EXISTS);
            }
            if (cyszUserDao.findAllByMobile(cyszUserDetailDto.getMobile()).size() > 0) {
                throw new CyszUserException(CyszUserResultEnum.MOBILE_EXISTS);
            }
            CyszUser cyszUser = new CyszUser();
            BeanUtils.copyProperties(cyszUserDetailDto, cyszUser);
            cyszUser.setPassword(defaultPassword);
            CyszUser cyszUserSaved = save(cyszUser);
            cyszUserDetailDto.setId(cyszUserSaved.getId());
            return cyszUserDetailDto;
        }
        else {
            CyszUser cyszUser = findOne(cyszUserDetailDto.getId()).orElse(null);
            if (cyszUser == null) {
                throw new BaseException(BaseResultEnum.USER_NOT_FIND);
            }
            CyszUser cyszUserFound =
                    cyszUserDao.findOneByUsername(cyszUserDetailDto.getUsername());
            if (cyszUserFound != null && ! cyszUserFound.getId().equals(cyszUser.getId())) {
                throw new CyszUserException(CyszUserResultEnum.USERNAME_EXISTS);
            }
            cyszUserFound = cyszUserDao.findOneByMobile(cyszUserDetailDto.getMobile());
            if (cyszUserFound != null && ! cyszUserFound.getId().equals(cyszUser.getId())) {
                throw new CyszUserException(CyszUserResultEnum.MOBILE_EXISTS);
            }
            cyszUser.setUsername(cyszUserDetailDto.getUsername());
            cyszUser.setBalance(cyszUserDetailDto.getBalance());
            cyszUser.setMobile(cyszUserDetailDto.getMobile());
            save(cyszUser);

            return cyszUserDetailDto;
        }
    }

    /**
     * 充值
     *
     * @param num 充值金额
     * @return 余额
     */
    public Double recharge(Double num) {
        CyszCurrentUser cyszCurrentUser = baseUtil.getCyszCurrentUser();
        CyszUser cyszUser = findOne(cyszCurrentUser.getId()).orElse(null);
        if (cyszUser == null) {
            throw new BaseException(BaseResultEnum.USER_NOT_FIND);
        }
        cyszUser.setBalance(cyszUser.getBalance() + num);
        save(cyszUser);
        return cyszUser.getBalance();
    }

    /**
     * 获取在一段日期之内的新增用户数
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 新增用户数
     */
    public Long countByCreateDateBetween(Date date1, Date date2) {
        return cyszUserDao.countByIsDeletedFalseAndCreateDateBetween(date1, date2);
    }
}
