package pixelcounter;

import java.awt.Color;
import java.awt.image.*;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import java.util.ArrayList;
import java.util.Scanner;

public class PixelCounter
{
    //longs for big images
    protected long numWhites;
    protected long numReds;
    protected long numBlues;
    protected long numGreens;
    
    ArrayList<Thread> threads;
    
    protected int numthreads;
    protected BufferedImage myimage;
    //protected File f;
    protected JFileChooser jf;
    protected ImageIO imgio;
    
    long before,now;
    int color;
    int x,y;
    int red,white,blue,green,alpha;
    
    public PixelCounter(int nthreads)
    {
        numthreads = 0;
        numWhites = 0;
        numReds = 0;
        numBlues = 0;
        numGreens = 0;
    
        threads = new ArrayList<>();
        
        System.out.println("Choose an image to be uploaded:");
        jf = new JFileChooser(".");
        if( jf.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
            System.exit(0);
        try
        {
            myimage = ImageIO.read(jf.getSelectedFile());
            System.out.println("Upload Complete");
        }
        catch(IOException e)
        {
            System.err.printf("Error importing "+e);
        }
    }
    
    protected void createThreads(int nthreads)
    {
        System.out.println("creating threads");
        int i;
        for(i=0;i<nthreads;++i)
        {
            Thread t = new Thread(()->{});
            t.start();
            this.threads.add(t);
        }
		for(Thread t:this.threads)
        {
            try
            {
                t.join();
            }
            catch(InterruptedException e)
            {
                System.exit(0);
            }
        }
        System.out.println("done creating threads");
    }
    
    protected void destroyThreads()
    {
        /*
        int i;
        for(i=0;i<threads.size()-1;++i)
        {
            threads.get(i).destroy();
        }*/
    }
    
    public void countWhitePixels()
    {
        before = System.nanoTime();
        
        int width = myimage.getWidth();
        int height = myimage.getHeight();
        
        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        
        int xcounter = 0;
        int ycounter = 0;
        
        while(ycounter < height)
        {   
            xcounter=0;
            while(xcounter < width)
            {
                /**/
                
                color = myimage.getRGB(xcounter, ycounter);
                
                red = (color & 0x00ff0000) >> 16;
                green = (color & 0x0000ff00) >> 8;
                blue = color & 0x000000ff;
                alpha = (color>>24) & 0xff;
                
                //System.out.println("Pixel Color: "+color);
                //System.out.println("Red: "+red);
                //System.out.println("Blue: "+blue);
                //System.out.println("Green: "+green);
                //System.out.println("Alpha: "+alpha);
                
                if(red>255 || blue > 255 || green > 255)
                    System.err.print("Some of the color values are odd");
                else if(red < 0 || blue < 0 || green < 0)
                    System.err.print("Some of the color values are odd");
                //Determine white pixels
                //source: http://www.rapidtables.com/web/color/white-color.htm
                else if(red == 255 && blue == 255 && green == 255)
                    this.numWhites++;
                else if(red == 248 && blue == 248 && green == 255)
                    this.numWhites++;
                else if(red == 255 && blue == 250 && green == 250)
                    this.numWhites++;
                else if(red == 255 && blue == 250 && green == 240)
                    this.numWhites++;
                else if(red >= 241 && blue >= 241 && green >= 241)
                    this.numWhites++;
                
                /**/
                
                //System.out.println("Current x "+xcounter);
                xcounter++;
            }
            
            //System.out.println("********************");
            //System.out.println("Current y "+ycounter);
            //System.out.println("********************");
            ycounter++;
        }
        
        long ntime = System.nanoTime();
        now = Math.abs(ntime-before);

        double milliseconds = now*0.000001;
        
        System.out.println("NanoSeconds: "+now);
        System.out.println("MilliSeconds: "+milliseconds);
        float speedup = (float)(milliseconds/1);
        System.out.println("Number of white Pixels: "+this.numWhites);
        
        //this.destroyThreads();
    }
    public void countRedPixels()
    {
        red = (color & 0x00ff0000) >> 16;
    }
    public void countBluePixels()
    {
        blue = color & 0x000000ff;
    }
    public void countGreenPixels()
    {
        green = (color & 0x0000ff00) >> 8;
    }
    
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of threads to be created:");
        int asd = sc.nextInt();
        
        PixelCounter px = new PixelCounter(asd);
        px.createThreads(asd);
        px.countWhitePixels();
    }
}
