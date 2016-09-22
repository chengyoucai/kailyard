package net.kailyard.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;/**
 *
 * <grid对象><br />
 * <功能详细描述>
 *
 */
public class Grid<T> implements Serializable {
    private static final long serialVersionUID = 2114966363322329660L;
    /**
     * 总行数
     */
    private Long total = 0L;
    /**
     * 结果集
     */
    private List<T> rows = new ArrayList<T>();

    public Grid() {
        super();
    }

    public Grid(Long total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
