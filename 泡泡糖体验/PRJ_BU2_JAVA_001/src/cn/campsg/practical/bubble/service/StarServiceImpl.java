package cn.campsg.practical.bubble.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.campsg.practical.bubble.entity.MovedStar;
import cn.campsg.practical.bubble.entity.Position;
import cn.campsg.practical.bubble.entity.Star;
import cn.campsg.practical.bubble.entity.Star.StarType;
import cn.campsg.practical.bubble.entity.StarList;
import cn.campsg.practical.bubble.util.StarsUtil;

/**
 * 
 * ������ҵ���������࣬����Ϊ�����ṩ���·���<br>
 * 1. ������Ļ�������������<br>
 * 2. �й��ж�<br>
 * 3. �������������ݼ���÷�<br>
 * 4. ����ʣ�������ǵĵ÷ֽ���<br>
 * 
 * @author Frank.Chen
 * @version 2.5
 *
 */
public class StarServiceImpl implements StarService {

	private Logger logger = Logger.getLogger(StarServiceImpl.class);

	/**
	 * ������Ļ������������ǣ�10 * 10��
	 * 
	 * @return �������б�-��������ʾ
	 */
	@Override
	public StarList createBubbleMatrix() {

		StarList stars = new StarList();

		for (int row = 0; row < StarService.MAX_ROW_SIZE; row++) {

			for (int col = 0; col < StarService.MAX_COLUMN_SIZE; col++) {

				Star star = new Star();
				// �����������ڻ����ϵ�λ��
				star.setPosition(new Position(row, col));
				// ���������������
				int typeIndex = (int) (Math.random() * StarService.STAR_TYPES);
				star.setType(StarType.valueOf(typeIndex));
				// �����б�
				stars.add(star);
			}
		}

		return stars;

	}

	/**
	 * �Ը��������ǣ��û�����ģ�Ϊ�����������ҡ�����·������Ѱ����ͬ���͵�������
	 * 
	 * @param base
	 *            ���������ǣ��û�����ģ�
	 * @param sList
	 *            ԭʼ�������б����������е������ǣ�
	 * @param clearStars
	 *            ��������������б�
	 */
	private void findStarsByPath(Star base, StarList sList, StarList clearStars) {
		// ��ȡ��ǰ����������ǵ��к���
		int row = base.getPosition().getRow();
		int col = base.getPosition().getColumn();
		StarType type = base.getType();

		Star star = null;

		// �����·�������ж�
		if (col - 1 >= 0) {
			// ���������߽������£���ȡ����������ǵ����������
			star = (Star) sList.findout(row, (col - 1));
			// �Ѿ����������������ڽ��������е��������б��л���null��ʾ
			// �Ѿ���������������������б����Ѿ����ڵ������������ظ��жϣ�����������ѭ����
			if (star != null && !clearStars.isexisted(star)) {
				if (star.getType() == type) {
					// ��������������ж����ݱ���һ��ʱ�������б�
					clearStars.add(StarsUtil.copy(star));
					// ���������·�������жϡ�
					findStarsByPath(star, sList, clearStars);
				}
			}
		}

		// ���Ҳ�·�������ж�
		if (col + 1 < StarService.MAX_COLUMN_SIZE) {
			// �������Ҳ�߽������£���ȡ����������ǵ��Ҳ�������
			star = (Star) sList.findout(row, (col + 1));
			// �Ѿ����������������ڽ��������е��������б��л���null��ʾ
			// �Ѿ���������������������б����Ѿ����ڵ������������ظ��жϣ�����������ѭ����
			if (star != null && !clearStars.isexisted(star)) {
				if (star.getType() == type) {
					// ��������������ж����ݱ���һ��ʱ�������б�
					clearStars.add(StarsUtil.copy(star));
					// �������Ҳ�·�������жϡ�
					findStarsByPath(star, sList, clearStars);
				}
			}
		}

		// ���Ϸ�·�������ж�
		if (row - 1 >= 0) {
			// �������Ϸ��߽������£���ȡ����������ǵ��Ϸ�������
			star = (Star) sList.findout((row - 1), col);
			// �Ѿ����������������ڽ��������е��������б��л���null��ʾ
			// �Ѿ���������������������б����Ѿ����ڵ������������ظ��жϣ�����������ѭ����
			if (star != null && !clearStars.isexisted(star)) {
				if (star.getType() == type) {
					// ��������������ж����ݱ���һ��ʱ�������б�
					clearStars.add(StarsUtil.copy(star));
					// �������Ϸ�·�������жϡ�
					findStarsByPath(star, sList, clearStars);
				}
			}
		}

		// ���·�·�������ж�
		if (row + 1 < MAX_ROW_SIZE) {
			// �������·��߽������£���ȡ����������ǵ��·�������
			star = (Star) sList.findout((row + 1), col);
			// �Ѿ����������������ڽ��������е��������б��л���null��ʾ
			// �Ѿ���������������������б����Ѿ����ڵ������������ظ��жϣ�����������ѭ����
			if (star != null && !clearStars.isexisted(star)) {
				if (star.getType() == type) {
					// ��������������ж����ݱ���һ��ʱ�������б�
					clearStars.add(StarsUtil.copy(star));
					// �������·�·�������жϡ�
					findStarsByPath(star, sList, clearStars);
				}
			}
		}

		// �����ĸ��ж϶����������ʾ���ܶ�û���������ˣ���ô�����ݹ鷽����
	}

