package cn.hengyumo.humor.base.system.role;


import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.base.system.exception.common.SystemException;
import cn.hengyumo.humor.base.system.exception.enums.SystemResultEnum;
import cn.hengyumo.humor.base.system.resource.SystemResourceEntity;
import cn.hengyumo.humor.base.system.resource.SystemResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SystemRoleService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/11
 */
@Service
public class SystemRoleService extends BaseService<SystemRoleEntity, Long> {

    @Resource
    private SystemResourceService systemResourceService;

    private SystemRoleEntity systemRoleEntity;

    private SystemResourceEntity systemResourceEntity;

    public SystemRoleEntity create(SystemRoleDto systemRoleDto) {
        SystemRoleEntity systemRoleEntity = new SystemRoleEntity();
        systemRoleEntity.setName(systemRoleDto.getName());
        systemRoleEntity.setIcon(systemRoleDto.getIcon());
        Set<SystemResourceEntity> systemResourceEntities = new HashSet<>();
        for (Long resourceId : systemRoleDto.getResourcesId()) {
            SystemResourceEntity systemResourceEntity =
                    systemResourceService.findOne(resourceId).orElse(null);
            if (systemResourceEntity == null) {
                throw new SystemException(SystemResultEnum.RESOURCE_NOT_FOUND);
            }
            systemResourceEntities.add(systemResourceEntity);
        }
        systemRoleEntity.setResources(systemResourceEntities);
        return save(systemRoleEntity);
    }

    public SystemRoleEntity update(Long id, SystemRoleDto systemRoleDto) {
        SystemRoleEntity systemRoleEntity = findOne(id).orElse(null);
        if (systemRoleEntity == null) {
            throw new SystemException(SystemResultEnum.ROLE_NOT_FOUND);
        }
        systemRoleEntity.setName(systemRoleDto.getName());
        systemRoleEntity.setIcon(systemRoleDto.getIcon());
        Set<SystemResourceEntity> systemResourceEntities = new HashSet<>();
        for (Long resourceId : systemRoleDto.getResourcesId()) {
            SystemResourceEntity systemResourceEntity =
                    systemResourceService.findOne(resourceId).orElse(null);
            if (systemResourceEntity == null) {
                throw new SystemException(SystemResultEnum.RESOURCE_NOT_FOUND);
            }
            systemResourceEntities.add(systemResourceEntity);
        }
        systemRoleEntity.setResources(systemResourceEntities);
        return save(systemRoleEntity);
    }

    public SystemRoleVo view(Long id) {
        SystemRoleEntity systemRoleEntity = findOne(id).orElse(null);
        if (systemRoleEntity == null) {
            throw new SystemException(SystemResultEnum.RESOURCE_NOT_FOUND);
        }
        SystemRoleVo systemRoleVo = new SystemRoleVo();
        BeanUtils.copyProperties(systemRoleEntity, systemRoleVo);
        List<Long> resourcesId = new ArrayList<>();
        for (SystemResourceEntity systemResourceEntity : systemRoleEntity.getResources()) {
            resourcesId.add(systemResourceEntity.getId());
        }
        systemRoleVo.setResourcesId(resourcesId);
        return systemRoleVo;
    }

    public void addResource(Long roleId, Long resourceId) {
        setRoleAndResource(roleId, resourceId);
        systemRoleEntity.getResources().add(systemResourceEntity);
        save(systemRoleEntity);
    }

    public void deleteResource(Long roleId, Long resourceId) {
        setRoleAndResource(roleId, resourceId);
        systemRoleEntity.getResources().remove(systemResourceEntity);
        save(systemRoleEntity);
    }

    private void setRoleAndResource(Long roleId, Long resourceId) {
        systemRoleEntity = findOne(roleId).orElse(null);
        if (systemRoleEntity == null) {
            throw new SystemException(SystemResultEnum.ROLE_NOT_FOUND);
        }
        systemResourceEntity = systemResourceService.findOne(resourceId).orElse(null);
        if (systemResourceEntity == null) {
            throw new SystemException(SystemResultEnum.RESOURCE_NOT_FOUND);
        }
    }
}
