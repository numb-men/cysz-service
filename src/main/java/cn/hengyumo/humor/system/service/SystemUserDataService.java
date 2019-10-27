package cn.hengyumo.humor.system.service;


import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.system.dto.SystemUserDetailDto;
import cn.hengyumo.humor.system.dto.SystemUserDetailSearchDto;
import cn.hengyumo.humor.system.entity.SystemRoleEntity;
import cn.hengyumo.humor.system.entity.SystemUser;
import cn.hengyumo.humor.system.entity.SystemUserData;
import cn.hengyumo.humor.system.exception.common.SystemException;
import cn.hengyumo.humor.system.exception.enums.SystemResultEnum;
import cn.hengyumo.humor.system.vo.SystemUserDetailVo;
import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SystemUserDataService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/9
 */
@Service
public class SystemUserDataService extends BaseService<SystemUserData, Long> {

    @Resource
    private SystemUserService systemUserService;

    @Resource
    private SystemRoleService systemRoleService;

    @Value("${hengyumo.base.system.default.password}")
    private String defaultPassword;


    public Page<SystemUserDetailVo> searchAndPaged(Integer pageNumber, Integer pageSize,
                                                   SystemUserDetailSearchDto searchDto) {
        Page<SystemUserDetailVo> page;
        Specification<SystemUserData> specification = createSearch(searchDto);
        if (pageSize > 0) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<SystemUserData> userPage = findAll(specification, pageable);
            List<SystemUserDetailVo> systemUserDetailVos = new ArrayList<>();
            List<SystemUserData> systemUserDatas = userPage.getContent();
            for (SystemUserData systemUserData : systemUserDatas) {
                SystemUser systemUser = systemUserData.getSystemUser();
                SystemUserDetailVo systemUserDetailVo = new SystemUserDetailVo();
                BeanUtils.copyProperties(systemUser, systemUserDetailVo);
                BeanUtils.copyProperties(systemUserData, systemUserDetailVo);
                systemUserDetailVo.setId(systemUser.getId());
                systemUserDetailVos.add(systemUserDetailVo);
            }
            page = new PageImpl<>(systemUserDetailVos, pageable, userPage.getTotalElements());
        }
        else {
            page = new PageImpl<>(detailList());
        }
        return page;
    }

    private String likeFormat(String s) {
        return String.format("%%%s%%", s);
    }

    private boolean notBlank(String s) {
        return s != null && ! s.equals("");
    }

    private Specification<SystemUserData> createSearch(SystemUserDetailSearchDto searchDto) {
        PredicateBuilder<SystemUserData> p1 = Specifications.<SystemUserData>and()
                .eq("systemUser.isDeleted", false)
                .eq("isDeleted", false);
        PredicateBuilder<SystemUserData> p2 = Specifications.or();
        if (notBlank(searchDto.getRealName())) {
            p2 = p2.like("realName", likeFormat(searchDto.getRealName()));
        }
        if (notBlank(searchDto.getEmail())) {
            p2 = p2.like("email", likeFormat(searchDto.getEmail()));
        }
        if (notBlank(searchDto.getMobile())) {
            p2 = p2.like("mobile", likeFormat(searchDto.getMobile()));
        }
        if (notBlank(searchDto.getRealName()) || notBlank(searchDto.getEmail())
                || notBlank(searchDto.getMobile())) {
            return p1.predicate(p2.build()).build();
        }
        return p1.build();
    }

    public SystemUserDetailVo save(SystemUserDetailDto systemUserDetailDto) {
        SystemUser systemUser = new SystemUser();
        systemUser.setName(systemUserDetailDto.getName());
        systemUser.setPassword(defaultPassword);
        Set<SystemRoleEntity> systemRoleEntities = new HashSet<>();
        for (long roleId : systemUserDetailDto.getRoles()) {
            SystemRoleEntity systemRoleEntity =
                    systemRoleService.findOne(roleId).orElse(null);
            if (systemRoleEntity == null) {
                throw new SystemException(SystemResultEnum.ROLE_NOT_FOUND);
            }
            systemRoleEntities.add(systemRoleEntity);
        }
        systemUser.setRoles(systemRoleEntities);
        SystemUserData systemUserData = new SystemUserData();
        BeanUtils.copyProperties(systemUserDetailDto, systemUserData);
        systemUser.setUserData(systemUserData);
        SystemUser systemUserSaved = systemUserService.save(systemUser);
        SystemUserDetailVo systemUserDetailVo = new SystemUserDetailVo();
        BeanUtils.copyProperties(systemUserSaved, systemUserDetailVo);
        BeanUtils.copyProperties(systemUserSaved.getUserData(), systemUserDetailVo);
        systemUserDetailVo.setId(systemUserSaved.getId());
        return systemUserDetailVo;
    }

    public SystemUserDetailVo update(Long id, SystemUserDetailDto systemUserDetailDto) {
        SystemUser systemUser = systemUserService.findOne(id).orElse(null);
        if (systemUser == null) {
            throw new SystemException(SystemResultEnum.USER_NOT_FOUND);
        }
        SystemUserData systemUserData = systemUser.getUserData();
        systemUser.setName(systemUserDetailDto.getName());
        Set<SystemRoleEntity> systemRoleEntities = new HashSet<>();
        for (long roleId : systemUserDetailDto.getRoles()) {
            SystemRoleEntity systemRoleEntity =
                    systemRoleService.findOne(roleId).orElse(null);
            if (systemRoleEntity == null) {
                throw new SystemException(SystemResultEnum.ROLE_NOT_FOUND);
            }
            systemRoleEntities.add(systemRoleEntity);
        }
        systemUser.setRoles(systemRoleEntities);
        BeanUtils.copyProperties(systemUserDetailDto, systemUserData);
        systemUser.setUserData(systemUserData);
        SystemUser systemUserSaved = systemUserService.save(systemUser);
        SystemUserDetailVo systemUserDetailVo = new SystemUserDetailVo();
        BeanUtils.copyProperties(systemUserSaved, systemUserDetailVo);
        BeanUtils.copyProperties(systemUserSaved.getUserData(), systemUserDetailVo);
        systemUserDetailVo.setId(systemUserSaved.getId());
        return systemUserDetailVo;
    }

    public List<SystemUserDetailVo> detailList() {
        List<SystemUserDetailVo> systemUserDetailVos = new ArrayList<>();
        List<SystemUser> systemUsers = systemUserService.findAll();
        for (SystemUser systemUser : systemUsers) {
            SystemUserData systemUserData = systemUser.getUserData();
            SystemUserDetailVo systemUserDetailVo = new SystemUserDetailVo();
            BeanUtils.copyProperties(systemUser, systemUserDetailVo);
            BeanUtils.copyProperties(systemUserData, systemUserDetailVo);
            systemUserDetailVo.setId(systemUser.getId());
            systemUserDetailVos.add(systemUserDetailVo);
        }
        return systemUserDetailVos;
    }

    public SystemUserDetailVo detail(Long id) {
        SystemUser systemUser = systemUserService.findOne(id).orElse(null);
        if (systemUser == null) {
            throw new SystemException(SystemResultEnum.USER_NOT_FOUND);
        }
        SystemUserData systemUserData = systemUser.getUserData();
        if (systemUserData == null) {
            systemUserData = new SystemUserData();
        }
        SystemUserDetailVo systemUserDetailVo = new SystemUserDetailVo();
        BeanUtils.copyProperties(systemUser, systemUserDetailVo);
        BeanUtils.copyProperties(systemUserData, systemUserDetailVo);
        systemUserDetailVo.setId(systemUser.getId());
        return systemUserDetailVo;
    }
}
