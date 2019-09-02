package application;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import java.math.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;



public class DbguiController implements Initializable{
	

	private Main myApp;			//用于和主控程序关联
	private String sql=null;
    static Statement stmt = null;
    private boolean flag_of_changedbyunreg=false;
    private boolean ks_changed=true;
    private boolean ys_initialize=false;
    private boolean hz_initialize=false;
    private boolean ys_changed=true;
    private boolean hz_changed=true;
    private boolean reg_ready=false;
    private BigDecimal if_suc=null;
    private KSXX ks_class=new KSXX();
    private KSYS ys_class=new KSYS();
    private HZXX hz_class=new HZXX();
    private GHXX gh_class=new GHXX();
    public ArrayList<KSYS> ys_list= new ArrayList<KSYS>(); 
    public ArrayList<HZXX> hz_list= new ArrayList<HZXX>(); 
    ObservableList<GHXX_show> ghxx = FXCollections.observableArrayList();
    ObservableList<GHXX_show> unreg_list = FXCollections.observableArrayList();
    @FXML
    private TableView<GHXX_show> table_patreg;
	    
	    @FXML
	    private Button btn_ok,btn_clear,btn_exit,btn_unreg;
	 
	    @FXML
	    private ComboBox<String> kslb,yslb,expert,hzlb;
	    @FXML
	    private TextField cost,pay,change,regnum;
	    
	    @FXML
	    private TableColumn<GHXX_show,String>tGHBH,tHZMC,tYSBH,tYSMC,tGHRC,tGHFY,tGHSJ;
	    @FXML
	    private Tab tab_reg,tab_unreg;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	tGHBH.setCellValueFactory(new PropertyValueFactory<GHXX_show,String>("GHBH"));  
    	tHZMC.setCellValueFactory(new PropertyValueFactory<GHXX_show,String>("HZMC"));  
    	tYSBH.setCellValueFactory(new PropertyValueFactory<GHXX_show,String>("YSBH")); 
    	tYSMC.setCellValueFactory(new PropertyValueFactory<GHXX_show,String>("YSMC"));
    	tGHRC.setCellValueFactory(new PropertyValueFactory<GHXX_show,String>("GHRC"));  
    	tGHFY.setCellValueFactory(new PropertyValueFactory<GHXX_show,String>("GHFY"));  
    	tGHSJ.setCellValueFactory(new PropertyValueFactory<GHXX_show,String>("GHSJ")); 
    	table_patreg.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
       
