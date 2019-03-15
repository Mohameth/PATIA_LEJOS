package HelloWorld;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import lejos.hardware.lcd.LCD;

public class IOFile {
	Properties prop;
	InputStream infile;
	OutputStream outfile;
	
	
	public IOFile() {
			this.prop = new Properties();
	}
	
	public void initInputFile() {
		
		try {
			this.infile = new FileInputStream("config.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
public void initOutputFile() {
		try {
			this.outfile = new FileOutputStream("config.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void emptyfile() {
		try {		
			//this.savefile;
			
			this.prop.clear();
			
			this.prop.store(this.outfile, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (this.outfile != null) {
				try {
					this.outfile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public int saveIntofile(String key, String value) {
		try {		
			//this.savefile;
			if (key.isEmpty() || value.isEmpty()) {
				return -1;
			}
			
			this.prop.setProperty(key, value);
			this.prop.store(this.outfile, null);

		} catch (IOException io) {
			io.printStackTrace();
		}
		return 0;
	}
	
	
	public String getValFromKey(String key) {
		String res=""; 
		try {
			// load a properties file
			prop.load(this.infile);
			res = prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
	public float[] getFloatArrayFromKey(String key) {
		String res=""; 
		try {
			// load a properties file
			prop.load(this.infile);
			res = prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		res = res.substring(1, res.length()-1);
		String[] b = res.split(",");
		
		float[] t = new float[b.length];
		for(int i=0;i<b.length;i++) {				
			t[i] = Float.parseFloat(b[i]);
		}
		return t;
	}
	
	public int closeFile() {
		int i = 0;
		if (this.infile != null) {
			try {
				this.infile.close();
				i++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (this.outfile != null) {
			try {
				this.outfile.close();
				i++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
	
	
	
}
