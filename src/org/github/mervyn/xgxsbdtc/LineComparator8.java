package org.github.mervyn.xgxsbdtc;

import java.util.Comparator;


public class LineComparator8 implements Comparator<Line> {
	private int groupId;
	public LineComparator8(int groupId){
		this.groupId = groupId;
	}
	@Override
	public int compare(Line line1, Line line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			Double biaoZhun1 = line1.getGroupList().get(groupId).getBiaoZhun();
			Double biaoZhun2 = line2.getGroupList().get(groupId).getBiaoZhun();
			if(biaoZhun1.compareTo(biaoZhun2) == 0){
				return 0;
			}if(biaoZhun1.compareTo(biaoZhun2) < 0){
				return -1;
			}else{
				return 1;
			}
		}
	}

}
