package cn.sh.ideal.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		HashMap<Integer, List<Object>> bardatas = new HashMap<Integer, List<Object>>();
		String str = "123|1234";
		String[] vals = str.split("\\|");

		for (int i = 0; i < vals.length; i++) {
			List<Object> datalist = bardatas.get(i);
			if (datalist == null) {
				datalist = new ArrayList<Object>();
				if ("Double".equals("Double")) {
					datalist.add(Double.parseDouble(vals[i]));
				} else {
					datalist.add(Long.parseLong(vals[i]));
				}
				bardatas.put(i, datalist);
			} else {
				if ("Double".equals("Double")) {
					datalist.add(Double.parseDouble(vals[i]));
				} else {
					datalist.add(Long.parseLong(vals[i]));
				}
			}
		}

		String str1 = "234|456789";
		vals = str1.split("\\|");

		for (int i = 0; i < vals.length; i++) {
			List<Object> datalist = bardatas.get(i);
			if (datalist == null) {
				datalist = new ArrayList<Object>();
				if ("Double".equals("Double")) {
					datalist.add(Double.parseDouble(vals[i]));
				} else {
					datalist.add(Long.parseLong(vals[i]));
				}
				bardatas.put(i, datalist);
			} else {
				if ("Double".equals("Double")) {
					datalist.add(Double.parseDouble(vals[i]));
				} else {
					datalist.add(Long.parseLong(vals[i]));
				}
			}
		}

		for (Object o : bardatas.get(0)) {
			System.out.println((Double)o);
		}
	}
}
