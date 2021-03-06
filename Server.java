import java.io.*;
import java.net.*;
/**
 * 实现服务器端
 * 用于接收上传的数据和供客户端下载数据
 * @author DELL
 *
 */
public class Server {
    private int port;
    private String host;
    private String dirPath;
    private static ServerSocket server;
    
    public Server(int port,String dirPath){
        this.port = port;
        this.dirPath = dirPath;
        this.server = null;
    }
    
    public void run(){
        if(server==null){
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("服务已启动...");
        while(true){
            try {
                //通过ServerSocket的accept方法建立连接,并获取客户端的Socket对象
                Socket client = server.accept();
                if(client==null) continue;
                new SocketConnection(client,this.dirPath).run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 实现服务器端的数据传输
     * @author DELL
     *
     */
    public class SocketConnection extends Thread{
        private Socket client;
        private String filePath;
        
        public SocketConnection(Socket client, String filePath){
            this.client = client;
            this.filePath = filePath;
        }
        
        public void run(){
            if(client==null) return;
            DataInputStream in= null; //读取Socket的输入流
            DataOutputStream dos = null; //写文件的输出流
            DataOutputStream out = null; //写Socket的输出流
            DataInputStream dis = null; //读文件的输入流
            try {
                //访问Scoket对象的getInputStream方法取得客户端发送过来的数据流
                in = new DataInputStream(new BufferedInputStream(client.getInputStream()));
                String recvInfo = in.readUTF(); //取得附带的指令及文件名
//                System.out.println(recvInfo);
                String[] info = recvInfo.split(" ");
                String fileName = info[1]; //获取文件名
//                System.out.println(fileName);
                if(filePath.endsWith("/")==false&&filePath.endsWith("\\")==false){
                    filePath+="\\";
                }
                filePath += fileName;
                if(info[0].equals("put")){
                    //从客户端上传到服务器
                    //开始接收文件
                    dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(filePath))));
                    int bufferSize = 10240;
                    byte[] buf = new byte[bufferSize];
                    int num =0;
                    while((num=in.read(buf))!=-1){
                        dos.write(buf, 0, num);
                    }
                    dos.flush();
                    System.out.println("数据接收完毕！");
                }else if(info[0].equals("get")){
                    //从服务器下载文件到客户端
                    dis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
                    //开始发送文件
                    int bufferSize = 10240;
                    byte[] buf = new byte[bufferSize];
                    out = new DataOutputStream(client.getOutputStream());
                    int num =0;
                    while((num=dis.read(buf))!=-1){
                        out.write(buf, 0, num);
                    }
                    out.flush();
                    System.out.println("发送成功！");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                try {
                    if(out!=null) out.close();
                    if(in!=null)  in.close();
                    if(dos!=null) dos.close();
                    if(dis!=null) dis.close();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        //设置服务器端口
        int port = 8888;
        //设置服务器文件存放位置
        String dirPath = "D:\\FTPService\\";
        new Server(port,dirPath).run();
    }
}