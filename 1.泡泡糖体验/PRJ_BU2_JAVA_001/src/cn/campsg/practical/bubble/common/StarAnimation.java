package cn.campsg.practical.bubble.common;

import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import cn.campsg.practical.bubble.entity.StarList;
import cn.campsg.practical.bubble.service.ScoreService;

public final class StarAnimation {

	// �����˶�·��ʱ��
	public static final int START_PATH_TIME = 300;
	// ���ݽ���ʱ��
	public static final int START_FADE_TIME = 300;
	// �����Զ���message�ĵȼ�
	public static final int MESSAGE_LEVEL = 6;
	// ��һ�������ǵķ���
	public static final int FIRST_START_SCORE = 5;
	// ��������ʱ��
	public static final int SCORE_FADE_TIME = 1000;
	// ����ƫ��ʱ��
	public static final int OFFSET_TIME = 200;
	// ����ƫ����
	public static final int OFFSET_SCORE = 10;
	// ����֪ͨλ��X����
	public static final int PASSNOTICE_X = 180;
	// ����֪ͨλ��Y����
	public static final int PASSNOTICE_Y = 180;
	// �����ǵ÷��˶�Ŀ��·��X����
	public static final double TARGET_PATH_X = 240;
	// �����ǵ÷��˶�Ŀ��·��Y����
	public static final double TARGET_PATH_Y = 122;
	// ����ǰ��ʾ������ʣ����Ϣ������ʼX����
	public static final double LEFTINF_START_X = 480;
	// ����ǰ��ʾ������ʣ����Ϣ������ʼY����
	public static final double LEFTINF_START_Y = 300;
	// ����ǰ��ʾ�����Ǹ����յ�����X
	public static final double LEFTINF_END_X = 0;
	// ����ǰ��ʾ�����ǽ�����Ϣ���Y����
	public static final double AWARDINF_START_Y = 350;
	// ������ʣ����Ϣˮƽ�˶�ʱ��
	public static final double LEFTINF_PATH_TIME = 300;
	// �����ǽ�����Ϣˮƽ�˶�ʱ��
	public static final double AWARDINF_H_TIME = 300;
	// �����ǽ�����Ϣ��ֱ�˶�ʱ��
	public static final double AWARDINF_V_TIME = 300;
	// ֪ͨ��һ����Ϣ��X����
	public static final double CHANGE_LEVEL_NOTICE_X = 120;
	// ֪ͨ��һ����Ϣ��Y����
	public static final double CHANGE_LEVEL_NOTICE_Y = 160;

	/**
	 * ������ֱ�ƶ���������ͼ����
	 * 
	 * @param starFrame
	 *            ��������ͼ����
	 * @param span
	 *            �ƶ�����
	 */
	public static Timeline slideYStar(Label starFrame, int span) {
		final Timeline timeline = new Timeline();
		// �ƶ�ģʽ��param0���ƶ�������ϵ��param1������ϵ��Ӧ���ƶ�����
		final KeyValue kv = new KeyValue(starFrame.layoutYProperty(), span);
		// �����ƶ�ʱ��
		final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();

		return timeline;
	}

	/**
	 * ����ˮƽ�ƶ���������ͼ����
	 * 
	 * @param starFrame
	 *            ��������ͼ����
	 * @param span
	 *            �ƶ�����
	 */
	public static Timeline slideXStar(Label starFrame, int span) {
		final Timeline timeline = new Timeline();
		// �ƶ�ģʽ��param0���ƶ�������ϵ��param1������ϵ��Ӧ���ƶ�����
		final KeyValue kv = new KeyValue(starFrame.layoutXProperty(), span);
		// �����ƶ�ʱ��
		final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();

		return timeline;
	}

