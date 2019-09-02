package application;
import java.sql.ResultSet;
import java.sql.SQLException;




public class KSXX {

	public String KSBH=null;
	public String KSMC=null;
	public String PYZS=null;


    public KSXX(String KSBH, String KSMC,String PYZS) {
    	this.KSBH=new String(KSBH);
    	this.KSMC=new String(KSMC);
    	this.PYZS=new String(PYZS);
    	
    }
    public KSXX() {
    
    }
    public KSXX(ResultSet rsks) {
    	try {
    	    this.KSBH=new String(rsks.getString("KSBH"));
    	    this.KSMC=new String(rsks.getString("KSMC"));
    	    this.PYZS=new String(rsks.getString("PYZS"));    	
    	   
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
    		if(o instanceof KSXX)
    		{
    			KSXX ks=(KSXX)o;
    			if(ks.KSBH.equals(this.KSBH)&&ks.KSMC.equals(this.KSMC)&&ks.PYZS.equals(this.PYZS))
    				return true;
    		}
    	}
    	return false;
    }
}
