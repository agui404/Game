package cn.mamobet.game.common.jackson;

import cn.hutool.core.util.StrUtil;
import cn.mamobet.game.common.BaseEnum;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

@Slf4j
public class BaseEnumDeserializer extends JsonDeserializer<Enum> {

	@Override
	public Enum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		JsonNode node = p.getCodec().readTree(p);
		String currentName = p.currentName();
		Object currentValue = p.getCurrentValue();
		Class findPropertyType = null;
		findPropertyType = BeanUtils.findPropertyType(currentName, currentValue.getClass());
		if (findPropertyType == null) {
			log.info("在" + currentValue.getClass() + "实体类中找不到" + currentName + "字段");
			return null;

		}
		String asText = null;
		asText = node.asText();
		if (StrUtil.isBlank(asText)) {
			return null;
		}

		if (BaseEnum.class.isAssignableFrom(findPropertyType)) {
			BaseEnum valueOf = null;
			if (StrUtil.isNotBlank(asText)) {
				valueOf = BaseEnum.getEnum(findPropertyType, asText);
			}
			if (valueOf != null) {
				return (Enum) valueOf;
			}
		}
		return Enum.valueOf(findPropertyType, asText);
	}
}
