            package prac;
            import java.io.*;
            import java.sql.*;
            import java.util.Vector;
            
            public class ExecSQL{
               static String url;
                static     String username;
                  static   String password;
                    Connection connection=null;
                   Statement stat=null;
                   DSP dsp=null;
               public  void parse(String sqlScript)
               {
                     try
                     {
                        try {
            
                            connection=DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\ADMIN\\Documents\\TEST.accdb");
                       stat=connection.createStatement();
                        } catch (SQLException ex) {
                           ex.printStackTrace();
                        }
            String []lines=sqlScript.split(";");
                        for (String line:lines){
                           if (line.trim().endsWith(";")){
                              line=line.trim();
                              line=line.substring(0,line.length()-1);
                            }
                           if (line.equals("")){
                               continue;
                            }
                           try {
                              boolean hasResultSet=stat.execute(line);
                              if (hasResultSet){
                                 showResultSet(stat);
                              }
                           } catch (Exception e) {
                              e.printStackTrace();
                           }
                        }
            
                     }
                     finally
                     {
                     }
                  }
            
               public Connection getConnection(String url) throws SQLException, IOException
               {
                  return DriverManager.getConnection(url);
               }
            
               public  void showResultSet(Statement stat) throws SQLException {
                   dsp = new DSP();
                   ResultSet result = stat.getResultSet();
                   ResultSetMetaData metaData = result.getMetaData();
                   int columnCount = metaData.getColumnCount();
                   Vector vector = new Vector();
                   int j = 0;
                   for (int i = 1; i <= columnCount; i++) {
                       j++;
                   }
                   StringBuilder builder=new StringBuilder();
            
                   for (int i = 1; i <= columnCount; i++) {
                       dsp.defaultTableModel.addColumn(metaData.getColumnLabel(i));
                       builder.append(metaData.getColumnLabel(i)+" ");
                   }
                    String colstring=builder.toString().trim();
                   String[] cols=colstring.split(" ");
                   while (result.next()) {
                       for (String k : cols)
                           dsp.defaultTableModel.addRow(new String[]{result.getString(k)});
                       }
                   }
               }
            
