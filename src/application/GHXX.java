package application;
import java.sql.*;
import java.math.*;

public class GHXX {
	public String GHBH=null;
	public String HZBH=null;
	public String YSBH=null;
    public String BRBH=null;
    public int GHRC=0;
    public byte THBZ=0;
    public BigDecimal GHFY;
    public Timestamp  RQSJ=null;
    public Timestamp  KBSJ=null;
    
    

    public GHXX(String GHBH, String HZBH,String YSBH, String BRBH, int GHRC,byte THBZ,BigDecimal GHFY,Timestamp RQSJ,Timestamp KBSJ) {
    	this.GHBH=new String(GHBH);
    	this.HZBH=new String(HZBH);
    	this.YSBH=new String(YSBH);
    	this.BRBH=new String(BRBH);
    	this.GHRC=GHRC;
    	this.THBZ=THBZ;
    	this.GHFY=GHFY;
    	this.RQSJ=RQSJ;
    	this.KBSJ=KBSJ;
    	
    }
    public GHXX() {
    	
    }
    public GHXX(ResultSet rsys) {
    	try {
    	    this.GHBH=new String(rsys.getString("GHBH"));
    	    this.HZBH=new String(rsys.getString("HZBH"));
    	    this.YSBH=new String(rsys.getString("YSBH"));    	
    	    this.BRBH=new String(rsys.getString("BRBH"));
    	    this.GHRC=rsys.getInt(GHRC);
    	    this.THBZ=rsys.getByte("THBZ");
    	    this.GHFY=rsys.getBigDecimal("GHFY");
    	    this.RQSJ=rsys.getTimestamp("RQSJ");
    	    this.KBSJ=rsys.getTimestamp("KBSJ");
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    }    
}
