package net.kailyard.common.utils;

/**
 * 异常工具类
 */
public class Exceptions {
	/**
	 * 
	 * 将CheckedException转换为RuntimeException
	 */
	public static RuntimeException unchecked(Throwable ex) {
		if (ex instanceof RuntimeException) {
			return (RuntimeException) ex;
		} else {
			return new RuntimeException(ex);
		}
	}


}
