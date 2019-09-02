package application;
import javafx.beans.property.SimpleStringProperty;

public class GHXX_show {
	private final SimpleStringProperty GHBH;
	private final SimpleStringProperty HZMC;
	private final SimpleStringProperty YSBH;
	private final SimpleStringProperty YSMC;
	private final SimpleStringProperty GHRC;
	private final SimpleStringProperty GHSJ;  
	private final SimpleStringProperty GHFY;
    
    public GHXX_show(String GHBH, String HZMC, String YSBH, String YSMC,String GHRC,String GHFY,String GHSJ) {
    	this.GHBH=new SimpleStringProperty(GHBH);
    	this.HZMC=new SimpleStringProperty(HZMC);
    	this.YSBH=new SimpleStringProperty(YSBH);
    	this.YSMC=new SimpleStringProperty(YSMC);   	
    	this.GHRC=new SimpleStringProperty(GHRC);
    	this.GHFY=new SimpleStringProperty(GHFY); 
    	this.GHSJ=new SimpleStringProperty(GHSJ);
    }
   public String getGHBH()
   {
	   String a=GHBH.get();
	   return a;
   }
   
   public void setGHBH(String s)
   {
	   GHBH.set(s);
   }
   
   public String getHZMC()
   {
	   return HZMC.get();
   }
   public void setHZMC(String s)
   {
	   HZMC.set(s);
   }
   public String getYSBH()
   {
	   return YSBH.get();
   }
   public void setYSBH(String s)
   {
	   YSBH.set(s);
   }
   public String getYSMC()
   {
	   return YSMC.get();
   }
   public void setYSMC(String s)
   {
	   YSMC.set(s);
   }
   public String getGHRC()
   {
	   return GHRC.get();
   }
   public void setGHRC(String s)
   {
	   GHRC.set(s);
   }
   public String getGHSJ()
   {
	   return GHSJ.get();
   }
   public void setGHSJ(String s)
   {
	   GHSJ.set(s);
   }
   public String getGHFY()
   {
	   return GHFY.get();
   }
   public void setGHFY(String s)
   {
	   GHFY.set(s);
   }
}
