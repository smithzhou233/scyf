package com.hngf.api.common.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 对返回的json格式化处理
 */
public class MyJsonMapper extends ObjectMapper {

    public MyJsonMapper(){
        super();
        //收到未知属性时不报异常
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        //Long类型转为String类型
        SimpleModule simpleModule = new SimpleModule();
        //在进出前后台的时候，设置BigDecimal和字符串之间转换
        simpleModule.addSerializer(BigDecimal.class,ToStringSerializer.instance);
        this.registerModule(simpleModule);
        //处理空指针时设置的值
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                String fieldName = gen.getOutputContext().getCurrentName();
                try {
                    //反射获取字段类型
                    Field field = gen.getCurrentValue().getClass().getDeclaredField(fieldName);
                    if (Objects.equals(field.getType(), String.class)) {
                        //字符串型空值""
                        gen.writeString("");
                        return;
                    }else if (Objects.equals(field.getType(), Map.class)) {
                        //列表型空值返回[]
                        gen.writeStartArray();
                        gen.writeEndArray();
                        return;
                    }else if (Objects.equals(field.getType(), List.class)) {
                        //map型空值返回{}
                        gen.writeStartObject();
                        gen.writeEndObject();
                        return;
                    }else if (Objects.equals(field.getType(), Integer.class) || Objects.equals(field.getType(), Long.class) || Objects.equals(field.getType(), Double.class)) {
                        //Integer类型 ""
                        gen.writeNumber(-1);
                        return;
                    }else if (Objects.equals(field.getType(), Date.class)) {
                        //Integer类型 返回原值或空字符串
                        gen.writeString("");
                        return;
                    }
                } catch (NoSuchFieldException e) {
                }
                //默认返回""
                gen.writeString("");
            }
        });
    }
}
