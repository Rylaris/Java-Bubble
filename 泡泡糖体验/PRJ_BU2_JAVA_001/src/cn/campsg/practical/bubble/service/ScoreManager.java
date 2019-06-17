package cn.campsg.practical.bubble.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import cn.campsg.practical.bubble.entity.Score;

/**
 * �������ڵ���score.conf�����ļ�,<br>
 * ����ȡ�ķ������ݽ��ᱻ������Score����С�
 * 
 * @author Frank.Chen
 * @version 1.0
 *
 */
public class ScoreManager {

	private Logger logger = Logger.getLogger(ScoreManager.class);

	/* score.conf�ļ���·�� */
	public static final String CONF_PATH = "score.conf";

	/* ���ڱ�������Ķ��� */
	private Score score = null;

	/* �������� */
	private static ScoreManager conf = null;

	/* ������������ */
	public static ScoreManager getInstance() {

		if (conf == null)
			conf = new ScoreManager();

		return conf;
	}

	private ScoreManager() {

		score = new Score();

		try {

			// ��ȡscore.conf�����ļ�
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(CONF_PATH)));// new
																					// FileReader(CONF_PATH));

			// ��ȡ�ؿ���������ǰ��ȡ�����ļ��еķ�����Ĭ�ϵ�һ�ص�ͨ�ط�
			score.setLevelScore(Integer.parseInt(br.readLine()));
			// ��ȡͨ����ÿ�ιؿ��ĵ����֣����磺��һ��1000�֣��ڶ���levelScore + step��
			score.setStep(Integer.parseInt(br.readLine()));
			// ��ȡ�ﵽ�ؿ��������޺�ķ�����ֵ�������磺�ﵽ���Ĺغ�ÿ�ص�������=step + increment
			score.setIncrement(Integer.parseInt(br.readLine()));
			// ��ȡ�ؿ���������ÿ���ط�������һ�Ρ�
			score.setLength(Integer.parseInt(br.readLine()));

			br.close();

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			score = null;

		} catch (NumberFormatException | IOException e) {
			logger.error(e.getMessage());
			score = null;
		}
	}

	public Score getScore() {
		return score;
	}

}
