package com.github.mybatis.generator.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * description:
 * createDate: 2020-03-25  12:24
 * lastModifiedDate:
 *
 * @author Coder_2015@outlook.com
 * @since 1.0.0
 */
public class MybatisPlusServicePlugin extends PluginAdapter {

	private static Set<String> tables = new HashSet<String>();
	private String baseServiceName;
	private String conditionSuperName;
	private String controllerImports;
	private String addMethodReturnType;
	private String addMethodBodyLine;
	private String delMethodReturnType;
	private String delMethodBodyLine;
	private String updateMethodReturnType;
	private String updateMethodBodyLine;
	private String getMethodReturnType;
	private String getMethodBodyLine;

	public boolean validate(List<String> warnings) {
		initConfig();
		return true;
	}

	private void initConfig() {
		File file = new File("generator.properties");
		try {
			InputStream inputStream = null;
			if (!file.exists()) {
				inputStream = new FileInputStream(file);
			} else {
				inputStream = this.getClass().getClassLoader().getResourceAsStream("generator.properties");
			}
			Properties properties = new Properties();
			properties.load(inputStream);
			baseServiceName=properties.getProperty("baseServiceName");
			conditionSuperName=properties.getProperty("conditionSuperName");
			controllerImports=properties.getProperty("controllerImports");
			addMethodReturnType=properties.getProperty("addMethodReturnType");
			addMethodBodyLine=properties.getProperty("addMethodBodyLine");
			delMethodReturnType=properties.getProperty("delMethodReturnType");
			delMethodBodyLine=properties.getProperty("delMethodBodyLine");
			updateMethodReturnType=properties.getProperty("updateMethodReturnType");
			updateMethodBodyLine=properties.getProperty("updateMethodBodyLine");
			getMethodReturnType=properties.getProperty("getMethodReturnType");
			getMethodBodyLine=properties.getProperty("getMethodBodyLine");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		if (introspectedTable.getTargetRuntime() == IntrospectedTable.TargetRuntime.MYBATIS3) {
			if (tables.contains(introspectedTable.getBaseRecordType())) {
				Collections.emptyList();
			}
			tables.add(introspectedTable.getBaseRecordType());
			String targetProject = introspectedTable.getContext().getJavaClientGeneratorConfiguration().getTargetProject();
			JavaFormatter javaFormatter = introspectedTable.getContext().getJavaFormatter();
			String targetPackage = introspectedTable.getContext().getJavaClientGeneratorConfiguration().getTargetPackage();
			int index = targetPackage.lastIndexOf('.');
			String basePackage = null;
			if (index > 0) {
				basePackage = targetPackage.substring(0, index);
			} else {
				basePackage = targetPackage;
			}
			String baseRecordType = introspectedTable.getBaseRecordType();
			String modelName = baseRecordType.substring(baseRecordType.lastIndexOf('.') + 1);
			List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<GeneratedJavaFile>();
			generatedJavaFiles.add(getServiceJava(introspectedTable, basePackage, modelName, targetProject, javaFormatter));
			generatedJavaFiles.add(getConditionJava(introspectedTable, basePackage, modelName, targetProject, javaFormatter));
			generatedJavaFiles.add(getControllerJava(introspectedTable, basePackage, modelName, targetProject, javaFormatter));
			return generatedJavaFiles;
		}
		return Collections.emptyList();
	}

	private GeneratedJavaFile getServiceJava(IntrospectedTable introspectedTable, String basePackage, String modelName, String targetProject,
			JavaFormatter javaFormatter) {

		FullyQualifiedJavaType serviceJavaType = new FullyQualifiedJavaType(basePackage + ".service." + modelName + "Service");
		TopLevelClass topLevelClass = new TopLevelClass(serviceJavaType);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		topLevelClass.addAnnotation("@Service");
		topLevelClass.addImportedType("org.springframework.stereotype.Service");
		topLevelClass.addImportedType(baseServiceName);
		topLevelClass.addImportedType(introspectedTable.getBaseRecordType());
		topLevelClass.addImportedType(introspectedTable.getMyBatis3JavaMapperType());
		FullyQualifiedJavaType superJavaType = new FullyQualifiedJavaType(baseServiceName);
		String mapperName = introspectedTable.getMyBatis3JavaMapperType();
		FullyQualifiedJavaType mapperJavaType = new FullyQualifiedJavaType(mapperName.substring(mapperName.lastIndexOf('.') + 1));
		FullyQualifiedJavaType modelJavaType = new FullyQualifiedJavaType(modelName);
		superJavaType.addTypeArgument(mapperJavaType);
		superJavaType.addTypeArgument(modelJavaType);
		topLevelClass.setSuperClass(superJavaType);
		GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(topLevelClass, targetProject, null, javaFormatter);
		return generatedJavaFile;
	}

	private GeneratedJavaFile getConditionJava(IntrospectedTable introspectedTable, String basePackage, String modelName, String targetProject,
			JavaFormatter javaFormatter) {
		FullyQualifiedJavaType conditionJavaType = new FullyQualifiedJavaType(basePackage + ".dto." + modelName + "Condition");
		FullyQualifiedJavaType superJavaType = new FullyQualifiedJavaType(conditionSuperName.substring(conditionSuperName.lastIndexOf('.') + 1));
		TopLevelClass topLevelClass = new TopLevelClass(conditionJavaType);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		topLevelClass.setSuperClass(superJavaType);
		topLevelClass.addImportedType(conditionSuperName);
		topLevelClass.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiModel"));
		topLevelClass.addAnnotation("@ApiModel");
		GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(topLevelClass, targetProject, null, javaFormatter);
		return generatedJavaFile;
	}

	private GeneratedJavaFile getControllerJava(IntrospectedTable introspectedTable, String basePackage, String modelName, String targetProject,
			JavaFormatter javaFormatter) {
		FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(basePackage + "." + modelName + "Controller");
		TopLevelClass topLevelClass = new TopLevelClass(fullyQualifiedJavaType);
		topLevelClass.setVisibility(JavaVisibility.PUBLIC);
		topLevelClass.addAnnotation("@Api");
		topLevelClass.addAnnotation("@RestController");
		topLevelClass.addAnnotation("@RequestMapping");
		topLevelClass.addImportedType("org.springframework.beans.factory.annotation.Autowired");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RestController");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.RequestMethod");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.ModelAttribute");
		topLevelClass.addImportedType("org.springframework.web.bind.annotation.PathVariable");
		topLevelClass.addImportedType("io.swagger.annotations.ApiOperation");
		topLevelClass.addImportedType("io.swagger.annotations.Api");

		topLevelClass.addImportedType(basePackage + ".domain." + modelName);
		topLevelClass.addImportedType(basePackage + ".dto." + modelName + "Condition");
		topLevelClass.addImportedType(basePackage + ".service." + modelName + "Service");
		String [] imports=controllerImports.split(",");
		for(String imp:imports){
			topLevelClass.addImportedType(imp);
		}

		FullyQualifiedJavaType modelJavaType = new FullyQualifiedJavaType(modelName);
		String modelFieldName = Character.toLowerCase(modelName.charAt(0)) + modelName.substring(1);
		String serviceFieldName = modelName + "Service";
		serviceFieldName = Character.toLowerCase(serviceFieldName.charAt(0)) + serviceFieldName.substring(1);

		Field serviceField = new Field(serviceFieldName, new FullyQualifiedJavaType(modelName + "Service"));
		serviceField.setVisibility(JavaVisibility.PRIVATE);
		serviceField.addAnnotation("@Autowired");
		topLevelClass.addField(serviceField);
		// 添加接口
		Method addMethod = new Method("add");
		addMethod.setVisibility(JavaVisibility.PUBLIC);
		addMethod.addAnnotation("@ApiOperation(value = \"添加\")");
		addMethod.addAnnotation("@RequestMapping(path = \"\",method = RequestMethod.POST)");
		addMethod.setReturnType(new FullyQualifiedJavaType(MessageFormat.format(addMethodReturnType,modelName)));
		Parameter addParameter = new Parameter(modelJavaType, modelFieldName);
		addParameter.addAnnotation("@ModelAttribute");
		addMethod.addParameter(addParameter);
		addMethod.addBodyLine(MessageFormat.format(addMethodBodyLine,serviceField.getName(),addParameter.getName()));
		topLevelClass.addMethod(addMethod);

		//删除接口
		Method delMethod = new Method("del");
		delMethod.setVisibility(JavaVisibility.PUBLIC);
		delMethod.addAnnotation("@ApiOperation(value = \"删除\")");
		delMethod.addAnnotation("@RequestMapping(path = \"/{id}\", method = RequestMethod.DELETE)");
		delMethod.setReturnType(new FullyQualifiedJavaType(delMethodReturnType));
		Parameter delParameter = new Parameter(new FullyQualifiedJavaType("Long"), "id");
		delParameter.addAnnotation("@PathVariable");
		delMethod.addParameter(delParameter);
		delMethod.addBodyLine(MessageFormat.format(delMethodBodyLine,serviceField.getName()));
		topLevelClass.addMethod(delMethod);

		// 修改接口
		Method updateMethod = new Method("update");
		updateMethod.setVisibility(JavaVisibility.PUBLIC);
		updateMethod.addAnnotation("@ApiOperation(value = \"修改\")");
		updateMethod.addAnnotation("@RequestMapping(path = \"\", method = RequestMethod.PUT)");
		updateMethod.setReturnType(new FullyQualifiedJavaType(MessageFormat.format(updateMethodReturnType,modelName)));
		Parameter updateParameter = new Parameter(modelJavaType, modelFieldName);
		updateParameter.addAnnotation("@ModelAttribute");
		updateMethod.addParameter(updateParameter);
		updateMethod.addBodyLine(MessageFormat.format(updateMethodBodyLine,serviceField.getName(),updateParameter.getName()));
		topLevelClass.addMethod(updateMethod);

		// 查询接口
		Method getMethod = new Method("find");
		getMethod.setVisibility(JavaVisibility.PUBLIC);
		getMethod.addAnnotation("@ApiOperation(value = \"分页查询\")");
		getMethod.addAnnotation("@RequestMapping(path = \"\", method = RequestMethod.GET)");
		getMethod.setReturnType(new FullyQualifiedJavaType(MessageFormat.format(getMethodReturnType,modelName)));
		Parameter getParameter = new Parameter(new FullyQualifiedJavaType(modelName + "Condition"), "condition");
		getParameter.addAnnotation("@ModelAttribute");
		getMethod.addParameter(getParameter);
		getMethod.addBodyLine(MessageFormat.format(getMethodBodyLine,serviceField.getName(),getParameter.getName()));
		topLevelClass.addMethod(getMethod);
		GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(topLevelClass, targetProject, null, javaFormatter);
		return generatedJavaFile;
	}
}
