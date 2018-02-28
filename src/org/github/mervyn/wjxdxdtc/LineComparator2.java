package org.github.mervyn.wjxdxdtc;

import java.util.Comparator;

import org.github.mervyn.utils.Line;

public class LineComparator2 implements Comparator<Line> {
	private int groupId;
	public LineComparator2(int groupId){
		this.groupId = groupId;
	}
	@Override
	public int compare(Line line1, Line line2) {
		if(line1 == null || line2 == null){
			return 0;
		}else{
			double line1Abs2 = line1.getGroupList().get(groupId).getMulBiaoZhun();
			double line2Abs2 = line2.getGroupList().get(groupId).getMulBiaoZhun();
			if(line1Abs2 == line2Abs2){
				return 0;
			}if(line1Abs2 < line2Abs2){
				return -1;
			}else{
				return 1;
			}
		}
	}

}
