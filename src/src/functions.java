/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kanika
 */
public class functions {

    protected int userLogin(String emailid, String password) {
        String ret = "";
        try {
            // TODO add your handling code here:

            //SENDING TO PHP
            String url = "http://kanikabhatia-photos.com/Team_File_Share/userlogin.php?emailid=" + emailid + "&password=" + password;
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String s = null;
            while ((s = in.readLine()) != null) {
                ret = ret + s;
            }
            in.close();

        } catch (IOException ex) {
            //Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (Integer.parseInt(ret) == 1) {
            writeusername(emailid);
        }
        return Integer.parseInt(ret);
    }

    protected String userRegistration(String data) {
        String retData = "";
        try {
            String array[] = data.split(",,");
            String firstname = array[0];
            String lastname = array[1];
            String emailid = array[2];
            String password = array[3];
            String url = "http://kanikabhatia-photos.com/Team_File_Share/reg2.php?firstname=" + firstname + "&lastname=" + lastname + "&emailid=" + emailid + "&password=" + password;
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String s = null;
            while ((s = in.readLine()) != null) {
                if (s.equals("Error")) {
                    retData = s;
                } else if (s.equals("Success")) {
                    retData = s;
                }
            }
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retData;
    }

    protected String fileUpload(String path, String fname) {
        String retData = "";
        try {
            String userid = readusername();
            String filePath = path;
            String fileName = fname;
            String url = "http://kanikabhatia-photos.com/Team_File_Share/uploads/tempfileupload.php?fname=" + fileName + "&userid=" + userid;
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            File fileRead = new File(filePath);
            int bytes = (int) fileRead.length();
            BufferedInputStream fos = new BufferedInputStream(new FileInputStream(filePath));
            for (int j = 0; j < bytes; j++) {
                os.write(fos.read());
            }
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String s = null;
            while ((s = in.readLine()) != null) {
                retData = s;
            }
            in.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retData;
    }

    protected String filelist() {
        String ret = "";
        try {
            int t = 0;
            String userid = readusername();
            String url = "http://kanikabhatia-photos.com/Team_File_Share/tempfilelist.php?userid=" + userid;
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String s = "";
            while ((s = in.readLine()) != null) {
                ret = ret + s;
            }

            in.close();

        } catch (MalformedURLException ex) {
            System.out.println("Error here");
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Error there");
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    protected String[] fileDownload(String fname) {
        String filelist = filelist();
        String[] userRequestedFileName = filelist.split(",");
        String path = "C:\\Users\\Kanika\\Documents\\My_Downloads\\";
        String userid = readusername();
        try {
            String url = "http://kanikabhatia-photos.com/Team_File_Share/uploads/tempfiledownload.php?userid=" + userid + "&filename=" + fname;
            //to be removed later
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String s, fn = "";
            while ((s = in.readLine()) != null) {
                fn = s;
                System.out.println(s);
            }
            String[] temp = fn.split("_");
            System.out.println(temp[1]);
            long startTime = System.currentTimeMillis();
            URL urlfile = new URL("http://kanikabhatia-photos.com/Team_File_Share/uploads/" + fn);
            urlfile.openConnection();
            InputStream reader = urlfile.openStream();
            fname = path + temp[1];
            FileOutputStream writer = new FileOutputStream(fname); //s
            byte[] buffer = new byte[1];
            int totalBytesRead = 0;
            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[1];
                totalBytesRead += bytesRead;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Done. " + (new Integer(totalBytesRead).toString()) + " bytes read (" + (new Long(endTime - startTime).toString()) + " millseconds).\n");
            writer.close();
            reader.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userRequestedFileName;
    }

    protected String readusername() {
        String username = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Temp\\userfile.txt"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                username = line;
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
        return username;
    }

    protected void writeusername(String name) {
        try {
            BufferedWriter buff = new BufferedWriter(new FileWriter("C:\\Temp\\userfile.txt"));
            buff.write(name);
            buff.close();
        } catch (IOException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static boolean emailValidation(String arg) {
        if ((arg.length() < 4) || !arg.contains("@") || !arg.contains(".")) {
            return true;
        }
        return false;
    }

    static boolean blankValidation(String arg) {
        if ((arg == null) || (arg.length() == 0)) {
            return true;
        }
        return false;
    }

    static boolean passwordValidation(String arg) {
        Pattern pattern = Pattern.compile("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#*=!])(?=[\\S]+$).{8,15})");
        Matcher matche = pattern.matcher(arg);
        if (matche.matches()) {
            return true;
        }
        return false;
    }

    protected void deleteusername() {
        File file = new File("C:\\Temp\\userfile.txt");
        if (file.exists()) {
            boolean filedelete = file.delete();
            System.out.println("file deleted" + filedelete);
        }
    }

    protected void deletefile(String name) {
        try {
            String userid = readusername();
            String fileName = name;
            String url = "http://kanikabhatia-photos.com/Team_File_Share/uploads/deleteUpload.php?fname=" + fileName + "&userid=" + userid;
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String s = null;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
            in.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void fileBackupDownload() {
        try {
            String userid = readusername();
            String path = "C:\\Users\\Kanika\\Documents\\My_Downloads\\";
            String url = "http://kanikabhatia-photos.com/Team_File_Share/uploads/backupzip.php?emailid=" + userid;
            HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setRequestMethod("POST");
            OutputStream os = httpUrlConnection.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String s = null;
            s = in.readLine();
            in.close();
            os.close();

            long startTime = System.currentTimeMillis();
            URL urlzip = new URL("http://kanikabhatia-photos.com/Team_File_Share/uploads/" + s);
            urlzip.openConnection();
            InputStream reader = urlzip.openStream();
            path = path + s;
            FileOutputStream writer = new FileOutputStream(path);
            byte[] buffer = new byte[1000];
            int totalBytesRead = 0;
            int bytesRead = 0;
            while ((bytesRead = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, bytesRead);
                buffer = new byte[1000];
                totalBytesRead += bytesRead;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Done. " + (new Integer(totalBytesRead).toString()) + " bytes read (" + (new Long(endTime - startTime).toString()) + " millseconds).\n");
            writer.close();
            reader.close();

            url = "http://kanikabhatia-photos.com/Team_File_Share/uploads/deletezip.php?file=" + s;
            httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();
            httpUrlConnection.setDoOutput(true);
            HttpURLConnection.setFollowRedirects(true);
            httpUrlConnection.setRequestMethod("POST");
            os = httpUrlConnection.getOutputStream();
            in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
            String s1 = null;
            s1 = in.readLine();
            System.out.println(s1);
            in.close();
            os.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
