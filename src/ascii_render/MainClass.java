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

		Clock clock = Clock.systemDefaultZone();
		
		long start = clock.millis();
//		ArrayList<Thread> threads = new ArrayList<>();
//		List<BufferedImage> rowsList = Collections.synchronizedList(new ArrayList<BufferedImage>());
//		List<BufferedImage> rowsListOutput = Collections.synchronizedList(new ArrayList<BufferedImage>());
//		
//		String[] listOfLetters = new String[] {".",",",":","|","o","u","U","O","$","@"}; 
//		Map<Integer, String> list = new HashMap<Integer, String>();
//
//		String sep = File.separator;
//		String path  = "C:" + sep + "Users"+ sep + "Toms"+ sep + "Desktop"+ sep + "ASCII_IMAGES" ;
//		
//		File file  = new File(path + sep + "EDGES_100x100.png");
//		String name  = "!EDGES_100x100_atan_gray.png";
//		String name1  = "!EDGES_100x100_atan_line.png";
//		File renderedOUTPUT = new File(path + sep + name);
//		File renderedOUTPUT1 = new File(path + sep + name1);
//		try {
//			
//		BufferedImage img = ImageIO.read(file);
//		int width = img.getWidth();
//		int height = img.getHeight();
//		width = width /10;
//		height = height / 10;
//		width = width * 10;
//		height = height*10;
//		
//		
//		BufferedImage render = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//		System.out.println("new width = " + render.getWidth() + " new height = " + render.getHeight());
//
//		for(int i = 0; i < height; i++) {
//			BufferedImage subi = img.getSubimage(0, i, width, 1);
//			rowsList.add(subi);
//			rowsListOutput.add(subi);
//	//		System.out.println((i + 1) + " rows out of "+ (height) + " have been added" );
//		}
//		
//		synchronized(rowsList) {
//			
//			for(BufferedImage sub : rowsList) {
//				Thread yThread = Thread.ofVirtual().start(() -> {
//					for(int x = 0; x < sub.getWidth(); x++) {
//						int pixel = sub.getRGB(x, 0);
//						int red = (pixel >> 16) & 0xFF;
//						int green  = (pixel >> 8) & 0xFF;
//						int blue = (pixel) & 0xFF;
//						
//						int sum = red + green + blue;
//						int avg  = sum/3;
//						
//						int gray = (avg << 16) | (avg << 8) | avg;
//						sub.setRGB(x, 0, gray);
//						
//						
//
//						
//					}
//					
//					int index = rowsList.indexOf(sub);
//					rowsListOutput.set(index, sub);
//					
//				});
//				
//				threads.add(yThread);
//				}
//			}
//			for(Thread t : threads) {
//				try {
//					t.join();
//				}catch(InterruptedException ex) {
//					ex.printStackTrace();
//				}
//			}
//		
//		------------------------------------------------------------------ get edges below
//			int y = 0;
//			for(BufferedImage subiOut : rowsList) {
//				render.getGraphics().drawImage(subiOut, 0, y, null);
//				y += 1;
//			}
//
//			ImageIO.write(render, "png", renderedOUTPUT);
//		rowsList.clear();
//		rowsListOutput.clear();
//		threads.clear();
//				
//		y = 0;
//		while(y < render.getHeight()) {
//			
//			BufferedImage subi = render.getSubimage(0, y, width, 10);
//			BufferedImage back = new BufferedImage(subi.getWidth(), subi.getHeight(), BufferedImage.TYPE_INT_ARGB);
//			rowsList.add(subi);
//			rowsListOutput.add(back);
//			
//			y += 10;
//		}
//		Font font = new Font("Arial", Font.BOLD, 8);
//		synchronized(rowsList) {
//			
//			for(BufferedImage subi : rowsList) {
//
//				Thread row = Thread.ofVirtual().start(() -> {
//					int index = rowsList.indexOf(subi);
//					BufferedImage subiOut = rowsListOutput.get(index);
//					Graphics g = subiOut.getGraphics();
//					Graphics2D g2d = (Graphics2D) g;
//					Rectangle2D.Double back = new Rectangle2D.Double(0, 0, subi.getWidth(), subi.getHeight());
//					g2d.setColor(new Color(0, 0, 0));
//					g2d.fill(back);
//					int x = 0;
//					while(x < subi.getWidth()) {
//						
//						int sumX = 0;
//						int sumY = 0;
//						
//						for(int rows = 0; rows < 10 ; rows += 1) {
//							int pixel0X = subi.getRGB(x, rows);
//							int pixel1X = subi.getRGB(x + 9, rows);
//							
//							int pixel2Y = subi.getRGB(x + rows, 0);
//							int pixel3Y = subi.getRGB(x + rows, 9);
//							
//							int col0X = pixel0X & 0xFF;
//							int col1X = pixel1X & 0xFF;
//							
//							int col2Y = pixel2Y & 0xFF;
//							int col3Y = pixel3Y & 0xFF;
//							
//							int oneDiffX = col1X - col0X;
//							int oneDiffY = col3Y - col2Y;
//							
//							sumX += oneDiffX;
//							sumY += oneDiffY;
//						}
//						
//						double avgDiffX = ((double) sumX)/10;
//						double avgDiffY = ((double) sumY)/10;
//						
////						avgDiffX = Math.abs(avgDiffX);
////						avgDiffY = Math.abs(avgDiffY);
//						
//						double dist = Math.sqrt(Math.pow(avgDiffX, 2) + Math.pow(avgDiffY, 2));
//						
//					
//						
//
//						
//
//						String letter = "";
//						if(dist > 20) {
//							double arg = avgDiffY / avgDiffX;
//							double angle = Math.atan(arg);
//							angle  = angle * (180/Math.PI);												
///*For the way the "/", "\\", "_" it looks as though the gradient points along the edge, but what I fucking retardedly did not think of, was the fact
// * that since the coordinate system for images has y-axis flipped, this causes the results to be like this, so when the result od atan(arg) is > 0 it is in fact below the x axis
// * so when the result of atan() is 15 degress, that means it is in fact -15 degrees if you convert it to the regular unit circle.
// */
//						if(angle <= 90 && angle > 85) {
//							letter = "_";
//						//	System.out.println("angle  = " + angle + " avgDiffX = " + avgDiffX + " avgDiffY = " + avgDiffY + " dist = " + dist);
//						}
//						if(angle >=15 && angle <= 85) {
//							letter = "/";
//						}
//						
//					
//						
//						if(angle < 5 && angle > -5) {
//							letter = "|";
//							System.out.println("angle  = " + angle + " avgDiffX = " + avgDiffX + " avgDiffY = " + avgDiffY + " dist = " + dist);
//						}
//						if(angle < -15 && angle > -85) {
//							letter = "\\";
//						}
//						
//						if(angle <= -85 && angle >= -90) {
//							letter = "_";
//			//					System.out.println("angle  = " + angle + " avgDiffX = " + avgDiffX + " avgDiffY = " + avgDiffY + " dist = " + dist);						
//						}
//
//								g2d.setFont(font);
//								g2d.setColor(Color.WHITE);
//								FontMetrics fm = g2d.getFontMetrics();
//								int base = fm.getAscent();
//								g2d.drawString(letter, x + 2 , base);
//						}
//						
//						
//						x += 10;
//					}
//				});
//			
//				threads.add(row);
//
//			}			
//		}
//		
//		for(Thread t : threads) {
//				try {
//					t.join();
//				}catch(InterruptedException ex) {
//					ex.printStackTrace();
//				}
//			}
//		
//		
//			y = 0;
//			for(BufferedImage subiOut : rowsListOutput) {
//				render.getGraphics().drawImage(subiOut, 0, y, null);
//				y += 10;
//			}
//
//			ImageIO.write(render, "png", renderedOUTPUT1);
//		rowsList.clear();
//		rowsListOutput.clear();
//		threads.clear();
//		
//		}catch(IOException ex) {
//			System.out.println("The file " + file.getAbsolutePath() +  "\n was not found!");
//		}
		
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
		
		
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


		AsciiRender aR = new AsciiRender("C:\\Users\\Toms\\Desktop\\ASCII_IMAGES", "Marta.jpg", "Marta_OUTLINES_10px_9pt.png");
		long end = clock.millis();
		System.out.println("Time it took to render = " + (end - start) + " milliseconds");
		
		}

}
