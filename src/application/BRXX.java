package application;
import java.sql.*;
import java.math.*;
public class BRXX {
	public String BRBH=null;
	public String BRMC=null;
	public String DLKL=null;	
    public Timestamp DLRQ=null;
    public BigDecimal YCJE;  //不能随便使用，如果病人同时登录两台电脑挂号
    
    public BRXX(String BRBH, String BRMC, String DLKL, BigDecimal YCJE,Timestamp DLRQ) {
    	this.BRBH=new String(BRBH);
    	this.BRMC=new String(BRMC);
    	this.DLRQ=DLRQ;    	
    	this.DLKL=new String(DLKL);
    	this.YCJE=YCJE;
    }
    public BRXX(ResultSet rsbr) {
    	try {
    	    this.BRBH=new String(rsbr.getString("BRBH"));
    	    this.BRMC=new String(rsbr.getString("BRMC"));
    	    this.DLKL=new String(rsbr.getString("DLKL"));
    	    this.YCJE=rsbr.getBigDecimal("YCJE");
    	    this.DLRQ=rsbr.getTimestamp("DLRQ");    	
    	    
    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    		
    	}
    }
}
