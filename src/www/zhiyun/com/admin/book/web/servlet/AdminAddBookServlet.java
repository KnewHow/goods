package www.zhiyun.com.admin.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import www.zhiyun.com.book.domain.Book;
import www.zhiyun.com.book.service.BookService;
import www.zhiyun.com.category.domain.Category;
import www.zhiyun.com.category.service.CategoryService;

import cn.itcast.commons.CommonUtils;
/**
 * 添加图书的Servlet
 * @author Administrator
 *
 */
public class AdminAddBookServlet extends HttpServlet {

	/**
	 * 添加图书模块
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 上传图片的三部曲
		 * 1、创建解析器工厂
		 * 2、创建解析器
		 * 3、进行解析
		 */
		
		
		//创建解析器工厂
		FileItemFactory factory = new DiskFileItemFactory();
		//创建解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		//设置每个文件的大小1024K *1024K*20 即 20B
		sfu.setFileSizeMax(1024*1024*20);
		
		//对request进行解析
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			error("您上传的图片大小超过20MB", request, response);
			return;
		}
		
		/*
		 *4.1把FileItemList集合对象封装 封装成Book里面
		 */
		Map<String,Object> map = new HashMap<String, Object>();
		for(FileItem fileItem :fileItemList){
			if(fileItem.isFormField()){//判断如果是普通表单项，就把数据封装到Map集合里面,并且还要设置编码
				map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
			}
		}
			//把非文件表单项都封装进Book对象
			Book book = CommonUtils.toBean(map, Book.class);
			Category category = CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			
			/*
			 * 4.2把上传的图片保存起来
			 * 	1、获取图片路径，截取之
			 *	2、给文件添加uuid前缀，避免文件重名
			 *	3、校验文件扩展名，只能是JPG格式
			 *	4、校验图片尺寸，
			 *	5、获取图片保存的真实路径，使用ServletContext#getRealPath();
			 *	6、保存之
			 *	7、把图片的路径的值设置给Book对象
			 */
			
		//1、获取图片的路径
			//获取文件表单对象
			FileItem fileItem = fileItemList.get(1);
			//获取图片名称
			String filename = fileItem.getName();
			//截取文件名，我们要相对路径，因为某些浏览器会带绝对路径
			int index = filename.indexOf("\\");// "//"是对/进行转义
			if(index!=-1){
				filename = filename.substring(index+1);
			}
			//给文件名添加绝对路径，避免重名
			filename =CommonUtils.uuid()+"_"+filename;
			
			
			//校验图片的扩展名
//System.out.println(filename);
			if(!filename.toLowerCase().endsWith(".jpg")){
				error("图片不是JPG格式，不允许上传", request, response);
				return;//使用return表示函数到此结束
			}
			
		//校验图片尺寸之前，必须要先把文件保存起来才能进行校验
				
			//保存图片
			String savepath = this.getServletContext().getRealPath("/book_img");
			
			//创建目标文件
			File desFile = new File(savepath,filename);
			
			try {
				//将文件对象写入磁盘内
				fileItem.write(desFile);
			} catch (Exception e) {
				throw new RuntimeException();
			}
				
		//校验图片尺寸
			//创建ImageIcon对象
			ImageIcon icon = new ImageIcon(desFile.getAbsolutePath());
			//使用ImageIcon创建Image对象
			Image image = icon.getImage();
			
			//获取了Image对象，就可以获取图片的高和宽
			if(image.getWidth(null)>350 || image.getHeight(null)>350){
				error("图片的尺寸超过350*350，不符合规定，不能上传", request, response);
				//先前已经保存了，现在既然不符合格式，就要删除了
				desFile.delete();
				return;
			}
			
			//把路径设置给Book对象
			book.setImage_w("book_img/"+filename);
			
			
			
			
		//第二张图
			//获取文件表单对象
			fileItem = fileItemList.get(2);
			//获取图片名称
			filename = fileItem.getName();
			//截取文件名，我们要相对路径，因为某些浏览器会带绝对路径
			index = filename.indexOf("\\");// "//"是对/进行转义
			if(index!=-1){
				filename = filename.substring(index+1);
			}
			//给文件名添加绝对路径，避免重名
			filename = CommonUtils.uuid()+"_"+filename;
			
			
			//校验图片的扩展名
			if(!filename.toLowerCase().endsWith(".jpg")){
				error("图片不是JPG格式，不允许上传", request, response);
				return;//使用return表示函数到此结束
			}
			
		//校验图片尺寸之前，必须要先把文件保存起来才能进行校验
				
			//保存图片
			savepath = this.getServletContext().getRealPath("/book_img");
			
			//创建目标文件
			desFile = new File(savepath,filename);
			
			try {
				//将文件对象写入磁盘内
				fileItem.write(desFile);
			} catch (Exception e) {
				throw new RuntimeException();
			}
				
		//校验图片尺寸
			//创建ImageIcon对象
			icon = new ImageIcon(desFile.getAbsolutePath());
			//使用ImageIcon创建Image对象
			image = icon.getImage();
			
			//获取了Image对象，就可以获取图片的高和宽
			if(image.getWidth(null)>350 || image.getHeight(null)>350){
				error("图片的尺寸超过350*350，不符合规定，不能上传", request, response);
				//先前已经保存了，现在既然不符合格式，就要删除了
				desFile.delete();
				return;
			}
			
			//把路径设置给Book对象
			book.setImage_b("book_img/"+filename);
			
			//设置bid
			book.setBid(CommonUtils.uuid());
			
			//把数据给BookService添加数据
			BookService bookService = new BookService();
			bookService.add(book);
			
			request.setAttribute("msg", "添加图书成功");
			request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
		
	}
	/**
	 * 错误信息提示
	 * @param msg
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void error(String msg,HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("msg", msg);
		request.setAttribute("parents", new CategoryService().findParents());
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
	}

}
