package net.coderodde.ppml.rateapp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * /statload
 */
public class LoadStaticDataServlet extends HttpServlet {

    private static final String PARTIAL_PATH_TO_STATIC_DATA = 
            "/WEB-INF/static/ml-100k";
    
    /**
     * The name of the file containing ratings.
     */
    private static final String RATINGS_FILE = "u.data";
    
    /**
     * The name of the file containing movie descriptions.
     */
    private static final String MOVIE_FILE = "u.item";
    
    /**
     * The name of the file containing user descriptions.
     */
    private static final String USER_FILE = "u.user";
    
    /**
     * The name of the file listing the genres used for classification.
     */
    private static final String GENRE_FILE = "u.genre";
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final ServletOutputStream os = response.getOutputStream();
        final String basicPath = getAbsolutePath(PARTIAL_PATH_TO_STATIC_DATA);
        final String SLASH = File.separator;
        
        os.println(readFile(basicPath + SLASH + GENRE_FILE));
        os.println(readFile(basicPath + SLASH + USER_FILE));
        os.println(readFile(basicPath + SLASH + MOVIE_FILE));
        os.println(readFile(basicPath + SLASH + RATINGS_FILE));
        
        os.close();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet loads static data into the database.";
    }

    /**
     * Returns the absolute path to a specified relative path.
     * 
     * @param  relativePath the path where the user wants to get.
     * @return the absolute path as a string.
     */
    private String getAbsolutePath(final String relativePath) {
        return getServletContext().getRealPath(relativePath);
    }
    
    /**
     * Reads a text file whose absolute path is <code>absolutePath</code>.
     * 
     * @param  absolutePath the absolute path of the file to read.
     * @return <code>null</code> if something fails or otherwise a string
     *         containing the text in the input file.
     */
    private String readFile(final String absolutePath) {
        final File file = new File(absolutePath);
        
        try {
            final Reader reader = 
                    new InputStreamReader(new FileInputStream(file), "ISO-8859-1");
            
            final Scanner scanner = new Scanner(reader);
            final StringBuilder builder = new StringBuilder();
            
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append('\n');
            }
            
            return builder.toString();
        } catch (final UnsupportedEncodingException uee) {
            uee.printStackTrace(System.err);
            return null;
        } catch (final FileNotFoundException fnfe) {
            fnfe.printStackTrace(System.err);
            return null;
        }
    }
}
