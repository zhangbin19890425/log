package cn.sh.ideal.web.tomcat.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class ResultMapUtil {
	
	/**
	 * 响应时间统计图处理，补全24小时
	 * @param map
	 * @return
	 */
	public static Map<String,String> ResTime(Map<String,String> map){
		Map<String,String> resultMap = new TreeMap<String, String>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int a1 = Integer.valueOf(o1.replaceAll("点", ""));
				int a2 = Integer.valueOf(o2.replaceAll("点", ""));
				return a1>a2?1:-1;
			}
		});
		
		for(int i=1;i<=24;i++){
			if(!map.containsKey(String.valueOf(i))){
				map.put(String.valueOf(i), "0");
			}
		}
		for (Entry<String, String> entry : map.entrySet()) {
			resultMap.put(entry.getKey()+"点", entry.getValue());
		}
		return resultMap;
		
	}
	
	   public static Object mapToObject(Map<String, Object> map, Class<?> beanClass)  {    
	        if (map == null)  
	            return null;  
	  
	        Object obj = null;
			try {
				obj = beanClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	  
	        try {
				org.apache.commons.beanutils.BeanUtils.populate(obj, map);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	  
	        return obj;  
	    }    
	
	
}