	/**
	 * �Խ��䶯��ɾ����������������
	 * 
	 * @param mAnchorPane
	 *            ��������ʾ����
	 * @param starFrame
	 *            ��������������
	 */
	public static void clearStarLable(AnchorPane mAnchorPane, Label starFrame) {
		fadeOut(starFrame).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mAnchorPane.getChildren().remove(starFrame);
			}
		});
	}

	/**
	 * JFX���䶯��
	 * 
	 * @param starFrame
	 *            ���ý�������
	 * 
	 * @return �������
	 */
	public static FadeTransition fadeOut(Label starFrame) {
		FadeTransition ft = new FadeTransition(Duration.millis(10), starFrame);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setAutoReverse(false);
		ft.play();
		return ft;
	}

	public static FadeTransition clearAwardLable(AnchorPane mAnchorPane,
			Label starFrame) {
		FadeTransition starFade = fadeOut(starFrame);
		fadeOut(starFrame).setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mAnchorPane.getChildren().remove(starFrame);
			}
		});
		return starFade;
	}

	/**
	 * 
	 * ���������ǵĵ÷ֶ���
	 * 
	 * @param root
	 *            ��������ʾ����
	 * @param clearStarsCoordinate
	 *            �����������ǵ����꼯��
	 * @param scoreService
	 *            �÷ַ�����
	 * @return ���������ǵĶ�������
	 */
	public static ParallelTransition getScoreAnimation(AnchorPane root,
			List<Integer> clearStarsCoordinate, ScoreService scoreService) {
		// ���ݼ��ϴ�С���������������Ǹ���Ϊ����һ�룬��ÿ����������X,Y���꣩
		int starNum = clearStarsCoordinate.size() / 2;
		// �������������Ǹ��������ܵ÷�
		String addScore = "" + scoreService.getScoreByNum(starNum);
		// ��ȡ��Ҫѧ������÷ֵ�Label
		Label scoreLabel = (Label) root.lookup("#lblMessage");
		// �����ʾ������2����20
		scoreLabel.setText(starNum + "����" + addScore);

		// ���ɿ��Բ���ִ�ж����Ķ���
		ParallelTransition parallelTransition = new ParallelTransition();
		int j = 0;
		for (int i = 0; i < starNum; i++) {
			// ����ȡ����i��������x����
			int starX = clearStarsCoordinate.get(j);
			j++;
			// ����ȡ����i��������y����
			int starY = clearStarsCoordinate.get(j);
			j++;
			// ���õ�i��������λ�ã� starX, starY������ʾ����(5+StarFormUtils.OFFSET_SCORE*i)
			Text starLabel = addNode(
					root,
					25,
					starX,
					starY,
					""
							+ (StarAnimation.FIRST_START_SCORE + StarAnimation.OFFSET_SCORE
									* i));
			// ���õ�i��������λ�ã� starX,
			// starY���˶�����StarFormUtils.TARGET_PATH_X,StarFormUtils.TARGET_PATH_Y��
			PathTransition starPath = scorePath(starX, starY,
					StarAnimation.TARGET_PATH_X, StarAnimation.TARGET_PATH_Y,
					StarAnimation.START_PATH_TIME + StarAnimation.OFFSET_TIME
							* i, starLabel);
			// ���õ�i�������ǽ���ʱ��
			FadeTransition starFade = scoreFadeOut(
					StarAnimation.START_FADE_TIME + StarAnimation.OFFSET_TIME
							* i, starLabel);
			starFade.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					root.getChildren().remove(starLabel);
				}
			});
			parallelTransition.getChildren().addAll(starPath, starFade);
			if (i == StarAnimation.MESSAGE_LEVEL) {
				addScore = Message.MESSAGE;
				break;
			}
		}
		// ����������ǻ���ܷ�������ʾ���꣬��ʼֵΪ����������ǵ�����
		int clickPointX = clearStarsCoordinate.get(0);
		int clickPointY = clearStarsCoordinate.get(1);

		// �жϵ�һ�������Ƿ��ڵ�1,2�У�����ʾ�ܷ������1�У�����������˿��ܲ���������ʾ����÷�
		if (clickPointX == 0 || clickPointX == StarFormUtils.STAR_WIDTH) {
			clickPointX = clickPointX + StarFormUtils.STAR_WIDTH;
		}

		// ���õ���ܵ÷�addScore
		Text clickPoint = addNode(root, 20, clickPointX, clickPointY, addScore);
		// ������ʾ����ܵ÷���600������ɴӷŴ�4����ԭʼ��С
		ScaleTransition clickPointScale = scoreScale(300, clickPoint, 4, 1);
		// ���õ���÷�������ʱ��10����
		FadeTransition clickPointFade = scoreFadeOut(10, clickPoint);
		clickPointFade.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				root.getChildren().remove(clickPoint);
			}
		});

		// ��������ִ�ж���clickPoint����÷���clickPointScale�����ţ�ͣ��50���룬�ٽ�����ʧ
		SequentialTransition seqTransition = new SequentialTransition(
				clickPointScale, new PauseTransition(Duration.millis(50)),
				clickPointFade);
		// ������ʾ����scoreLabel�Ľ���ʱ��2200����
		FadeTransition scoreLabelFade = scoreFadeOut(
				StarAnimation.SCORE_FADE_TIME, scoreLabel);
		parallelTransition.getChildren().addAll(seqTransition, scoreLabelFade);

		// ����ѭ������
		parallelTransition.setCycleCount(1);

		return parallelTransition;

	}

	/**
	 * ���ýڵ�Ľ���Ч��
	 * 
	 * @param duration���ý���ʱ��
	 * @param scoreLabel����Ŀ�꽥��ڵ�
	 * 
	 * */
	public static FadeTransition scoreFadeOut(int duration, Node scoreLabel) {
		// ����Ҫ���н����Ŀ��scoreLabel��������ʱ��duration
		FadeTransition fadeTransition = new FadeTransition(
				Duration.millis(duration), scoreLabel);
		// ���ý�����ʼֵ
		fadeTransition.setFromValue(1.0f);
		// ������ֵֹ
		fadeTransition.setToValue(0.0);
		// ����ѭ���������Ƿ�ת
		fadeTransition.setCycleCount(1);
		fadeTransition.setAutoReverse(false);
		return fadeTransition;
	}

	/**
	 * ����Ŀ��ڵ���˶�·��
	 * 
	 * @param scoreLabelX�ڵ��˶���ʼλ��x����
	 * @param scoreLabelY�ڵ��˶���ʼλ��y����
	 * @param duration�ڵ��˶�ʱ��
	 * @param scoreLabel�˶���Ŀ��ڵ�
	 * 
	 * */
	public static PathTransition scorePath(double scoreLabelX,
			double scoreLabelY, double targetX, double targetY,
			double duration, Node scoreLabel) {
		Path path = new Path();
		// ����Ŀ��·������ʼ����scoreLabelX��scoreLabelY
		path.getElements().add(new MoveTo(scoreLabelX, scoreLabelY));
		// �����˶��켣������ֱ���˶���Ŀ������(targetX, targetY)
		path.getElements().add(new LineTo(targetX, targetY));
		// new ��·������
		PathTransition pathTransition = new PathTransition();
		// ���õ���Ŀ��·��ʱ��
		pathTransition.setDuration(Duration.millis(duration));
		// ���ð���path·���˶�
		pathTransition.setPath(path);
		// ������Ҫ�˶���label
		pathTransition.setNode(scoreLabel);
		// ����ѭ���������Ƿ�ת
		pathTransition.setCycleCount(1);
		pathTransition.setAutoReverse(false);
		return pathTransition;
	}

	/**
	 * ����Node�ڵ������Ч��
	 * 
	 * @param duration����ʱ��
	 * @param scoreLabel���ŵ�Ŀ��ڵ�
	 * @param setFrom������ʼ��С
	 * @param setTo���ŵ�Ŀ���С
	 * 
	 * */
	public static ScaleTransition scoreScale(int duration, Node scoreLabel,
			int setFrom, int setTO) {

		ScaleTransition scaleTransition = new ScaleTransition(
				Duration.millis(duration), scoreLabel);
		// �������ŵ�X,Y�����С��ʼ
		scaleTransition.setFromX(setFrom);
		scaleTransition.setFromY(setFrom);
		// ��������X,Y�����Ŀ���С
		scaleTransition.setToX(setTO);
		scaleTransition.setToY(setTO);
		// ����ѭ����ʾ����
		scaleTransition.setCycleCount(1);
		// �Ƿ��Զ���ת
		scaleTransition.setAutoReverse(false);

		return scaleTransition;
	}

	/**
	 * �������������Ľڵ�
	 * 
	 * @param fatherNode��Ҫ���ӽڵ�ĸ��ڵ�
	 * @param fontSize�ڵ�������ݵ������С
	 * @param scoreLabelX�ڵ��x��ʼ����
	 * @param scoreLabelY�ڵ����ʼ����
	 * @param addScore�ڵ���������
	 * 
	 * @return ���������ɵĽڵ�
	 * */
	public static Text addNode(AnchorPane fatherNode, int fontSize,
			double scoreLabelX, double scoreLabelY, String addScore) {

		// ÿ���������������ʱ�ķ���
		Text scorePer = new Text();
		// ������ʾ���������С����ɫ
		scorePer.setFont(Font.font(null, FontWeight.BOLD, fontSize));
		scorePer.setFill(Color.WHITE);
		// ��������Text��λ������
		scorePer.setX(scoreLabelX);
		scorePer.setY(scoreLabelY);
		// �����������
		scorePer.setText("" + addScore);
		// ��Text��ӵ�Ҫ��ʾ�����fatherNode
		fatherNode.getChildren().add(scorePer);
		return scorePer;
	}

	/**
	 * �ﵽ���ص÷ֺ󶯻�֪ͨ
	 * */
	public static void passedFlag(AnchorPane mAnchorPane) {
		// ���ù���֪ͨ��λ�ú����ݣ���ϲ���أ�
		Text passNotice = (Text) addNode(mAnchorPane, 40,
				StarAnimation.PASSNOTICE_X, StarAnimation.PASSNOTICE_Y,
				Message.LEVEL_OK);
		passNotice.setFill(Color.WHITE);
		// ���ý�����������ʱ�䣬�����ȷŴ�3����
		ScaleTransition noticeScale = scoreScale(1000, passNotice, 3, 1);
		// ���ý������ݽ���ʱ��1s
		FadeTransition noticeFade = scoreFadeOut(1000, passNotice);

		SequentialTransition passNoticeTransition = new SequentialTransition(noticeScale,
				new PauseTransition(Duration.millis(50)), noticeFade);
		passNoticeTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mAnchorPane.getChildren().remove(passNotice);
			}
		});
		passNoticeTransition.play();
	}

	/**
	 * ������ʾ��������
	 * 
	 * @param root����Ҫ��ʾ�ĸ��ڵ�
	 * @param awardScore��������
	 * @param leftStarNumʣ�������Ǹ���
	 */
	public static ParallelTransition awardAnimation(AnchorPane root,
			int awardScore, StarList mCurretStars) {
		// ��ȡ����������
		int leftStarNum = mCurretStars.size();
		// ������ʾ��Ϣ��ʣ����������leftInf
		Text leftInf = (Text) addNode(root, 40, StarAnimation.LEFTINF_START_X,
				StarAnimation.LEFTINF_START_Y, "ʣ�� " + leftStarNum);
		// ����������������awardInf
		Text awardInf = (Text) addNode(root, 38, StarAnimation.LEFTINF_START_X,
				StarAnimation.AWARDINF_START_Y, "����" + awardScore);
		// ����������ʣ����Ϣ�˶�ǰ���·��
		PathTransition firstLeftInfPathTransition = scorePath(
				StarAnimation.LEFTINF_START_X, StarAnimation.LEFTINF_START_Y,
				StarAnimation.TARGET_PATH_X, StarAnimation.LEFTINF_START_Y,
				StarAnimation.LEFTINF_PATH_TIME, leftInf);
		// ����������ʣ����Ϣ�����˶�·��
		PathTransition secondLeftInfPathTransition = scorePath(
				StarAnimation.TARGET_PATH_X, StarAnimation.LEFTINF_START_Y,
				StarAnimation.LEFTINF_END_X, StarAnimation.LEFTINF_START_Y,
				StarAnimation.LEFTINF_PATH_TIME, leftInf);
		// ������������ˮƽ�˶�����
		Polyline polylineX = new Polyline();
		// ��������������ֱ�˶�����
		Polyline polylineY = new Polyline();
		polylineX.getPoints().addAll(
				new Double[] {
						// ���ˮƽ�˶���ʼ�㣨LEFTINF_START_X, AWARDINF_START_Y,��
						// �յ�(TARGET_PATH_X,AWARDINF_START_Y )
						StarAnimation.LEFTINF_START_X,
						StarAnimation.AWARDINF_START_Y,
						StarAnimation.TARGET_PATH_X,
						StarAnimation.AWARDINF_START_Y });
		// ������������0�����ô�ֱ�˶����򣬷���ˮƽ�Ƴ�
		if (awardScore > 0) {
			// ���ˮƽ�˶���ʼ�㣨TARGET_PATH_X, AWARDINF_START_Y,��
			// �յ�(TARGET_PATH_X,TARGET_PATH_Y )
			polylineY.getPoints().addAll(
					new Double[] { StarAnimation.TARGET_PATH_X,
							StarAnimation.AWARDINF_START_Y,
							StarAnimation.TARGET_PATH_X,
							StarAnimation.TARGET_PATH_Y });
		} else {
			// ����Ϊ0��ˮƽ�˶�
			polylineY.getPoints().addAll(
					new Double[] { StarAnimation.TARGET_PATH_X,
							StarAnimation.AWARDINF_START_Y,
							StarAnimation.LEFTINF_END_X,
							StarAnimation.AWARDINF_START_Y });
		}
		// ����ˮƽ�����˶�·������
		PathTransition awardInfPathTransitionX = new PathTransition(
				Duration.millis(StarAnimation.AWARDINF_H_TIME), polylineX);
		// ������ֱ�����˶�·������
		PathTransition awardInfPathTransitionY = new PathTransition(
				Duration.millis(StarAnimation.AWARDINF_V_TIME), polylineY);
		// ����Ҫ�˶��Ķ���
		awardInfPathTransitionX.setNode(awardInf);
		awardInfPathTransitionY.setNode(awardInf);

		// ����ʣ���������뽱����������ʱ��
		FadeTransition leftInfFade = scoreFadeOut(10, leftInf);
		FadeTransition awardInfFade = scoreFadeOut(10, awardInf);
		// ʣ������������ִ��ˮƽ�˶�����ͣ1s����,ˮƽ�˶��Ƴ�
		SequentialTransition leftInfSwq = new SequentialTransition(
				firstLeftInfPathTransition, new PauseTransition(
						Duration.millis(1500)), secondLeftInfPathTransition,
				leftInfFade);
		leftInfFade.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				root.getChildren().remove(leftInf);
			}
		});
		// ������������ִ��ˮƽ�˶�����ͣ1s����ֱ����ˮƽ���˶�������
		SequentialTransition awardInfSwq = new SequentialTransition(
				awardInfPathTransitionX, new PauseTransition(
						Duration.millis(1500)), awardInfPathTransitionY,
				awardInfFade);
		awardInfSwq.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				root.getChildren().remove(awardInf);
			}
		});
		// ��������ִ�ж���
		ParallelTransition parallelTransition = new ParallelTransition();
		// ���Ҫ����ִ�еĶ���
		parallelTransition.getChildren().addAll(leftInfSwq, awardInfSwq);
		// ��������
		parallelTransition.setCycleCount(1);
		parallelTransition.play();

		return parallelTransition;

	}

	/**
	 * �л���ʱ������ʾ��Ϣ��������Ŀ�������
	 * 
	 * @param mAnchorPane
	 *            Ҫ��ʾ������Ϣ�����
	 */
	public static SequentialTransition passNotice(AnchorPane mAnchorPane,
			int nextLevel, int targetScore) {
		// ��ȡ��Ҫ�������X������
		double noticeX = StarAnimation.CHANGE_LEVEL_NOTICE_X;
		// ��ȡ��Ҫ�������Y������
		double noticeY = StarAnimation.CHANGE_LEVEL_NOTICE_Y;
		// ����������ݣ�ͨ�صȼ���Ŀ�����
		Text passNotice = new Text("       ��" + nextLevel + "��\n" + "Ŀ�������"
				+ targetScore);
		passNotice.setLayoutX(noticeX);
		passNotice.setLayoutY(noticeY);
		passNotice.setFill(Color.WHITE);
		passNotice.setFont(Font.font(null, FontWeight.BOLD, 35));
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				mAnchorPane.getChildren().add(passNotice);
			}
		});

		// ����passNotice����ʱ��
		FadeTransition passFade = scoreFadeOut(10, passNotice);
		// ����ִ�У�ͣ��2s��ִ�н���
		SequentialTransition seqTransition = new SequentialTransition(
				new PauseTransition(Duration.millis(2000)), // ��ͣ2s
				passFade);
		seqTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				mAnchorPane.getChildren().remove(passNotice);
			}
		});
		seqTransition.play();
		return seqTransition;
	}

}