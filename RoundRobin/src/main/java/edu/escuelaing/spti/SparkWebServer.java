package edu.escuelaing.spti;

import spark.Request;
import spark.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import static spark.Spark.*;

/**
 * Class that works as RoundRobin.
 * @author Maria Fernanda Hernandez Vargas
 * @author Andres Orlando Sotelo Fajardo
 * @author Johan David Rueda Rofríguez
 */
public class SparkWebServer {
    private static int load = 0;

    /**
     * Method that connects with logService, works as controller
     * @param args
     */
    public static void main(String[] args) {
        port(getPort());
        get("/hello", (req, res) -> "Hello Docker");
        get ("/index", (req, res) -> inputDataPage(req, res));
        get ("/results", (req, res) -> {
            int loadCont;
            String function = req.queryParams("user");
            loadCont = getLoad();
            readURL("http://ec2-35-175-129-150.compute-1.amazonaws.com:5100"+loadCont+"/results?user=" + function);
            loadCont++;
            if (loadCont > 2) {
                loadCont = 0;
                setLoad(loadCont);
            } else {
                setLoad(loadCont);
            }
            System.out.println(loadCont);
            return inputDataPage(req, res);
        });
        get ("/consult", (req, res) -> {
            int loadCont;
            res.type("application/json");
            loadCont = getLoad();
            String url = readURL("http://ec2-35-175-129-150.compute-1.amazonaws.com:5100"+loadCont+"/consult");
            loadCont++;
            if (loadCont > 2) {
                loadCont = 0;
                setLoad(loadCont);
            } else {
                setLoad(loadCont);
            }
            System.out.println(loadCont);
            return url;
        });
    }

    /**
     * Method that does html function, front of the webSite
     * @param req
     * @param res
     * @return
     */
    public static String inputDataPage (Request req, Response res) {
        String pageContent
                = "<!DOCTYPE html>"
                + "<html>"
                + "<body>"
                + "<h2> Servicio Web </h2>"
                + "<h4>A continuacion ingrese su nombre</h4>"
                + "<form action=\"/results\">"
                + "  Nombre:"
                + "  <input type=\"text\" name=\"user\" size= 10  placeholder=\"Nombre\n\">"
                + "  <br><br>"
                + "  <input type=\"submit\" value=\"Enviar\">"
                + "  <br><br>"
                + "</form>"
                + "<form action=\"/consult\">"
                + "  <input type=\"submit\" value=\"Consultar\">"
                + "</form>"
                + "</body>"
                + "</html>";
        return pageContent;
    }

    /**
     * Method that reads input as URL
     * @param sitetoread
     * @return
     */
    public static String readURL(String sitetoread) {
        String resData = null;
        try {
            URL siteURL = new URL(sitetoread);
            URLConnection urlConnection = siteURL.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine = null;
            resData = "";
            while ((inputLine = reader.readLine()) != null) {
                resData += inputLine;
            }
        } catch (IOException x) {
            resData = "";
            System.err.println(x);
        }
        return resData;
    }

    /**
     * Method that returns int, variable that helps to load balancer
     * @return
     */
    public static int getLoad (){
        return load;
    }

    /**
     * Method that refresh variable of load balancer
     * @param loadCont
     */
    public static void setLoad(int loadCont){
        load = loadCont;
    }

    /**
     * Method that return port of the service
     * @return
     */
    private static int getPort (){
        if(System.getenv("PORT") != null){
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }
}