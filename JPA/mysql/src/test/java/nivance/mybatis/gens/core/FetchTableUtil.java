package nivance.mybatis.gens.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;

public class FetchTableUtil{
	public static void main(String[] args){
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.24.31:3306/iso";
		String user = "iso";
		String pwd = "mysql";
		String sql = "select table_name  from information_schema.tables where table_schema='iso'";
		try(Connection connect = DriverManager.getConnection(url,user,pwd);
				Statement statement = connect.createStatement();
				ResultSet rs = statement.executeQuery(sql);){
			Class.forName(driver);
			if(!connect.isClosed()){
				//<table tableName="T_PARA_3DLIMITED_FEATURE_HIS" domainObjectName="ParaDbproperties"/>
				while(rs.next()){
					String tablename = rs.getString("table_name");
					String[] tmps = StringUtils.substring(tablename, 2).toLowerCase().split("_");
					StringBuffer sb = new StringBuffer();
					for(String str : tmps){
						sb.append(StringUtils.capitalize(str));
					}
					String domain = sb.toString();
//					System.out.println("<table tableName=\""+tablename+"\" domainObjectName=\""+domain+"\"/>");
//					System.out.println("<entry key=\""+domain.toLowerCase()+"\" value-ref=\""+StringUtils.uncapitalize(domain)+"Dao\" />");
//					System.out.println("TABLE_DB.put(\""+domain.toLowerCase()+"\",Domain.mysql.toString());");
//					System.out.println(domain.toLowerCase()+",");
//					accaccount("mysql",AccAccount.class),
//					System.out.println(domain.toLowerCase()+"(\"mysql\","+domain+".class),");
					System.out.println(domain.toLowerCase()+"("+domain+".class,\""+tablename+"\"),");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}