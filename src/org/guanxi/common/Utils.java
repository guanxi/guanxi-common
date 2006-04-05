//: "The contents of this file are subject to the Mozilla Public License
//: Version 1.1 (the "License"); you may not use this file except in
//: compliance with the License. You may obtain a copy of the License at
//: http://www.mozilla.org/MPL/
//:
//: Software distributed under the License is distributed on an "AS IS"
//: basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//: License for the specific language governing rights and limitations
//: under the License.
//:
//: The Original Code is Guanxi (http://www.guanxi.uhi.ac.uk).
//:
//: The Initial Developer of the Original Code is Alistair Young alistair@smo.uhi.ac.uk.
//: Portions created by SMO WWW Development Group are Copyright (C) 2005 SMO WWW Development Group.
//: All Rights Reserved.
//:
/* CVS Header
   $Id$
   $Log$
   Revision 1.11  2006/04/05 12:18:27  alistairskye
   Added zipDirectory()

   Revision 1.10  2005/11/07 09:38:23  alistairskye
   initLogger takes log filename instead of ServletConfig now, to allow plugins to use it

   Revision 1.9  2005/11/03 16:34:00  alistairskye
   Added initLogger()

   Revision 1.8  2005/08/10 13:05:42  alistairskye
   Added dumpXML()

   Revision 1.7  2005/07/22 10:42:17  alistairskye
   Added getConfigOption()

   Revision 1.6  2005/07/11 10:52:22  alistairskye
   Package restructure

   Revision 1.5  2005/06/21 09:26:01  alistairskye
   Modified decodeBase64() to return a String

   Revision 1.4  2005/06/21 09:14:40  alistairskye
   Added decodeBase64()

   Revision 1.3  2005/05/04 13:33:55  alistairskye
   base64() moved here from org.Guanxi.SAMUEL.Utils.Utils

   Revision 1.2  2005/04/15 10:02:41  alistairskye
   License updated

*/

package org.guanxi.common;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.exceptions.Base64DecodingException;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.xml.DOMConfigurator;
import org.guanxi.common.definitions.Guanxi;
import org.guanxi.common.definitions.Logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.rmi.server.UID;
import java.io.StringWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;

/**
 * <font size=5><b></b></font>
 *
 * @author Alistair Young alistair@smo.uhi.ac.uk
 */
public class Utils
{
  /**
   * Returns the parameters and their values from an HTTP request
   *
   * @param request HTTP request object
   * @return Hashtable of parameters and their values
   */
  public static Hashtable getRequestParameters(HttpServletRequest request) {
    Hashtable params = new Hashtable();
    Enumeration e = request.getParameterNames();
    String name,value;

    while (e.hasMoreElements()) {
      name = (String)e.nextElement();
      value = request.getParameter(name);
      params.put(name, value);
    }

    return params;
  }

  public static String getUniqueID() {
    UID uid = new UID();
    return uid.toString();
  }

  public static String base64(Document inDocToEncode) {
    try {
      DOMSource domSource = new DOMSource(inDocToEncode);
      StringWriter writer = new StringWriter();
      StreamResult result = new StreamResult(writer);
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.transform(domSource, result);
      return Base64.encode(writer.toString().getBytes());
    }
    catch(TransformerException te) {
      return null;
    }
  }

  public static String decodeBase64(String b64Data) {
    try {
      return new String(Base64.decode(b64Data));
    }
    catch(Base64DecodingException bde) {
      return null;
    }
  }

