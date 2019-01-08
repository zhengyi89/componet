package com.zbjdl.common.config.controller;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zbjdl.boss.admin.frame.BaseController;
import com.zbjdl.common.utils.BeanUtils;
import com.zbjdl.common.utils.config.enumtype.ValueDataTypeEnum;
import com.zbjdl.common.utils.config.enumtype.ValueTypeEnum;
import com.zbjdl.common.utils.config.facade.ConfigManagementFacade;
import com.zbjdl.common.utils.config.facade.ConfigQueryFacade;
import com.zbjdl.common.utils.config.param.ConfigDTO;
import com.zbjdl.common.utils.config.utils.ConfigUtil;

/**
 * 配置信息管理 action
 * 
 */
@Controller
@RequestMapping("/config")
public class ConfigAction  extends BaseController{

	private static final long serialVersionUID = 7257176374051875952L;

	private static final String MAP = "MAP";

	private static final String LSIT = "LIST";

	private static final String VALUE = "VALUE";

	private static final String STRUCTURE = "STRUCTURE";


	@Autowired
	private ConfigManagementFacade managementFacade;
	@Autowired
	private ConfigQueryFacade queryFacade;


//	private String valueDataTypeStr;
//
//
//	private String code;
//
//	private String description;
//
//	private String fileName;




    @InitBinder("config")
    public void initBinderConfigDTO(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("config.");
    }

	@RequestMapping("/add")
	public String add(HttpServletRequest request,HttpServletResponse response) {
		request.setAttribute("namespaceList",
				queryFacade.queryConfigNamespace());
		request.setAttribute("typeList",
				queryFacade.queryConfigType());
		request.setAttribute("sysNameMap",
				ConfigUtil.getSysName());
		String configNamespace = request.getParameter("config_namespace");
		String configType = request.getParameter("config_type");
		request.setAttribute("config_namespace", configNamespace);
		request.setAttribute("config_type", configType);
		return "config/add-config";
	}

	/**
	 * 把页面传过来的参数转化成数据实体
	 * 
	 * @return config
	 */
	private ConfigDTO paramToConfig(HttpServletRequest request,ConfigDTO config) {

		// 获得请求
		String valueType = config.getValueTypeStr();
		ConfigDTO cfg = null;
		// 不同情况的转换
		if (valueType.equals(ConfigAction.VALUE)) {
			// 当为普通类型
			cfg = new ConfigDTO();
			// COPY 属性 和设置一些特殊的属性
			BeanUtils.copyProperties(config, cfg);
			cfg.setValueType(ValueTypeEnum.valueOf(config.getValueTypeStr()));
			cfg.setValueDataType(ValueDataTypeEnum.valueOf(config
					.getValueDataTypeStr()));
		} else if (valueType.equals(ConfigAction.MAP)) {
			// map 类型时
			cfg = new ConfigDTO();
			BeanUtils.copyProperties(config, cfg);
			cfg.setValueType(ValueTypeEnum.valueOf(config.getValueTypeStr()));
			cfg.setValueDataType(ValueDataTypeEnum.valueOf(config
					.getValueDataTypeStr()));
			// 获得页面的MAP KEY 和VALUE 值
			String[] mapKeys = request.getParameterValues("mapKey");
			String[] mapValues = request.getParameterValues("mapValue");
			Map<String, Object> configs = new LinkedHashMap<String, Object>();
			for (int i = 0; i < mapKeys.length; i++) {
				String key = mapKeys[i];
				String value = mapValues[i];
				configs.put(key, value);
			}
			// 转化value 的xml格式的字符
			String valueStr = ConfigUtil.toXmlStr(
					ValueTypeEnum.valueOf(config.getValueTypeStr()), configs);
			cfg.setValue(valueStr);

		} else if (valueType.equals(ConfigAction.LSIT)) {
			// LIST 类型时
			cfg = new ConfigDTO();
			BeanUtils.copyProperties(config, cfg);
			cfg.setValueType(ValueTypeEnum.valueOf(config.getValueTypeStr()));
			cfg.setValueDataType(ValueDataTypeEnum.valueOf(config
					.getValueDataTypeStr()));
			String[] listValues = request.getParameterValues("listValue");
			List<Object> list = new ArrayList<Object>();
			for (String ls : listValues) {
				list.add(ls);
			}
			String valueStr = ConfigUtil.toXmlStr(
					ValueTypeEnum.valueOf(config.getValueTypeStr()), list);
			cfg.setValue(valueStr);

		} else if (valueType.equals(ConfigAction.STRUCTURE)) {
			// 自定义类型时
			cfg = new ConfigDTO();
			BeanUtils.copyProperties(config, cfg);
			cfg.setValueType(ValueTypeEnum.valueOf(config.getValueTypeStr()));
			cfg.setValueDataType(null);
			String[] itemKeys = request.getParameterValues("itemKey");
			String[] itemTypes = request.getParameterValues("itemType");
			String[] itemValues = request.getParameterValues("itemValue");
			Map<String, Object> items = new HashMap<String, Object>();
			for (int i = 0; i < itemKeys.length; i++) {
				String key = itemKeys[i];
				String type = itemTypes[i];
				String value = itemValues[i];
				ValueDataTypeEnum valueDataType = ValueDataTypeEnum
				.valueOf(type);
				Object obj = ConfigUtil.str2Object(valueDataType, value);

				items.put(key.trim(), obj);
			}
			String valueStr = ConfigUtil.toXmlStr(
					ValueTypeEnum.valueOf(config.getValueTypeStr()), items);
			cfg.setValue(valueStr);
		}
		return cfg;
	}

