package com.github.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;

import java.util.List;

/**
 * description:
 * createDate: 2020-03-24  19:04
 * lastModifiedDate:
 *
 * @author aichenchen@eversec.cn
 * @since 1.0.0
 */
public class MybatisPlusMapperPlugin extends PluginAdapter {
	public boolean validate(List<String> warnings) {
		return true;
	}
	@Override
	public boolean clientGenerated(Interface interfaze,
			IntrospectedTable introspectedTable) {
		if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
			interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
			interfaze.addImportedType(new FullyQualifiedJavaType("com.baomidou.mybatisplus.core.mapper.BaseMapper"));
			interfaze.addAnnotation("@Mapper");
			FullyQualifiedJavaType fullyQualifiedJavaType=new FullyQualifiedJavaType("BaseMapper");
			fullyQualifiedJavaType.addTypeArgument(new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
			interfaze.addSuperInterface(fullyQualifiedJavaType);
		}
		return true;
	}
}
