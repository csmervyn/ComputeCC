package org.github.mervyn.fwjtjxg;

import java.math.BigDecimal;
import java.util.Comparator;

import org.github.mervyn.fwjxgxsxg.SingleColumnLine;

public class LineComparator implements Comparator<SingleColumnLine> {

	@Override
	public int compare(SingleColumnLine line1, SingleColumnLine line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			BigDecimal former = new BigDecimal(line1.getbBiaoMulXibiao());
			BigDecimal latter = new BigDecimal(line2.getbBiaoMulXibiao());
			if(former.compareTo(latter) == 0){
				return 0;
			}else if(former.compareTo(latter) < 0){
				return -1;
			}else{
				return 1;
			}
		}
	}

}
