package cn.campsg.practical.bubble;

import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.campsg.practical.bubble.common.StarAnimation;
//import cn.campsg.practical.bubble.common.StarAnimation;
import cn.campsg.practical.bubble.common.StarFormUtils;
import cn.campsg.practical.bubble.entity.Star;
import cn.campsg.practical.bubble.entity.StarList;
import cn.campsg.practical.bubble.service.StarService;

/**
 * �����Ǵ����࣬������ʾ���������С����������ǵ���¼��붯��
 * 
 * 
 * @author Frank.Chen
 * @version 1.5
 */
public class MainForm extends Application {

	private AnchorPane mRoot = null;

	private AnchorPane mAnchorPane = null;

	private StarList mCurretStars = null;

	private StarService mStarService = null;

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
	
	/**
	 * ҳ�����ʱ�����¿�ʼ�µ����������ʱ���ʼ��������
	 * 
	 * @param root
	 *            �����ܲ���
	 * 
	 */
	private void initGameStars(AnchorPane root) {

		// ����ҵ����ȡ���10*10����������
		mCurretStars = mStarService.createStars();

		if (mCurretStars == null || mCurretStars.size() == 0) {
			return;
		}

		mRoot = root;

		// ��ȡ�����ǲ��ֶ���
		mAnchorPane = (AnchorPane) root.lookup("#game_pane");
		
		StartEventHandler handler = new StartEventHandler();

		// ��StarList�е�����Star����ת����Label��ͼ����
		List<Label> starFrames = StarFormUtils.convert(mCurretStars);
		for(Label starFrame : starFrames)
			starFrame.setOnMouseClicked(handler);

		// ��Label���뵽��ͼ������
		for (int i = 0; i < starFrames.size(); i++) {
			mAnchorPane.getChildren().add(starFrames.get(i));
		}

		Label lastLevelTargetScore1 = (Label) root.lookup("#targetScore");
		lastLevelTargetScore1.setText(lastLevelTargetScore+"");
	}

	/**
	 * ��ʼ�������ط��������
	 */
	private void initServiceComponent() {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"service.xml");
		try {
			mStarService = (StarService) context.getBean("starService");
		} catch (Exception e) {
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
		}

		@Override
		public void handle(MouseEvent event) {

//			if(isClickAvailable == false)
//				return;
//			
//			isClickAvailable = false;
//			
			// ��ȡ���������������ͼ
			Label starFrame = (Label) event.getTarget();

			// ����ͼת��Ϊ������ʵ��
			Star base = StarFormUtils.convert(starFrame);

			// �ӷ���˻�ȡ��Ҫ������������б�
			StarList clearStars = mStarService.tobeClearedStars(base,
					mCurretStars);

			// �������������ǣ���ֹͣ����¼��Ĵ������磺�û�������������ܱ�û�й�����ɫ�������ǣ�
//			if (clearStars == null || clearStars.size() == 0){
//				isClickAvailable = true;
//				return;
//			}
//
//			// �ӷ���˻�ȡ��Ҫ�ƶ��������ǣ���ֱ����ģ�
//			StarList movedYStars = mStarService.getVMovedStars(clearStars,
//					mCurretStars);

			// ���ݴӷ���˻�ȡ��Ҫ������������б������Ӧ����ͼ
			clearStars(clearStars);


			// �����߳�ͬ���������ڱ�֤ˮƽ�������ƶ����ڴ�ֱ�ƶ�
//			CountDownLatch yLatch = new CountDownLatch(movedYStars.size());
//
//			// �ƶ������ϵ������ǣ�����֤ˮƽ�ƶ��ڴ�ֱ�ƶ������
//			moveYStars(movedYStars, yLatch);
//
//
//			// �ƶ�ˮƽ����������ǣ��ڴ�ֱ����������ɺ������
//			new MoveXStarController(yLatch).start();
//
//			// ��ʾ��������
//			showScore(clearStars);
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

}
