package cn.mamobet.game.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;


public class CommonEnum {
    /**
     * 环境配置
     */
    @Getter
    public enum Environment implements BaseEnum {
        DEV("dev", "开发"), TEST("test", "测试"), DEMO("demo", "演示"), PROD("prod", "生产"),
        ;
        @EnumValue
        @JsonValue
        private final String value;
        private final String desc;

        Environment(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonCreator
        public Environment valOf(String value) {
            for (Environment item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        public static Environment toEnumObject(String value) {
            for (Environment item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public String desc() {
            return desc;
        }
    }


    /**
     * 是否
     */
    @Getter
    public enum YesOrNo implements BaseEnum {
        NO("0", "否"), YES("1", "是");
        @EnumValue
        @JsonValue
        private final String value;
        private final String desc;

        YesOrNo(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonCreator
        public YesOrNo valOf(String value) {
            for (YesOrNo item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        public static YesOrNo toEnumObject(String value) {
            for (YesOrNo item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public String desc() {
            return desc;
        }
    }


    /**
     * 计算公式
     */
    @Getter
    public enum CalcMode  implements BaseEnum {
        BY_PAYOUT("1", "根据派彩值计算"),
        BY_WIN_RATE  ("2", "获胜几率计算");
        @EnumValue
        @JsonValue
        private final String value;
        private final String desc;

        CalcMode(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonCreator
        public CalcMode valOf(String value) {
            for (CalcMode item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        public static CalcMode toEnumObject(String value) {
            for (CalcMode item : values()) {
                if (item.getValue().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public String desc() {
            return desc;
        }
    }

}
