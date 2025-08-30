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
	
	
	/*
	 * Source list  - simplified image strips -> one color per 10x10 px area
	 * Edge Source list - list of same strips, but not simplified and is used for the edge detection
	 * destinationList - list of finished strips to be be stuck together for the final image
	 */
	List<BufferedImage> sourceList = Collections.synchronizedList(new ArrayList<BufferedImage>());//simplified image strip list for letters
	List<BufferedImage> destinationList = Collections.synchronizedList(new ArrayList<BufferedImage>());
	List<BufferedImage> sourceListEdges = Collections.synchronizedList(new ArrayList<BufferedImage>());//not simplified image strips for edge detection

	
	List<BufferedImage> rowsListForGrayscaling = Collections.synchronizedList(new ArrayList<BufferedImage>());
	Map<Integer, String> letters = Collections.synchronizedMap(new HashMap<Integer, String>());
	
	ArrayList<Thread> yThread = new ArrayList<>();
	BufferedImage imageOriginal;
	BufferedImage imageOriginalEdges;
	BufferedImage destinationImage;
	int size = 10;
	String fontName = "Arial";
	Font font = new Font("Arial", Font.BOLD, 9);
	char qed = '\u25a0';
	String qedStr = "" + qed;
	char alef = '\u05d0';
	char em = '\u2014';
	String emdash = "" + em;
	String alefStr = "" + alef;
	String[] listOfLetters = new String[] {" "," "," "," "," "," ", " "," "," "," ", " "};
