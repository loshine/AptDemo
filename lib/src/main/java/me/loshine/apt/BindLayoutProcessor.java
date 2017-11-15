package me.loshine.apt;

import com.google.auto.service.AutoService;
import com.google.common.reflect.TypeToken;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import me.loshine.annotation.BindLayout;
import me.loshine.annotation.LayoutBinder;


/**
 * Created by longshuai on 2017/11/15.
 */
@AutoService(Processor.class)
public class BindLayoutProcessor extends AbstractProcessor {

    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnv.getElementUtils();
    }

    /**
     * getSupportedSourceVersion()方法返回 Java 版本 默认为Java6
     *
     * @return Java 版本
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 返回要处理的注解的结合 这里只处理 RouterAnnotation 类型的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindLayout.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //来自javapoet  动态生成方法
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindLayout.class);
        for (Element element : elements) {
            // 判断是不是 Class
            TypeElement typeElement = (TypeElement) element;
            BindLayout bindLayout = typeElement.getAnnotation(BindLayout.class);

            MethodSpec.Builder bindViewMethodSpecBuilder = MethodSpec.methodBuilder("bindView")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(ClassName.get(Object.class), "obj")
                    .addStatement("((android.app.Activity)obj).setContentView($L)", bindLayout.value());

            TypeSpec typeSpec = TypeSpec.classBuilder(element.getSimpleName() + "LayoutBinder")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(LayoutBinder.class)
                    .addMethod(bindViewMethodSpecBuilder.build())
                    .build();
            JavaFile javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }
}