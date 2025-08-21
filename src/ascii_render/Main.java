package ascii_render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.*;
import java.util.*;
import java.awt.font.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] list = new String[] {"D", "X", "O", "k", "b", "?", ">", "!", "|", "."}; 
		String letter = "B";
		String sep = File.separator;
		String path  = "C:" + sep + "Users"+ sep + "Toms"+ sep + "Desktop"+ sep + "ASCII_IMAGES" ;
		File output = new File(path + sep + "EmptyImage.png");

		File outputSmaller = new File(path + sep + "land1234.png");
		File file  = new File(path);
		BufferedImage img = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		try {
			BufferedImage image = ImageIO.read(new File(path + sep + "the_copied_image.png"));
			int height = image.getHeight();
			int width = image.getWidth();
			System.out.println("width = " + width + " height = " + height);
			Image smaller = image.getScaledInstance(width/10, height/10, Image.SCALE_DEFAULT);
			int newH = smaller.getHeight(null);
			int newW = smaller.getWidth(null);
			BufferedImage smaller1 = new BufferedImage(newH, newW, BufferedImage.TYPE_INT_ARGB);
			smaller1.getGraphics().drawImage(smaller, 0, 0, null);
			System.out.println("smaller1 : " + smaller1.getHeight());
			System.out.println("newH = " + newH + " newW = " + newW);
			Image larger = smaller1.getScaledInstance(newH * 10, newH * 10, Image.SCALE_DEFAULT);
			BufferedImage larger1 = new BufferedImage(newH * 10, newW * 10, BufferedImage.TYPE_INT_ARGB);
			larger1.getGraphics().drawImage(larger, 0, 0, null);
			ImageIO.write(larger1, "png", outputSmaller);
			System.out.println("path of smaller1 = " + outputSmaller.getAbsolutePath());

		}catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		Font txt = new Font("Arial", Font.BOLD, 139);
		float size = txt.getSize2D();
		
	
		Graphics g = img.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		int w = g2d.getFontMetrics().stringWidth(letter);
		Rectangle2D.Double bckgrnd = new Rectangle2D.Double(0,0,1000,1000);
		g2d.setColor(new Color(150, 150, 150));
		g2d.fill(bckgrnd);
		
		g2d.setFont(txt);
		g2d.setColor(Color.WHITE);
		int xLocation = 0;
		int i = 0;
		while(xLocation <= 1000) {
			
			g2d.drawString(list[i%10], xLocation, img.getHeight()/2);
			xLocation += 100;
			i++;
		}
		g2d.dispose();

		
		
		try {
			ImageIO.write(img, "png", output);
			System.out.println("The file has been saved : " + file.getAbsolutePath() + "\n" + "String T px width  = " + size );
		}catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
