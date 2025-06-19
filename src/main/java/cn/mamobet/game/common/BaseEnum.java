package cn.mamobet.game.common;

public interface BaseEnum {
	/**
	 * 枚举值
	 */
	String value();

	/**
	 * 枚举说明
	 */
	String desc();


	static <T extends BaseEnum> T getEnum(Class<T> type, String value) {
		T[] objs = type.getEnumConstants();
		for (T em : objs) {
			if (em.value().equals(value)) {
				return em;
			}
		}
		return null;
	}

}
