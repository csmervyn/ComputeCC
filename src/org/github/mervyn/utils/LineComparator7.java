package org.github.mervyn.utils;

import java.util.Comparator;

public class LineComparator7 implements Comparator<Line> {

	@Override
	public int compare(Line line1, Line line2) {
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
