package cn.mamobet.game.common.jackson;

import cn.mamobet.game.common.BaseEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 枚举类型json数据返回 desc
 */
public class BaseEnumSerializer extends JsonSerializer<BaseEnum> {
	@Override
	public void serialize(BaseEnum baseEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
		jsonGenerator.writeString(String.valueOf(baseEnum.value()));
		jsonGenerator.writeStringField(jsonGenerator.getOutputContext().getCurrentName() + "Label", baseEnum.desc());
	}
}
