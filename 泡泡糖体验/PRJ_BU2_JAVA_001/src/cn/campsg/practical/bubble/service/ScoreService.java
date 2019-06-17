package cn.campsg.practical.bubble.service;

import cn.campsg.practical.bubble.entity.StarList;

public interface ScoreService {
	
	/* ʣ�಻�����������ݽ�������ߵ÷� */
	public static final int TOP_AWARD_SCORE = 2000;

	/* ʣ�಻�����������ݽ����ļ��� */
	public static final int AWARD_LIMIT = 10;
	
	/* ʣ�಻�����������ݵ�����������͵÷�  */
	public static final int LOWER_AWARD_SCORE = 20;
	
	/* ����һ�������ǵ���͵÷� */
	public static final int LOWER_SCORE = 5;

	/**
	 * �������һ�ص�ͨ�ط���
	 * 
	 * @param ��һ�عؿ�
	 * @return ������ĵ÷�
	 * 
	 */
	public int nextLevelTarget(int nextLevel);
	
	/**
	 * ��õ�ǰ�ص�ͨ�ط���
	 * 
	 * @return ������ĵ÷�
	 * 
	 */
	public int getCurrentLevelScore();

	/**
	 * ���ݵ�����ȡ�Ĵ����������Ǽ�����÷�<br>
	 * ����������������÷ֹ���: <br>
	 * 1. ������1�������� --> ��5�� <br>
	 * 2. ������2�������� --> ��15�� <br>
	 * 3. ������3�������� --> ��25�� <br>
	 * 4. ������4�������� --> ��35��<br>
	 * 5. ������5�������� --> ��45��<br>
	 * 
	 * @param stars ���ݵ�����ȡ�Ĵ����������Ǽ���
	 * @return ������ĵ÷�
	 * */
	public int getScoreByStars(StarList stars);

	/**
	 * ���ݵ�����ȡ�Ĵ���������������������÷�<br>
	 * ����������������÷ֹ���: <br>
	 * 1. ������1�������� --> ��5�� <br>
	 * 2. ������2�������� --> ��15�� <br>
	 * 3. ������3�������� --> ��25�� <br>
	 * 4. ������4�������� --> ��35��<br>
	 * 5. ������5�������� --> ��45��<br>
	 * 
	 * @param stars ���ݵ�����ȡ�Ĵ���������������
	 * @return ������ĵ÷�
	 * */
	public int getScoreByNum(int stars);

	/**
	 * �޿������������ǣ�����ʣ���޿��������ݿ��Ի�ý�������
	 * 
	 * @param awardStarNumʣ�������Ǹ���
	 * @return ʣ�������ǽ�������
	 * */
	public int getAwardScore(int awardStarNum);

	/**
	 * �жϵ�ǰ�����Ƿ��Ѿ��ﵽ������һ�ص�Ҫ��
	 * 
	 * @param score ��ǰ�÷�
	 * @return true:���������һ�أ�false:�����������һ��
	 */
	public boolean isChangeLevel(int score);

	/**
	 * ���ݵ÷���ؿ����ж��Ƿ���Ҫ���֡���ϲͨ�ء�<br>
	 * ע�⣺��һ���У�����ϲͨ�ء����ҽ�����ʾһ�Ρ�
	 * 
	 * @param currentLevel ��ǰ�ؿ��ȼ�   
	 * @param score ��ǰ�÷�
	 * @return true:֪ͨ��false:��֪ͨ
	 */
	public boolean isNoticedPassLevel(int currentLevel, int score);
}