	/**
	 * �û���������ǣ���ȡ���������������������б�
	 * 
	 * @param base
	 *            ���û������������
	 * @param sList
	 *            ��ǰ���������ݵ��б�
	 * @return ��Ҫ�����������
	 */
	@Override
	public StarList tobeClearedStars(Star base, StarList mCurrent) {

		// ���ڱ���������������
		StarList clearStars = new StarList();

		// �ӵ�ǰ�б��л�ȡָ������ָ���е������ǣ�base�����ǣ�
		// ������������Ϊ������󱣴����б���
		// ע�⣺���д�����������Ƕ�Ӧ����ԭʼ�Ľ����������б���
		clearStars.add(base);

		// �Ա����������Ϊ���������ҡ����²�ͬ·��Ѱ����ͬ���ͣ���ɫ���Ĵ����������
		findStarsByPath(base, mCurrent, clearStars);

		if (clearStars.size() == 1)
			clearStars.clear();

		if (logger.isDebugEnabled())
			logger.debug("��������������ڴ��б�:" + clearStars);

		return clearStars;
	}

	/**
	 * ���������Ǻ󣬻�ȡ���ƶ��������б�(���޴�ֱ�б��������)<br>
	 * �ù��̶ܹ������������������֮������
	 * 
	 * @param clearStars
	 *            ��������������б��Դ���Ϊ�ж����ƶ������ǵĻ�����
	 * @param currentStarList
	 *            ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return ���ƶ��������б�
	 */
	public StarList getVMovedStars(StarList clearStars,
			StarList currentStarList) {

		if (clearStars == null || clearStars.size() == 0)
			return null;

		// ���ڱ�����ƶ���������
		StarList moveStars = new StarList();

		// �Դ������������ִ�з������
		/*
		 * ����: ��������������Ϊ��(2,1);(3,1);(4,1);(2,2);(3,2);(4,2)
		 * �����1-{(2,1);(3,1);(4,1)}��2-{(2,2);(3,2);(4,2)}
		 */
		Map<Integer, StarList> groupedStars = StarsUtil.group(clearStars);

		// ���л�ȡ���д�ɾ���������ǣ�һ�п��ܶ�Ӧ���������ǣ�
		Iterator<Integer> keys = groupedStars.keySet().iterator();
		while (keys.hasNext()) {
			// ��ȡ�����
			Integer column = keys.next();
			// ��ȡ��ǰ�����ж�Ӧ��Ҫɾ������������
			StarList values = groupedStars.get(column);

			// ��������������������еײ������������ƶ��������ǣ�
			int starPosition = values.lastElement().getPosition().getRow();

			// �ƶ��������
			int span = 0;

			// �ӵײ������������ƶ���������
			for (int row = starPosition; row >= 0; row--) {
				// ��ȡ������
				Star star = currentStarList.findout(row, column);
				// ��Ȼû�е������������ж��������ǵ�ǰ�����޿���������������ôֹͣ��ǰ������
				if (star == null)
					break;

				// ���������������������������У����ʾ�������Ǵ���������ƶ�
				// ��ô���ƶ�·�����ȼ�1
				if (clearStars.isexisted(star)) {
					span++;
					continue;
				}

				MovedStar mStar = StarsUtil.toMovedStar(star);
				// ���������������ƶ�����
				mStar.getMovedPosition().setRow(
						mStar.getPosition().getRow() + span);
				// ���޸�
				mStar.getMovedPosition().setColumn(
						mStar.getPosition().getColumn());
				// ������ƶ���������
				moveStars.add(mStar);

			}
		}
		if (logger.isDebugEnabled())
			logger.debug("���ƶ��������ڴ��б���ֱ�ƶ�����ģ�:" + moveStars + "���ƶ������Ǹ���Ϊ��"
					+ moveStars.size());

		return moveStars;
	}

