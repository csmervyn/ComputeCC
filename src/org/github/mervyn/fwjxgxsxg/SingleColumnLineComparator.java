package org.github.mervyn.fwjxgxsxg;

import java.math.BigDecimal;
import java.util.Comparator;

public class SingleColumnLineComparator implements Comparator<SingleColumnLine> {
	private int columnId;
	public SingleColumnLineComparator(int columnId){
		this.columnId = columnId;
	}
	
	@Override
	public int compare(SingleColumnLine line1, SingleColumnLine line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			BigDecimal data1 = null;
			if(line1.getColumnList().get(columnId).isLong()){
				data1 = new BigDecimal(line1.getColumnList().get(columnId).getLongValue());
			}else{
				data1 = new BigDecimal(line1.getColumnList().get(columnId).getDoubleValue());
			}
			BigDecimal data2 = null;
			if(line2.getColumnList().get(columnId).isLong()){
				data2 = new BigDecimal(line2.getColumnList().get(columnId).getLongValue());
			}else{
				data2 = new BigDecimal(line2.getColumnList().get(columnId).getDoubleValue());
			}
			if(data1.compareTo(data2) == 0){
				return 0;
			}if(data1.compareTo(data2) < 0){
				return -1;
			}else{
				return 1;
			}
		}
	}

}
