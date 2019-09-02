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
	//数据库连接信息
    String strCon = "jdbc:mysql://localhost:3306/hos?useSSL=false&serverTimezone=Asia/Shanghai";
	String strUserName = "root"; 	// 数据库的用户名称
	String strPWD = "yyx416"; // 数据库的密码
	public java.sql.Connection con =null;	
	public short brys=0; 		//等于0时无人登录，等于1时为病人登录，等于2为医生登录
	//病人基本信息
	public ArrayList<BRXX> brxx= new ArrayList<BRXX>(); //for doctors
	public ArrayList<KSYS> ksys= new ArrayList<KSYS>(); //for patients
	public ArrayList<KSXX> ksxx= new ArrayList<KSXX>(); 
	public ArrayList<HZXX> hzxx= new ArrayList<HZXX>();
	public BRXX dlbr=null;  	//登录的病人信息
	public KSYS dlys=null;		//登录的医生信息
	public Stage primaryStage=null;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage=primaryStage;
			//连接数据库
			Class.forName("com.mysql.cj.jdbc.Driver");
            con = java.sql.DriverManager.getConnection(strCon, strUserName, strPWD);
            if(con!=null) System.out.println("已顺利连接到数据库。");		
            //读入病人和医生信息
            Statement stat = con.createStatement();
            ResultSet rsbr = stat.executeQuery("SELECT * FROM T_BRXX");
            while(rsbr.next()){ //取得一行记录，放在rs里
            	brxx.add(new BRXX(rsbr));
            };
            rsbr.close();
            ResultSet rsys = stat.executeQuery("SELECT * FROM T_KSYS");
            while(rsys.next()){ //取得一行记录，放在rs里
            	ksys.add(new KSYS(rsys));
            };       
            rsys.close();
            
            ResultSet rsks = stat.executeQuery("SELECT * FROM T_KSXX");
            while(rsks.next()){ //取得一行记录，放在rs里
            	ksxx.add(new KSXX(rsks));
            };   
            rsks.close();
           ResultSet rshz = stat.executeQuery("SELECT * FROM T_HZXX");
            while(rshz.next()){ //取得一行记录，放在rs里
            	hzxx.add(new HZXX(rshz));
            };   
            rshz.close();
            stat.close();
			//装载登录界面
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
