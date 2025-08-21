package ascii_render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Clock;

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
public class MainClass {

	
	
	public static void main(String[] args) {
//		String[] listOfLetters = new String[] {".",",",":","|","o","u","U","O","$","@"}; 
//		Map<Integer, String> list = new HashMap<Integer, String>();
//		int i = 0;
//		for(String s : listOfLetters) {
//			list.put(i, s);
//			i++;
//		}
//		Map<Integer, String> lettersMap = Collections.synchronizedMap(list);
//		System.out.println("synchronized map content = " + list);
//		String sep = File.separator;
//		String path  = "C:" + sep + "Users"+ sep + "Toms"+ sep + "Desktop"+ sep + "ASCII_IMAGES" ;
//		String inputName = "Paklajs.png";
//		String outName = "Image.png";
//
//		String outNameL = "ImageL.png";
//		File output  = new File(path + sep + outName);
//
//		File outputL  = new File(path + sep + outNameL);
//		File renderedOUTPUT  = new File(path + sep + "RenderASCII_Paklajs1.png");
//		
//		try {
//			
//			BufferedImage img = ImageIO.read(new File(path + sep + inputName));
//			int width = img.getWidth();
//			int height = img.getHeight();
////			System.out.println(" ORIGINAL : W = " + width + " H = " + height);
//			Image smaller = img.getScaledInstance(width/10, height/10, Image.SCALE_DEFAULT);
//			int heightS = height/10;
//			int widthS = width/10;
//			BufferedImage smallerB = new BufferedImage(widthS, heightS, BufferedImage.TYPE_INT_RGB);
//			smallerB.getGraphics().drawImage(smaller, 0, 0, null);
//			Image larger = smaller.getScaledInstance(widthS * 10, heightS * 10, Image.SCALE_DEFAULT);
//			BufferedImage largerB = new BufferedImage(widthS * 10, heightS * 10, BufferedImage.TYPE_INT_RGB);
//			largerB.getGraphics().drawImage(larger, 0, 0, null);
////			ImageIO.write(smallerB, "png", output);
////			ImageIO.write(largerB, "png", outputL);
//			
////			System.out.println("OUTPUT FILE : " + output.getAbsolutePath() + "\n NEW LARGER DIMENSIONS : W = " + largerB.getWidth() + " H = " + largerB.getHeight());
//			
//			List<BufferedImage> subimages = Collections.synchronizedList(new ArrayList<BufferedImage>());
//			List<BufferedImage> subimagesOut = Collections.synchronizedList(new ArrayList<BufferedImage>());
//			
//			ArrayList<Thread> threads = new ArrayList<>();
//			int y = 0;
//			BufferedImage render = new BufferedImage(largerB.getWidth(), largerB.getHeight(), BufferedImage.TYPE_INT_RGB);
//			Font font = new Font("Arial", Font.BOLD, 8);
//
//			while(y < largerB.getHeight()) {
//				BufferedImage sub = largerB.getSubimage(0, y, largerB.getWidth(), 10);
//				boolean isAdded = subimages.add(sub);
//				y+=10;
//			//	System.out.println("Added sub from y0 = "+ (y - 10) + " to y1 = " + y);
//				
//			}
//		
//			synchronized(subimages) {
//				
//				for(BufferedImage subi : subimages) {
//					
////								Graphics g = subi.getGraphics();
////								Graphics2D g2d = (Graphics2D) g;
//								BufferedImage subiOut= new BufferedImage(subi.getWidth(), subi.getHeight(), BufferedImage.TYPE_INT_RGB);
//								
//						Thread yThread = Thread.ofVirtual().start(() -> {
//
//								Graphics gOut = subiOut.getGraphics();
//								Graphics2D g2dOut = (Graphics2D) gOut;
//								
//								Rectangle2D.Double bckgrnd = new Rectangle2D.Double(0, 0, subi.getWidth(), subi.getHeight());
//								g2dOut.setColor(new Color(0, 0, 0));
//								g2dOut.fill(bckgrnd);
//							for(int x = 0; x < subi.getWidth(); x += 10) {
//								int pix = subi.getRGB(x, 0);
//								int red = (pix >>> 16) & 0xFF;
//								int green = (pix >>> 8) & 0xFF;
//								int blue = (pix) & 0xFF;
//								int sum = red + green + blue;
//								int avg = sum/3;
//								double avgNormalized = (double) avg;
//								avgNormalized = avgNormalized /255;
//			//					System.out.println("avgNormalized avg/255 = " + avgNormalized);
//								avgNormalized *= 10;
//								
//								avg = (int) avgNormalized;
//				//				System.out.println("avg new = " + avg);
//								synchronized(lettersMap) {
//								String letter = lettersMap.get(avg);
//
//			//					System.out.println("The letter to be drawn = " + letter);
//								g2dOut.setFont(font);
//								g2dOut.setColor(Color.WHITE);
//								FontMetrics fm = g2dOut.getFontMetrics();
//								int base = fm.getAscent();
//								g2dOut.drawString(letter, x, base);
//								
//								}
//								
////								
//							}
//						});
//						
//						threads.add(yThread);
//
//						subimagesOut.add(subimages.indexOf(subi), subiOut);	
//					
//				//		System.out.println("subi Curr = " + subimages.indexOf(subi));
//				}
//			}
//			for(Thread t : threads) {
//				try {
//					t.join();
//				}catch(InterruptedException ex) {
//					ex.printStackTrace();
//				}
//			}
//				y = 0;
//			for(BufferedImage subiOut : subimagesOut) {
//				render.getGraphics().drawImage(subiOut, 0, y, null);
//				y += 10;
//			}
//			ImageIO.write(render, "png", renderedOUTPUT);
//
//		}catch(IOException ex) {
//			
//		}
		
		Clock clock = Clock.systemDefaultZone();
		
		long start = clock.millis();
		AsciiRender aR = new AsciiRender("C:\\Users\\Toms\\Desktop\\ASCII_IMAGES", "ES_1234.png", "ES_12341_20px_15pt.png");
		long end = clock.millis();
		System.out.println("Time it took to render = " + (end - start) + " milliseconds");
		
		}

}
