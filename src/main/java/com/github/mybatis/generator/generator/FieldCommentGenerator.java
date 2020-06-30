
package com.github.mybatis.generator.generator;

import java.util.Properties;
import java.util.Set;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * description: 为生成的实体类的字段添加数据库字段注释
 * createDate: 2020-03-19 18:02:02
 * lastModifiedDate:
 *
 * @author Coder_2015@outlook.com
 * @since 1.0.0
 */
public class FieldCommentGenerator implements CommentGenerator {

    public void addConfigurationProperties(Properties properties) {

    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.getRemarks() != null && introspectedColumn.getRemarks().length() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("/** ");
            sb.append(introspectedColumn.getRemarks());
            sb.append(" **/");
            field.addJavaDocLine(sb.toString());
        }
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
    }

    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        

    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        

    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        

    }

    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        

    }

    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        

    }

    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        

    }

    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        

    }

    public void addJavaFileComment(CompilationUnit compilationUnit) {
        

    }

    public void addComment(XmlElement xmlElement) {
        

    }

    public void addRootComment(XmlElement rootElement) {
        

    }

    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        

    }

    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn,
                                           Set<FullyQualifiedJavaType> imports) {
        

    }

    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        

    }

    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn,
                                   Set<FullyQualifiedJavaType> imports) {
        

    }

    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
        

    }

}
