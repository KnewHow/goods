package goods_test;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.servlet.BaseServlet;

public class TestServlet extends BaseServlet {
	public String test_1(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("msg", "恭喜，您已经注册成功，请马上去邮箱激活");
		request.setAttribute("code", "success");
		return "f:/jsps/msg.jsp";
	}
}
