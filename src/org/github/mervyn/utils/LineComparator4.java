package org.github.mervyn.utils;

import java.util.Comparator;

public class LineComparator4 implements Comparator<Line> {
	private int groupId;
	public LineComparator4(int groupId){
		this.groupId = groupId;
	}
	@Override
	public int compare(Line line1, Line line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			double line1Average = line1.getGroupList().get(groupId).getAverage();
			double line2Average = line2.getGroupList().get(groupId).getAverage();
			if(line1Average == line2Average){
				return 0;
			}if(line1Average < line2Average){
				return -1;
			}else{
				return 1;
			}
		}
	}
}
