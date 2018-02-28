package org.github.mervyn.utils;

import java.util.Comparator;

public class LineComparator5 implements Comparator<Line> {
	private int groupId;
	public LineComparator5(int groupId){
		this.groupId = groupId;
	}
	@Override
	public int compare(Line line1, Line line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			double line1Abs3 = line1.getGroupList().get(groupId).getAbs5Value();
			double line2Abs3 = line2.getGroupList().get(groupId).getAbs5Value();
			if(line1Abs3 == line2Abs3){
				return 0;
			}if(line1Abs3 < line2Abs3){
				return -1;
			}else{
				return 1;
			}
		}
	}
}
