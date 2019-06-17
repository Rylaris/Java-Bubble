package cn.campsg.practical.bubble;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.campsg.practical.bubble.common.Message;
import cn.campsg.practical.bubble.common.StarAnimation;
import cn.campsg.practical.bubble.common.StarFormUtils;
import cn.campsg.practical.bubble.entity.MovedStar;
import cn.campsg.practical.bubble.entity.Star;
import cn.campsg.practical.bubble.entity.StarList;
import cn.campsg.practical.bubble.service.ScoreService;
import cn.campsg.practical.bubble.service.StarService;

/**
 * �����Ǵ����࣬������ʾ���������С����������ǵ���¼��붯��
 * 
 * 
 * @author Frank.Chen
 * @version 1.5
 */
public class MainForm extends Application {

	private Logger logger = Logger.getLogger(MainForm.class);

	private AnchorPane mRoot = null;

	private AnchorPane mAnchorPane = null;

	private Label scoreLabel = null;

	private StarList mCurretStars = null;

	private StarService mStarService = null;

	private ScoreService mScoreService = null;

	// ��ʼ����Ϊ0
	private static int score = 0;

	// ��ʼ�ؿ�Ϊ1
	private static int currentLevel = 1;

	// ����ͨ����һ��ʱ�ķ�����Ҳ���ǵ�ǰ��һ�ص���ʼ����
	private static int lastLevelScore = 0;

	// δͨ��ʱ���浱ǰ�ؿ��ĵȼ�(��ʼֵΪ��һ��)
	private static int lastLevel = 1;

	// δͨ��ʱ��ǰͨ�ص�Ŀ���������ʼĿ�����1000��
	private static int lastLevelTargetScore = 0;
	
	public static void show(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		initServiceComponent();

		try {
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass()
					.getResource("/res/layout/main_layout.fxml"));
			
			if (logger.isDebugEnabled())
				addDebugListener(root);

			// �������ּ��뵽��ͼ������
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);

			// ҳ�����ʱ�����¿�ʼ�µ����������ʱ���ʼ��������
			initGameStars(root);

