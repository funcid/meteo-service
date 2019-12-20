import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * @author func 09.11.2019
 */
public class Servlet extends HttpServlet {

    private File file;

    public Servlet() {
        Date date = new Date();
        file = new File("weather/" + (date.getYear() + 1900) + "_" + ((date.getMonth() % 12 + 1)) + "_" + (date.getDay() + 8) + ".txt");
        if (!file.exists())
            file.getParentFile().mkdirs();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println("<html>");
        if (request.getParameter("loc") != null) {
            FileWriter writer = new FileWriter(file, true);
            writer.append(request.getParameter("loc"))
                    .append("[time=")
                    .append(String.valueOf(new Date().getTime()))
                    .append("&temperature=")
                    .append(request.getParameter("temp"))
                    .append("&pressure=")
                    .append(request.getParameter("pressure"))
                    .append("&humidity=")
                    .append(request.getParameter("humidity"))
                    .append("]<br>");
            writer.flush();
            writer.close();
        }
        printWriter.println(
                "{ <a>Этот сайт серверная часть проекта, сайт администратора <a href=\"http://funcid.ru\">funcid.ru</a>." +
                        "<br>Сюда попадают даннае с датчиков погоды. Данные доступны всем." +
                        "<br>Последний файл: " + file.getName() + ". <a href=\"https://github.com/S1mpleFunc/WeatherStation\">Исходник проекта</a>. }</a><br>"
        );
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.lines().forEach(printWriter::println);
        }
        printWriter.println("</html>");
        printWriter.close();
    }
}