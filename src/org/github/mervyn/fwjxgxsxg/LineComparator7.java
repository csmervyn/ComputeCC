package org.github.mervyn.fwjxgxsxg;

import java.util.Comparator;

public class LineComparator7 implements Comparator<SingleColumnLine> {

	@Override
	public int compare(SingleColumnLine line1, SingleColumnLine line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			if(line1.getNum() == line2.getNum()){
				return 0;
			}else if(line1.getNum() < line2.getNum()){
				return -1;
			}else{
				return 1;
			}
		}
	}

}
