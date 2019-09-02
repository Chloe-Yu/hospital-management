package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.sql.*;
//import java.sql.Statement;
//import java.sql.ResultSet;
import java.util.ArrayList;

public class Main extends Application {
	//���ݿ�������Ϣ
    String strCon = "jdbc:mysql://localhost:3306/hos?useSSL=false&serverTimezone=Asia/Shanghai";
	String strUserName = "root"; 	// ���ݿ���û�����
	String strPWD = "yyx416"; // ���ݿ������
	public java.sql.Connection con =null;	
	public short brys=0; 		//����0ʱ���˵�¼������1ʱΪ���˵�¼������2Ϊҽ����¼
	//���˻�����Ϣ
	public ArrayList<BRXX> brxx= new ArrayList<BRXX>(); //for doctors
	public ArrayList<KSYS> ksys= new ArrayList<KSYS>(); //for patients
	public ArrayList<KSXX> ksxx= new ArrayList<KSXX>(); 
	public ArrayList<HZXX> hzxx= new ArrayList<HZXX>();
	public BRXX dlbr=null;  	//��¼�Ĳ�����Ϣ
	public KSYS dlys=null;		//��¼��ҽ����Ϣ
	public Stage primaryStage=null;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage=primaryStage;
			//�������ݿ�
			Class.forName("com.mysql.cj.jdbc.Driver");
            con = java.sql.DriverManager.getConnection(strCon, strUserName, strPWD);
            if(con!=null) System.out.println("��˳�����ӵ����ݿ⡣");		
            //���벡�˺�ҽ����Ϣ
            Statement stat = con.createStatement();
            ResultSet rsbr = stat.executeQuery("SELECT * FROM T_BRXX");
            while(rsbr.next()){ //ȡ��һ�м�¼������rs��
            	brxx.add(new BRXX(rsbr));
            };
            rsbr.close();
            ResultSet rsys = stat.executeQuery("SELECT * FROM T_KSYS");
            while(rsys.next()){ //ȡ��һ�м�¼������rs��
            	ksys.add(new KSYS(rsys));
            };       
            rsys.close();
            
            ResultSet rsks = stat.executeQuery("SELECT * FROM T_KSXX");
            while(rsks.next()){ //ȡ��һ�м�¼������rs��
            	ksxx.add(new KSXX(rsks));
            };   
            rsks.close();
           ResultSet rshz = stat.executeQuery("SELECT * FROM T_HZXX");
            while(rshz.next()){ //ȡ��һ�м�¼������rs��
            	hzxx.add(new HZXX(rshz));
            };   
            rshz.close();
            stat.close();
			//װ�ص�¼����
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
			Parent root = (Parent)loader.load();
			MainController controller=(MainController) loader.getController();
	        controller.setUp(this);
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
