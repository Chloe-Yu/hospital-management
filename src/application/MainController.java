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
	private Main myApp;			//���ں����س������
	//��������е�������Ŀ
	@FXML
	private ComboBox<String> yhlb;
	@FXML 
    private ComboBox<String> yhzh;
    @FXML    //����ɾ����ÿ��@FXMLֻ��һ���ؼ�
    private TextField yhkl;
    @FXML
    private Button qdan;
    @FXML
    private Button tcan;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//yhlb.getItems().removeAll(yhlb.getItems());
    	yhlb.getItems().addAll( "���˵�¼", "ҽ����¼");
    	yhlb.getSelectionModel().select(0);
    	//yhzh.getItems().addAll( "xx���˵�¼", "xxҽ����¼"); 
    	yhzh.setOnMouseClicked(e -> onyhzhEnter( e)); 
    //	yhzh.setOnMouseEntered(e -> onyhzhEnter( e)); 	//ȥ��ע����õ�1�ַ���ͨ��Lambda���ʽ�����¼�
    //	yhzh.setOnMouseExited(e -> onyhzhExit( e));		//ȥ��ע����õ�1�ַ���ͨ��Lambda���ʽ�����¼�
    	//yhzh.setOnKeyPressed(e->onkeyPre(e));
    	yhzh.setVisibleRowCount(5);
    	qdan.setOnAction(e->onqueding(e));
    	tcan.setOnAction(e->quit(e));
    }   
    
      
    //��1�ַ���ͨ��Lambda���ʽ�����¼�
    @FXML
    private void onyhzhEnter(MouseEvent event)  { //�ɲ�Ҫyhzh����
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
    	//�������ؽ���
		//װ�ص�¼����
    	if(yhzh.getValue()==null)
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), 
           			"�˺Ŵ���", "����", JOptionPane.WARNING_MESSAGE);
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
 	              			"�˺Ŵ���", "����", JOptionPane.WARNING_MESSAGE);
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
        			
        			JOptionPane.showMessageDialog(new JFrame().getContentPane(), "�������", "����", JOptionPane.WARNING_MESSAGE);
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
			System.out.println("��¼�ɹ���");
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
			myApp.primaryStage.setScene(scene);  //��¼�����ؽ���������õ�¼�����Stage�����ؽ���򿪲˵��������½�����Դ����µ�Stage
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
 	              			"�˺Ŵ���", "����", JOptionPane.WARNING_MESSAGE);
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
 	              			"�������", "����", JOptionPane.WARNING_MESSAGE);
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
			System.out.println("��¼�ɹ���");
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
			myApp.primaryStage.setScene(scene);  //��¼�����ؽ���������õ�¼�����Stage�����ؽ���򿪲˵��������½�����Դ����µ�Stage
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
