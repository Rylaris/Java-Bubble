package cn.campsg.practical.bubble.common;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import cn.campsg.practical.bubble.entity.Position;
import cn.campsg.practical.bubble.entity.Star;
import cn.campsg.practical.bubble.entity.StarList;
import cn.campsg.practical.bubble.entity.Star.StarType;
import cn.campsg.practical.bubble.service.StarService;

/**
 * 
 * ���幤���࣬�ṩ���¹��ܣ�<br>
 * <ul>
 * <li>1. ��Label��ͼ����ת��Ϊ�����Ƕ���</li>
 * <li>2. �������Ƕ���ת��ΪLabel��ͼ����</li>
 * <li>3. ������д������������ǵ�����</li>
 * </ul>
 * 
 * 
 * 
 * @author Frank.Chen
 * @version 1.5
 */
public final class StarFormUtils {

	/* ������ͼ���� */
	public static final int STAR_WIDTH = 48;

	/* ������ͼ��߶� */
	public static final int STAR_HEIGHT = 48;

	/* ���������ƶ����� */
	public static final int SCORE_MOVE_DISTANCE = 230;

	public static List<String> mStarStylesheets = null;

	static {
		mStarStylesheets = new ArrayList<String>();
		mStarStylesheets.add("blue_star");
		mStarStylesheets.add("green_star");
		mStarStylesheets.add("yellow_star");
		mStarStylesheets.add("red_star");
		mStarStylesheets.add("purple_star");
	}

	/**
	 * ��Label��ͼת��Ϊ�����Ƕ���
	 * 
	 * @param starFrame
	 *            Label��ͼ
	 * @return �����Ƕ���
	 * 
	 * @see List<Label> convert(StarList stars, StartEventHandler handler)
	 */
	public static Star convert(Label starFrame) {
		Star star = new Star();

		// ���Label��ͼ�ڲ����ж�Ӧ���к��У�Label��ͼ�����������ڳ�ʼ��ʱ�ĸ�ֵ
		int row = Integer.parseInt(starFrame.getUserData().toString()
				.split("[;]")[0]);
		int col = Integer.parseInt(starFrame.getUserData().toString()
				.split("[;]")[1]);
		star.setPosition(new Position(row, col));

		// ������ʽ
		int styleIndex = mStarStylesheets.indexOf(starFrame.getStyleClass()
				.get(starFrame.getStyleClass().size() - 1));
		star.setType(StarType.valueOf(styleIndex));

		return star;
	}
	
//	/**
//	 * �������Ǽ���ת��ΪLabel��ͼ����
//	 * 
//	 * @param stars �����Ǽ���
//	 * @return Label��ͼ����
//	 */
//	public static List<Label> convert(StarList stars) {
//		return convert(stars, null);
//	}

	/**
	 * �������Ǽ���ת��ΪLabel��ͼ���ϣ����������¼�����ֲ�룩
	 * 
	 * @param stars �����Ǽ���
	 * @return Label��ͼ����
	 */
	public static List<Label> convert(StarList stars) {

		List<Label> starFrames = new ArrayList<Label>();

		int row = 0;
		int col = 0;

		for (int i = 0; i < stars.size(); i++) {
			if(stars.get(i) == null)
				continue;
			
			// ��ȡStar��������
			row = stars.get(i).getPosition().getRow();
			col = stars.get(i).getPosition().getColumn();

			if (row < 0 || row >= StarService.MAX_ROW_SIZE)
				continue;

			if (col < 0 || col >= StarService.MAX_COLUMN_SIZE)
				continue;

			// ����������Label��ͼ�ĳ����
			Label starFrame = new Label();
			starFrame.setPrefWidth(STAR_WIDTH);
			starFrame.setPrefHeight(STAR_HEIGHT);

//			// ���õ���¼�
//			if (handler != null)
//				starFrame.setOnMouseClicked(handler);

			// Label��ͼ��IDΪs+�к�+�кţ����磺s00,s01��
			starFrame.setId("s" + row + col);
			// ΪLabel����һ���̶��������ԣ����ڱ�ǵ�ǰLabel�������У������ж�
			starFrame.setUserData(row + ";" + col);
			// ����Label������
			starFrame.setLayoutX(col * STAR_WIDTH);
			starFrame.setLayoutY(row * STAR_HEIGHT);
			// ���������ǵ���ʽ
			starFrame.getStyleClass().add(
					mStarStylesheets.get(stars.get(i).getType().value()));

			// ������б�
			starFrames.add(starFrame);
		}

		return starFrames;
	}
	
	/**
	 * �ӽ���������������Ѱ��ָ�������Ƕ�Ӧ��Label��ͼ����
	 * 
	 * @param stars			ָ���������Ƕ��󼯺�
	 * @param parent		��������������
	 * @return					Label��ͼ���󼯺�
	 */
	public static List<Label> findFrames(StarList stars, Pane parent) {
		List<Label> starFrames = new ArrayList<Label>();

		for (int i = 0; i < stars.size(); i++) {
			
			Label label = findFrame(stars.get(i), parent);

			if (label != null)
				starFrames.add(label);
		}

		return starFrames;
	}

	/**
	 * �ӽ���������������Ѱ��ָ�������Ƕ�Ӧ��Label��ͼ����
	 * 
	 * @param star			ָ���������Ƕ���
	 * @param parent		��������������
	 * @return					Label��ͼ����
	 */
	public static Label findFrame(Star star, Pane parent) {
		int row = star.getPosition().getRow();
		int col = star.getPosition().getColumn();
		
		//ͨ��idѰ�������ǵ��к���ǰ��s
		String id = "#s" + row + col;

		Label label = (Label) parent.lookup(id);

		return label;
	}

	/**
	 * ������д������������ǵ����꣬��ŷ�ʽ��һ��������ռ�����ռ䣬��һ����x���ڶ�����y
	 * 
	 * @param clearStars���������������б�
	 * @return �������б�����Ӧ��x,y���꼯��
	 * */
	public static List<Integer> getClearStarsCoordinate(StarList clearStars) {
		List<Integer> clearStarsCoordinate = new ArrayList<Integer>();

		for (int i = 0; i < clearStars.size(); i++) {
			// ���������������ǵ�������
			int starRow = clearStars.get(i).getPosition().getRow();
			int starColumn = clearStars.get(i).getPosition().getColumn();
			// ��������ͼ����mAnchorPane����ת��Ϊ�����root�¶�Ӧ��x����
			int starX = starColumn * STAR_WIDTH;
			// ��������ͼ����mAnchorPane����ת��Ϊ�����root�¶�Ӧ��y����
			int starY = starRow * STAR_WIDTH + SCORE_MOVE_DISTANCE;
			// �����ݶ�Ӧ��������ӵ�List������
			clearStarsCoordinate.add(starX);
			clearStarsCoordinate.add(starY);

		}
		// �����������б���root����Ӧ������
		return clearStarsCoordinate;
	}

}
