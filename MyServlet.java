package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//Reading The User Input From HTML File
		String city = request.getParameter("cityName");
		//Setting API Key in a Variable
		String apiKey = "40c8952eb3540393621b45bfe2a1accf";
		//Creating Api URL For Api Calling
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey;
		
		try {
		//API Integration
		@Deprecated(since="20")
		URL url = new URL(apiUrl);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		//Reading The Data From Network
		InputStream inputstream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputstream);
		
		// Want To Store The Data In String
		Scanner scan = new Scanner(reader);
		StringBuilder collectData = new StringBuilder();
		
		while(scan.hasNext()) {
			collectData.append(scan.nextLine());
		}
		
		
		scan.close();
		
		//Data Type Casting = Parsing The Data Into JSON
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(collectData.toString(), JsonObject.class);
		
		
		//Data Separation
		//Date & Time
		long dateTime = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTime).toString();
		
		
		//Temperature
		double temperatureKelvin =  jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCalcius = (int)(temperatureKelvin - 273.15);
		
		//Humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//Wind Speed
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		
		//Weather Condition
		
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		//Set The Data For JSP Page 
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatureCalcius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherData",collectData.toString());
		
		connection.disconnect();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		//Forward The Request To The weather.jsp page for rendering
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}