//	String[] listOfLetters = new String[] {" ",".","~","T","A","M", "R","#","@", qedStr, qedStr};
	String directory;
	
	public AsciiRender(String path,String sourceName, String newName) {
		String sep = File.separator;
		directory = path;
		File file  = new File(path + sep + sourceName);
		
		
		int i = 0;
		for(String s : listOfLetters) {
			letters.put(i, s);
			i++;
		}
		try{
			imageOriginal = ImageIO.read(file);
			imageOriginalEdges = ImageIO.read(file);
			rescale();
			edgeSourceToGrayScale();

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
	
	//rename to prepare images -> resize the source images for edgedetection
	public void rescale() {
		
		int height  = imageOriginal.getHeight();
		int width = imageOriginal.getWidth();
		height  = height/size;
		width = width/size;
		Image smaller = imageOriginal.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		height = height * size;
		width = width * size;
		Image larger = smaller.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		imageOriginal = image;
		imageOriginal.getGraphics().drawImage(larger, 0, 0, null);
		
		
		//rescale the edge source image to be the exact same size as the other image
		Image edges = imageOriginalEdges.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage scaledEdgesSource = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		imageOriginalEdges = scaledEdgesSource;
		imageOriginalEdges.getGraphics().drawImage(edges, 0, 0, null);
		
	}
	
	public int index(int pixel) {
		
		int red = (pixel >> 16) & 0xFF;
		int green  = (pixel >> 8) & 0xFF;
		int blue = (pixel) & 0xFF;
		
		int sum = red + green + blue;
		sum = sum/3;
		 double avg = ((double) sum) / 255;
		 avg = avg * 10;
		 int index = (int) avg;
		
		return index;
	}

//separate the subimages for edge detection source and edge detection ouput
	public void separateIntoSubimages() {
		String sep = File.separator;
	
		int y = 0;
		int width = imageOriginal.getWidth();
//		System.out.println("NEW HEIGHT = " + imageOriginal.getHeight());
		while(y < imageOriginal.getHeight()) {
			BufferedImage subimage = imageOriginal.getSubimage(0, y, width, size);
			BufferedImage background = new BufferedImage(width, size, BufferedImage.TYPE_INT_RGB);
			BufferedImage subimageEdgesSource = imageOriginalEdges.getSubimage(0, y, width, size);
			BufferedImage backgoundEdges = new BufferedImage(width, size, BufferedImage.TYPE_INT_RGB);
			
			sourceList.add(subimage);
			destinationList.add(background);
//			System.out.println("subimage to Source = " + y + " from " + imageOriginal.getHeight());
		
			boolean addedToEdge = sourceListEdges.add(subimageEdgesSource);
//			destinationListEdges.add(backgoundEdges);
//			System.out.println("subimage to SourceListEdged = " + y + " from " + imageOriginal.getHeight() + " added  = " + addedToEdge);
			y += size;
		}
		
		System.out.println("All subimages separated !");
	}
	
//renderOneStrip method but for edges(){} at each thread of subimage, deploy two thread, one for the edghe method one for the method below
	
	public void edgeSourceToGrayScale() {
		ArrayList<Thread> threads = new ArrayList<Thread>();
		int height = imageOriginalEdges.getHeight();
		int width = imageOriginalEdges.getWidth();
		for(int y  = 0; y < height; y++) {
			BufferedImage row = imageOriginalEdges.getSubimage(0, y, imageOriginalEdges.getWidth(), 1);
			rowsListForGrayscaling.add(row);
		}
//		synchronized(rowsListForGrayscaling){
			for(BufferedImage row: rowsListForGrayscaling) {
				Thread t = Thread.ofVirtual().start(() -> {
					for(int x = 0; x < row.getWidth(); x++) {
						int pixel = row.getRGB(x, 0);
						int red = (pixel >> 16) & 0xFF;
						int green = (pixel >> 8) & 0xFF;
						int blue = pixel & 0xFF;
						
						int sum = red + green + blue;
						int avg  = sum/3;
						
						pixel = (avg << 16) | (avg << 8) | avg;
	//				System.out.println("avg color = " + pixel);
						row.setRGB(x, 0, pixel);
					}
				});
				
				threads.add(t);
			}
//		}
		
		for(Thread t : threads) {
			try {
				t.join();
			}catch(InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		int y = 0;
		for(BufferedImage grayRow : rowsListForGrayscaling) {
			imageOriginalEdges.getGraphics().drawImage(grayRow, 0, y, null);
//		System.out.println("grayscale row = " + y);
			y += 1;
		}
		try {
			String sep = File.separator;
			File gray = new File(directory + sep + "grayBeforeSeparating.png");
			ImageIO.write(imageOriginalEdges, "png", gray);
		}catch(IOException ex) {
			System.out.println("Save of grayscale not fucking successful");
		}
	}
	
//	public double getGradient(int size, int xStart, BufferedImage subimg) {
//		
//		int index = sourceList.indexOf(subimg);
//		
//		subimg = sourceListEdges.get(index);
//		
//		int sumX = 0;
//		int sumY = 0;
//		
//		for(int rows = 0; rows < size; rows++) {
//			
//			int pixel0X = subimg.getRGB(xStart, rows);
//			int pixel1X = subimg.getRGB(xStart + size - 1, rows);
//			
//			int pixel2Y = subimg.getRGB(xStart + rows, 0);
//			int pixel3Y = subimg.getRGB(xStart + rows, size - 1);
//			
//			int col0X = pixel0X & 0xFF;
//			int col1X = pixel1X & 0xFF;
//			
//			int col2Y = pixel2Y & 0xFF;
//			int col3Y = pixel3Y & 0xFF;
//			
//			int oneDiffX = col1X - col0X;
//			int oneDiffY = col3Y - col2Y;
//			
//			sumX += oneDiffX;
//			sumY += oneDiffY;
//			
//		}
//		
//		double avgDiffX = ((double) sumX)/ 10;
//		double avgDiffY = ((double) sumY) /10;
//		
//		double dist = Math.sqrt(Math.pow(avgDiffX, 2) + Math.pow(avgDiffY, 2));
//		
//		
//		
//		return 0;
//	}
	

	/**
	 * Takes in one strip from the sourList of simplified strips as an argument, then with index() method estimates the character key for the hashma
	 * of characters. Then based on which index this parameter is, the respective strip for edge detection is gotten from the edge source list to estimate if there is in fact an edge
	 * so if there really is an edge then instead of a char value from the hash map, the respective edge angle symbol is assigned to that location
	 * Also the indexOf method is used to get the right dummy strip from the destination list and the respective edges and nonedges are infact written onto that object, this way the
	 * order of the strips remains correct, so it can be easily assembled just by iterating over the destination list. It seems to me now, that there was no need to make that
	 * list also synchronized, because BufferedImage is not thread safe so even if the destination list is, it still would have problems with concurrency.
	 * Also this may be the reason why I was not able to just state the index of an element and add it like to he destination list for example if the list is empty, like
	 * the need for dummy strips how it seems to me is that based on number of elements the capacity of it grows, so if there are no elements and the first strip the program
	 * finishes is the 300th thread it is way outside of the capacity even if it is at least the number of elements. So If I am not mistaken, then if the capacity is based on the
	 * number of elements being 75% of the potential capacity then even if therea are 10 elements in the list, the 300th element is way out of that capacity
	 * @param subimg
	 * @param indexOf
	 */
	
	public void renderOneStrip(BufferedImage subimg,int indexOf) {
		BufferedImage subimgOut = destinationList.get(indexOf);
		Graphics gOut = subimgOut.getGraphics();
		Graphics2D g2dOut = (Graphics2D) gOut;
		
		Rectangle2D.Double bckgrnd = new Rectangle2D.Double(0, 0, subimg.getWidth(), subimg.getHeight());
		g2dOut.setColor(new Color(0, 0, 0));
		g2dOut.fill(bckgrnd);
		 
		for(int x = 0; x < subimg.getWidth(); x += size) {
			
//			System.out.println("render strip nr = " + x);
			
			String letter = "";
			
			int pixel  = subimg.getRGB(x, 0);
			int index = index(pixel);
			
//			int indexGray = sourceList.indexOf(subimg);
		
	//		synchronized(sourceListEdges) {
			
		subimg = sourceListEdges.get(indexOf);
		
//		System.out.println("indexOf current strip = " +  indexOf);
		
		int sumX = 0;
		int sumY = 0;
		
		for(int rows = 0; rows < size; rows++) {
			
			int pixel0X = subimg.getRGB(x, rows);
			int pixel1X = subimg.getRGB(x + size - 1, rows);
			
			int pixel2Y = subimg.getRGB(x + rows, 0);
			int pixel3Y = subimg.getRGB(x + rows, size - 1);
			
			int col0X = pixel0X & 0xFF;
			int col1X = pixel1X & 0xFF;
			
			int col2Y = pixel2Y & 0xFF;
			int col3Y = pixel3Y & 0xFF;
			
			int oneDiffX = col1X - col0X;
			int oneDiffY = col3Y - col2Y;
			
			sumX += oneDiffX;
			sumY += oneDiffY;
			
		}
		
		double avgDiffX = ((double) sumX)/ 10;
		double avgDiffY = ((double) sumY) /10;
		
		double dist = Math.sqrt(Math.pow(avgDiffX, 2) + Math.pow(avgDiffY, 2));
		
		
		
		if(dist > 20) {
		
		
			
			double arg = avgDiffY/avgDiffX;
			
			double angle  = Math.atan(arg);
//			double angle  = Math.atan2(avgDiffY, avgDiffX);	
			angle  = angle * (180 / Math.PI);
			
	//		System.out.println("dist > 20 angle  = " + angle);
			
			if((angle <= 90 && angle >= 75) || (angle >= -90 && angle <= -75)) {
				letter = emdash;
				System.out.println("angle between -20 and 20 = " + angle);
			}
			if(angle > 15 && angle < 75) {
				letter = "/";
			}
			if(angle <= 15 && angle >= -15) {
			letter = "|";
			}
			if(angle < -15 && angle > -75) {
				letter = "\\";
			}
				g2dOut.setFont(font);
				g2dOut.setColor(Color.WHITE);
				FontMetrics fm = g2dOut.getFontMetrics();
				int base = fm.getAscent();
				g2dOut.drawString(letter, x, base);
			
			
		}else {
		
			synchronized(letters) {
				letter = letters.get(index);


			}
			
				g2dOut.setFont(font);
				g2dOut.setColor(Color.WHITE);
				FontMetrics fm = g2dOut.getFontMetrics();
				int base = fm.getAscent();
				g2dOut.drawString(letter, x, base);
		}
	//		}// edge or letter retrieve synchronized block

				
//				System.out.println("The letter = " + letter);
	}//loop for actual pixels

	}
	
	public void renderImage(String newName) {
		
		//synchronized(sourceList) {
			
	//	}
		//}
			int c = 0;
			for(int i = 0; i < sourceList.size(); i++) {
				
				final int ind = i;
				final BufferedImage subimg = sourceList.get(i);
				int indexOf = sourceList.indexOf(subimg);
			Thread oneThread = Thread.ofVirtual().start(() ->{
					
					renderOneStrip(subimg, ind);
					
					
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
			}
			File renderedOUTPUT = new File(directory + File.separator + newName);
		try {
			ImageIO.write(destinationImage, "png", renderedOUTPUT);

		}catch(IOException ex) {
			
		}
			
		
		
	}
	
}
