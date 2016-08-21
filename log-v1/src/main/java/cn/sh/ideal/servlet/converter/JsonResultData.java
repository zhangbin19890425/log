package cn.sh.ideal.servlet.converter;

import cn.sh.ideal.servlet.HttpStatus;

/**
 * @ClassName: JSON 数据对象
 * @author: Administrator
 * @date: 2015年9月7日 下午2:48:47
 */
public class JsonResultData {

	private String code = HttpStatus.CODE_200;
	private String description;
	private Object data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public JsonResultData() {}

	public JsonResultData(Object data) {
		this.data = data;
	}

	public JsonResultData(String code, Object data) {
		this.code = code;
		this.data = data;
	}

	public JsonResultData(String code, Object data, String description) {
		this.code = code;
		this.description = description;
		this.data = data;
	}

	public static JsonResultData wrapper(Object data) {
		return new JsonResultData(data);
	}

	public static JsonResultData wrapper(String code, Object data) {
		return new JsonResultData(code, data);
	}

	public static JsonResultData wrapper(String code, Object data, String description) {
		return new JsonResultData(code, data, description);
	}
}