	/**
	 * ���������Ǻ󣬻�ȡ���ƶ��������б�(����ˮƽ�б��������)<br>
	 * �ù��̶ܹ��ڴ�ֱ�б��������֮������
	 * 
	 * @param currentStarList
	 *            ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * 
	 * @return ���ƶ��������б�
	 */
	public StarList getHMovedStars(StarList currentStarList) {

		// ��ȡ���б���������ǵ���
		List<Integer> nullColumns = getEmpyColumns(currentStarList);

		// û����ȫ����������ǵ��У�����null����֪����X�������ƶ�
		if (nullColumns == null || nullColumns.size() == 0)
			return null;

		StarList moveStars = new StarList();

		// ��ȡ�ж���ʾ��
		int starPosition = nullColumns.get(0) + 1;

		// ˮƽ�ƶ�����
		int span = 1;

		// ���ж���ʾ��->�Ҳ�߽�����ж�
		for (int column = starPosition; column < StarService.MAX_COLUMN_SIZE; column++) {
			// ����һ���б������ˮƽ�ƶ�����+1
			if (nullColumns.contains(column)) {
				span++;
				continue;
			}

			// ��ǰ�����������û�б���գ��򽫴��ƶ��������Ǽ��뵽�ƶ��б���
			for (int row = (StarService.MAX_ROW_SIZE - 1); row >= 0; row--) {

				Star star = currentStarList.findout(row, column);

				if (star == null)
					break;

				
				MovedStar mStar = StarsUtil.toMovedStar(star);
				// ���������������ƶ�����
				mStar.getMovedPosition().setRow(mStar.getPosition().getRow());
				// ���޸�
				mStar.getMovedPosition().setColumn(
						mStar.getPosition().getColumn() - span);
				// ������ƶ���������
				moveStars.add(mStar);
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("���ƶ��������ڴ��б�ˮƽ�ƶ�����ģ�:" + moveStars);

		return moveStars;

	}

	/**
	 * ��ȡ��������������ǵ������
	 * 
	 * @param currentStarList
	 *            ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return ��������������ǵ�����ż���
	 */
	private List<Integer> getEmpyColumns(StarList currentStarList) {

		List<Integer> ret = new ArrayList<Integer>();

		// ���ÿ�еײ������������ǣ�����ײ��������ǲ����ڣ���ô������Ϊ�ѱ����
		for (int column = 0; column < StarService.MAX_COLUMN_SIZE; column++) {

			if (currentStarList.findout((StarService.MAX_ROW_SIZE - 1), column) == null)
				ret.add(column);
		}

		if (logger.isDebugEnabled())
			logger.debug("��ǰ�ڴ��б�����Ŀ���:" + ret);

		return ret;
	}

	/**
	 * �ж��Ƿ񻹴���δ������������
	 * 
	 * @param currentStarList
	 *            ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return true:��Ȼ��δ������������,false:û��δ������������
	 * 
	 */
	@Override
	public boolean tobeEliminated(StarList currentStarList) {

		// �������������б�
		StarList clearStars = new StarList();

		for (int i = 0; i < currentStarList.size(); i++) {

			Star star = currentStarList.get(i);

			if (star != null)
				findStarsByPath(star, currentStarList, clearStars);

			// ����������������б�����0�����ʾ���п������������ǣ�����true
			if (clearStars.size() > 0)
				return true;
		}

		if (logger.isDebugEnabled())
			logger.debug("�����ڿ������������Ǹ���=" + getLeftStarNum(currentStarList));

		return false;
	}

	/**
	 * ��ȡʣ�������Ǹ���
	 * 
	 * @param mCurretStars
	 *            ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return ʣ�������Ǹ���
	 * */
	private int getLeftStarNum(StarList currentStarList) {
		int leftStar = 0;
		// �����������б��Ѳ�Ϊnull�������Ǽ�Ϊʣ��������
		for (int i = 0; i < currentStarList.size(); i++) {
			if (currentStarList.get(i) != null)
				leftStar++;
		}
		if (logger.isDebugEnabled())
			logger.debug("��ʣ��δ����������������Ϊ" + leftStar);
		// ����ʣ����������Ŀ
		return leftStar;
	}

	/**
	 * ��ȡ�޷������������б�
	 * 
	 * @param curretStars
	 *            ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return �޷������������б�
	 * */
	public StarList getAwardStarList(StarList curretStars) {
		StarList awardStarList = new StarList();

		// ����curretStars���Ѳ�Ϊnull�������Ǽӵ������������б���
		for (int i = 0; i < curretStars.size(); i++) {
			// tempStar = curretStars.get(i);
			if (curretStars.get(i) != null) {
				awardStarList.add(StarsUtil.copy(curretStars.get(i)));
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("��ʣ��δ����������������Ϊ" + awardStarList);
		// ���ؽ����������б�
		return awardStarList;

	}

}
