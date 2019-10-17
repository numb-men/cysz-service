package cn.hengyumo.humor.base.system.dict;

import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.base.system.config.SystemConfig;
import cn.hengyumo.humor.base.system.config.SystemDict;
import cn.hengyumo.humor.base.system.config.SystemDictItem;
import cn.hengyumo.humor.base.system.config.UseSystemDict;
import cn.hengyumo.humor.base.system.dict.item.SystemDictItemEntity;
import cn.hengyumo.humor.base.system.dict.item.SystemDictItemService;
import cn.hengyumo.humor.base.system.exception.common.SystemException;
import cn.hengyumo.humor.base.system.exception.enums.SystemResultEnum;
import cn.hengyumo.humor.base.utils.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * SystemDictService
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/18
 */
@Service
@Slf4j
public class SystemDictService extends BaseService<SystemDictEntity, Long> implements SystemConfig {

    @Value("${hengyumo.dict.scan}")
    private String basePackageScan;

    private List<Class<?>> dataClasses;

    private List<SystemDict> systemDictList;

    private List<SystemDictEntity> systemDictEntityList;

    @Resource
    private SystemDictDao systemDictDao;

    @Resource
    private SystemDictItemService systemDictItemService;


    @Cacheable(cacheNames = "cache", key = "#root.method.returnType + '_' + #name")
    public SystemDictEntity findByName(String name) {
        SystemDictEntity systemDictEntity = systemDictDao.findByName(name);
        if (systemDictEntity == null) {
            log.warn("dict with name '" + name +
                    "' not found, may be you forgot create it");
            throw new SystemException(SystemResultEnum.DICT_NOT_FOUND);
        }
        return systemDictEntity;
    }

    public void deleteDictByIdCascade(Long id) {
        SystemDictEntity systemDictEntity = findOne(id).orElse(null);
        if (systemDictEntity == null) {
            throw new SystemException(SystemResultEnum.DICT_NOT_FOUND);
        }
        List<SystemDictItemEntity> systemDictItemEntities = systemDictEntity.getChildren();
        for (SystemDictItemEntity systemDictItemEntity : systemDictItemEntities) {
            systemDictItemService.delete(systemDictItemEntity.getId());
        }
        delete(id);
    }

    public SystemDictItemEntity addOrUpdateDictItem(Long id, SystemDictItemEntity systemDictItemEntity) {
        systemDictItemEntity = systemDictItemService.save(systemDictItemEntity);
        SystemDictEntity systemDictEntity = findOne(id).orElse(null);
        if (systemDictEntity == null) {
            throw new SystemException(SystemResultEnum.DICT_NOT_FOUND);
        }
        List<SystemDictItemEntity> itemEntities = systemDictEntity.getChildren();
        for (int i = 0; i < itemEntities.size(); i ++) {
            SystemDictItemEntity itemEntity = itemEntities.get(i);
            if (itemEntity.getId().equals(systemDictEntity.getId())) {
                itemEntities.set(i, itemEntity);
                save(systemDictEntity);
                return systemDictItemEntity;
            }
        }
        itemEntities.add(systemDictItemEntity);
        save(systemDictEntity);
        return systemDictItemEntity;
    }

    public void createDict() {
        dataClasses = getDataClass();
        if (dataClasses.size() == 0) {
            log.warn("Use system dict class not found");
            return;
        }
        createSystemDictList();
        if (systemDictList.size() == 0) {
            log.warn("don't find systemDict");
            return;
        }
        createSystemDictEntityList();
        checkDeclare();
        saveSystemDictEntityList();
    }

    public void clearDict() {
        systemDictItemService.clear();
        baseDao.deleteAll();
    }

    // 扫描获取所有Data
    private List<Class<?>> getDataClass() {
        return ClassUtil.getClassListByAnnotation(
                basePackageScan, UseSystemDict.class);
    }

    private void createSystemDictList() {
        systemDictList = new ArrayList<>();
        for (Class clazz : dataClasses) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(SystemDict.class)) {
                    systemDictList.add(field.getAnnotation(SystemDict.class));
                }
            }
        }
    }

    private void createSystemDictEntityList() {
        systemDictEntityList = new ArrayList<>();
        for (SystemDict systemDict : systemDictList) {
            if (systemDict.items().length != 0) {
                SystemDictEntity systemDictEntity = new SystemDictEntity();
                systemDictEntity.setName(systemDict.name());
                if (systemDict.dictIcon().equals("")) {
                    systemDictEntity.setIcon(DEFAULT_DICT_ICON);
                } else {
                    systemDictEntity.setIcon(systemDict.dictIcon());
                }
                if (systemDict.comment().equals("")) {
                    systemDictEntity.setComment(systemDict.name());
                } else {
                    systemDictEntity.setComment(systemDict.comment());
                }
                List<SystemDictItemEntity> systemDictItemEntityList = new ArrayList<>();
                int index = 1;
                for (SystemDictItem systemDictItem : systemDict.items()) {
                    SystemDictItemEntity systemDictItemEntity = new SystemDictItemEntity();
                    systemDictItemEntity.setValueIndex(index ++);
                    systemDictItemEntity.setValue(systemDictItem.value());
                    systemDictItemEntity.setOrderBy(0);
                    if (systemDictItem.icon().equals("")) {
                        systemDictItemEntity.setIcon(DEFAULT_DICT_ITEM_ICON);
                    } else {
                        systemDictItemEntity.setIcon(systemDictItem.icon());
                    }
                    systemDictItemEntityList.add(systemDictItemEntity);
                }
                systemDictEntity.setOrderBy(0);
                systemDictEntity.setChildren(systemDictItemEntityList);
                systemDictEntityList.add(systemDictEntity);
            }
        }
    }

    private void checkDeclare() {
        // 判断是否有的字典未声明过
        for (SystemDict systemDict : systemDictList) {
            if (systemDict.items().length == 0) {
                boolean found = false;
                for (SystemDictEntity systemDictEntity : systemDictEntityList) {
                    if (systemDictEntity.getName().equals(systemDict.name())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    log.warn(systemDict.name() + " is not declare");
                }
            }
        }
    }

    @Transactional
    public void saveSystemDictEntityList() {
        for (SystemDictEntity systemDictEntity : systemDictEntityList) {
            // 先清除旧的
            if (findByName(systemDictEntity.getName()) != null) {
                systemDictDao.deleteByName(systemDictEntity.getName());
            }
            save(systemDictEntity);
        }
    }
}
