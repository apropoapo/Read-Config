/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package myAPI.readConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The class readConfig can read specific parameters out of a config file. Rules
 * for the config file: <br/>
 * - it has to be a text format <br/>
 * - every parameter or structuring command has to be written down in a new line
 * <br/>
 * - words with doublepoint at the end (e.g. FirstSet:) describe a structuring
 * command <br/>
 * - words with an equal sign at the end (e.g. ParameterOne=) describe a
 * parameter variable which can be read in <br/>
 * - Every parameter text that is readable has to be set in quotation marks
 * anywhere behind the equal sign in the same line <br/>
 * - The text/parameter which shall be read in, may not contain a quotation mark
 * <br/>
 * <br/>
 * The config file must have the format like this example: <br/>
 * <br/>
 * # Comments.... <br/>
 * # <br/>
 * FirstSet: <br/>
 * ParameterOne="hello" <br/>
 * ParameterOne = "world" <br/>
 * ParameterTwo="Hallo World" <br/>
 * SecondSet: <br/>
 * ParameterThree="HaLlO wOrLd" <br/>
 *
 * @author B. Döpfner
 * @version 1.0
 */
public class ReadConfig {

    /**
     * Contains the pathname of the config file.
     */
    private String pathname;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            ReadConfig rc = new ReadConfig("C:\\Users\\Hallo\\Desktop\\config.conf");
            String[] strArr = rc.readParameter("FirstSet", "ParameterOne");
            for (String s : strArr) {
                System.out.println(s);
            }

        } catch (Exception exce) {
            System.out.println(exce.getMessage() + "");

        }
    }

    /**
     * The recommended Constructor of the ReadConfig Class.
     *
     * @param pathname The pathname of the config file.
     *
     */
    public ReadConfig(String pathname) {
        this.pathname = pathname;
    }

    /**
     * Returns the pathname of the object.
     *
     * @return The local variable pathname of the object.
     * @throws NullPointerException throws NullPointerException if the pathname
     * of the object has not been set before.
     */
    public String getPathname() {
        return this.pathname;
    }

    /**
     * Sets the local variable pathname to a new value.
     *
     * @param pathname The new pathname of the config file.
     */
    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    /**
     *
     * @param structuringLines_Ordered The structoring line that comes before
     * the parameterName. Can be null.
     * @param parameterName The name of the searched parameter.
     * @return Returns the values of the searched parameter. Multiple values are
     * possible.
     */
    public String[] readParameter(String structuringLine, String parameterName) throws FileNotFoundException, IOException {
        // return String arraylist
        ArrayList<String> result = new ArrayList<>();

        // return String array  
        String[] resArray;

        // open file
        File file = new File(pathname);

        // Autoclosable streams
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {

            String line;

            // Until EOF
            while (!(line = br.readLine()).isEmpty()) {
                // Enter structuringLine
                if (line.contains(structuringLine) && !line.trim().startsWith("#")) {
                    // Until EOF or next structuringLine 
                    while (!((line = br.readLine()).isEmpty() || line.contains(":") && !line.trim().startsWith("#"))) {
                        if (line.contains(parameterName) && !line.trim().startsWith("#")) {
                            // right side of the parameter line
                            String temp = line.split("=")[1];
                            // gets the parameter out of the quotation marks and adds it to the result list
                            result.add(temp.split("\"")[1].trim());
                        }

                    }
                    if (result.isEmpty()) {
                        throw new IllegalArgumentException("In the config file, the block " + structuringLine + "does not contain an parameter named " + parameterName + ". Please check the parameterName and config file.");
                    } else {
                        // Convert ArrayList to Array
                        resArray = new String[result.size()];
                        result.toArray(resArray);
                        return resArray;

                    }


                }


            }
            if (result.isEmpty()) 
                throw new IllegalArgumentException("In the config file doesn't contain the block " + structuringLine + ". Please check the structureLine parameter and the config file.");
        }

        // Convert ArrayList to Array
        resArray = new String[result.size()];
        result.toArray(resArray);
        return resArray;



    }
}
