package wenqiang_web.wenqiang_mavenWeb;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.net.ftp.FTPClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.sftp.SftpFile;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
public class LinuxTest {
    
    
    public static Connection getOpenedConnection(String host, String username,  
              
            String password) throws IOException {  
               
                Connection conn = new Connection(host);  
          
                conn.connect(); // make sure the connection is opened  
          
                boolean isAuthenticated = conn.authenticateWithPassword(username,  
          
                password);  
          
                if (isAuthenticated == false)  
          
                    throw new IOException("Authentication failed.");  
          
                return conn;  
          
            } 
    
    public static void main(String[] args) {  
        /*Connection con = new Connection("192.168.0.198",22);
        //连接
        con.connect();
        //远程服务器的用户名密码
        boolean isAuthed = con.authenticateWithPassword("root","123456");
        //建立SCP客户端
        SCPClient scpClient = con.createSCPClient();
        //服务器端的文件下载到本地的目录下
        scpClient.getFile("/home/oracle/RUNNING.txt", "C:/");*/
        
        
        SshClient client=new SshClient();
        try{
            client.connect("192.168.0.198");
            //设置用户名和密码
            PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
            pwd.setUsername("root");
            pwd.setPassword("123456");
            int result=client.authenticate(pwd);
            if(result==AuthenticationProtocolState.COMPLETE){//如果连接完成
                System.out.println("==============="+result);
                List<SftpFile> list = client.openSftpClient().ls("/home/");
                for (SftpFile f : list) {
                    System.out.println(f.getFilename());  
                    System.out.println(f.getAbsolutePath());
                    if(f.getFilename().equals("aliases")){
                        OutputStream os = new FileOutputStream("d:/mail/"+f.getFilename());
                        client.openSftpClient().get("/home", os);
                        //以行为单位读取文件start
                        File file = new File("d:/mail/aliases");
                        BufferedReader reader = null;
                        try {
                            System.out.println("以行为单位读取文件内容，一次读一整行：");
                            reader = new BufferedReader(new FileReader(file));
                            String tempString = null;
                            int line = 1;//行号
                            //一次读入一行，直到读入null为文件结束
                            while ((tempString = reader.readLine()) != null) {
                                //显示行号
                                System.out.println("line " + line + ": " + tempString);
                                line++;
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e1) {
                                }
                            }
                        }
                        //以行为单位读取文件end
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }


       
 
    
    /**  
     * @param args  
     * @throws IOException   
     */    
    public static void main(String[] args) throws IOException {    
        UploadFileToMachine("【需求规格说明书】中国外运物流资源GIS平台.docx");
      
    }    
      
    
    
    /** 
     * 向共享目录上传文件   
     * @param remoteUrl 
     * @param localFilePath 
     */  
    public static void copyFileToMachine(String fileName){
        SshClient client=new SshClient();
        try{
         String connectIP = "192.168.0.198";
         String connectName = "root";
         String connectPassword = "123456";
         String connectPath ="/home/";
            client.connect(connectIP);
            //设置用户名和密码
            PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
            pwd.setUsername(connectName);
            pwd.setPassword(connectPassword);
            int result=client.authenticate(pwd);
            if(result==AuthenticationProtocolState.COMPLETE){//如果连接完成
                System.out.println("==============="+result);
                List<SftpFile> list = client.openSftpClient().ls(connectPath);
                for (SftpFile f : list) {
             String fileBathPath ="d:/mail/";
 
             System.out.println(f.getFilename()+"/////"+fileName);
             if(f.getFilename().equals(fileName)){
                 if (!(new File("d:/mail/").isDirectory())) {
                     new File("d:/mail/").mkdir();
                    }
             
                        OutputStream os = new FileOutputStream(fileBathPath + fileName);
                       // InputStream oo=new FileInputStream(fileBathPath + fileName);
                      //  client.openSftpClient().put(oo,connectPath+f.getFilename());
                     client.openSftpClient().get(connectPath+f.getFilename(), os);
                        
             }
                        
                }
            }
        }catch(Exception e){
       
            e.printStackTrace();
        }
    }  