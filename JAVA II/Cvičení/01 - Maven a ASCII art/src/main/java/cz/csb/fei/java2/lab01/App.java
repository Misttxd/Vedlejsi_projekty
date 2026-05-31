package cz.csb.fei.java2.lab01;

import cz.vsb.fei.java2.lab01text2asciiart.ConversionException;
import cz.vsb.fei.java2.lab01text2asciiart.Text2AsciiArt;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 *  Class <b>App</b> - main class
 */
@Log4j2
public class App {

	public static void main(String[] args) throws IOException {

		String text = "";
		if (args[0].equals("-text")) {
			text = args[1];
		} else if (args[0].equals("-cli")) {
			BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

			text = r.readLine();
		} else {
			text = "nic";
		}
        try {
            String result = new Text2AsciiArt().convert(text);
			System.out.println(result);
        } catch (ConversionException e) {
			System.out.println("neco je spatne");
            throw new RuntimeException(e);
        }

	}
	
}