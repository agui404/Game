/*
 * Copyright (c) 2020 falcon4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.mamobet.game.common;

import cn.hutool.core.date.DatePattern;
import cn.mamobet.game.common.jackson.BaseEnumDeserializer;
import cn.mamobet.game.common.jackson.BaseEnumSerializer;
import cn.mamobet.game.common.jackson.JavaTimeModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * JacksonConfig
 *
 * @author lengleng
 * @author L.cm
 * @author lishangbu
 * @date 2020-06-17
 */
@Configuration
public class JacksonConfiguration {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> {
			builder.locale(Locale.CHINA);
			builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
			builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
			builder.serializerByType(Long.class, ToStringSerializer.instance);
			builder.serializerByType(BaseEnum.class, new BaseEnumSerializer());
			builder.deserializerByType(Enum.class, new BaseEnumDeserializer());
			builder.modules(new JavaTimeModule());
		};
	}

}
