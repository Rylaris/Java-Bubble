package cn.campsg.practical.bubble.service;

import org.apache.log4j.Logger;

import cn.campsg.practical.bubble.entity.Score;
import cn.campsg.practical.bubble.entity.StarList;

/**
 * 
 * ��������������Ϊ�����ṩ���¹���<br>
 * 1. ����ؿ�ͨ�ط���<br>
 * 2. �й��ж�<br>
 * 3. �������������ݼ���÷�<br>
 * 4. ����ʣ�������ǵĵ÷ֽ���<br>
 * 
 * @author Frank.Chen
 * @version 1.1
 *
 */
public class ScoreServiceImpl implements ScoreService {

	private Logger logger = Logger.getLogger(ScoreServiceImpl.class);

	/* ���������ļ������� */
	private ScoreManager mConfiguration = null;

	/* �����ж���ǰ�ؿ�����ϲͨ�ء��Ƿ��Ѿ���֪ͨ��һ�� */
	public static int mLevelCounter = 1;

	public ScoreServiceImpl() {
		mConfiguration = ScoreManager.getInstance();
	}

	/**
	 * ���ݵ÷���ؿ����ж��Ƿ���Ҫ���֡���ϲͨ�ء�<br>
	 * ע�⣺��һ���У�����ϲͨ�ء����ҽ�����ʾһ�Ρ�
	 * 
	 * @param currentLevel ��ǰ�ؿ��ȼ�   
	 * @param score ��ǰ�÷�
	 * @return true:֪ͨ��false:��֪ͨ
	 */
	@Override
	public boolean isNoticedPassLevel(int currentLevel, int score) {
		
		//�жϷ����Ƿ��Ѿ��ﵽͨ��Ҫ��
		if (!isChangeLevel(score))
			return false;

		// �жϵ�ǰ�ؿ��Ƿ��Ѿ���ʾ����ϲͨ�ء�������
		if (currentLevel != mLevelCounter) 
			return false;
		
		//���û����ʾ������ϲͨ�ء�����ô����true������ͨ���ۼ�mLevelCounter��ֹ��γ��֡���ϲͨ�ء�
		mLevelCounter++;
		return true;

	}

	/**
	 * ����Ҫ�л��Ĺؿ��ȼ���ȡĿ�����
	 * 
	 * @param ��һ�صĹؿ�
	 * @return ������ĵ÷�
	 * 
	 */
	@Override
	public int nextLevelTarget(int nextLevel) {
		
		Score score = mConfiguration.getScore();
		
		if (score == null)
			return 0;

		// ���±��汾�ص�Ŀ�����
		score.setLevelScore(score.getLevelScore() + (nextLevel - 1)
				/ score.getLength() * score.getIncrement() + score.getStep());

		// ���ر���Ŀ�����
		return score.getLevelScore();
	}
	
	/**
	 * ��õ�ǰ�ص�ͨ�ط���
	 * 
	 * @return ������ĵ÷�
	 * 
	 */
	@Override
	public int getCurrentLevelScore() {

		return mConfiguration.getScore().getLevelScore();
	}

	/**
	 * ���ݵ�����ȡ�Ĵ����������Ǽ�����÷�<br>
	 * ����������������÷ֹ���: <br>
	 * 1. ������1�������� --> ��5�� <br>
	 * 2. ������2�������� --> ��15�� <br>
	 * 3. ������3�������� --> ��25�� <br>
	 * 4. ������4�������� --> ��35��<br>
	 * 5. ������5�������� --> ��45��<br>
	 * 
	 * @param stars
	 *            ���ݵ�����ȡ�Ĵ����������Ǽ���
	 * @return ������ĵ÷�
	 * */
	@Override
	public int getScoreByStars(StarList stars) {

		return getScoreByNum(stars.size());
	}

	/**
	 * ���ݵ�����ȡ�Ĵ���������������������÷�<br>
	 * ����������������÷ֹ���: <br>
	 * 1. ������1�������� --> ��5�� <br>
	 * 2. ������2�������� --> ��15�� <br>
	 * 3. ������3�������� --> ��25�� <br>
	 * 4. ������4�������� --> ��35��<br>
	 * 5. ������5�������� --> ��45��<br>
	 * 
	 * @param stars
	 *            ���ݵ�����ȡ�Ĵ���������������
	 * @return ������ĵ÷�
	 * 
	 * */
	@Override
	public int getScoreByNum(int stars) {

		if (stars <= 0)
			return 0;

		// �������������������������ǵ��ܵ÷�
		int score = LOWER_SCORE * stars * stars;

		if (logger.isDebugEnabled())
			logger.debug("���������Ǹ���Ϊ:" + stars + "--�������÷���Ϊ��" + score);

		return score;
	}

	/**
	 * �޿������������ǣ�����ʣ���޿��������ݿ��Ի�ý�������
	 * 
	 * �޿����������ǽ����÷ֹ���: <br>
	 * 1. ʣ��1�������� --> ����1620�� <br>
	 * 2. ʣ��2�������� --> ����1280�� <br>
	 * 3. ʣ��3�������� --> ����980�� <br>
	 * 4. ʣ��4�������� --> ����720�� <br>
	 * 5. ʣ��5�������� --> ����500�� <br>
	 * 
	 * @param awardStarNumʣ�������Ǹ���
	 * @return ʣ�������ǽ�������
	 * */
	@Override
	public int getAwardScore(int leftStarNum) {

		// �ж��Ƿ�ﵽʣ�������ǽ�����Χ
		if (leftStarNum < AWARD_LIMIT)
			return LOWER_AWARD_SCORE*(leftStarNum - AWARD_LIMIT)*(leftStarNum - AWARD_LIMIT);
		else
			return 0;

	}

	/**
	 * �жϵ�ǰ�����Ƿ��Ѿ��ﵽ������һ�ص�Ҫ��
	 * 
	 * @param score ��ǰ�÷�
	 * @return true:���������һ�أ�false:�����������һ��
	 */
	@Override
	public boolean isChangeLevel(int score) {

		int targetScore = mConfiguration.getScore().getLevelScore();

		return score >= targetScore ? true : false;

	}
}
