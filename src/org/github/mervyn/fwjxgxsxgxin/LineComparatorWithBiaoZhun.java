package org.github.mervyn.fwjxgxsxgxin;

import java.util.Comparator;

import org.github.mervyn.fwjxgxsxg.SingleColumnLine;

public class LineComparatorWithBiaoZhun implements Comparator<SingleColumnLine> {

	@Override
	public int compare(SingleColumnLine line1, SingleColumnLine line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			if(line1.getB() == line2.getB()){
				return 0;
			}else if(line1.getB() < line2.getB()){
				return -1;
			}else{
				return 1;
			}
		}
	}

}
