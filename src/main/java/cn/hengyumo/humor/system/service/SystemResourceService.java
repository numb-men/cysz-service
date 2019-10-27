package cn.hengyumo.humor.system.service;


import cn.hengyumo.humor.base.exception.common.BaseException;
import cn.hengyumo.humor.base.exception.enums.BaseResultEnum;
import cn.hengyumo.humor.base.mvc.BaseService;
import cn.hengyumo.humor.system.annotation.SystemResource;
import cn.hengyumo.humor.system.annotation.SystemResourceClass;
import cn.hengyumo.humor.system.config.SystemConfig;
import cn.hengyumo.humor.system.dao.SystemResourceDao;
import cn.hengyumo.humor.system.entity.SystemResourceEntity;
import cn.hengyumo.humor.system.enums.SystemResourceType;
import cn.hengyumo.humor.system.exception.common.SystemException;
import cn.hengyumo.humor.system.exception.enums.SystemResultEnum;
import cn.hengyumo.humor.utils.ClassUtil;
import cn.hengyumo.humor.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * SystemResourceService
 *
 * 系统资源服务、通过反射，生成系统资源
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/10
 */
@Service
@Slf4j
@SuppressWarnings("Duplicates")
public class SystemResourceService extends BaseService<SystemResourceEntity, Long>
                                                    implements SystemConfig {

    @Resource
    private SystemResourceDao systemResourceDao;

    private Map<RequestMappingInfo, HandlerMethod> mappingMethodMap;

    private List<Class<?>> parentClasses;

    private int defaultSystemResourceNum;

    @Value("${hengyumo.resource.scan}")
    private String basePackageScan;

    @Cacheable(cacheNames = "cache", key = "#root.method.returnType")
    public List<SystemResourceEntity> getSystemResourceEntities() {
        return systemResourceDao.findAll();
    }

    // 级联
    public void deleteResourcesByIdCascade(Long id) {
        List<SystemResourceEntity> resourceEntities = getResourcesByIdCascade(id);
        for (SystemResourceEntity systemResourceEntity : resourceEntities) {
            delete(systemResourceEntity.getId());
        }
    }

    // 级联
    public List<SystemResourceEntity> getResourcesByIdCascade(Long id) {
        SystemResourceEntity systemResourceEntity = findOne(id).orElse(null);
        if (systemResourceEntity == null) {
            throw new SystemException(SystemResultEnum.RESOURCE_NOT_FOUND);
        }
        List<SystemResourceEntity> resourceEntities = new ArrayList<>();
        resourceEntities.add(systemResourceEntity);
        List<SystemResourceEntity> childResources = systemResourceDao.findAllByParentId(id);
        for (SystemResourceEntity childResource : childResources) {
            resourceEntities.addAll(getResourcesByIdCascade(childResource.getId()));
        }

        return resourceEntities;
    }

    // 清空资源
    public void clearResources() {
        baseDao.deleteAll();
    }

    // 生成资源
    public void createResources(HttpServletRequest request) {
        parentClasses = getControllerClass();
        if (parentClasses.size() == 0) {
            log.warn("controller class not found");
        }
        saveClassResource();
        ServletContext servletContext = request.getSession().getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
        mappingMethodMap = mapping.getHandlerMethods();
        traverseClass();
        createTree();
    }

    // 保存SystemResourceClass的系统资源
    private void saveClassResource() {
        for (Class parentClass : parentClasses) {
            SystemResourceClass systemResourceClass =
                    (SystemResourceClass) parentClass.getAnnotation(SystemResourceClass.class);
            if (systemResourceClass != null) {
                String parentCode = StringUtil.camelCaseToSymbol(
                                systemResourceClass.parentResource(), ".");

                String icon = systemResourceClass.icon();

                String name = systemResourceClass.resourceName();
                String code = systemResourceClass.code();

                String comment = systemResourceClass.comment();
                String type = systemResourceClass.type().getType();

                if (name.equals("")) {
                    name = systemResourceClass.resourceName();
                }
                if (code.equals("")) {
                    code = StringUtil.camelCaseToSymbol(name, ".");
                }
                if (icon.equals("")) {
                    icon = DEFAULT_RESOURCE_ICON;
                }
                if (parentCode.equals("")) {
                    parentCode = DEFAULT_RESOURCE_PARENT_CODE;
                }

                saveSystemResource(parentCode, icon, name, comment, code, type, "", "");
            }
        }
    }

    // 遍历类，生成资源
    private void traverseClass() {
        defaultSystemResourceNum = 0;
        for (Class parentClass : parentClasses) {
            Set<Method> methods = getClassMethod(parentClass);
            for (Method method : methods) {
                Map.Entry<RequestMappingInfo, HandlerMethod> entry = isMappingMethod(method, parentClass);
                if (entry == null) {
                    continue;
                }
                RequestMappingInfo requestMappingInfo = entry.getKey();
                Method mappingMethod = entry.getValue().getMethod();
                RequestMethodsRequestCondition requestMethods = requestMappingInfo.getMethodsCondition();
                PatternsRequestCondition patternsRequest = requestMappingInfo.getPatternsCondition();

                String requestMethodsString = requestMethods.getMethods().toString();
                String url = patternsRequest.getPatterns().toString();

                if (mappingMethod.isAnnotationPresent(SystemResource.class)) {
                    SystemResource systemResource = mappingMethod.getAnnotation(SystemResource.class);
                    createByAnnotation(systemResource, parentClass, mappingMethod, requestMethodsString, url);
                }
                else {
                    if (parentClass.isAnnotationPresent(SystemResourceClass.class)) {
                        SystemResourceClass systemResourceClass =
                                (SystemResourceClass) parentClass.getAnnotation(SystemResourceClass.class);
                        createByClassAnnotation(systemResourceClass, mappingMethod, requestMethodsString, url);
                    }
                    else {
                        defaultSystemResourceNum ++;
                        createByDefault(mappingMethod, requestMethodsString, url);
                    }
                }
            }
        }
    }

    // 扫描获取所有RestController
    private List<Class<?>> getControllerClass() {
        return ClassUtil.getClassListByAnnotation(
                basePackageScan , RestController.class);
    }

    // 获取类和其父类的所有方法
    private Set<Method> getClassMethod(Class clazz) {
        Set<Method> methods = new HashSet<>();
        Class superClass = clazz.getSuperclass();
        methods.addAll(Arrays.asList(superClass.getMethods()));
        methods.addAll(Arrays.asList(clazz.getMethods()));
        return methods;
    }

    // 判断一个controller类的一个方法和mappingMethodMap中是否有匹配的，如果匹配则返回方法mapping信息
    private Map.Entry<RequestMappingInfo, HandlerMethod> isMappingMethod(Method method, Class<?> parentClass) {
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : mappingMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            PatternsRequestCondition patternsRequest = entry.getKey().getPatternsCondition();
            Set<String> patternsSet = patternsRequest.getPatterns();
            Method m = handlerMethod.getMethod();
            RequestMapping requestMappingAnnotation =  parentClass.getAnnotation(RequestMapping.class);
            for (String path : requestMappingAnnotation.value()) {
                for (String pattern : patternsSet) {
                    if (pattern.startsWith(path) && m.equals(method)) {
                        return entry;
                    }
                }
            }
        }
        return null;
    }

    // 加SystemResource注解，不一定加SystemResourceClass注解，由相应的不同情况生成资源
    private void createByAnnotation(SystemResource systemResource, Class parentClass, Method mappingMethod,
                                    String method, String url) {

        String parentCode = systemResource.parentCode();
        String icon = systemResource.icon();

        String name = systemResource.name();
        String code = systemResource.code();

        String comment = systemResource.comment();
        String type = systemResource.type().getType();

        SystemResourceClass systemResourceClass
                = (SystemResourceClass) parentClass.getAnnotation(SystemResourceClass.class);

        if (systemResourceClass == null) {
            if (icon.equals("")) {
                icon = DEFAULT_RESOURCE_ICON;
            }
            if (parentCode.equals("")) {
                parentCode = DEFAULT_RESOURCE_PARENT_CODE;
            }
            if (name.equals("") || code.equals("")) {
                log.warn("You use @SystemResource in method " + mappingMethod.getName() +
                        ", but not set param with (name, code)," +
                        " please set @SystemResourceClass in " + parentClass.getName() + " class");
                throw new BaseException(BaseResultEnum.FAIL);
            }
        }
        else {
            String resourceName = systemResourceClass.resourceName();
            if (name.equals("")) {
                name = resourceName + StringUtil.upperFirst(mappingMethod.getName());
            }
            if (code.equals("")) {
                code = StringUtil.camelCaseToSymbol(resourceName, ".")
                        + "." + StringUtil.camelCaseToSymbol(mappingMethod.getName(), ".");
            }
            if (parentCode.equals("")) {
                parentCode = systemResourceClass.code();
                if (parentCode.equals("")) {
                    parentCode = StringUtil.camelCaseToSymbol(systemResourceClass.resourceName(), ".");
                }
            }
            if (icon.equals("")) {
                icon = systemResourceClass.icon();
                if (icon.equals("")) {
                    icon = DEFAULT_RESOURCE_ICON;
                }
            }
        }

        saveSystemResource(parentCode, icon, name,
                comment, code, type, method, url);
    }

    // 不加SystemResource、加SystemResourceClass注解，由所属controller类生成资源
    private void createByClassAnnotation(SystemResourceClass systemResourceClass, Method mappingMethod,
                                         String method, String url) {

        String parentCode = systemResourceClass.code();
        String icon = systemResourceClass.icon();
        String resourceName = systemResourceClass.resourceName();
        String name = resourceName + StringUtil.upperFirst(mappingMethod.getName());
        String comment = StringUtil.camelCaseToSymbol(mappingMethod.getName(), " ")
                + " " + StringUtil.camelCaseToSymbol(resourceName, " ");
        String code = StringUtil.camelCaseToSymbol(resourceName, ".")
                + "." + StringUtil.camelCaseToSymbol(mappingMethod.getName(), ".");
        String type = SystemResourceType.BUTTON.getType();

        if (parentCode.equals("")) {
            parentCode = StringUtil.camelCaseToSymbol(systemResourceClass.resourceName(), ".");
        }
        if (icon.equals("")) {
            icon = DEFAULT_RESOURCE_ICON;
        }

        saveSystemResource(parentCode, icon, name, comment, code, type, method, url);
    }

    // 不加SystemResource且不加SystemResourceClass注解，默认生成资源
    private void createByDefault(Method mappingMethod, String method, String url) {

        log.warn(mappingMethod.toString() +
                " don't have @SystemResource annotation, create default resource");
        String code = String.format("%s.%04d", DEFAULT_RESOURCE_CODE_PREFIX, defaultSystemResourceNum);
        String type = DEFAULT_RESOURCE_TYPE.getType();

        saveSystemResource(DEFAULT_RESOURCE_PARENT_CODE, DEFAULT_RESOURCE_ICON,
                DEFAULT_RESOURCE_NAME, DEFAULT_RESOURCE_COMMENT, code, type, method, url);
    }

    // 保存系统资源
    private void saveSystemResource(String parentCode, String icon, String name, String comment,
                                    String code, String type, String method, String url) {

        SystemResourceEntity systemResourceEntity = new SystemResourceEntity();

        systemResourceEntity.setName(name);
        systemResourceEntity.setParentId(DEFAULT_RESOURCE_PARENT_ID);
        systemResourceEntity.setParentCode(parentCode);
        systemResourceEntity.setIcon(icon);
        systemResourceEntity.setComment(comment);
        systemResourceEntity.setCode(code);
        systemResourceEntity.setType(type);
        systemResourceEntity.setOrderBy(DEFAULT_RESOURCE_ORDER_BY);
        systemResourceEntity.setMethod(method);
        systemResourceEntity.setUrl(url);

        if (! systemResourceDao.existsByCode(systemResourceEntity.getCode())) {
            save(systemResourceEntity);
        }
    }

    // 更新数据库，建立资源树
    private void createTree() {
        List<SystemResourceEntity> systemResourceEntities = findAll();
        for (SystemResourceEntity systemResourceEntity : systemResourceEntities) {
            Long parentId = findParent(systemResourceEntity, systemResourceEntities);
            if (parentId != null && ! parentId.equals(DEFAULT_RESOURCE_PARENT_ID)) {
                systemResourceEntity.setParentId(parentId);
                save(systemResourceEntity);
            }
        }
    }

    // 查找资源的父资源Id
    private Long findParent(SystemResourceEntity systemResourceEntity,
                               List<SystemResourceEntity> systemResourceEntities) {
        Long parentId = null;
        String parentCode = systemResourceEntity.getParentCode();
        if (parentCode.equals(DEFAULT_RESOURCE_PARENT_CODE)) {
            return systemResourceEntity.getParentId();
        }
        for (SystemResourceEntity s : systemResourceEntities) {
            if (s.getCode().equals(parentCode)) {
                parentId = s.getId();
            }
        }
        if (parentId == null) {
            log.warn(systemResourceEntity.getCode() + "'s parentCode "
                    + systemResourceEntity.getParentCode() + " not found,"
                    + " please check you code.");
        }
        return parentId;
    }
}
