package project4;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class DrawImage {
	
	DrawImage(){	
	
	}

    /**
     * Print an image to a JFrame.
     * 
     * Can be called with null title.
     * 
     * @param img Image to print
     * @param title Title of new image
     */
	public void ShowImage(Mat img, String title) {
	    MatOfByte matOfByte = new MatOfByte();

	    Highgui.imencode(".jpg", img, matOfByte);
	    byte[] byteArray = matOfByte.toArray();
	    BufferedImage bufImage = null;

	    try {
	        InputStream in = new ByteArrayInputStream(byteArray);
	        bufImage = ImageIO.read(in);
	    	JFrame frame = new JFrame();
	        JLabel lbl = new JLabel(new ImageIcon(bufImage));
	        frame.getContentPane().add(lbl);
	        lbl.repaint();
	        frame.pack();
	        
	        frame.setVisible(true);
	        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setTitle(title);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
