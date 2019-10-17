package cn.hengyumo.humor.base.system.dict;

import cn.hengyumo.humor.base.system.config.SystemDict;
import cn.hengyumo.humor.base.system.dict.item.SystemDictItemEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * SystemDictSerializer
 *
 * @author hengyumo
 * @version 1.0
 * @since 2019/9/19
 */
@Component
@Slf4j
public class SystemDictSerializer extends JsonSerializer<Integer> {

    @Resource
    private SystemDictService systemDictService;

    @Override
    public void serialize(Integer integer, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        Object o =  jsonGenerator.getCurrentValue();
        Class clazz = o.getClass();
        JsonStreamContext jsonStreamContext = jsonGenerator.getOutputContext();
        String fieldName = jsonStreamContext.getCurrentName();
        boolean found = false;
        try {
            // 注意私有域的获取要用getDeclaredField
            Field field = clazz.getDeclaredField(fieldName);
            if (field.isAnnotationPresent(SystemDict.class)) {
                SystemDict systemDict = field.getAnnotation(SystemDict.class);
                String dictName = systemDict.name();
                // 字典查找
                SystemDictEntity systemDictEntity = systemDictService.findByName(dictName);
                for (SystemDictItemEntity systemDictItemEntity : systemDictEntity.getChildren()) {
                    if (systemDictItemEntity.getValueIndex().equals(integer)) {
                        jsonGenerator.writeString(systemDictItemEntity.getValue());
                        found = true;
                        break;
                    }
                }
            }
            else {
                log.warn(fieldName + " not found @SystemDict annotation");
            }
        } catch (NoSuchFieldException e) {
            log.warn("not such field -- " + fieldName);
        }
        if (! found) {
            log.warn(fieldName + " not found dict matched");
            jsonGenerator.writeString(String.valueOf(integer));
        }
    }
}
