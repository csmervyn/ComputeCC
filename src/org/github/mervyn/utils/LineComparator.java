package org.github.mervyn.utils;

import java.util.Comparator;

public class LineComparator implements Comparator<Line> {
	private int groupId;
	public LineComparator(int groupId){
		this.groupId = groupId;
	}
	@Override
	public int compare(Line line1, Line line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			return line1.getGroupList().get(groupId).compareTo(line2.getGroupList().get(groupId));
		}
	}

}
