package com.elndu.transform.utils;

import com.elndu.transform.bean.PropertySource;
import com.elndu.transform.enums.CommonExceptionEnum;
import com.elndu.transform.exception.CommonException;
import org.apache.commons.lang3.StringUtils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;

import java.util.*;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author elndu
 * @version 1.0, 2020/7/9 20:21
 * @since JDK 1.8
 */
public class YamlUtils
{

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param propertyMap :
     * @return java.lang.String
     * elndu  2020/9/21 - 11:23
     **/
    public static String propertyToString(Map<String, Object> propertyMap)
    {
        StringBuffer sb = new StringBuffer();
        if (null != propertyMap || propertyMap.size() > 0)
        {
            Set<Map.Entry<String, Object>> entries = propertyMap.entrySet();
            for (Map.Entry<String, Object> entry : entries)
            {
                sb.append(entry.getKey() + " = " + entry.getValue() + "\n");
            }
        }

        return sb.toString();
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param content :
     * @return boolean
     * elndu  2020/7/17 - 9:19
     **/
    public static boolean checkYaml(String content)
    {
        if (StringUtils.isBlank(content))
        {
            return true;
        }
        try
        {
            Yaml yaml = new Yaml();
            Map yamMap = yaml.load(content);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new CommonException(CommonExceptionEnum.YAML_COVERT_ERROR);
        }

        return true;
    }

    /**
     * [简要描述]:yaml的string转化为properties的Map
     * [详细描述]:<br/>
     *
     * @param ymlStr :
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * elndu  2020/7/10 - 9:42
     **/
    public static Map<String, Object> convertToPropertiesMap(String ymlStr)
    {
        if (StringUtils.isBlank(ymlStr))
        {
            return new HashMap<>();
        }
        Yaml yaml = new Yaml();
        Map yamMap = (Map) yaml.load(ymlStr);
        Map<String, Object> propertiesMap = convertToProperties(yamMap);
        return propertiesMap;
    }

    /**
     * [简要描述]:yaml格式转化为properties格式map
     * [详细描述]:<br/>
     *
     * @param ymlStr :
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * elndu  2020/7/9 - 12:45
     **/
    public static Map<String, Object> convertToPropertyMap(String ymlStr)
    {
        Map<String, Object> resultMap = new HashMap<>();
        Yaml yaml = new Yaml();
        try
        {
            Map yamMap = (Map) yaml.load(ymlStr);
            String rootKeyName = "";
            topropertyMap(rootKeyName, yamMap, resultMap);
        }
        catch (Exception e)
        {
            throw new CommonException(CommonExceptionEnum.COVERT_TO_YAML_MAP_FAIL);
        }
        return resultMap;
    }

    /**
     * [简要描述]:将yaml格式的map转化为properties格式的Map
     * [详细描述]:<br/>
     *
     * @param keyName :
     * @param yamMap :
     * @param resultMap :
     * @return void
     * elndu  2020/7/9 - 12:46
     **/
    private static void topropertyMap(String keyName, Map yamMap, Map<String, Object> resultMap)
    {
        String orginalKeyName = keyName;
        Set<String> set = yamMap.keySet();
        for (String key : set)
        {
            keyName = orginalKeyName;
            if (StringUtils.isNotBlank(keyName))
            {
                keyName = keyName + "." + key;
            }
            else
            {
                keyName = key;
            }
            if (yamMap.get(key) instanceof Map)
            {
                topropertyMap(keyName, (Map) yamMap.get(key), resultMap);
            }
            else
            {
                resultMap.put(keyName, yamMap.get(key));
            }
        }
    }

    public static Map<String, Object> convertToYMLMap(Map<String, Object> propertiesMap)
    {
        Map<String, Object> properties = convertToProperties(propertiesMap);
        Map<String, Object> rootMap = new LinkedHashMap();
        Iterator var4 = properties.entrySet().iterator();

        while (var4.hasNext())
        {
            Map.Entry<String, Object> entry = (Map.Entry) var4.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            YamlUtils.PropertyNavigator nav = new YamlUtils.PropertyNavigator(key);
            nav.setMapValue(rootMap, value);
        }
        return rootMap;
    }

    public static Map<String, Object> convertToProperties(Map<String, Object> propertiesMap)
    {
        Map<String, Map<String, Object>> map = new LinkedHashMap();
        PropertySource propertySource = new PropertySource("covertTo", propertiesMap);
        List<PropertySource> sources = new ArrayList();
        sources.add(propertySource);
        Collections.reverse(sources);
        Map<String, Object> combinedMap = new TreeMap();
        Iterator var5 = sources.iterator();

        label47:
        while (var5.hasNext())
        {
            PropertySource source = (PropertySource) var5.next();
            Map<?, ?> value = source.getSource();
            Iterator var8 = value.keySet().iterator();

            while (true)
            {
                while (true)
                {
                    if (!var8.hasNext())
                    {
                        continue label47;
                    }

                    String key = (String) var8.next();
                    if (!key.contains("["))
                    {
                        combinedMap.put(key, value.get(key));
                    }
                    else
                    {
                        key = key.substring(0, key.indexOf("["));
                        Map<String, Object> filtered = new TreeMap();
                        Iterator var11 = value.keySet().iterator();

                        while (var11.hasNext())
                        {
                            String index = (String) var11.next();
                            if (index.startsWith(key + "["))
                            {
                                filtered.put(index, value.get(index));
                            }
                        }

                        map.put(key, filtered);
                    }
                }
            }
        }

        var5 = map.entrySet().iterator();

        while (var5.hasNext())
        {
            Map.Entry<String, Map<String, Object>> entry = (Map.Entry) var5.next();
            combinedMap.putAll((Map) entry.getValue());
        }

        postProcessProperties(combinedMap);
        return combinedMap;
    }

    private static void postProcessProperties(Map<String, Object> propertiesMap)
    {
        Iterator iter = propertiesMap.keySet().iterator();

        while (iter.hasNext())
        {
            String key = (String) iter.next();
            if (key.equals("spring.profiles"))
            {
                iter.remove();
            }
        }

    }

    private static class PropertyNavigator
    {
        private final String propertyKey;
        private int currentPos;
        private YamlUtils.PropertyNavigator.NodeType valueType;

        private PropertyNavigator(String propertyKey)
        {
            this.propertyKey = propertyKey;
            this.currentPos = -1;
            this.valueType = YamlUtils.PropertyNavigator.NodeType.MAP;
        }

        private void setMapValue(Map<String, Object> map, Object value)
        {
            String key = this.getKey();
            Object list;
            if (YamlUtils.PropertyNavigator.NodeType.MAP.equals(this.valueType))
            {
                list = (Map) map.get(key);
                if (list == null)
                {
                    list = new LinkedHashMap();
                    map.put(key, list);
                }

                this.setMapValue((Map) list, value);
            }
            else if (YamlUtils.PropertyNavigator.NodeType.ARRAY.equals(this.valueType))
            {
                list = (List) map.get(key);
                if (list == null)
                {
                    list = new ArrayList();
                    map.put(key, list);
                }

                this.setListValue((List) list, value);
            }
            else
            {
                map.put(key, value);
            }

        }

        private void setListValue(List<Object> list, Object value)
        {
            int index = this.getIndex();

            while (list.size() <= index)
            {
                list.add((Object) null);
            }

            Object nestedList;
            if (YamlUtils.PropertyNavigator.NodeType.MAP.equals(this.valueType))
            {
                nestedList = (Map) list.get(index);
                if (nestedList == null)
                {
                    nestedList = new LinkedHashMap();
                    list.set(index, nestedList);
                }

                this.setMapValue((Map) nestedList, value);
            }
            else if (YamlUtils.PropertyNavigator.NodeType.ARRAY.equals(this.valueType))
            {
                nestedList = (List) list.get(index);
                if (nestedList == null)
                {
                    nestedList = new ArrayList();
                    list.set(index, nestedList);
                }

                this.setListValue((List) nestedList, value);
            }
            else
            {
                list.set(index, value);
            }

        }

        private int getIndex()
        {
            int start = this.currentPos + 1;

            int index;
            for (index = start; index < this.propertyKey.length(); ++index)
            {
                char c = this.propertyKey.charAt(index);
                if (c == ']')
                {
                    this.currentPos = index;
                    break;
                }

                if (!Character.isDigit(c))
                {
                    throw new IllegalArgumentException("Invalid key: " + this.propertyKey);
                }
            }

            if (this.currentPos >= start && this.currentPos != start)
            {
                index = Integer.parseInt(this.propertyKey.substring(start, this.currentPos));
                ++this.currentPos;
                if (this.currentPos == this.propertyKey.length())
                {
                    this.valueType = YamlUtils.PropertyNavigator.NodeType.LEAF;
                }
                else
                {
                    switch (this.propertyKey.charAt(this.currentPos))
                    {
                        case '.':
                            this.valueType = YamlUtils.PropertyNavigator.NodeType.MAP;
                            break;
                        case '[':
                            this.valueType = YamlUtils.PropertyNavigator.NodeType.ARRAY;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid key: " + this.propertyKey);
                    }
                }

                return index;
            }
            else
            {
                throw new IllegalArgumentException("Invalid key: " + this.propertyKey);
            }
        }

        private String getKey()
        {
            int start = this.currentPos + 1;

            for (int i = start; i < this.propertyKey.length(); ++i)
            {
                char currentChar = this.propertyKey.charAt(i);
                if (currentChar == '.')
                {
                    this.valueType = YamlUtils.PropertyNavigator.NodeType.MAP;
                    this.currentPos = i;
                    break;
                }

                if (currentChar == '[')
                {
                    this.valueType = YamlUtils.PropertyNavigator.NodeType.ARRAY;
                    this.currentPos = i;
                    break;
                }
            }

            if (this.currentPos < start)
            {
                this.currentPos = this.propertyKey.length();
                this.valueType = YamlUtils.PropertyNavigator.NodeType.LEAF;
            }
            else if (this.currentPos == start)
            {
                throw new IllegalArgumentException("Invalid key: " + this.propertyKey);
            }

            return this.propertyKey.substring(start, this.currentPos);
        }

        private static enum NodeType
        {
            LEAF,
            MAP,
            ARRAY;

            private NodeType()
            {
            }
        }
    }
}
