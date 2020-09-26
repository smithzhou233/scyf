package com.hngf.web.common.plugins;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.hngf.web.common.annotation.ApiJsonProperty;
import com.hngf.web.common.annotation.ApiParameterJsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.CtField;
import org.apache.ibatis.javassist.NotFoundException;
import org.apache.ibatis.javassist.bytecode.AnnotationsAttribute;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.annotation.Annotation;
import org.apache.ibatis.javassist.bytecode.annotation.BooleanMemberValue;
import org.apache.ibatis.javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * plugin加载顺序，默认是最后加载
 * 此类用于swagger的接口参数描述
 */
@Component
@Order
public class SwaggerJsonParameterPlugin implements ParameterBuilderPlugin {

    @Autowired
    private TypeResolver typeResolver;

    private final static String BASE_PACKAGE = "com.hngf.entity.custom.";  //动态生成的Class名


    /**
     * 在Swagger2中将该字段封装成一个model存进Swagger2容器中，此类用于给swagger添加json数据类型的返回结果的描述
     *
     * @param parameterContext
     */
    @Override
    public void apply(ParameterContext parameterContext) {

        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        if (methodParameter.getParameterType().canCreateSubtype(JSONObject.class) || methodParameter.getParameterType().canCreateSubtype(Map.class) || methodParameter.getParameterType().canCreateSubtype(String.class)) { //判断是否需要修改对象ModelRef,这里我判断的是Map类型和String类型需要重新修改ModelRef对象
            Optional<ApiParameterJsonObject> optional = methodParameter.findAnnotation(ApiParameterJsonObject.class);  //根据参数上的ApiParameterJsonObject注解中的参数动态生成Class
            if (optional.isPresent()) {
                String name = optional.get().name();  //model 名称
                String curClassName =getModelClassCurrentName(parameterContext.getOperationContext(), name);//model class名称
                ApiJsonProperty[] properties = optional.get().value();
                parameterContext.getDocumentationContext().getAdditionalModels().add(typeResolver.resolve(createRefModel(properties, curClassName)));  //像documentContext的Models中添加我们新生成的Class
                parameterContext.parameterBuilder()  //修改Map参数的ModelRef为我们动态生成的class
                        .parameterType("body")
                        .modelRef(new ModelRef(curClassName))
                        .name(curClassName);
            }
        }
    }



    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private String getModelClassCurrentName(OperationContext operationContext, String name){
        StringBuilder curClassName = new StringBuilder();//model class名称
        String requestMappingPattern = operationContext.requestMappingPattern();

        if(requestMappingPattern.startsWith("/")){
            requestMappingPattern = requestMappingPattern.substring(1);
        }
        String[] requestMappingPatternArray = requestMappingPattern.split("/");
        for(int i= 0;i< requestMappingPatternArray.length ; i++ ){
            curClassName.append(getFiistUpper(requestMappingPatternArray[i]));
        }
        curClassName.append(getFiistUpper(name));

        return curClassName.toString();
    }

    /**
     *
     * @param source
     * @return
     */
    private String getFiistUpper(String source ){
        if(null == source && StringUtils.isBlank(source)){
            return source;
        }
        StringBuilder resultStr = new StringBuilder();
        resultStr.append(Character.toString(source.charAt(0)).toUpperCase()).append(source.substring(1));
        return resultStr.toString();
    }
    /**
     * 根据propertys中的值动态生成含有Swagger注解的javaBeen
     */

    private Class createRefModel(ApiJsonProperty[] propertys, String name) {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(BASE_PACKAGE+name);
        try {
            for (ApiJsonProperty property : propertys) {
                ctClass.addField(createField(property, ctClass));
            }
            return ctClass.toClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据property的值生成含有swagger apiModelProperty注解的属性
     */

    private CtField createField(ApiJsonProperty property, CtClass ctClass){

        try {
            CtField ctField = new CtField(getFieldType(property.type()), property.key(), ctClass);
            ctField.setModifiers(Modifier.PUBLIC);
            ConstPool constPool = ctClass.getClassFile().getConstPool();
            AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
            Annotation ann = new Annotation("io.swagger.annotations.ApiModelProperty" , constPool);
            if("".equals(property.value())){
                ann.addMemberValue("value", new StringMemberValue(property.description(), constPool));
            }else {
                ann.addMemberValue("value", new StringMemberValue(property.value(), constPool));
            }
            ann.addMemberValue("description", new StringMemberValue(property.description(), constPool));
            ann.addMemberValue("type", new StringMemberValue(property.type(), constPool));
            ann.addMemberValue("required", new BooleanMemberValue(property.required(), constPool));
            ann.addMemberValue("defaultValue", new StringMemberValue(property.defaultValue(), constPool));
            ann.addMemberValue("allowMultiple", new BooleanMemberValue(property.allowMultiple(),constPool));
            ann.addMemberValue("example" , new StringMemberValue(property.example() , constPool));

            attr.addAnnotation(ann);
            ctField.getFieldInfo().addAttribute(attr);
            return ctField;
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }
    private CtClass getFieldType(String type) throws NotFoundException {
        CtClass fileType = null;
        switch (type) {
            case "string":
                fileType = ClassPool.getDefault().get(String.class.getName());
                break;
            case "int":
                fileType = ClassPool.getDefault().get(Integer.class.getName());
                break;
            case "short":
                fileType = ClassPool.getDefault().get(Short.class.getName());
                break;
            case "char":
                fileType = ClassPool.getDefault().get(Character.class.getName());
                break;
            case "byte":
                fileType = ClassPool.getDefault().get(Byte.class.getName());
                break;
            case "float":
                fileType = ClassPool.getDefault().get(Float.class.getName());
                break;
            case "double":
                fileType = ClassPool.getDefault().get(Double.class.getName());
                break;
            case "long":
                fileType = ClassPool.getDefault().get(Long.class.getName());
                break;
            case "boolean":
                fileType = ClassPool.getDefault().get(Boolean.class.getName());
                break;
            default:
                fileType = ClassPool.getDefault().get(type);
                break;
        }
        return fileType;

    }
}
