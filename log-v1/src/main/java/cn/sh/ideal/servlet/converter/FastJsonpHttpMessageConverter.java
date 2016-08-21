package cn.sh.ideal.servlet.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;

import cn.sh.ideal.servlet.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

public class FastJsonpHttpMessageConverter extends FastJsonHttpMessageConverter {
	@Override
	public void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		if (obj instanceof JSONPObject) {
			JSONPObject jsonp = (JSONPObject) obj;
			OutputStream out = outputMessage.getBody();
			String text = jsonp.getFunction() + "(" + JSON.toJSONString(jsonp.getParameters(), this.getFeatures()) + ")";
			out.write(text.getBytes(this.getCharset()));
		} else if (obj instanceof JsonResultData) {
			try {
				super.writeInternal(obj, outputMessage);
			} catch (Exception e) {
				super.writeInternal(JsonResultData.wrapper(HttpStatus.CODE_500, null, "数据请求错误[" + e.getMessage() + "]"), outputMessage);
			}
		} else {
			super.writeInternal(obj, outputMessage);
		}
	}

	@Override
	public void setFeatures(SerializerFeature... features) {
		super.setFeatures(features);
	}

	@Override
	public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
		super.setSupportedMediaTypes(supportedMediaTypes);
	}

}
