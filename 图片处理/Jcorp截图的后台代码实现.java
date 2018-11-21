import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Crop")
public class Crop extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
             String basePath = request.getServletContext().getRealPath("/");
             int cropX=0,cropY=0,cropW=0,cropH=0;
             cropX= Integer.parseInt(request.getParameter("cropX"));
             cropY= Integer.parseInt(request.getParameter("cropY"));
             cropW= Integer.parseInt(request.getParameter("cropW"));
             cropH= Integer.parseInt(request.getParameter("cropH"));
             
             System.out.println(basePath+"image/flower.jpg");
                     
             File file =new File(basePath+"image/flower.jpg");
             BufferedImage outImage=ImageIO.read(file);
             int type = outImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : outImage.getType();
             BufferedImage cropImage=outImage.getSubimage(cropX, cropY,cropW, cropH );
            
             String outputPath="image/"+(new Date()).getTime()+"flower.jpg";
            
             File cropfile = new File(basePath+outputPath);
            
             ImageIO.write(cropImage, "jpg",cropfile);
            
             PrintWriter out =response.getWriter();
            
             out.write("<img src='"+outputPath+"'  height=200 border=1 />");
            
             out.close();
    }

}