			primaryStage.setTitle("����������-Popstar3");
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void addDebugListener(AnchorPane root) {
		
		root.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				logger.debug(event.getTarget().getClass().getName());
				
				if(event.getTarget() instanceof Text) {
					logger.debug(((Text)event.getTarget()).getText());
				}
			}
		});
	}

	/**
	 * ҳ�����ʱ�����¿�ʼ�µ����������ʱ���ʼ��������
	 * 
	 * @param root
	 *            �����ܲ���
	 * 
	 */
	private void initGameStars(AnchorPane root) {

		// ����ҵ����ȡ���10*10����������
		mCurretStars = mStarService.createBubbleMatrix();

		if (mCurretStars == null || mCurretStars.size() == 0) {
			logger.error("�����û���ṩ��ȷ������������");
			return;
		}

		mRoot = root;

		// ��ȡ�����ǲ��ֶ���
		mAnchorPane = (AnchorPane) root.lookup("#game_pane");
		
		// ����״̬�¿���ʾ�����ǵ�����
		if (logger.isDebugEnabled())
			showStarsCoordinate();

		StartEventHandler handler = new StartEventHandler();

		// ��StarList�е�����Star����ת����Label��ͼ����
		List<Label> starFrames = StarFormUtils.convert(mCurretStars);
		for(Label starFrame : starFrames)
			starFrame.setOnMouseClicked(handler);

		// ��Label���뵽��ͼ������
		for (int i = 0; i < starFrames.size(); i++) {
			mAnchorPane.getChildren().add(starFrames.get(i));
		}

		// ��õ�һ�ص�ͨ�ط���
		lastLevelTargetScore = mScoreService.getCurrentLevelScore();

		Label lastLevelTargetScore1 = (Label) root.lookup("#targetScore");
		lastLevelTargetScore1.setText(lastLevelTargetScore+"");
	}

	/**
	 * ����״̬�¿���ʾ�����ǵ�����
	 * 
	 * @param root
	 * @param pane
	 */
	private void showStarsCoordinate() {

		// ��ȡ�˵�����
		FlowPane menu_title = (FlowPane) mRoot.lookup("#menu_title");

		menu_title.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				for (int i = 0; i < mAnchorPane.getChildren().size(); i++) {
					Label button = (Label) mAnchorPane.getChildren().get(i);
					button.setText("(" + button.getId() + ")");
				}
			}

		});

		menu_title.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				for (int i = 0; i < mAnchorPane.getChildren().size(); i++) {
					Label button = (Label) mAnchorPane.getChildren().get(i);
					button.setText("");
				}

			}

		});
	}

	/**
	 * ��ʼ�������ط��������
	 */
	private void initServiceComponent() {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"service.xml");

		try {
			mStarService = (StarService) context.getBean("starService");

			mScoreService = (ScoreService) context.getBean("scoreService");

		} catch (Exception e) {

			logger.error("�޷�����StarService��ScoreService��ϵͳ�Ƿ��˳�");
			System.exit(0);
		}
	}
	
	protected boolean isClickAvailable = true;
	
	/**
	 * �����Ǳ�������¼��������
	 */
	public class StartEventHandler implements EventHandler<MouseEvent> {

		public StartEventHandler() {
			mAnchorPane = (AnchorPane) mRoot.lookup("#game_pane");
			scoreLabel = (Label) mRoot.lookup("#lblScore");
		}

		@Override
		public void handle(MouseEvent event) {

			if(isClickAvailable == false)
				return;
			
			isClickAvailable = false;
			
			// ��ȡ���������������ͼ
			Label starFrame = (Label) event.getTarget();

			// ����ͼת��Ϊ������ʵ��
			Star base = StarFormUtils.convert(starFrame);

			// �ӷ���˻�ȡ��Ҫ������������б�
			StarList clearStars = mStarService.tobeClearedStars(base,
					mCurretStars);

			// �������������ǣ���ֹͣ����¼��Ĵ������磺�û�������������ܱ�û�й�����ɫ�������ǣ�
			if (clearStars == null || clearStars.size() == 0){
				isClickAvailable = true;
				return;
			}

			// �ӷ���˻�ȡ��Ҫ�ƶ��������ǣ���ֱ����ģ�
			StarList movedYStars = mStarService.getVMovedStars(clearStars,
					mCurretStars);

			// ���ݴӷ���˻�ȡ��Ҫ������������б������Ӧ����ͼ
			clearStars(clearStars);

			if (logger.isDebugEnabled())
				logger.debug("�������������Ǻ��ڴ������Ƕ�������:" + mCurretStars);

			// �����߳�ͬ���������ڱ�֤ˮƽ�������ƶ����ڴ�ֱ�ƶ�
			CountDownLatch yLatch = new CountDownLatch(movedYStars.size());

			// �ƶ������ϵ������ǣ�����֤ˮƽ�ƶ��ڴ�ֱ�ƶ������
			moveYStars(movedYStars, yLatch);

			if (logger.isDebugEnabled())
				logger.debug("��ֱ�����������ƶ���Y�ᣩ���ڴ������Ƕ�������:" + mCurretStars);

			// �ƶ�ˮƽ����������ǣ��ڴ�ֱ����������ɺ������
			new MoveXStarController(yLatch).start();

			// ��ʾ��������
			showScore(clearStars);
		}

	}

	/**
	 * ÿ������һ�������Ǻ���ʾ��������
	 * 
	 * @param clearStars
	 *            ���������������б�
	 * 
	 */
	private void showScore(StarList clearStars) {

		int addScore = mScoreService.getScoreByStars(clearStars);

		if (addScore != 0) {

			// ������д��ƶ������ǵ�����
			List<Integer> clearStarsCoordinate = StarFormUtils
					.getClearStarsCoordinate(clearStars);

			// ������ʾ����������ӷ���
			ParallelTransition parallelTransition = StarAnimation
					.getScoreAnimation(mRoot, clearStarsCoordinate,
							mScoreService);
			parallelTransition.play();

			score += addScore;

			// ˢ���ܵ÷�
			scoreLabel.setText("����: " + score);
		}

		// ��������һ�δﵽÿһ�ص�Ŀ�����ʱ����ͨ��֪ͨ
		if (mScoreService.isNoticedPassLevel(currentLevel, score))
			StarAnimation.passedFlag(mAnchorPane);
	}

	private class RestartEvent implements EventHandler<MouseEvent> {
		private AnchorPane root = null;
		private Label failedNotice = null;

		public RestartEvent(AnchorPane root, Label failedNotice) {
			this.root = root;
			this.failedNotice = failedNotice;
		}

		@Override
		public void handle(MouseEvent event) {

			// ���ʧ��֪ͨ
			StarAnimation.clearAwardLable(root, failedNotice);
			// ��ʼ���س�ʼ�ɼ�
			score = lastLevelScore;
			// ���³�ʼ��
			initGameStars(root);

			// �õ�ͨ�صȼ���Ŀ�����Label
			Label lastLevelNum = (Label) root.lookup("#lblCheckpoint");
			Label lastLevelTargetScore1 = (Label) root.lookup("#targetScore");
			Label rebackScore = (Label) root.lookup("#lblScore");

			// ��ʼͨ�صȼ�
			lastLevelNum.setText("��" + lastLevel + "��");

			// ��ʼĿ�����
			lastLevelTargetScore1.setText("" + lastLevelTargetScore);

			// �ع��÷�����
			rebackScore.setText("" + lastLevelScore);

		}

	}

	/**
	 * ˮƽ���������ǵ��ƶ��̣߳������ڴ�ֱ�����߳��ƶ���ִ�У�
	 * 
	 */
	class MoveXStarController extends Thread {

		private CountDownLatch latch = null;

		public MoveXStarController(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {

			try {
				// ȷ��ˮƽ�ƶ��ڴ�ֱ�ƶ������
				latch.await();
				Thread.sleep(100);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// ��ȡ��Ҫ�ƶ��Ķ�����
			StarList xStars = mStarService.getHMovedStars(mCurretStars);

			int size = xStars == null ? 0 : xStars.size();

			// ��û�п����Ŀ��ʱ�����ж��Ƿ��й�
			CountDownLatch xLatch = new CountDownLatch(size);
			
			moveXStars(xStars, xLatch);

			new ClearScreenController(xLatch).start();

		}

	}

	/**
	 * ���޿������������ǵ������߳�
	 *
	 */
	class ClearScreenController extends Thread {

		private CountDownLatch latch = null;

		public ClearScreenController(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {

			try {
				// ȷ��ˮƽ�ƶ��ڴ�ֱ�ƶ������
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			isClickAvailable = true;

			// ��Y�᷽����X�᷽�������Ƕ��Ѿ��ƶ���Ϻ��߳�ִ����Ϻ�
			// ���ж��Ƿ��п������������ǣ��������ִ�������뽱������
			if (mStarService.tobeEliminated(mCurretStars))
				return;

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// ִ�������뽱������
					runAwardAnimation();
				}

			});

		}
	}

	/**
	 * �ƶ������ϵ������ǣ�����֤ˮƽ�ƶ��ڴ�ֱ�ƶ������
	 * 
	 * @param movedStars
	 *            ������Ҫ�ƶ����������б�x��+y�ᣩ
	 * @param yStars
	 *            ����ֱ������Ҫ�ƶ����������б�y�ᣩ
	 * @param latch
	 *            �߳�ͬ����������ȷ��ˮƽ�ƶ��ڴ�ֱ�ƶ������
	 */
	private void moveYStars(StarList movedStars, CountDownLatch latch) {

		for (int i = 0; i < movedStars.size(); i++) {
			// ��ȡ���ƶ���������
			MovedStar mStar = (MovedStar)movedStars.get(i);

			// �ƶ�����Ҫ��ϸ�ڴ��������ǵ���������ʽ
			mCurretStars.update(mStar.getPosition().getRow(), mStar
					.getPosition().getColumn(), mStar
					.getMovedPosition().getRow(), mStar.getMovedPosition()
					.getColumn());

			// ת��Ϊ��ͼ����
			Label starFrame = StarFormUtils.findFrame(mStar,
					mAnchorPane);

			// �����ƶ�������
			StarAnimation.slideYStar(
					starFrame,
					mStar.getMovedPosition().getRow()
							* StarFormUtils.STAR_HEIGHT).setOnFinished(
					new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							latch.countDown();
						}
					});

			// ���¿ؼ���IDֵ
			starFrame.setId("s" + mStar.getMovedPosition().getRow()
					+ mStar.getMovedPosition().getColumn());
			// ���¿ؼ��Ĵ�������
			starFrame.setUserData(mStar.getMovedPosition().getRow() + ";"
					+ mStar.getMovedPosition().getColumn());

			// ����������
			mStar.getPosition()
					.setColumn(mStar.getMovedPosition().getColumn());

		}

	}

	/**
	 * �ƶ�������ˮƽ�����������
	 * 
	 * @param xStars
	 *            ��ˮƽ������Ҫ�ƶ����������б�x�ᣩ
	 */
	private void moveXStars(StarList xStars, CountDownLatch xLatch) {

		if(xStars == null)
			return;
		
		for (int i = 0; i < xStars.size(); i++) {
			// ��ȡ���ƶ���������
			MovedStar mStar = (MovedStar)xStars.get(i);

			// �ƶ�����Ҫ��ϸ�ڴ��������ǵ���������ʽ
			mCurretStars.update(mStar.getPosition().getRow(), mStar
					.getPosition().getColumn(), mStar
					.getMovedPosition().getRow(), mStar.getMovedPosition()
					.getColumn());

			// ת��Ϊ��ͼ����
			Label starFrame = StarFormUtils.findFrame(mStar,
					mAnchorPane);

			// �����ƶ�������
			StarAnimation.slideXStar(
					starFrame,
					mStar.getMovedPosition().getColumn()
							* StarFormUtils.STAR_WIDTH).setOnFinished(
					new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							if (xStars != null && xStars.size() > 0)
								xLatch.countDown();
						}
					});

			// ���¿ؼ���IDֵ
			starFrame.setId("s" + mStar.getMovedPosition().getRow()
					+ mStar.getMovedPosition().getColumn());
			starFrame.setUserData(mStar.getMovedPosition().getRow() + ";"
					+ mStar.getMovedPosition().getColumn());

		}
	}

	/**
	 * ���ݴӷ���˻�ȡ��Ҫ������������б������Ӧ����ͼ
	 * 
	 * @param clearStars
	 *            �ӷ���˻�ȡ��Ҫ������������б�
	 */
	private void clearStars(StarList clearStars) {
		if (clearStars == null || clearStars.size() == 0)
			return;

		for (Star star : clearStars) {

			Label starFrame = StarFormUtils.findFrame(star, mAnchorPane);

			// ɾ�������ϵ�������
			StarAnimation.clearStarLable(mAnchorPane, starFrame);

			// ɾ���ڴ��е������ǣ�����汣��һ�£�
			mCurretStars.setNull(star.getPosition().getRow(), star
					.getPosition().getColumn());
		}
	}

	/**
	 * ִ�������뽱������
	 * @return 
	 */
	private void runAwardAnimation() {
		// ����ͼ�в���������������ת��Ϊ������������
		StarList awardStarList = mStarService.getAwardStarList(mCurretStars);
		// ���ü�������ȷ��ȫ�����������������й�
		CountDownLatch slach = new CountDownLatch(awardStarList.size());
		// ����������ʼֵΪ0
		int awardScore = mScoreService.getAwardScore(awardStarList.size());
		// ������ʾ������������������ʾ���������󴥷������¼�
		StarAnimation.awardAnimation(mRoot, awardScore, awardStarList)
				.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// ���������
						// clearStars(awardStarList);

						score += awardScore;
						// ˢ���ܵ÷�
						scoreLabel.setText("����: " + score);
						// ÿ����һ�������ǣ������������Ǽ�������1
						clearLeftStar(awardStarList, slach);
						// ��֤����������Ǻ��Ƿ��й�
						new ChangeLevelThread(slach).start();

					}
				});
	}

	/**
	 * 
	 * �йض������������ö��������������������߳�ִ����Ϻ�ſ���ִ��
	 *
	 */
	class ChangeLevelThread extends Thread {
		private CountDownLatch latch = null;

		public ChangeLevelThread(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {
			try {
				latch.await();
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					// �Ƿ������й�Ҫ��
					if (mScoreService.isChangeLevel(score)) {
						// �й�
						changeLevel();
					} else {
						// ͨ��ʧ�ܣ���ʾʧ��֪ͨ
						Label failedNotice = new Label(Message.LEVEL_FAULT);
						failedNotice.setFont(new Font(40.0));
						failedNotice.setLayoutX(63.0);
						failedNotice.setLayoutY(280.0);
						failedNotice.setTextFill(Color.WHITE);
						mRoot.getChildren().add(failedNotice);

						// ע������¼���ѡ���Ƿ����ͨ��
						RestartEvent restart = new RestartEvent(mRoot,
								failedNotice);
						failedNotice.setOnMouseClicked(restart);

					}
				}
			});

		}

	}

	// ����Ҫ����������Ǹ���
	private void clearLeftStar(StarList awardStarList,
			CountDownLatch awardStarLatch) {
		if (awardStarList == null || awardStarList.size() == 0) {
			return;
		}

		for (Star star : awardStarList) {
			Label starFrame = StarFormUtils.findFrame(star, mAnchorPane);
			// ɾ�������ϵĽ���������,����������һ�������Ǻ󽫴����������Ǹ�����1
			StarAnimation.clearAwardLable(mAnchorPane, starFrame)
					.setOnFinished(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							// ɾ���ڴ��е������ǣ�����汣��һ�£�
							mCurretStars.setNull(star.getPosition().getRow(),
									star.getPosition().getColumn());
							// ����������Ǹ���������1
							awardStarLatch.countDown();
							if (logger.isDebugEnabled()) {
								logger.debug("��ǰʣ������������=====" + awardStarList
										+ "countDl=="
										+ awardStarLatch.getCount());
							}
						}

					});

		}

	}

	/**
	 * �йض���
	 */
	private void changeLevel() {
		// �л��ؿ�����Ϸ�ȼ���1
		currentLevel++;
		// �����л��ؿ��ȼ������ڴ���ʧ�ָܻ�
		lastLevel = currentLevel;
		// �����л��ؿ�����������ʧ�ָܻ���ʼ����
		lastLevelScore = score;
		// �����л��ؿ�ʱ�Ķ�Ӧ��Ŀ�����

		int nextLevelTargetScore = mScoreService.nextLevelTarget(currentLevel);
		// ��ʾ��һ�ؿ��ȼ���Ŀ�������ɺ��ٽ�����һ��
		StarAnimation.passNotice(mAnchorPane, currentLevel,
				nextLevelTargetScore).setOnFinished(
				new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						initGameStars(mRoot);
						// �õ��ؿ��ȼ�
						Label levelNum = (Label) mRoot.lookup("#lblCheckpoint");

						// �õ�Ŀ��÷���ͼ����
						Label targetScore = (Label) mRoot
								.lookup("#targetScore");
						// ���ùؿ��ȼ�
						levelNum.setText("��" + currentLevel + "��");
						// ����Ŀ�����
						targetScore.setText("" + nextLevelTargetScore);
						// ����Ŀ�����������ʧ�ָܻ�ʱ����Ŀ�����
						lastLevelTargetScore = nextLevelTargetScore;
						return;

					}

				});
	}

}
