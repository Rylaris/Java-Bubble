package cn.campsg.practical.bubble.service;

import cn.campsg.practical.bubble.entity.Star;
import cn.campsg.practical.bubble.entity.StarList;

public interface StarService {
	
	/* ������������� */
	public static final int MAX_ROW_SIZE = 10;
	
	/* ������������� */
	public static final int MAX_COLUMN_SIZE = 10;
	
	/* �����ǵ������� */
	public static final int STAR_TYPES = 5;
		
	/**
	 * ������Ļ������������ǣ�10 * 10��
	 * 
	 * @return �������б�-��������ʾ
	 */
	public StarList createBubbleMatrix();
	
	/**
	 * �û���������ǣ���ȡ���������������������б�
	 * 
	 * @param base ���û������������
	 * @param sList ��ǰ���������ݵ��б�
	 * @return ��Ҫ�����������
	 */
	public StarList tobeClearedStars(Star base,StarList sList);
	
	/**
	 * ���������Ǻ󣬻�ȡ���ƶ��������б�(���޴�ֱ�б��������)<br>
	 * �ù��̶ܹ������������������֮������
	 * 
	 * @see tobeClearedStars
	 * 
	 * @param clearStars ��������������б��Դ���Ϊ�ж����ƶ������ǵĻ�����
	 * @param currentStarList ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return ���ƶ��������б�
	 */
	public StarList getVMovedStars(StarList clearStars,StarList currentStarList);
	
	/**
	 * ���������Ǻ󣬻�ȡ���ƶ��������б�(����ˮƽ�б��������)<br>
	 * �ù��̶ܹ��ڴ�ֱ�б��������֮������
	 * 
	 * @see getYMovedStars
	 * 
	 * @param currentStarList ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return ���ƶ��������б�
	 */
	public StarList getHMovedStars(StarList currentStarList);
	
	/**
	 * �ж��Ƿ񻹴���δ������������
	 * 
	 * @param currentStarList ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return true:��Ȼ��δ������������,false:û��δ������������
	 */
	public boolean tobeEliminated(StarList currentStarList);
	
	/**
	 * ��ȡ�޷������������б�
	 * 
	 * @param curretStars ��ǰ�����Ľ����������б��Ѿ�����������������null��ʾ��
	 * @return �޷������������б�
	 * */
	public StarList getAwardStarList(StarList currentStarList);
	

}
