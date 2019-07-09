package com.util.commons.util;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class CSVUtils {
    /**
     *
     * @param response
     * @param list
     * @param name
     */
    public static void exportCSV(HttpServletResponse response, List<String> list, String name) {
        try {
            response.setContentType("application/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String( name.getBytes("gb2312"), "ISO8859-1" ));
            String content = "";
            for (String l : list) {
                content += l + "\r";
            }
            InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
            int len = 0;
            byte[] buffer = new byte[1024];
            response.setCharacterEncoding("UTF-8");
            OutputStream out = response.getOutputStream();
            while ((len = inputStream.read(buffer)) > 0) {
                out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
                out.write(buffer, 0, len);
            }
            inputStream.close();
            out.close();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

}
