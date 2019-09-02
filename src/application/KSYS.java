package application;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class KSYS {
	public String YSBH=null;
	public String KSBH=null;
	public String YSMC=null;
	public String PYZS=null;
    public String DLKL=null;
    public byte SFZJ=0;
    public Timestamp DLRQ=null;

    public KSYS(String YSBH, String KSBH,String YSMC,String PYZS, String DLKL, byte SFZJ,Timestamp DLRQ) {
    	this.YSBH=new String(YSBH);
    	this.KSBH=new String(KSBH);
    	this.YSMC=new String(YSMC);
    	this.PYZS=new String(PYZS);
    	this.DLKL=new String(DLKL);
    	this.SFZJ=SFZJ;
    	this.DLRQ=DLRQ;
    }
    public KSYS() {
    	
    }
    public KSYS(ResultSet rsys) {
    	try {
    	    this.YSBH=new String(rsys.getString("YSBH"));
    	    this.KSBH=new String(rsys.getString("KSBH"));
    	    this.YSMC=new String(rsys.getString("YSMC"));
    	    this.PYZS=new String(rsys.getString("PYZS"));    	
    	    this.DLKL=new String(rsys.getString("DLKL"));
    	    this.SFZJ=rsys.getByte("SFZJ");
    	    this.DLRQ=rsys.getTimestamp("DLRQ");
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    }    
    public boolean equals(Object o)
    {
    	if(o==null)
    	{
    		return false;
    	}
    	else 
    	{
    		if(o instanceof KSYS)
    		{
    			KSYS ks=(KSYS)o;
    			if(ks.YSBH.equals(this.YSBH)&&ks.YSMC.equals(this.YSMC))
    				return true;
    		}
    	}
    	return false;
    }
}
