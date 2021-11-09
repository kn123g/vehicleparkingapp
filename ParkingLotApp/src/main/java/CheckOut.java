

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import parking.MainService;
import parking.ServiceResponse;

@WebServlet("/checkout")
public class CheckOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private MainService obj = MainService.instance;
	private Gson gson = new Gson();
	private SimpleDateFormat  sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
       
    public CheckOut() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuffer jb = new StringBuffer();
		String line = null;
        response.addHeader("Access-Control-Allow-Origin", "*");
		try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		    jb.append(line);
		    String json = new String(jb);
		    VehicleJSONTemplate v = this.gson.fromJson(json,VehicleJSONTemplate.class);
		    Date exit = sdf.parse(v.exit_time);
		    try {
				ServiceResponse sr = obj.checkOut(v.vehicle_number,exit);
		    	response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				if(sr.action) {
					response.setStatus(HttpServletResponse.SC_OK);
				}else {
					response.setStatus(HttpServletResponse.SC_CONFLICT);
				}

				response.getWriter().append(sr.message);	
		    }
		    catch(Exception e) {
		    	
		    }
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().append("Bad request type");	
			
		}  
	}
	@Override
	  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
	      setAccessControlHeaders(resp);
	      resp.setStatus(HttpServletResponse.SC_OK);
	  }

	  private void setAccessControlHeaders(HttpServletResponse resp) {
		  resp.setHeader("Access-Control-Allow-Origin", "*");
	      resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
	      resp.setHeader("Access-Control-Allow-Headers","Origin,Content-Type");
	  }

}
