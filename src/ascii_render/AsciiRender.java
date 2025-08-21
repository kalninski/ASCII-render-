package ascii_render;

import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

public class AsciiRender {
	
	List<BufferedImage> sourceList = Collections.synchronizedList(new ArrayList<BufferedImage>());
	List<BufferedImage> destinationList = Collections.synchronizedList(new ArrayList<BufferedImage>());
	Map<Integer, String> letters = Collections.synchronizedMap(new HashMap<Integer, String>());
	ArrayList<Thread> yThread = new ArrayList<>();
	BufferedImage imageOriginal;
	BufferedImage destinationImage;
	int size = 20;
	String fontName = "Arial";
	Font font = new Font("Arial", Font.BOLD, 15);
	String[] listOfLetters = new String[] {" ",".",":","|","k","O","T","M","S","$", "@"};
	String directory;
	
	public AsciiRender(String path,String sourceName, String newName) {
		String sep = File.separator;
		directory = path;
		File file  = new File(path + sep + sourceName);
		
		
		int i = 0;
		for(String s : listOfLetters) {
			letters.put(i, s);
			i++;
//			System.out.println( i + "=" + s);
		}
		try{
			imageOriginal = ImageIO.read(file);
			lowerResolution();
			separateIntoSubimages();
			destinationImage  = new BufferedImage(imageOriginal.getWidth(), imageOriginal.getHeight(), BufferedImage.TYPE_INT_RGB);
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		renderImage(newName);
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
//	public void setFont(String fontName) {
//		this.fontName = fontName;
//	}
	
	public void lowerResolution() {
		
		int height  = imageOriginal.getHeight();
		int width = imageOriginal.getWidth();
//		System.out.println("old w = " + width + " old h =  "+ height);
		height  = height/size;
		width = width/size;
		Image smaller = imageOriginal.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		height = height * size;
		width = width * size;
		Image larger = smaller.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imageOriginal = image;
		imageOriginal.getGraphics().drawImage(larger, 0, 0, null);
//		System.out.println("new scale  w = " + width + " h = " + imageOriginal.getHeight());
	}
	
	public int index(int pixel) {
		
		int red = (pixel >>> 16) & 0xFF;
		int green  = (pixel >>> 8) & 0xFF;
		int blue = (pixel) & 0xFF;
		
		int sum = red + green + blue;
		sum = sum/3;
		 double avg = ((double) sum) / 255;
		 avg = avg * 10;
		 int index = (int) avg;
		
		return index;
	}
	
	public void separateIntoSubimages() {
		int y = 0;
		int width = imageOriginal.getWidth();
//		System.out.println("NEW HEIGHT = " + imageOriginal.getHeight());
		while(y < imageOriginal.getHeight()) {
			BufferedImage subimage = imageOriginal.getSubimage(0, y, width, size);
			BufferedImage background = new BufferedImage(width, size, BufferedImage.TYPE_INT_RGB);
			sourceList.add(subimage);
			destinationList.add(background);
		
			y += size;
//		System.out.println("Added sub from y0 = "+ (y - 10) + " to y1 = " + y);
		}
	}
	
	public void renderOneStrip(BufferedImage subimg,int indexOf) {
		BufferedImage subimgOut = new BufferedImage(subimg.getWidth(), size, BufferedImage.TYPE_INT_RGB);
//		System.out.println("renderOneStrip() called");
		Graphics gOut = subimgOut.getGraphics();
		Graphics2D g2dOut = (Graphics2D) gOut;
		
		Rectangle2D.Double bckgrnd = new Rectangle2D.Double(0, 0, subimg.getWidth(), subimg.getHeight());
		g2dOut.setColor(new Color(0, 0, 0));
		g2dOut.fill(bckgrnd);
		 
		for(int x = 0; x < subimg.getWidth(); x += size) {
			int pixel  = subimg.getRGB(x, 0);
			int index = index(pixel);
//			System.out.println("index = " + index);
			synchronized(letters) {
				String letter = letters.get(index);
//				System.out.println("The letter to be drawn = " + letter + " index = " + index);
				g2dOut.setFont(font);
				g2dOut.setColor(Color.WHITE);
				FontMetrics fm = g2dOut.getFontMetrics();
				int base = fm.getAscent();
//				System.out.println("letter to be printed = " + letter + " at index = " + index);
				g2dOut.drawString(letter, x, base);
			}
		}
//		int indexOf = sourceList.indexOf(subimg);
//		System.out.println("subimg Curr = " + sourceList.indexOf(subimg));
		destinationList.set(indexOf, subimgOut);
	}
	
	public void renderImage(String newName) {
//		System.out.println("Ŗender image called");
		synchronized(sourceList) {
			int c = 0;
			for(BufferedImage subimg : sourceList) {
				
//			System.out.println("subimg height  = " + subimg.getHeight() + " num. subimg = "  + c);
	//		c++;
				int indexOf = sourceList.indexOf(subimg);
			Thread oneThread = Thread.ofVirtual().start(() ->{
					
					renderOneStrip(subimg, indexOf);
					
				});
				
				yThread.add(oneThread);
				
			}
			
			for(Thread t : yThread) {
				try {
					t.join();
				}catch(InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			int y = 0;
			for(BufferedImage subiOut : destinationList) {
				destinationImage.getGraphics().drawImage(subiOut, 0, y, null);
				y += size;
//				System.out.println("destanation image y = " + y);
			}
			File renderedOUTPUT = new File(directory + File.separator + newName);
		try {
			ImageIO.write(destinationImage, "png", renderedOUTPUT);

		}catch(IOException ex) {
			
		}
			
		}
		
	}
	
}
