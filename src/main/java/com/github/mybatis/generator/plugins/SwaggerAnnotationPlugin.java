
package com.github.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * description: FieldCommentGenerator
 * createDate: 2020-03-19 18:02:02
 * lastModifiedDate:
 *
 * @author Coder_2015@outlook.com
 * @since 1.0.0
 */
public class SwaggerAnnotationPlugin extends PluginAdapter {

	public boolean validate(List<String> warnings) {
		return true;
	}
	
	@Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
		if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
			StringBuilder sb = new StringBuilder();
			sb.append("/** ");
			sb.append(introspectedTable.getRemarks());
			sb.append(" **/");
			topLevelClass.addJavaDocLine(sb.toString());
			topLevelClass.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiModel"));
			topLevelClass.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiModelProperty"));
			topLevelClass.addAnnotation("@ApiModel");
		}
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field,
            TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            Plugin.ModelClassType modelClassType) {
    	if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
		    if(introspectedColumn.getRemarks()!=null&&introspectedColumn.getRemarks().length()>0) {
			    field.addAnnotation("@ApiModelProperty(value = \""+introspectedColumn.getRemarks()+"\")");
		    }else{
			    field.addAnnotation("@ApiModelProperty(value = \""+introspectedColumn.getActualColumnName()+"\")");
		    }

		}
        return true;
    }

}
