package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.input.MouseEvent ;
import javafx.stage.WindowEvent;


public class MainController  implements Initializable {
	private Main myApp;			//用于和主控程序关联
	//定义界面中的输入栏目
	@FXML
	private ComboBox<String> yhlb;
	@FXML 
    private ComboBox<String> yhzh;
    @FXML    //不能删除：每个@FXML只管一个控件
    private TextField yhkl;
    @FXML
    private Button qdan;
    @FXML
    private Button tcan;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//yhlb.getItems().removeAll(yhlb.getItems());
    	yhlb.getItems().addAll( "病人登录", "医生登录");
    	yhlb.getSelectionModel().select(0);
    	//yhzh.getItems().addAll( "xx病人登录", "xx医生登录"); 
    	yhzh.setOnMouseClicked(e -> onyhzhEnter( e)); 
    //	yhzh.setOnMouseEntered(e -> onyhzhEnter( e)); 	//去掉注解后用第1种方法通过Lambda表达式处理事件
    //	yhzh.setOnMouseExited(e -> onyhzhExit( e));		//去掉注解后用第1种方法通过Lambda表达式处理事件
    	//yhzh.setOnKeyPressed(e->onkeyPre(e));
    	yhzh.setVisibleRowCount(5);
    	qdan.setOnAction(e->onqueding(e));
    	tcan.setOnAction(e->quit(e));
    }   
    
      
    //第1种方法通过Lambda表达式处理事件
    @FXML
    private void onyhzhEnter(MouseEvent event)  { //可不要yhzh参数
    	//System.out.println("mouse entered");
    	yhzh.getItems().removeAll(yhzh.getItems());
    	if(yhlb.getSelectionModel().getSelectedIndex()==0)
    	{
        	int len=myApp.brxx.size();
        	for(int x=0; x<len; x++)
        		yhzh.getItems().add(myApp.brxx.get(x).BRBH+"\t"+myApp.brxx.get(x).BRMC);
    	}
    	else
    	{
    		int len=myApp.ksys.size();
        	for(int x=0; x<len; x++)
        		yhzh.getItems().add(myApp.ksys.get(x).YSBH+"\t"+myApp.ksys.get(x).YSMC+"\t"+myApp.ksys.get(x).PYZS);
    	}
    	
    	
    	yhzh.show();
    }  
    /*
    @FXML
    private void onyhzhExit( MouseEvent event)  {
    	yhzh.hide();
    }   

    */
    @FXML
    private void quit(ActionEvent e)  {
    	Event.fireEvent(myApp.primaryStage,
    			new WindowEvent(myApp.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
    
    
    
    @FXML
    private void onqueding(ActionEvent event)  {
    	//加载主控界面
		//装载登录界面
    	if(yhzh.getValue()==null)
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
           			"账号错误！", "警告", JOptionPane.WARNING_MESSAGE);
    		return;
    	}
    	String account=yhzh.getValue().substring(0,6);
    	String pass=yhkl.getText();
    	
    	if(yhlb.getSelectionModel().getSelectedIndex()==0)//patient
    	{
    		try
    		{
    			Statement stat = myApp.con.createStatement();
    			String sql="SELECT * FROM T_BRXX WHERE BRBH="+"'"+account+"'";
        		ResultSet res = stat.executeQuery(sql);
        		
        		String result=null;
        		if(res.next())
        		{
        			myApp.dlbr=new BRXX(res);
        			result=res.getString("DLKL");
        		}
        		else
        		{
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	              			"账号错误！", "警告", JOptionPane.WARNING_MESSAGE);
        			res.close();
        			stat.close();
        			return;
        		}
        		
        		if(result.equals(pass))
        		{	//not yet implemented count number of today
        			stat.executeUpdate("UPDATE T_BRXX SET DLRQ ="+"'"+new Timestamp(System.currentTimeMillis())+"' WHERE BRBH='"+account+"'");
        			//myApp.dlbr=new BRXX(res);
        			myApp.brys=1;
        			res.close();
        			stat.close();
        		}
        		else
        		{
        			
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), "密码错误！", "警告", JOptionPane.WARNING_MESSAGE);
        			res.close();
        			stat.close();
        			return;
        		}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    			return;
    		}
    		
    		yhkl.clear();
			System.out.println("登录成功。");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Info.fxml"));
			Parent root =null;
			try {
			    root = (Parent)loader.load();
			}
		    catch(Exception e) {
			    e.printStackTrace();
			    return;
		    }
			DbguiController controller=(DbguiController ) loader.getController();
			controller.setUp(myApp);
			Scene scene = new Scene(root,600,400);
			//Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			myApp.primaryStage.setScene(scene);  //登录后，主控界面可以利用登录界面的Stage，主控界面打开菜单弹出的新界面可以创建新的Stage
			myApp.primaryStage.show();    	
    	}
    	else if(yhlb.getSelectionModel().getSelectedIndex()==1)//doctor
    	{
    		String result=null;
    		String sql=null;
    		try
    		{
    			Statement stat = myApp.con.createStatement();
        		ResultSet res = stat.executeQuery("SELECT * FROM T_KSYS WHERE YSBH="+"'"+account+"'");
        		if(res.next())
        		{
        			myApp.dlys=new KSYS(res);
        			result=res.getString("DLKL");
        		}
        		else
        		{
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	              			"账号错误！", "警告", JOptionPane.WARNING_MESSAGE);
        			res.close();
        			stat.close();
        			return;
        		}
        		
        		if(result.equals(pass))
        		{
        			sql="UPDATE T_KSYS SET DLRQ ="+"'"+new Timestamp(System.currentTimeMillis())+"' WHERE YSBH='"+account+"'";
        			stat.executeUpdate(sql);
        			//myApp.dlys=new KSYS(res);
        			myApp.brys=2;
        			res.close();
        			stat.close();
        			
        		}
        		else
        		{
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
 	              			"密码错误！", "警告", JOptionPane.WARNING_MESSAGE);
        			res.close();
        			stat.close();
        			return;
        		}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    			return;
    		}
    		
    		yhkl.clear();
			System.out.println("登录成功。");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Doctor.fxml"));
			Parent root =null;
			try {
			    root = (Parent)loader.load();
			}
		    catch(Exception e) {
			    e.printStackTrace();
			    return;
		    }
			DoctorController controller=(DoctorController ) loader.getController();
			controller.setUp(myApp);
			Scene scene = new Scene(root,600,400);
			//Scene scene = new Scene(root,600,400);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			myApp.primaryStage.setScene(scene);  //登录后，主控界面可以利用登录界面的Stage，主控界面打开菜单弹出的新界面可以创建新的Stage
			myApp.primaryStage.show();    	
    	}
    	else
    	{
    		return;
    	}
		
       
		
	
    } 
    
    public void setUp(Main application) {
        myApp = application;

    }    
}
