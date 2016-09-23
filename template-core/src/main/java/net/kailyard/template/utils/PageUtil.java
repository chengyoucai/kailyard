package net.kailyard.template.utils;

import com.google.common.base.Preconditions;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 分页组织工具
 *
 */
public final class PageUtil {
    private PageUtil(){}

    /**
     * 创建分页请求.
     *
     * @param page
     * @param size
     * @return
     */
    public static PageRequest build(int page, int size) {
        return build(page, size, null);
    }

    /**
     * 创建分页请求,指定排序字段
     *
     * @param page
     * @param size
     * @param direction
     * @param orderBys
     * @return
     */
    public static PageRequest build(int page, int size,
            Sort.Direction direction, String... orderBys) {
        Sort sort = null;
        if (null != direction && ArrayUtils.isNotEmpty(orderBys)) {
            sort = new Sort(direction, orderBys);
        }
        return build(page, size, sort);
    }

    public static PageRequest build(int page, int size,
            String direction, String... orderBys) {
        Sort sort = null;
        if (null != direction && ArrayUtils.isNotEmpty(orderBys)) {
            sort = new Sort(Sort.Direction.fromStringOrNull(direction), orderBys);
        }
        return build(page, size, sort);
    }

    /**
     * 创建分页请求,指定排序字段,排序字段的排序规则各异
     *
     * @param page
     * @param size
     * @param directions
     * @param orderBys
     * @return
     */
    public static PageRequest build(int page, int size,
            List<Sort.Direction> directions, List<String> orderBys) {
        Sort sort = null;
        if (!CollectionUtils.isEmpty(directions)
                && !CollectionUtils.isEmpty(orderBys)) {
            Preconditions.checkArgument((directions.size() == orderBys.size()),
                    "directions 和 orderBys 大小必须一样.");
            for (int i = 0, len = directions.size(); i < len; i++) {
                if (i == 0) {
                    sort = new Sort(directions.get(i), orderBys.get(i));
                } else {
                    sort.and(new Sort(directions.get(i), orderBys.get(i)));
                }
            }
        }
        return build(page, size, sort);
    }

    /**
     * 创建分页请求
     *
     * @param page 必须大于1
     * @param size   必须大于1
     * @param sort
     * @return
     */
    public static PageRequest build(int page, int size,
            Sort sort) {
        Preconditions.checkArgument((page >= 1), "page 必须大于 1.");
        Preconditions.checkArgument((size >= 1), "size 必须大于 1.");

        return new PageRequest(page - 1, size, sort);
    }
}
