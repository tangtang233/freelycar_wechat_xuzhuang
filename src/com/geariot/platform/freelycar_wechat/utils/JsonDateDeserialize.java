package com.geariot.platform.freelycar_wechat.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonDateDeserialize extends JsonDeserializer<Date> {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final Logger log = LogManager.getLogger(JsonDateDeserialize.class);
	
	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext arg1) throws IOException, JsonProcessingException {
		try {
			return FORMAT.parse(jsonParser.getText());
		} catch (ParseException e) {
			e.printStackTrace();
			log.debug("日期反序列化失败");
			throw new RuntimeException(e);
		}
	}

}
