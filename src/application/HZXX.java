package application;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.*;

public class HZXX {
	public String HZBH=null;
	public String HZMC=null;
	public String PYZS=null;
    public String KSBH=null;
    public byte  SFZJ=0;
    public int  GHRS=0;
    public BigDecimal GHFY;
    
    

    public HZXX(String HZBH, String HZMC,String PYZS, String KSBH, byte SFZJ,int GHRS,BigDecimal GHFY) {
    	this.HZBH=new String(HZBH);
    	this.HZMC=new String(HZMC);
    	this.PYZS=new String(PYZS);
    	this.KSBH=new String(KSBH);
    	this.SFZJ=SFZJ;
    	this.GHRS=GHRS;
    	this.GHFY=GHFY;
    }
    public HZXX() {
    	
    }
    public HZXX(ResultSet rsys) {
    	try {
    	    this.HZBH=new String(rsys.getString("HZBH"));
    	    this.HZMC=new String(rsys.getString("HZMC"));
    	    this.PYZS=new String(rsys.getString("PYZS"));    	
    	    this.KSBH=new String(rsys.getString("KSBH"));
    	    this.SFZJ=rsys.getByte("SFZJ");
    	    this.GHRS=rsys.getInt("GHRS");
    	    this.GHFY=rsys.getBigDecimal("GHFY");
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
    		if(o instanceof HZXX)
    		{
    			HZXX ks=(HZXX)o;
    			if(ks.HZBH.equals(this.HZBH)&&ks.HZMC.equals(this.HZMC))
    				return true;
    		}
    	}
    	return false;
    }
}
