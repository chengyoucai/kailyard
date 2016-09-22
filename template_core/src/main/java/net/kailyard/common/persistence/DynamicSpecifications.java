package net.kailyard.common.persistence;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.kailyard.common.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class DynamicSpecifications {
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicSpecifications.class);
	private DynamicSpecifications(){}

    @SuppressWarnings("unchecked")
	public static <T> Specification<T> bySearchFilter(
			final Collection<SearchFilter> filters, final Class<T> clazz) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				if (!CollectionUtils.isEmpty(filters)) {
					List<Predicate> predicates = Lists.newArrayList();
					for (SearchFilter filter : filters) {
						// nested path translate, 如Task的名为"user.name"的filedName,
						// 转换为Task.user.name属性
						List<String> names = Splitter.on(Constants.SEP_DOT).omitEmptyStrings().splitToList(filter.fieldName);

						//						String[] names = StringUtils.split(,
						//								Constants.SEP_DOT);
						Path<Object> expression = root.get(names.get(0));
						for (int i = 1, len = names.size(); i < len; i++) {
							expression = expression.get(names.get(i));
						}

						// logic operator
						switch (filter.operator) {
							case EQ:
								predicates.add(builder.equal(expression,
										filter.value));
								break;
							case NE:
								predicates.add(builder.notEqual(expression,
										filter.value));
								break;
							case LIKE:
								predicates.add(builder.like(
										expression.as(String.class),
										"%" + filter.value + "%"));
								break;
							case NOTLIKE:
								predicates.add(builder.notLike(
										expression.as(String.class),
										"%" + filter.value + "%"));
								break;
							case GT:
								predicates.add(
										builder.gt(expression.as(Number.class),
												(Number) filter.value));
								break;
							case LT:
								predicates.add(
										builder.lt(expression.as(Number.class),
												(Number) filter.value));
								break;
							case GTE:
								predicates.add(
										builder.ge(expression.as(Number.class),
												(Number) filter.value));
								break;
							case LTE:
								predicates.add(
										builder.le(expression.as(Number.class),
												(Number) filter.value));
								break;
							case IN:
								List<String> values_1 = Splitter.on(Constants.SEP_COMMA).omitEmptyStrings().splitToList(filter.value.toString());

								//								String[] values_1 = StringUtils.split(
								//										filter.value.toString(),
								//										Constants.SEP_COMMA);
								predicates.add(expression.in(values_1));
								break;
							case NOTIN:
								List<String> values_2 = Splitter.on(Constants.SEP_COMMA).omitEmptyStrings().splitToList(filter.value.toString());
								//								String[] values_2 = StringUtils.split(
								//										filter.value.toString(),
								//										Constants.SEP_COMMA);
								predicates.add(builder
										.not(expression.in(values_2)));
								break;
							case ISNULL:
								predicates.add(builder.isNull(expression));
								break;
							case ISNOTNULL:
								predicates.add(builder.isNotNull(expression));
								break;
							case BETWEENLONG:
								try{
									List<String> v = Splitter.on(Constants.SEP_COMMA).omitEmptyStrings().splitToList(filter.value.toString());

									//                                    String[] v = StringUtils.split(
									//                                            filter.value.toString(),
									//                                            Constants.SEP_COMMA);
									if(v.size()!=2){
										throw new NumberFormatException("value:[" + filter.value.toString() + "] must divided by comma.");
									}
									Long l1 = Long.parseLong(v.get(0));
									Long l2 = Long.parseLong(v.get(1));
									predicates.add(
											builder.ge(expression.as(Long.class),
													l1));
									predicates.add(
											builder.le(expression.as(Long.class),
													l2));
								} catch (NumberFormatException e){
									LOGGER.warn("BETWEEN must be long,filter.value[{}]", filter.value);
								}
								break;
							case ISTRUE:
								predicates.add(builder.isTrue(expression.as(Boolean.class)));
								break;
							case ISFALSE:
								predicates.add(builder.isTrue(expression.as(Boolean.class)));
								break;
							default:
								break;
						}
					}

					// 将所有条件用 and 联合起来
					if (!CollectionUtils.isEmpty(predicates)) {
						return builder.and(predicates
								.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}

    @SuppressWarnings("unchecked")
	public static <T> Specification<T> buildSpecification(
			Map<String, Object> searchParams, Class<T> t) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec = (Specification<T>) DynamicSpecifications
				.bySearchFilter(filters.values(), t.getClass());
		return spec;
	}
}
