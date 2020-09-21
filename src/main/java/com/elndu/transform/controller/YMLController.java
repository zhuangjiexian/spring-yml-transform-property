package com.elndu.transform.controller;

import com.elndu.transform.enums.CommonExceptionEnum;
import com.elndu.transform.exception.CommonException;
import com.elndu.transform.utils.YamlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.Property;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
public class YMLController
{

    /**
     * [简要描述]: yml转化为peoperty
     * [详细描述]:<br/>
     *
     * @param ymlString :
     * @return java.lang.String
     * elndu  2020/9/21 - 9:39
     **/
    @PostMapping("/transform/to/property")
    public String transformToProperty(@RequestParam("ymlString") String ymlString)
    {
        if (StringUtils.isBlank(ymlString))
        {
            throw new CommonException(CommonExceptionEnum.YAML_IS_NULL);
        }
        //将yml文件转化为property格式的map
        Map<String, Object> propertyMap = YamlUtils.convertToPropertyMap(ymlString);
        //转化为yml格式string
        Yaml yaml = new Yaml();
        return yaml.dumpAsMap(propertyMap);
    }

    /**
     * [简要描述]:property转化为yml
     * [详细描述]:<br/>
     *
     * @param propertyString :
     * @return java.lang.String
     * elndu  2020/9/21 - 14:37
     **/
    @PostMapping("/transform/to/yml")
    public String transformToYML(@RequestParam("propertyString") String propertyString)
    {
        if (StringUtils.isBlank(propertyString))
        {
            throw new CommonException(CommonExceptionEnum.PROPERTY_IS_NULL);
        }
        Map<String, Object> propertyMap = YamlUtils.convertToPropertyMap(propertyString);
        Map<String, Object> ymlMap = YamlUtils.convertToYMLMap(propertyMap);
        Yaml yaml = new Yaml();
        return yaml.dumpAsMap(ymlMap);
    }

    /**
     * [简要描述]:合并yml
     * [详细描述]:<br/>
     *
     * @param ymlTop :
     * @param ymlBottom :
     * @return java.lang.String
     * elndu  2020/9/21 - 14:40
     **/
    @PostMapping("/merge/yml")
    public String mergeYML(@RequestParam("ymlTop") String ymlTop, @RequestParam("ymlBottom") String ymlBottom)
    {
        if(StringUtils.isBlank(ymlTop) && StringUtils.isBlank(ymlBottom)){
            throw new CommonException(CommonExceptionEnum.MERGE_TEXT_IS_NULL);
        }
        //合并property的map
        Map<String,Object> mergePropertyMap = new HashMap<>();
        Map<String,Object> topPropertyMap = new HashMap<>();
        if(StringUtils.isNotBlank(ymlTop) && YamlUtils.checkYaml(ymlTop)){
            topPropertyMap = YamlUtils.convertToPropertyMap(ymlTop);
        }

        Map<String,Object> bottomPropertyMap = new HashMap<>();
        if(StringUtils.isNotBlank(ymlBottom) && YamlUtils.checkYaml(ymlBottom)){
            bottomPropertyMap = YamlUtils.convertToPropertyMap(ymlBottom);
        }

        //合并两个property的map可以去除重复key
        mergePropertyMap.putAll(topPropertyMap);
        mergePropertyMap.putAll(bottomPropertyMap);

        //转化为yml类型的map
        Map<String, Object> mergeYmlMap = YamlUtils.convertToYMLMap(mergePropertyMap);
        Yaml yaml = new Yaml();
        return yaml.dumpAsMap(mergeYmlMap);
    }
}