	@RequestMapping(value="/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("config") ConfigDTO config ,
            BindingResult result,HttpServletRequest request,Model model
            ) {

		// 转换配置
		ConfigDTO configDTO = paramToConfig(request,config);
		// 创建配置
		Long id = managementFacade.createConfig(configDTO);
//		configId = id;
		return "redirect:list?namespace="+configDTO.getNamespace()+"&type="+configDTO.getType();
	}

	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "configId", required = true) Long configId,Model model) throws Exception {

		request.setAttribute("namespaceList",
				queryFacade.queryConfigNamespace());
		request.setAttribute("typeList",
				queryFacade.queryConfigType());
		request.setAttribute("sysNameMap",
				ConfigUtil.getSysName());
		// 获得对应Id的配置对象
		ConfigDTO cfg = queryFacade.queryConfigById(configId);
		if (cfg != null) {
			ConfigDTO config = new ConfigDTO();
			BeanUtils.copyProperties(cfg, config);
			if (cfg.getValueDataType() != null)
				config.setValueDataTypeStr(cfg.getValueDataType().name());

			config.setValueTypeStr(cfg.getValueType().name());
			if (cfg.getValueType().name().equals(ConfigAction.MAP)) {
				Map<String, Object> map = (Map<String, Object>) ConfigUtil
				.XmlStrToObject(cfg.getValueType(),
						cfg.getValueDataType(), cfg.getValue());
				request.setAttribute("map", map);
			} else if (cfg.getValueType().name().equals(ConfigAction.LSIT)) {
				List<Object> list;
				list = (List<Object>) ConfigUtil.XmlStrToObject(
						cfg.getValueType(), cfg.getValueDataType(),
						cfg.getValue());
				request.setAttribute("list", list);
			} else if (cfg.getValueType().name().equals(ConfigAction.STRUCTURE)) {
				Map<String, Object> entityMap;
				entityMap = (Map<String, Object>) ConfigUtil.XmlStrToObject(
						cfg.getValueType(), cfg.getValueDataType(),
						cfg.getValue());
				Map<String, String> typeMap = new HashMap<String, String>();
				for (String key : entityMap.keySet()) {
					Object value = entityMap.get(key);
					String type = this.getObjType(value);
					typeMap.put(key, type);
				}
				request.setAttribute("entityMap", entityMap);
				request.setAttribute("typeMap", typeMap);
			}
            model.addAttribute("config", config);

			return "config/edit";
		} else
			return "fail";
	}

	/**
	 * 获得值的类型字符
	 * 
	 * @param value
	 * @return string
	 */
	private String getObjType(Object value) {
		if (value instanceof Long)
			return ValueDataTypeEnum.INTEGER.name();
		else if (value instanceof Double)
			return ValueDataTypeEnum.DOUBLE.name();
		else if (value instanceof String)
			return ValueDataTypeEnum.STRING.name();
		else if (value instanceof Boolean)
			return ValueDataTypeEnum.BOOLEAN.name();
		else if (value instanceof Date) {
			Date date = (Date) value;
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			if (c.get(Calendar.SECOND) == 0 && c.get(Calendar.HOUR) == 0
					&& c.get(Calendar.MINUTE) == 0)
				return ValueDataTypeEnum.DATE.name();
			else
				return ValueDataTypeEnum.DATETIME.name();
		}

		return null;
	}
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
	public String update(@ModelAttribute("config") ConfigDTO config ,
            BindingResult result,HttpServletRequest request,Model model
            ) {
		// 转换配置
		ConfigDTO configDTO = paramToConfig(request,config);
		managementFacade.changeConfig(configDTO);
		return "redirect:list?namespace="+config.getNamespace()+"&type="+config.getType();
	}
	@RequestMapping("/list")
	public String list(HttpServletRequest request,@ModelAttribute ConfigDTO config) {
		request.setAttribute("namespaceList",
				queryFacade.queryConfigNamespace());
		request.setAttribute("typeList",
				queryFacade.queryConfigType());
		request.setAttribute("sysNameMap",
				ConfigUtil.getSysName());
		return "config/list-config";
	}

	/**
	 * 验证枚举字符是否合法
	 * 
	 * @param enumStr
	 *            枚举字符
	 * @param clazz
	 *            需要验证的类
	 * @return 返回是与否
	 */
	@SuppressWarnings("rawtypes")
	private Boolean validateEnmuStr(String enumStr, Class clazz) {
		// 当为配置类型的枚举时验证
		if (clazz == ValueDataTypeEnum.class) {
			for (ValueDataTypeEnum valueDataType : ValueDataTypeEnum.values()) {
				if (valueDataType.name().equals(enumStr))
					return true;
			}
		}
		// 当为配置值数据类型的枚举验证
		if (clazz == ValueTypeEnum.class) {
			for (ValueTypeEnum valueType : ValueTypeEnum.values()) {
				if (valueType.name().equals(enumStr))
					return true;
			}
		}
		return false;

	}

	@RequestMapping("/detail")
	public String detail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "configId", required = true) Long configId,Model model) {
		// 获得对应Id的配置对象
		ConfigDTO cfg = queryFacade.queryConfigById(configId);
		request.setAttribute("sysNameMap",
				ConfigUtil.getSysName());
		if (cfg != null) {
			ConfigDTO config = new ConfigDTO();
			BeanUtils.copyProperties(cfg, config);
			try {
				if (cfg.getValueDataType() != null)
					config.setValueDataTypeStr(cfg.getValueDataType().name());

				config.setValueTypeStr(cfg.getValueType().name());
				if (cfg.getValueType().name().equals(ConfigAction.MAP)) {
					Map<String, Object> map;

					map = (Map<String, Object>) ConfigUtil.XmlStrToObject(
							cfg.getValueType(), cfg.getValueDataType(),
							cfg.getValue());

					request.setAttribute("map", map);
				} else if (cfg.getValueType().name().equals(ConfigAction.LSIT)) {
					List<Object> list;

					list = (List<Object>) ConfigUtil.XmlStrToObject(
							cfg.getValueType(), cfg.getValueDataType(),
							cfg.getValue());

					request.setAttribute("list", list);
				} else if (cfg.getValueType().name()
						.equals(ConfigAction.STRUCTURE)) {
					Map<String, Object> entityMap;

					entityMap = (Map<String, Object>) ConfigUtil
					.XmlStrToObject(cfg.getValueType(),
							cfg.getValueDataType(), cfg.getValue());

					request.setAttribute("entityMap", entityMap);
				}
			} catch (DocumentException e) {
				logger.error("", e);
			}
			request.setAttribute("config",config);
			return "config/detail";
		} else
			return "fail";
	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "configId", required = false) Long configId,Model model) {
		managementFacade.deleteConfig(configId);
		return "redirect:list";
	}

	@RequestMapping("/copyEdit")
	public String copyEdit(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "configId", required = false) Long configId,Model model) {
		request.setAttribute("namespaceList",
				queryFacade.queryConfigNamespace());
		request.setAttribute("typeList",
				queryFacade.queryConfigType());
		request.setAttribute("sysNameMap",
				ConfigUtil.getSysName());

		// 获得对应Id的配置对象
		ConfigDTO cfg = queryFacade.queryConfigById(configId);
		if (cfg != null) {
			ConfigDTO config = new ConfigDTO();
			BeanUtils.copyProperties(cfg, config);
			try {
				if (cfg.getValueDataType() != null)
					config.setValueDataTypeStr(cfg.getValueDataType().name());

				config.setValueTypeStr(cfg.getValueType().name());
				if (cfg.getValueType().name().equals(ConfigAction.MAP)) {
					Map<String, Object> map = (Map<String, Object>) ConfigUtil
					.XmlStrToObject(cfg.getValueType(),
							cfg.getValueDataType(), cfg.getValue());
					request.setAttribute("map", map);
				} else if (cfg.getValueType().name().equals(ConfigAction.LSIT)) {
					List<Object> list;
					list = (List<Object>) ConfigUtil.XmlStrToObject(
							cfg.getValueType(), cfg.getValueDataType(),
							cfg.getValue());
					request.setAttribute("list", list);
				} else if (cfg.getValueType().name()
						.equals(ConfigAction.STRUCTURE)) {
					Map<String, Object> entityMap;
					entityMap = (Map<String, Object>) ConfigUtil
					.XmlStrToObject(cfg.getValueType(),
							cfg.getValueDataType(), cfg.getValue());
					Map<String, String> typeMap = new HashMap<String, String>();
					for (String key : entityMap.keySet()) {
						Object value = entityMap.get(key);
						String type = this.getObjType(value);
						typeMap.put(key, type);
					}
					request.setAttribute("entityMap", entityMap);
					request.setAttribute("typeMap", typeMap);
				}
			} catch (DocumentException e) {
				logger.error("", e);
			}
			request.setAttribute("config", config);

			return "config/copy-edit";
		} else
			return "fail";

	}





	public void setManagementFacade(ConfigManagementFacade managementFacade) {
		this.managementFacade = managementFacade;
	}

	public void setQueryFacade(ConfigQueryFacade queryFacade) {
		this.queryFacade = queryFacade;
	}
}
