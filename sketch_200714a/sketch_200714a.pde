import java.lang.reflect.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
int rows = 568/4,cols = 600/4,counter = cols*rows-1;
float ma = 0,mean = 0,variance;
int minr = 1,minwr = 1,maxr = 1000000,maxwr = 1000000,W = 1200,H = 600,edgeLength;
String []functions ;
PImage pimg;
Img img;
boolean update;
//String[]{sobelMax
//String []wf = {"gaussianS(1.0,20)","sobelSP(40.0,150.0)"};
//String []wf = {"gaussianS(1.0,20)","canny(4,150.0,50)"};
//String []wf = {"mean(4)","variance3(3)"};
//String []wf = {"gaussianS(1.0,10)","mean(1.0,4)"};
//String []wf = {"sobelGS(5.0,1,2.0)","canny(5,150.0,50)"};
//String []wf = {"canny(4,150.0,50)"};
//String []wf = {"img.cell.superPixel(3)","sobelS(5.0)","canny(4,130.0,50)"};
//String []wf = {"sobelS(3.0,0)"};
//String []wf = {"canny2(4,150.0,50)"};
String []wf = {"gaussianS(2.0,5)","sobel(10.0)","sobelMax2(255.0)","canny(5,150.0,50)"};
String imPath;
String shaderPath;
cell cell;
void settings(){
  size(W,H,P2D);
}
void setup(){
  //String imPath = dataPath("images")+"\\";
  String imAndroidPath = dataPath("images").replace("/data/","")+"/";
  String shaderPath = dataPath("shaders").replace("/data/","")+"/";
  img = new Img(imAndroidPath+"b3.jpg");
  //cell = new cell(imPath+"\\"+"car.jpg");
  //cell.getContour();
  //colorMode(HSB);
  //img.cell.superPixel(3);
};
int x =0,y =0;
int count = 0;
void draw(){
  background(50);
  logic();
  //
  //if(count==0)img.cell.superPixel(10);
  //count++;
  
  
  
  
  fill(0);
  img.displayWF(wf);
  //if(count==0)img.sobelMax2(250.0);
  //count++;
  //if(mousePressed)image(img.cell.pImage,0,0);
  //else image(img.img,0,0);
  //image(img.img,0,0);
  
  //text(frameRate,10,10);
  //text(edgeLength,10,20);
};

void logic(){
  if(pmouseX!=mouseX||pmouseY!=mouseY){
    if(img.img.width>width&&mouseX>0)
    x = (int)map(mouseX,0,img.img.width,0,width);
    if(img.img.height>height&&mouseY>0)
    y = (int)map(mouseY,0,height,0,img.img.height);
  }
  float a = 0,b=0;
  if(mouseX>0) a = map(mouseX,0,width,0,1);
  if(mouseY>0) b = map(mouseY,0,height,0,1);
  if(mouseX>0) edgeLength = (int)map(mouseX,0,width,0,10);
  img.s_mult = a;
  if(pmouseX!=mouseX||pmouseY!=mouseY)update = true;
  else update = false;
  //if(img.cell.canny!=null)image(img.cell.canny,0,0);
  //if(img.cell!=null)println(img.cell.canny);
  //pricellln(img.cell.contours.size());
  //fill(255,0,0);
  //text(frameRate,10,10);
};
