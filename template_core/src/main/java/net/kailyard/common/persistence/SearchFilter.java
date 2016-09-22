package net.kailyard.common.persistence;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import net.kailyard.common.utils.Constants;
import org.apache.shiro.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 搜索条件组合
 *
 */
public class SearchFilter {
	public enum Operator {
		EQ, NE, LIKE, NOTLIKE, GT, LT, GTE, LTE, IN, NOTIN, ISNULL, ISNOTNULL, BETWEENLONG, ISTRUE, ISFALSE
	}

	public String fieldName;
	public Object value;
	public Operator operator;

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 *
	 * <searchParams中key的格式为OPERATOR_FIELDNAME><br />
	 * <功能详细描述>
	 *
	 * @param searchParams
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static Map<String, SearchFilter> parse(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = Maps.newHashMap();

		if (!CollectionUtils.isEmpty(searchParams)) {
			for (Entry<String, Object> entry : searchParams.entrySet()) {
				// 过滤掉空值
				String key = entry.getKey();
				if (Strings.isNullOrEmpty(key)) {
					continue;
				}
				Object value = entry.getValue();
				if (null == value || "".equals(value)) {
					continue;
				}

				// 拆分operator与filedAttribute
				List<String> names = Splitter.on(Constants.SEP_UNDERLINE).omitEmptyStrings().splitToList(key);
				if (names.size() != 2) {
					throw new IllegalArgumentException(
							key + " is not a valid search filter name.");
				}
				Operator operator = Operator.valueOf(names.get(0));
				String filedName = names.get(1);

				// 创建searchFilter
				SearchFilter filter = new SearchFilter(filedName, operator,
						value);
				filters.put(key, filter);
			}
		}
		return filters;
	}
}
