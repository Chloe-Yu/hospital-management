package application;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;


import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;

import javafx.stage.WindowEvent;


public class DoctorController implements Initializable{
	private Main myApp;			//用于和主控程序关联
	

	@FXML
	private Button exit_btn;
	@FXML
	private Button logout_btn;
	
	static Statement stmt = null,stmt2=null;
    ObservableList<BRXX_show> pat_list = FXCollections.observableArrayList();
    ObservableList<Income> income_list = FXCollections.observableArrayList();
    
	@FXML
	TableView<BRXX_show> table_reg;
	@FXML
	TableView<Income> table_income;
    @FXML
    private Button btn_exit;
    @FXML
    private TableColumn<?, ?> col_regnum,col_patname,col_regtime,col_regtype
    	,col_officename,col_docnum,col_docname,col_regtype2,col_regcount,col_income,col_valid;
    @FXML
    TextField text_begin,text_end;
    @FXML
    DatePicker date_end,date_begin;

    @Override
    public void initialize(URL url ,ResourceBundle rb) 
    {
    	date_begin.setValue(LocalDate.now());
    	date_end.setValue(LocalDate.now());;
    	col_regnum.setCellValueFactory(new PropertyValueFactory<>("regNum"));  
    	col_patname.setCellValueFactory(new PropertyValueFactory<>("patName"));  
    	col_regtime.setCellValueFactory(new PropertyValueFactory<>("regTime")); 
    	col_regtype.setCellValueFactory(new PropertyValueFactory<>("regType"));
    	col_valid.setCellValueFactory(new PropertyValueFactory<>("valid"));
    	
    	col_officename.setCellValueFactory(new PropertyValueFactory<>("officeName"));  
    	col_docnum.setCellValueFactory(new PropertyValueFactory<>("docNum"));  
    	col_docname.setCellValueFactory(new PropertyValueFactory<>("docName")); 
    	col_regtype2.setCellValueFactory(new PropertyValueFactory<>("regType"));
    	col_regcount.setCellValueFactory(new PropertyValueFactory<>("regCount"));  
    	col_income.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
    }
    
    @FXML
    private void on_mouse_entered(Event event)
    {
    	try
		{
    		String time_begin,time_end;
    		LocalDate datetmp = date_begin.getValue();
    		LocalDate datetmp2 = date_end.getValue();
    		if(datetmp==null||datetmp2==null)
    		{
    			time_begin = LocalDate.now().toString();
    			time_end = LocalDate.now().toString();
    		}
    		else
    		{
    			time_begin = datetmp.toString();
    			time_end = datetmp2.toString();
    		}
    		time_begin += " 00:00:00";
    		time_end += " 23:59:59";
    		pat_list.clear();
    		income_list.clear();
  	        
  	         stmt = myApp.con.createStatement();
  	         stmt2 = myApp.con.createStatement();
  	         
   	      
   	         String sql = "select gh.GHBH,br.BRMC,gh.RQSJ,hz.SFZJ,gh.THBZ from T_GHXX gh,T_BRXX br,T_HZXX hz where YSBH= '"+myApp.dlys.YSBH+"' AND gh.BRBH=br.BRBH AND gh.HZBH=hz.HZBH ";
   	         ResultSet rs = stmt.executeQuery(sql);
   	         String register_num=null;
   	         String reg_datetime=null,pat_name=null,expertstr=null,doc_num=null;
   	         String office_name = null,doc_name=null,unreg_str=null,total_cost=null;
   	      
   	         
   	         String regcount = null;
   	        
   	         while(rs.next())
   	         {
   	        	register_num = rs.getString("GHBH");
   	        	//registration_num = rs.getString("registration_num");
   	        	pat_name = rs.getString("BRMC");
   	        	reg_datetime = rs.getString("RQSJ");
   	        	unreg_str =(rs.getByte("THBZ")==1)?"否":"是";
   	        	expertstr = rs.getByte("hz.SFZJ")==1? "专家号":"普通号";
   	        	pat_list.add(new BRXX_show(register_num,pat_name,reg_datetime,expertstr,unreg_str));
   	         }
   	         rs.close();
   	         sql = "select KS.KSMC,GH.YSBH,YS.YSMC,HZ.SFZJ,"
   	         		+ "count(*) as many,sum(GH.GHFY) as much "
   	         		+ "from T_GHXX GH,T_HZXX HZ,T_KSXX KS,T_KSYS YS "
   	         		+ "where "
   	         		+" GH.THBZ=0"
   	         	    + " and GH.RQSJ>='"+time_begin+"' and "
	         		+ " GH.RQSJ<='"+time_end+"'" 
   	         		+ " and GH.HZBH=HZ.HZBH " 
   	         		+ "and GH.YSBH=YS.YSBH " 
   	         		+ "and KS.KSBH=HZ.KSBH "
   	         		+ " group by GH.YSBH,HZ.SFZJ";
   	         rs = stmt.executeQuery(sql);
   	         while(rs.next())
   	         {
   	        	office_name = rs.getString("KS.KSMC");
   	        	doc_num = rs.getString("GH.YSBH");
   	        	doc_name = rs.getString("YS.YSMC");
   	        	
   	        	regcount = rs.getString("many");
   	        	total_cost = rs.getString("much");
   	        	expertstr = rs.getByte("HZ.SFZJ")==1? "专家号":"普通号";
   	        	income_list.add(new Income(office_name, doc_num,doc_name,expertstr,regcount,total_cost));
   	         }
   	         if(rs!=null)
   	        	 rs.close();
   	        
   	         stmt2.close();
   	         stmt.close();
		}
    	catch(SQLException se){
	         	// 处理 JDBC 错误
	         	se.printStackTrace();
	    }
    	finally
    	{
    		table_reg.setItems(pat_list);
    		table_income.setItems(income_list);
    	}
    }
    
    
    
    @FXML
    private void exit(ActionEvent e)  {
    	Event.fireEvent(myApp.primaryStage,
    			new WindowEvent(myApp.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
   
    public void setUp(Main application) {
        myApp = application;

    }     
}
