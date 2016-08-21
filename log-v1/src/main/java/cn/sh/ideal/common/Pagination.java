package cn.sh.ideal.common;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.PageInfo;

/**
 * 
 * 分页
 *
 */
@SuppressWarnings("serial")
public class Pagination implements Serializable {
	private long total;
	private List<?> rows;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public static <E> Pagination build(List<E> rows) {
		Pagination page = new Pagination();
		page.setRows(rows);
		page.setTotal(new PageInfo<E>(rows).getTotal());
		return page;
	}
}