  /**
   * Gets the text value of a particular node in the config file
   *
   * @param configNode The parent node of the node we're interested in
   * @param option The node whose text we want
   * @return Text value of the node
   */
  public static String getConfigOption(Node configNode, String option) {
    NodeList childNodes = null;
    int childCount = 0;

    NodeList nodes = configNode.getChildNodes();

    if (nodes == null) return "-1";

    for (int count=0; count < nodes.getLength(); count++) {
      if (nodes.item(count).getLocalName() == null) continue;

      // Have we found the node we're looking for?
      if (nodes.item(count).getLocalName().equalsIgnoreCase(option)) {
        // Get the child nodes. We're looking for the text node containing the value
        childNodes = nodes.item(count).getChildNodes();

        for (childCount=0; childCount < childNodes.getLength(); childCount++) {
          // Got a text node?
          if (childNodes.item(childCount).getNodeType() == Node.TEXT_NODE) {
            // Anything in it?
            if (childNodes.item(childCount).getNodeValue() != null)
              // Sometimes you can get blank text nodes with Xerces
              if (!childNodes.item(childCount).getNodeValue().equalsIgnoreCase("")) {
                return childNodes.item(childCount).getNodeValue();
            }
          }
        }
      }
    }

    return "-1";
  }

  public static void dumpXML(Logger log, String xml, String message) {
    log.debug("=======================================================" +
              System.getProperty("line.separator") +
              System.getProperty("line.separator") +
              message +
              System.getProperty("line.separator") +
              System.getProperty("line.separator") +
              xml +
              System.getProperty("line.separator") +
              System.getProperty("line.separator") +
              "=======================================================");
  }

  public static void initLogger(ServletContext context, String logFilename, Logger log, String name) throws GuanxiException {
    // Get the logfile path and name from web.xml...
    String logDir = context.getInitParameter(Guanxi.LOGDIR_PARAMETER);
    // ...work out if it's relative to the webapp root...
    if ((logDir.startsWith("WEB-INF")) || (logDir.startsWith(System.getProperty("file.separator") + "WEB-INF")))
      logDir = context.getRealPath(logDir);
    // ...tidy it up...
    if (!logDir.endsWith(System.getProperty("file.separator")))
      logDir += System.getProperty("file.separator");
    // ...and add the name of the log file
    String logFile = logDir + logFilename;

    DOMConfigurator.configure(context.getRealPath(Logging.DEFAULT_IDP_CONFIG_FILE));

    PatternLayout defaultLayout = new PatternLayout(Logging.DEFAULT_LAYOUT);

    RollingFileAppender rollingFileAppender = new RollingFileAppender();
    rollingFileAppender.setName(name);
    try {
      rollingFileAppender.setFile(logFile, true, false, 0);
    }
    catch(IOException ioe) {
      throw new GuanxiException(ioe.getMessage());
    }
    rollingFileAppender.setMaxFileSize("1MB");
    rollingFileAppender.setMaxBackupIndex(5);
    rollingFileAppender.setLayout(defaultLayout);

    log.removeAllAppenders();
    log.addAppender(rollingFileAppender);
    log.setAdditivity(false);
  }

  public static void zipDirectory(String dir, ZipOutputStream zipStream) {
    try {
      File dirToZip = new File(dir);

      // Have a gander in the directory to see what's there
      String[] dirList = dirToZip.list();

      byte[] readBuffer = new byte[2156];
      int bytesIn = 0;

      for (int count=0; count < dirList.length; count++) {
        File dirFile = new File(dirToZip, dirList[count]);

        // If the next entity is a directory, call recursively
        if (dirFile.isDirectory()) {
          zipDirectory(dirFile.getPath(), zipStream);
          continue;
        }

        // Get a handle to the file...
        FileInputStream fis = new FileInputStream(dirFile);
        // ...get an entry ready for it ...
        ZipEntry zipEntry = new ZipEntry(dirFile.getPath());
        // ...bung it in the ZIP stream...
        zipStream.putNextEntry(zipEntry);
        // ...and read the data into the ZIP stream
        while ((bytesIn = fis.read(readBuffer)) != -1) {
          zipStream.write(readBuffer, 0, bytesIn);
        }
        
        fis.close();
      }
    }
    catch(Exception e) {
      System.out.println(e);
    }
  }
}