    	kslb.setOnAction(e->ks_okay(e));
    	kslb.setOnMouseClicked(e->ks_clicked(e));
    	kslb.setOnKeyReleased(e->ks_typed(e));
    	//kslb.setOnMouseClicked(e->on_listoffice_clicked(e));
    	yslb.setOnAction(e->ys_okay(e));
    	yslb.setOnMouseClicked(e->ys_clicked(e));
    	yslb.setOnKeyReleased(e->ys_typed(e));
    	expert.setOnAction(e->expert_selected(e));
    	hzlb.setOnAction(e->hz_okay(e));
    	hzlb.setOnMouseClicked(e->hz_clicked(e));
    	hzlb.setOnKeyReleased(e->hz_typed(e));
    	pay.setOnAction(e->pay_okay(e));
    	btn_ok.setOnMouseClicked(e->btn_reg_clicked(e));
        btn_exit.setOnMouseClicked(e->btn_exit_clicked(e));
    }  


 
    @FXML
    private void ks_clicked(MouseEvent event)  { 
    //按近似度显示    	

    	if(ks_changed)
    	{
    		kslb.getItems().removeAll(kslb.getItems());
    		int len = myApp.ksxx.size();
    		
    		String filled = kslb.getValue();
    		int i=0;
    		if(filled==null||filled.isEmpty())
    		{

            	for(int x=0; x<len; x++)
            		kslb.getItems().add(myApp.ksxx.get(x).PYZS+" "+myApp.ksxx.get(x).KSBH+" "+myApp.ksxx.get(x).KSMC);
    			
    		}
    		else
    		{
    			int fill_len=filled.length();
    			while(i<fill_len&&filled.charAt(i)!=' ') 
    				i++;//get the PYZS
        		filled=filled.substring(0, i);
        		
            	
            	double mom=0;
        		double son=0;
            	double sim[]=new double[len];
            	int index[]=new int[len];
            	for(int x=0; x<len; x++)
            	{
            		
            		String pass= myApp.ksxx.get(x).PYZS;
            		mom=stringCompare(filled,pass);
            		son=i+pass.length();
            		sim[x]=2*mom/son;
            		index[x]=x;
            	}
            	for(int x=0;x<len;x++)
            	{
            		for(int j=0;j<len-1;j++){//内层循环控制每一趟排序多少次
            		if(sim[index[j]]>sim[index[j+1]]){
            		int temp=index[j];
            		index[j]=index[j+1];
            		index[j+1]=temp;
            		}
            	}
            	}
            	for(int x=len-1; x>=0; x--)
            		{
            		kslb.getItems().add(myApp.ksxx.get(index[x]).PYZS+" "+myApp.ksxx.get(index[x]).KSBH+" "+myApp.ksxx.get(index[x]).KSMC);
            		}
            	
    		}
    		
        	
           
    	}
    	 //kslb.show();
         ks_changed=false;
    }

    @FXML
    private void ks_okay(ActionEvent e)
    {	//检查是否valid
    	yslb.getItems().clear();
        yslb.disableProperty().setValue(true);
        hzlb.disableProperty().setValue(true);
       // expert.getItems().clear();
        hzlb.getItems().clear();
        //hz_list.clear();
        cost.clear();
    	pay.clear();
    	change.clear();
    	regnum.clear();
    
        String ks=kslb.getValue();
        
        if(ks==null) return;
        int len=ks.length();
        Byte count=0;
        Boolean valid=false;
        int j=0;
        for(int i=0;i<len;i++)
        {
        	if(ks.charAt(i)==' ')
        	{
        		if(count==0)
        		{
        			ks_class.PYZS=ks.substring(0, i);
        			j=i;
        			count++;
        			
        		}
        		else
        		{
        			ks_class.KSBH=ks.substring(j+1,i);
        			if(i+1==len) break;	
        			ks_class.KSMC=ks.substring(i+1);
        			valid=myApp.ksxx.contains(ks_class);
        			break;
        		}
        		
        	}
        }
        if(valid)
        {
        	
        	 yslb.disableProperty().setValue(false);
        	 hzlb.disableProperty().setValue(false);
        	 ys_initialize=true;  
        	 ys_changed=true;
        	 hz_changed=true;
        	 }
        
    	
    }
    
    @FXML
    private void ks_typed(KeyEvent e)
    {	//检查是否完整 更新项
    	//筛选更新列表
    	ks_changed=true;
    }

    
    @FXML
    private void ys_clicked(MouseEvent event)  { 
    	
    	if(ys_initialize)//get the doctor list for the ks
    	{try
    		{
    			ys_list.clear();
    			stmt=myApp.con.createStatement();
    			sql="SELECT * FROM T_KSYS WHERE KSBH='"+ks_class.KSBH+"'";
    			ResultSet rs=stmt.executeQuery(sql);
    			 while(rs.next()){ 
    	            	ys_list.add(new KSYS(rs));
    	            }; 
    	            rs.close();
    	            stmt.close();
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}
    	ys_initialize=false;
    	}
    	if(ys_changed)
    	{
    		yslb.getItems().removeAll(yslb.getItems());
    		int len = ys_list.size();
    		
    		String filled = yslb.getValue();
    		int i=0;
    		if(filled==null||filled.isEmpty())
    		{

            	for(int x=0; x<len; x++)
            		yslb.getItems().add(ys_list.get(x).PYZS+" "+ys_list.get(x).YSBH+" "+ys_list.get(x).YSMC);
    			
    		}
    		else
    		{
    			int fill_len=filled.length();
    			while(i<fill_len&&filled.charAt(i)!=' ') 
    				i++;//get the PYZS
        		filled=filled.substring(0, i);
        		
            	
            	double mom=0;
        		double son=0;
            	double sim[]=new double[len];
            	int index[]=new int[len];
            	for(int x=0; x<len; x++)
            	{
            		
            		String pass= ys_list.get(x).PYZS;
            		mom=stringCompare(filled,pass);
            		son=i+pass.length();
            		sim[x]=2*mom/son;
            		index[x]=x;
            	}
            	for(int x=0;x<len;x++)
            	{
            		for(int j=0;j<len-1;j++){//内层循环控制每一趟排序多少次
            		if(sim[index[j]]>sim[index[j+1]]){
            		int temp=index[j];
            		index[j]=index[j+1];
            		index[j+1]=temp;
            		}
            	}
            	}
            	for(int x=len-1; x>=0; x--)
            		{
            		yslb.getItems().add(ys_list.get(index[x]).PYZS+" "+ys_list.get(index[x]).YSBH+" "+ys_list.get(index[x]).YSMC);
            		}
            	
    		}
    		
        	
    		ys_changed=false;
    	}
    	
        
    }

    @FXML
    private void ys_okay(ActionEvent e)
    {	//检查是否完整 更新项
    	//筛选更新列表
              
    	
        expert.getItems().clear();
        hzlb.getItems().clear();
        cost.clear();
    	pay.clear();
    	change.clear();
    	regnum.clear();
        ys_changed=true;
        String ks=yslb.getValue();
        if(ks==null) return;
        int len=ks.length();
        Byte count=0;
        Boolean valid=false;
        int j=0;
        for(int i=0;i<len;i++)
        {
        	if(ks.charAt(i)==' ')
        	{
        		if(count==0)
        		{
        			ys_class.PYZS=ks.substring(0, i);
        			j=i;
        			count++;
        			
        		}
        		else
        		{
        			ys_class.YSBH=ks.substring(j+1,i);
        			if(i+1==len) break;	
        			ys_class.YSMC=ks.substring(i+1);
        			valid=ys_list.contains(ys_class);
        			break;
        		}
        		
        	}
        }
        if(valid)
        {
        	try
    		{
    			
    			stmt=myApp.con.createStatement();
    			sql="SELECT * FROM T_KSYS WHERE YSBH='"+ys_class.YSBH+"'";
    			ResultSet rs=stmt.executeQuery(sql);
    			 if(rs.next()){ 
    	            	ys_class=new KSYS(rs);
    	            }; 
    	            rs.close();
    	            stmt.close();
    		}
    		catch(Exception err){
    			err.printStackTrace();
    		}
       
        	expert.getItems().clear();
        	if(ys_class.SFZJ==1)
        	{
        		expert.getItems().add("1	专家");
        		expert.getItems().add("0	普通");
        	}
        	else
        	{
        		expert.getItems().add("0	普通");
        	}
        
        	
        }
     
    	
    }
    
    @FXML
    private void ys_typed(KeyEvent e)
    {	//检查是否完整 更新项
    	//筛选更新列表
    	ys_changed=true;
    }

    @FXML
    private void expert_selected(ActionEvent e)
    {
    	hz_initialize=true;
    	hz_changed=true;
    }
    @FXML
    private void hz_clicked(MouseEvent event)  { 
    	
    	if(hz_initialize)//get the hzr list for the ks and lb
    	{try
    		{
    			hz_list.clear();
    			stmt=myApp.con.createStatement();
    			sql="SELECT * FROM T_HZXX WHERE KSBH='"+ks_class.KSBH+"' AND SFZJ="+expert.getValue().charAt(0);
    			ResultSet rs=stmt.executeQuery(sql);
    			 while(rs.next()){ 
    	            	hz_list.add(new HZXX(rs));
    	            }; 
    	            rs.close();
    	            stmt.close();
    		}
    		catch(Exception e){
    			e.printStackTrace();
    		}

    	}
    	if(hz_changed)
    	{
    		hzlb.getItems().removeAll(hzlb.getItems());
    		int len = hz_list.size();
    		
    		String filled = hzlb.getValue();
    		int i=0;
    		if(filled==null||filled.isEmpty())
    		{

            	for(int x=0; x<len; x++)
            		hzlb.getItems().add(hz_list.get(x).PYZS+" "+hz_list.get(x).HZBH+" "+hz_list.get(x).HZMC);
    			
    		}
    		else
    		{
    			int fill_len=filled.length();
    			while(i<fill_len&&filled.charAt(i)!=' ') 
    				i++;//get the PYZS
        		filled=filled.substring(0, i);
        		
            	
            	double mom=0;
        		double son=0;
            	double sim[]=new double[len];
            	int index[]=new int[len];
            	for(int x=0; x<len; x++)
            	{
            		
            		String pass= hz_list.get(x).PYZS;
            		mom=stringCompare(filled,pass);
            		son=i+pass.length();
            		sim[x]=2*mom/son;
            		index[x]=x;
            	}
            	for(int x=0;x<len;x++)
            	{
            		for(int j=0;j<len-1;j++){//内层循环控制每一趟排序多少次
            		if(sim[index[j]]>sim[index[j+1]]){
            		int temp=index[j];
            		index[j]=index[j+1];
            		index[j+1]=temp;
            		}
            	}
            	}
            	for(int x=len-1; x>=0; x--)
            		{
            		hzlb.getItems().add(hz_list.get(index[x]).PYZS+" "+hz_list.get(index[x]).HZBH+" "+hz_list.get(index[x]).HZMC);
            		}
            	
    		}
    		
        	
           
    	}
    	
        hz_changed=false;
    }

    @FXML
    private void hz_okay(ActionEvent e)
    {	//检查是否完整 更新项
    	//筛选更新列表
              
    
        cost.clear();
    	pay.clear();
    	change.clear();
    	regnum.clear();
    
        String ks=hzlb.getValue();
        if(ks==null) return;
        int len=ks.length();
        Byte count=0;
        Boolean valid=false;
        int j=0;
        for(int i=0;i<len;i++)
        {
        	if(ks.charAt(i)==' ')
        	{
        		if(count==0)
        		{
        			hz_class.PYZS=ks.substring(0, i);
        			j=i;
        			count++;
        			
        		}
        		else
        		{
        			hz_class.HZBH=ks.substring(j+1,i);
        			if(i+1==len) break;	
        			hz_class.HZMC=ks.substring(i+1);
        			valid=hz_list.contains(hz_class);
        			break;
        		}
        		
        	}
        }
        if(valid)
        {
        	try
    		{
    			
    			stmt=myApp.con.createStatement();
    			sql="SELECT * FROM T_HZXX WHERE HZBH='"+hz_class.HZBH+"'";
    			ResultSet rs=stmt.executeQuery(sql);
    			 if(rs.next()){ 
    	            	hz_class=new HZXX(rs);
    	            }; 
    	            
    	            
    	            //不是在这里 在点挂号的时候   添加检查人数的语句   生成编号
    	      
    	            rs.close();
    	            stmt.close();
             
    		}
    		catch(Exception err){
    			err.printStackTrace();
    		}
        	
        	if_suc=myApp.dlbr.YCJE.subtract(hz_class.GHFY);
     
        	if(if_suc.compareTo(BigDecimal.ZERO)>=0)
        	{
        		pay.disableProperty().set(true);
        		change.disableProperty().set(true);
        		cost.setText("0");
        		reg_ready=true;
        
        		
        	}
        	else
        	{
        		pay.disableProperty().set(false);
        		change.disableProperty().set(false);
        		cost.setText(if_suc.abs().toString());
        		reg_ready=false;
        	}

        }
        else
        {
        	
        	reg_ready=false;
        }
    	
    }
    
    @FXML
    private void hz_typed(KeyEvent e)
    {	//检查是否完整 更新项
    	//筛选更新列表
    	hz_changed=true;
    
    }

    @FXML
    private void pay_okay(ActionEvent e) {
    	BigDecimal money=new BigDecimal(pay.getText());
    	BigDecimal tmp=if_suc.add(money);
    	//BigDecimal tmp=myApp.dlbr.YCJE.add(money);
    	//ycje_update=ycje_update.add(money);
    	if(tmp.compareTo(BigDecimal.ZERO)>=0)
    	{
    		if_suc=tmp;
    		change.setText(if_suc.toString());
    		reg_ready=true;
    	}
    	else
    	{
    		change.setText("还需至少"+tmp.abs().toString()+"元");
    		reg_ready=false;
    	}
    }
    
    @FXML
    private void btn_reg_clicked(MouseEvent event)
    {
    	if(reg_ready)
    	{
    		try
    		{
    			Timestamp tie=new Timestamp(System.currentTimeMillis());
    		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		    String time=(sdf.format(tie)).concat(" 00:00:00");
    		    tie=Timestamp.valueOf(time);
    			int validnum=0;
    			stmt=myApp.con.createStatement();
    			sql="SELECT COUNT(*) AS COUNT  FROM T_GHXX WHERE HZBH='"+hz_class.HZBH+"'"+"AND RQSJ >= '"+tie+"'";
    			ResultSet rs=stmt.executeQuery(sql);
    			if(rs.next())
    			{//当天挂号人数
    				gh_class.GHRC=rs.getInt("count")+1;
    			}
    			rs.close();
    			sql="SELECT COUNT(*) AS COUNT  FROM T_GHXX WHERE HZBH='"+hz_class.HZBH+"'"+"AND RQSJ >= '"+tie+"' AND THBZ=0";
    			rs=stmt.executeQuery(sql);
    			if(rs.next())
    			{//当天有效挂号人数
    				validnum=rs.getInt("count")+1;
    			}
    			rs.close();
    			if(hz_class.GHRS<validnum)
    			{
    				JOptionPane.showMessageDialog(new JFrame().getContentPane(), "当天该号种挂号人次达到上限", "警告", JOptionPane.WARNING_MESSAGE);
    				return;
    			}
    			sql="SELECT COUNT(*) AS COUNT  FROM T_GHXX";
    			ResultSet rss=stmt.executeQuery(sql);
    			if(rss.next())
    			{
    				gh_class.GHBH=String.format("%06d", rss.getInt("count")+1);
    				//String.valueOf(rss.getInt("count")+1).;
    			}
    			
    	        stmt.close();
    		}
    		catch(Exception err){
    			err.printStackTrace();
    		}
    		
    		gh_class.BRBH=myApp.dlbr.BRBH;
    		gh_class.GHFY=hz_class.GHFY;
    		gh_class.HZBH=hz_class.HZBH;
    		gh_class.RQSJ=new Timestamp(System.currentTimeMillis());
    		gh_class.YSBH=ys_class.YSBH;
    		gh_class.THBZ=0;
    		
    		try
    		{
    			
    			stmt=myApp.con.createStatement();
    			sql="INSERT INTO T_GHXX VALUES('" + gh_class.GHBH + "','" + gh_class.HZBH+"','"+gh_class.YSBH+"','"+gh_class.BRBH+"',"+gh_class.GHRC+","+gh_class.THBZ+","+gh_class.GHFY+",'"+gh_class.RQSJ+"',NULL)";
    			stmt.executeUpdate(sql);
    	        stmt.close();
    		}
    		catch(Exception err){
    			err.printStackTrace();
    			JOptionPane.showMessageDialog(new JFrame().getContentPane(), "插入失败,请重新尝试", "警告", JOptionPane.WARNING_MESSAGE);
    			return;
    		}
    		
    		try
    		{
    			
    			stmt=myApp.con.createStatement();
    			sql="UPDATE T_BRXX SET YCJE="+if_suc+" WHERE BRBH='"+myApp.dlbr.BRBH+"'";
    			stmt.executeUpdate(sql);
    	        stmt.close();
    		}
    		catch(Exception err){
    			err.printStackTrace();
    		//do something	
    		}
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "挂号成功", "提示", JOptionPane.INFORMATION_MESSAGE);
    		myApp.dlbr.YCJE=if_suc;
    		kslb.getItems().clear();
    		yslb.getItems().clear();
            yslb.disableProperty().setValue(true);
            hzlb.disableProperty().setValue(true);
           // expert.getItems().clear();
            hzlb.getItems().clear();
            cost.clear();
        	pay.clear();
        	change.clear();
        	regnum.setText(gh_class.GHBH);
            ks_changed=true;
          
            ys_initialize=false;
            hz_initialize=false;
            ys_changed=true;
            
            hz_changed=true;
          
            reg_ready=false;
            ks_class=new KSXX();
            ys_class=new KSYS();
            hz_class=new HZXX();
            gh_class=new GHXX();
    		
    	}
    	else
    	{
    		JOptionPane.showMessageDialog(new JFrame().getContentPane(), "输入不合法！", "警告", JOptionPane.WARNING_MESSAGE);
    	}
    }
    public void setUp(Main application) {
        myApp = application;

    }     
    
    @FXML
    private void tab_changed(Event event)
    {
    	String regnum,regname,docnum,docname;
    	String regtime;
    	String regcount;
    	String regcost;
    	if(tab_unreg.selectedProperty().get())
    	{
		try
		{
 	        
 	        stmt = myApp.con.createStatement();
  	        String sql;
  	        ResultSet rs=null;
     		sql = "select count(*) as regcount from T_GHXX where BRBH='"+myApp.dlbr.BRBH+"' AND ISNULL(KBSJ) AND THBZ=0";
     		rs = stmt.executeQuery(sql);
     		if(rs.next())
     		{
     			int count = rs.getInt("regcount");
     			int col_count = ghxx.size();
     			
     			if(count!=col_count || flag_of_changedbyunreg)
     			{
     				ghxx.clear();
     				sql = "select reg1.GHBH,hz.HZMC,reg1.YSBH,doc.YSMC,reg1.GHRC,reg1.THBZ,reg1.RQSJ,reg1.GHFY,reg1.KBSJ "
     	 	    			+ "from T_GHXX reg1,T_KSYS doc,T_HZXX hz "
     	 	    			+ "where reg1.BRBH='"+myApp.dlbr.BRBH+"' "
     	 	    			+ "and reg1.HZBH=hz.HZBH and doc.YSBH=reg1.YSBH AND ISNULL(KBSJ) AND THBZ=0 ";
     	   	         rs = stmt.executeQuery(sql);
     	   	         while(rs.next())
     	   	         {
     	   	        	 regnum = rs.getString("reg1.GHBH");
     	   	             regname = rs.getString("hz.HZMC"); 
     	   	             docnum = rs.getString("reg1.YSBH");
     	   	             docname = rs.getString("doc.YSMC");
     	   	             regcount = rs.getString("reg1.GHRC");
     	   	             regcost = rs.getString("reg1.GHFY");
     	   	            // flag_unreg = rs.getByte("reg1.THBZ");
     	   	             regtime = rs.getString("reg1.RQSJ");
     	   	             //kbsj=rs.getString("KBSJ");
     	   	             //if(flag_unreg==0&& kbsj==null);//只显示没退号的和没看病的
     	   	             ghxx.add(new GHXX_show(regnum,regname,docnum,docname,regcount,regcost,regtime));
     	   	         }
     	   	         table_patreg.setItems(ghxx);
     	   	         flag_of_changedbyunreg = false;
     			}
     		}
     		
  	        stmt.close();
  	       rs.close();
  	     }catch(SQLException se){
  	         // 处理 JDBC 错误
  	         se.printStackTrace();
  	     }
    	}
    }
    @FXML
    private void btn_exit_clicked(MouseEvent event)
    {
    	Event.fireEvent(myApp.primaryStage,
    		new WindowEvent(myApp.primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST ));
    }
  @FXML
    private void btn_unreg_clicked(ActionEvent event)
    {
    	
    	unreg_list = table_patreg.getSelectionModel().getSelectedItems();
    	GHXX_show patreg;
    	
    	for(int i=0;i<unreg_list.size();i++)
    	{
    		patreg = unreg_list.get(i);
    		
    		flag_of_changedbyunreg = true;
    		try
    		{
      	      
    			 String sql;
    	  	     stmt = myApp.con.createStatement();	         
      	         sql = "update t_ghxx set THBZ=1 where GHBH = '"+patreg.getGHBH()+"'";
      	         stmt.executeUpdate(sql);
      	        // ycje_update=ycje_update.add(new BigDecimal(patreg.getGHFY()));
      	         myApp.dlbr.YCJE =myApp.dlbr.YCJE.add(new BigDecimal(patreg.getGHFY()));
      	         sql ="update t_brxx set YCJE="+ myApp.dlbr.YCJE.toString()+"where BRBH='"+myApp.dlbr.BRBH+"'";
      	       
      	         stmt.executeUpdate(sql);
      	         stmt.close();
      	     }catch(SQLException se){
      	         // 处理 JDBC 错误
      	         se.printStackTrace();
      	         return;
      	     }
    	}
    	JOptionPane.showMessageDialog(new JFrame().getContentPane(), "退号成功", "提示", JOptionPane.INFORMATION_MESSAGE);
    	tab_changed(new Event(null));//refresh the gui
    }

public int stringCompare(String str1,String str2)
{
	
	int len1, len2;
    len1 = str1.length();
    len2 = str2.length();
    int maxLen = len1 > len2 ? len1 : len2;
    int max=0;// 保存最长子串长度的数组
    
    int[] c = new int[maxLen];
    int i, j;
    for (i = 0; i < len2; i++) {
      for (j = len1 - 1; j >= 0; j--)
      {
        if (str2.charAt(i) == str1.charAt(j)) 
        {
          if ((i == 0) || (j == 0))
            c[j] = 1;
          else
            c[j] = c[j - 1] + 1;//此时C[j-1]还是上次循环中的值，因为还没被重新赋值
        } 
        else
        {
          c[j] = 0;
        }
       
        if (c[j] > max) {
          max = c[j]; 
        }

      }
    }
   return max;
  
}
}