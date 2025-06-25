/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 *
 * @author Ngo Quoc Hung - CE191184
 */
@WebServlet(name="ImageListServlet", urlPatterns={"/image-list"})
public class ImageListServlet extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String imageFolderPath = getServletContext().getRealPath("/assets/img");
        File imageFolder = new File(imageFolderPath);
        List<String> imageNames = new ArrayList<String>();

        if (imageFolder.exists() && imageFolder.isDirectory()) {
            File[] files = imageFolder.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.isFile()) {
                        imageNames.add(file.getName());
                    }
                }
            }
        }

        request.setAttribute("images", imageNames);
        request.getRequestDispatcher("/WEB-INF/admin/add.jsp").forward(request, response);
    }
}

