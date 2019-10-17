package cn.hengyumo.humor.cysz.user;

import cn.hengyumo.humor.base.cache.EhcacheCache;
import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.base.system.user.SystemAdmin;
import cn.hengyumo.humor.base.system.user.SystemUser;
import cn.hengyumo.humor.base.system.user.SystemUserLoginDto;
import cn.hengyumo.humor.base.system.user.data.SystemUserData;
import cn.hengyumo.humor.base.system.user.data.SystemUserDetailSearchDto;
import cn.hengyumo.humor.base.system.user.data.SystemUserDetailVo;
import cn.hengyumo.humor.base.utils.BaseUtil;
import cn.hengyumo.humor.base.utils.TokenUtil;
import cn.hengyumo.humor.base.validators.PhoneValidatorClass;
import cn.hengyumo.humor.cysz.exception.common.CyszUserException;
import cn.hengyumo.humor.cysz.exception.enums.CyszUserResultEnum;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String likeFormat(String s) {
        return String.format("%%%s%%", s);
    }

    private boolean notBlank(String s) {
        return s != null && ! s.equals("");
    }

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

    public Long countByCreateDateBetween(Date date1, Date date2) {
        return cyszUserDao.countByIsDeletedFalseAndCreateDateBetween(date1, date2);
    }
}
