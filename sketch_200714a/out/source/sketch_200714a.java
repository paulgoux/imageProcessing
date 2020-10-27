import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_200714a extends PApplet {

ArrayList <cell> queue = new ArrayList<cell>();
ArrayList <cell> temp = new ArrayList<cell>();
ArrayList <cell> temp2 = new ArrayList<cell>();
ArrayList <cell> temp3 = new ArrayList<cell>();

int rows = 568/4,cols = 600/4,counter = cols*rows-1;
float ma = 0,mean = 0,variance;
int minr = 1,minwr = 1,maxr = 1000000,maxwr = 1000000,W = 1200,H = 600;
float cutoff = 5,mult = 1,offset = 00,mult2 = 15;

PImage pimg;
Img img;
cell tissue;

public void settings(){
  size(W,H);
}
public void setup(){
  
  pimg = load("btfly.jpg");
  img = new Img("s1.jpg");
  //img.blur(1);
  img.mean(5,70);
  //println("mean ");
  // img.variance(2);
  //img.threshold(20,img.mean);
  //mult = img.Variance/3;
  //mult2 = mult*0.7;
  
  img.sobel(0,2);
  //img.variance(1,img.sobel);
  img.sobel2(1,15);
  //img.sobelMax(img.sobel,100);
  //img.threshold(10,img.sobel);
  
  img.combine(img.sobel,img.sobel2);
  img.variance(1,img.combined);
  //img.sobelMax(img.sobel,50);
  //img.sobel(img.sobelMax);
  //img.sobelMin();
  
  //img.threshold(5,img.sobel);
  //img.variance(10);
  
  rows = img.img.height;
  cols = img.img.width;
  //rows = 100;
  //cols = 100;
  //tissue = new cell(img.sobel,cols,rows);
  //tissue.getNeighbours();
  //tissue.findSpaces();
  //tissue.findWalls();
  //tissue.findEdges();
  //tissue.sortEdges();
  //tissue.trimEdges();
  
};
int x =0,y =0;
public void draw(){
  background(50);
  //tissue.getNeighbours();
  //tissue.draw();
  //tissue.drawCells();
  //tissue.drawEdges();
  //if(mousePressed)tissue.drawWalls();
  //tissue.drawRegions();
  
  //text(tissue.ry,10,20);
  //image(tissue.backup,0,0);
  if(pmouseX!=mouseX||pmouseY!=mouseY){
    if(img.img.width>width&&mouseX>0)
    x = (int)map(mouseX,0,img.img.width,0,width);;
    if(img.img.height>height&&mouseY>0)
    y = (int)map(mouseY,0,height,0,img.img.height);
    ;
  }
  //image(img.threshold,-x,-y);
  //image(img.variance,-x,-y);
  if(mousePressed)image(img.combined,-x,-y);
  //else image(img.sobelMax,-x,-y);
  //image(img.sobelMin,-x,-y);
  //image(img.mean,-x,-y);
  //else image(img.blur,-x,-y);
  //else image(img.sobel2,-x,-y);
  //else image(img.combined,-x,-y);
  else image(img.variance,-x,-y);
  //image(pimg,0,0);
  //image(img.sobelGradient,-x,-y);
  //fill(255);
  //rect(x,y,width,height);
  fill(0);
  //if(mousePressed)fill(255);
  text(frameRate,10,10);
  
  text(x + " " + y,10,20);
};
class cell {
  float x, y, h, res, w, ry, rows_, cols_;
  int id, xpos, ypos, walls, parent = -1, chainid=-1, counter, cols, rows;
  boolean visited, wall, link, edge, border, v1, v2, v3, v4;
  ArrayList <cell> cells;
  ArrayList <cell> neighbours = new ArrayList<cell>();
  ArrayList <cell> neighbours2 = new ArrayList<cell>();
  ArrayList< ArrayList<cell>> regions = new ArrayList<ArrayList<cell>>();
  ArrayList< ArrayList<cell>> wallRegions = new ArrayList<ArrayList<cell>>();
  ArrayList< ArrayList<cell>> sortedEdges = new ArrayList<ArrayList<cell>>();
  ArrayList< ArrayList<cell>> unsortedEdges = new ArrayList<ArrayList<cell>>();
  ArrayList< ArrayList<cell>> trimmedEdges = new ArrayList<ArrayList<cell>>();

  ArrayList<cell> region = new ArrayList<cell>();
  ArrayList<cell> edges = new ArrayList<cell>();
  ArrayList <Boolean> wallFlags = new ArrayList<Boolean>();
  //ArrayList <int> wallFlags2 = new ArrayList<int>();
  //ArrayList <Boolean> vertices = new ArrayList<Boolean>();
  
  PVector [] vertices = new PVector[16];
  int col = color(random(255), random(255), random(255));
  
  PImage img,backup;

  cell(int a, int b) {
    //this.img = img; 
    cols = a;
    rows = b;
    rows_ = PApplet.parseFloat(b);
    cells = new ArrayList<cell>();
    w = img.width;
    h = img.height;
    res = img.width/cols;
    ry = img.height/100.0f;
    counter = rows * cols -1;
    backup = new PImage(cols,rows,RGB);
    float n = map(cutoff, 0, 100, 0, 255);
    
    backup.loadPixels();
    for (int i=0; i<cols; i++) {
      for (int j=0; j<rows; j++) {
        int p = j + i * cols;
        float h = floor(random(100));
        cell c = new cell(p, res*i, ry*j, i, j, h, this);
        if (h>cutoff)c.wall = true;
        else c.wall = false;
        cells.add(c);
        //backup[p] = color(
      }
    }
    backup.updatePixels();
  };
  
  cell(PImage img, int a, int b) {
    this.img = img; 
    cols = a;
    rows = b;
    //cols = img.width;
    //rows = img.height;
    rows_ = PApplet.parseFloat(rows);
    cols_ = PApplet.parseFloat(cols);
    cells = new ArrayList<cell>();
    w = width;
    h = H;
    res = img.width/cols;
    //res = 1;
    ry = img.height/rows;
    //ry = 1;
    counter = rows * cols -1;
    backup = new PImage(cols,rows,RGB);
    float n = map(cutoff, 0, 100, 0, 255);
    
    backup.loadPixels();
    for (int j=0; j<rows; j++) {
      for (int i=0; i<cols; i++) {
        int p = PApplet.parseInt(i*res + j*ry * img.width);
          if (p<img.pixels.length) {
            float r = red(img.pixels[p]);
            float g = green(img.pixels[p]);
            float bb = blue(img.pixels[p]);
            float br = brightness(img.pixels[p]);
            float h = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
            //println(r);
            cell c = new cell(i+j*cols, img.width/cols*i, img.height/rows*j, i, j, h, this);
            //if (r<cutoff)c.wall = true;
            //else c.wall = false;
            cells.add(c);
            int p2 = PApplet.parseInt(i + j * cols);
            backup.pixels[p2] = color(h);
        }
      }
    }
    
  }

  //cell(PImage img, int a, int b) {
  //  this.img = img; 
  //  cols = a;
  //  rows = b;
  //  //cols = img.width;
  //  //rows = img.height;
  //  rows_ = float(rows);
  //  cols_ = float(cols);
  //  cells = new ArrayList<cell>();
  //  w = width;
  //  h = H;
  //  res = img.width/cols;
  //  //res = 1;
  //  ry = img.height/rows;
  //  //ry = 1;
  //  counter = rows * cols -1;

    
  //  mean = 0;
  //  for (int j=0; j<rows; j++) {
  //    for (int i=0; i<cols; i++) {
  //      int p = int(i*res + j*ry * img.width);
  //        if (p<img.pixels.length) {
  //          float r = red(img.pixels[p]);
  //          float g = green(img.pixels[p]);
  //          float bb = blue(img.pixels[p]);
  //          float br = brightness(img.pixels[p]);
  //          float h = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
  //          //h = brightness(img.pixels[p]);
  //          //mean += color(img.pixels[p]);
  //          mean += h;
  
  //          cell c = new cell(i+j*cols, img.width/cols*i, img.height/rows*j, i, j, h, this);
  //        cells.add(c);
  //      }
  //    }
  //  }
    
  //  mean /= cells.size();
  //  //mean +=-5;
  //  //mean = sqrt(mean);
  //  if(mean<0)mean = -mean;
  //  float n = map(mean, 0, 100, 0, 255);
  //  for (int i=0; i<cells.size(); i++) {
            
  
  //          cell c = cells.get(i);
            
  //          if (c.h>n) {
  //            c.wall = true;
  //            c.col = (0);
  //          } else {
  //            c.wall = false;
  //            c.col = (255);
  //          }
  //  }
  //  println("mean " + mean);
  //  println("n " + n);
  //};


  cell(int id, float x, float y, int xpos, int ypos, float h, cell c) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.h = h;
    this.xpos = xpos;
    this.ypos = ypos;
    res = c.res;
    ry = c.ry;
    cols = c.cols;
    rows = c.rows;
    //for(int i=0;i<4;i++){
    //  wallFlags.add(true);
    //}
    //vertices[0] = null;
    //vertices[1] = new PVector(x,y+ry/2,x+res/2,y+ry);
    //vertices[2] = new PVector(x+res/2,y,x+res/2,y);
    //vertices[3] = new PVector(x,y+ry/2,x+res,y+ry/2);
    //vertices[4] = new PVector(x+res/2,y,x+res/2,y+ry/2);
    //vertices[5] = new PVector(x,y+ry/2,x+res/2,y);
    //vertices[6] = new PVector(x+res/2,y,x+res/2,y+ry);
    //vertices[7] = new PVector(x,y+ry/2,x+res/2,y);
    //vertices[8] = new PVector(x,y+ry/2,x+res/2,y);
    //vertices[9] = new PVector(x+res/2,y,x+res/2,y+ry);
    //vertices[10] = new PVector(x+res/2,y+ry/2,x+res/2,y);
    //vertices[11] = new PVector(x+res/2,y,x+res,y+ry/2);
    //vertices[12] = new PVector(x,y+ry/2,x+res,y+ry/2);
    //vertices[13] = new PVector(x+res/2,y,x+res,y+ry/2);
    //vertices[14] = new PVector(x,y+ry/2,x+res/2,y+ry/2);
    //vertices[15] = null;
  };

  public void connectWalls() {
  };

  public void marchingSquares() {
    //for(int i=0;i<wallFlags2.size();i++){

    //}
  };

  public void getNeighbours() {

    for (int k=0; k<cells.size(); k++) {
      cell c = cells.get(k);
      for (int i=c.xpos-1; i<=c.xpos+1; i++) {
        for (int j=c.ypos-1; j<=c.ypos+1; j++) {
          int p = i+j * cols;
          if (j>=0&&j<rows&&i>=0&&i<cols&&p<cells.size()) {
            cell c2 = cells.get(p);
            if (c2!=c) {

              if ((c.xpos==c2.xpos||c.ypos==c2.ypos)) {
                if (!c.neighbours.contains(c2))c.neighbours.add(c2);
              }
              if (!c.neighbours2.contains(c2))c.neighbours2.add(c2);
            }
          } else {
            c.neighbours2.add(null);
            if ((c.xpos==i||c.ypos==j))c.neighbours.add(null);
          }
        }
      }
    }
  };

  public void findSpaces() {
    cell c = null;
    while (counter>=0) {
      c = cells.get(counter);
      queue = new ArrayList<cell>();

      if (!c.visited&&!c.wall) {
        queue.add(c);
        temp = new ArrayList<cell>();
        temp2 = new ArrayList<cell>();
        while (queue.size()>0) {
          c = queue.get(0);
          int w = 0;
          if (!temp2.contains(c)) {
            temp.add(c);
            temp2.add(c);
          }
          if (!c.visited&&!c.wall) {
            c.visited = true;

            for (int i=0; i<c.neighbours.size(); i++) {

              cell c1 = c.neighbours.get(i);
              if (c1!=null) {
                if (c1.wall) {
                  w++;
                } else if (!queue.contains(c1)&&!c1.visited)queue.add(c1);
              } else w++;
            }
          }
          c.walls = w;
          if (temp.size()>0) {
            int count = 0;
            for (int i=0; i<regions.size(); i++) {
              //for(int j=0;j<temp.size();j++){
              if (regions.get(i).contains(temp2.get(0)))count ++;
              break;
            }
            if (count==0) {
              regions.add(temp);
              c.region = temp;
            }
          }
          if (queue.size()>=1) {
            c = queue.remove(0);
          }
        }
      } else counter --;
    }
    println("step 1");
    println("wall regions " + wallRegions.size());
    println("regions " + regions.size());

    for (int i=0; i<regions.size(); i++) {
      if (regions.get(i).size()<minr||regions.get(i).size()>maxr) {
        //println("remove regions " + regions.get(i).size());
        for (int j=0; j<regions.get(i).size(); j++) {
          cell c1 = regions.get(i).get(j);
          c1.visited = false;
          c1.wall = true;
        }
      }
      //else println("inner regions " + regions.get(i).size());
    }
    
  };

  public void findWalls() {
    regions = new ArrayList<ArrayList<cell>>();
    cell c = null;
    counter = rows * cols - 1;
    while (counter>=0) {
      
      c = cells.get(counter);
      queue = new ArrayList<cell>();

      if (!c.v1&&c.wall) {
        
        queue.add(c);
        temp = new ArrayList<cell>();
        temp2 = new ArrayList<cell>();
        while (queue.size()>0) {
          
          c = queue.get(0);
          int w = 0;
          if (!temp2.contains(c)) {
            temp.add(c);
            temp2.add(c);
          }
          if (!c.v1) {
            c.v1 = true;

            for (int i=0; i<c.neighbours.size(); i++) {

              cell c1 = c.neighbours.get(i);
              if (c1!=null) {
                if (!c1.wall) {
                  w++;
                } else {
                  if (!temp2.contains(c1)) {
                    queue.add(c1);
                  }
                }
              } else w++;
            }
          } else if (!c.wall)c.v1 = true;

          c.walls = w;
          if (temp.size()>0) {

            int count = 0;
            for (int i=0; i<wallRegions.size(); i++) {
              //for(int j=0;j<temp2.size();j++){
              if (wallRegions.get(i).contains(temp2.get(0))) {
                count ++;
                break;
              }
            }
            if (count==0) {
              wallRegions.add(temp);
              c.region = temp;
            }
          }
          if (queue.size()>=1) {
            c = queue.remove(0);
          }
        }
      } else counter --;
    }

    for (int i=wallRegions.size()-1; i>-1; i--) {
      if (wallRegions.get(i).size()<minwr||wallRegions.get(i).size()>maxwr) {

        for (int j=0; j<wallRegions.get(i).size(); j++) {
          cell c1 = wallRegions.get(i).get(j);
          c1.visited = false;
          c1.wall = false;
        }
        wallRegions.remove(i);
      }
      //else println("wall inner regions " + wallRegions.get(i).size());
    }
    println("step 2");
    println("wall regions " + wallRegions.size());
    println("regions " + regions.size());
    //regions = new ArrayList<ArrayList<cell>>();
  };

  public void findEdges() {


    cell c = null;
    counter = cols * rows -1;
    while (counter>=0) {
      c = cells.get(counter);
      queue = new ArrayList<cell>();

      if (!c.v2&&!c.wall) {
        queue.add(c);
        temp = new ArrayList<cell>();
        temp2 = new ArrayList<cell>();
        temp3 = new ArrayList<cell>();
        while (queue.size()>0) {
          c = queue.get(0);
          int w = 0;
          temp.add(c);
          temp3.add(c);
          if (!c.v2) {
            c.v2 = true;

            for (int i=0; i<c.neighbours.size(); i++) {

              cell c1 = c.neighbours.get(i);
              if (c1!=null) {
                if (c1.wall) {
                  w++;
                } else if (!temp3.contains(c1)&&!c1.v2)queue.add(c1);
              } else w++;
            }
            c.walls = w;
            if (w>0) {
              c.edge = true;
              if (!temp2.contains(c))temp2.add(c);
            }
          } else if (c.wall)c.v2 = true;
          if (queue.size()>0) {
            c = queue.remove(0);
          }
        }

        if (temp.size()>0) {

          int count = 0;
          for (int i=0; i<regions.size(); i++) {
            //for(int j=0;j<temp3.size();j++){
            if (regions.get(i).contains(temp3.get(0))) {
              count ++;
              break;
            }
          }
          if (count==0) {

            for (int i=temp2.size()-1; i>-1; i--) {
              if (!temp2.get(i).edge)temp2.remove(i);
            }
            unsortedEdges.add(temp2);
            regions.add(temp);
            c.region = temp;
          }
        }
      } else counter --;
    }
    println("step 3");
    println("wall regions " + wallRegions.size());
    println("regions " + regions.size());
  };

  public void sortEdges() {
    cell c = null;
    for (int i=unsortedEdges.size()-1; i>-1; i--) {

      //println("region "+ i + " " + regions.get(i).size());
      if (unsortedEdges.get(i).size()>0) {
        //println("adding edge " + unsortedEdges.get(i).size() +" " + unsortedEdges.get(i).size());
        for (int j=0; j<unsortedEdges.get(i).size(); j++) {
          c = unsortedEdges.get(i).get(j);
          c.visited = false;
          c.edge = true;
        }
      } else unsortedEdges.remove(i);
    }
    //regions = new ArrayList<ArrayList<cell>>();
    for (int i=0; i<unsortedEdges.size(); i++) {
      c = null;
      //println("Unsorted size " + unsortedEdges.get(i).size());
      while (unsortedEdges.get(i).size()>0) {
        c = unsortedEdges.get(i).remove(0);
        queue = new ArrayList<cell>();
        temp = new ArrayList<cell>();
        temp2 = new ArrayList<cell>();
        if (!c.v4&&c.edge) {
          queue.add(c);
          temp2.add(c);
          //c.connect();
          while (queue.size()>0) {
            c = queue.get(0);
            int w = 0;
            if (!temp.contains(c))temp.add(c);
            if (!c.v4) {
              c.v4 = true;
              for (int j=0; j<c.neighbours2.size(); j++) {

                cell c1 = c.neighbours2.get(j);
                if (c1!=null) {
                  if (c1.wall) {
                    w++;
                  }
                  if (c1.edge&&!c1.v4)queue.add(c1);
                } else w++;
              }
              c.walls = w;
              if (w>0)c.edge = true;
            } else if (!c.edge)c.v4 = true;
            if (temp.size()>0) {

              int count = 0;
              for (int j=0; j<sortedEdges.size(); j++) {

                if (sortedEdges.get(j).contains(temp.get(0))) {
                  count ++ ;
                  break;
                }
              }
              if (count==0) {
                sortedEdges.add(temp);
                c.edges = temp;
              }
            }
            if (queue.size()>=1) {
              c = queue.remove(0);
            }
          }
        }
        if (!c.edge)c.v4 = true;
      }
    }
    println("step 4");
    println("sorted " + sortedEdges.size());
    println("wall regions " + wallRegions.size());
    println("regions " + regions.size());
    println("Begin");
  };

  public void draw() {

    fill(0, 0, 255);
    rect(0, 0, img.width, img.height);
    fill(0);
  };

  public void drawRegions() {
    fill(0);
    text("regions " + regions.size(),110,20);
    for(int i=0;i<regions.size();i++){
      text(regions.get(i).size(),110,30+10*i);
    }
    for (int i=0; i<regions.size(); i++) {
      //text(regions.size(),40,10);
      for (int j=0; j<regions.get(i).size(); j++) {
        cell c = regions.get(i).get(j);
        c.display();
        //c.debug();
        //text(i,c.x + res/2,c.y + ry/2);
      }
    }
  };

  public void drawWalls() {
    //fill(0);
    //text("wall Regions " + wallRegions.size(),190,20);
    for (int i=0; i<wallRegions.size(); i++) {
      //text(wallRegions.get(i).size(),190,30+10*i);
      for (int j=0; j<wallRegions.get(i).size(); j++) {
        cell c = wallRegions.get(i).get(j);
        c.display();
      }
    }
  };

  public void drawCells() {
    for (int i=0; i<cells.size(); i++) {
      cell c = cells.get(i);
      //println(str(c.wall));
      //if(mousePressed)
      //if(mousePressed)c.display2();
      c.display();
      //c.debug();
      //text(c.xpos + " " + c.ypos, c.x + res/2, c.y + ry/2);
      //text(i, c.x + res/2, c.y + ry);
      //if(c.edge&&!c.wall)c.display3();
    }
  };

  public void drawEdges() {
    fill(0);
    if (mousePressed)fill(255);
    text("sorted Edges " + sortedEdges.size(), 100, 20);

    //for (int i=0; i<sortedEdges.size(); i++) {
    //  //println(sortedEdges.get(i).size());
    //  text(sortedEdges.get(i).size(), 10+40, 30+10*i);
    //}
    //int p = floor(map(mouseX,0,width,0,sortedEdges.size()));
    //text(p,200,100);
    for (int i=0; i<sortedEdges.size(); i++) {
      for (int j=0; j<sortedEdges.get(i).size(); j++) {
        cell c = sortedEdges.get(i).get(j);
        //if(p==i)
        c.display();
        //c.debug();

        //if(c.edges!=null)c.display2();
        //text(j,c.x + r/2,c.y + ry/2);
      }
    }
  };

  public void drawTrimmedEdges(){

    for(int i=0;i<sortedEdges.size();i++){
      if(sortedEdges.get(i).size()>2)
      for (int j=0; j<sortedEdges.get(i).size()-1; j++) {
      cell c = sortedEdges.get(i).get(j);
      cell c1 = null;
      if(j<sortedEdges.get(i).size())c1 = sortedEdges.get(i).get(j+1);
      else c1 = sortedEdges.get(i).get(0);
      stroke(0);
      strokeWeight(1);
      line(c.x+c.w,c.y+c.h/2,c1.x+c1.w,c1.y+c1.h/2);
    }}
  };
  
  public void trimEdges(){
    for(int i=0;i<sortedEdges.size();i++){
      ArrayList<cell> A = sortedEdges.get(i);
      if(A.size()>2){
        for(int j=0;j<A.size();j++){
          
          cell a = A.get(j);
          cell b = null;
          cell c = null;
          
          if(j+2<A.size()){
          b = A.get(j+1);
          c = A.get(j+2);
          
          if(((b.x==a.x)&&(c.x==a.x)||b.y==a.y&&(c.y==a.y))){
            A.remove(j+1);
            j--;
            
          }else{
          }}else if(j+1<A.size()){
            b = A.get(j+1);
      }}
      
    }}
  };
  
  public void drawNeighbours() {
    //if(pos()){
    for (int i =0; i<neighbours.size(); i++) {
      cell c = neighbours.get(i);
      if (c!=null)c.display3();
    }
  };

  public void display() {
    //ry = 6.6;
    if (!wall) {

      noStroke();

      fill(0, 0, 255);
      if (edge&&mousePressed)fill(255, 0, 255);
      //if(link)fill(0,0,0);
      rect(x, y, res, ry);
    } else {
      noStroke();

      fill(0);
      //if(edges.size()>0)fill(255,0,0);
      rect(x, y, res, ry);
    }
    //stroke(0);
    //drawWalls();
  };

  public void display2() {
    fill(0);
    rect(x, y, res, ry);
  };

  public void display3() {
    stroke(0);
    noFill();
    rect(x, y, res, ry);
  };

  public boolean pos(){
    return mouseX>x&&mouseX<x+res&&mouseY>y&&mouseY<y+ry;
  };

  public void debug() {
    noStroke();
    
    fill(0);
    text(neighbours.size(),x,y+ry/2);
    
    if (mouseX>x&&mouseX<x+res&&mouseY>y&&mouseY<y+ry) {
      fill(0,0,255);
      rect(x,y,res,ry);
      for (int i=0; i<neighbours.size(); i++) {
        cell c = neighbours.get(i);
        fill(255, 0, 0, 100);
        if (c!=null) {
          fill(255, 0, 0);
          rect(c.x, c.y, res, ry);
        }
      }
    }
  };

  //void drawWalls(){
  //  stroke(0);
  //  strokeWeight(1);
  //  //if(wallFlags.size()>0)println(wallFlags.size());
  //  for(int i=0;i<wallFlags.size();i++){

  //     boolean b = wallFlags.get(i);
  //     cell c = neighbours.get(i);
  //     if(!b&&edge){
  //         text(chainid,x + res/2,y +ry/2);
  //         if(c.xpos<xpos){

  //           //line(x,y,x,y+ry);
  //           //vertex(x,y);
  //           //vertex(x,y+ry);
  //         }
  //         if(c.xpos>xpos){
  //           //line(x+r,y,x+r,y+ry);
  //           //vertex(x+r,y);
  //           //vertex(x+r,y+ry);
  //         }
  //         if(c.ypos<ypos){
  //           //line(x,y,x+r,y);
  //           //vertex(x,y);
  //           //vertex(x+r,y);
  //         }
  //         if(c.ypos>ypos){
  //           line(x,y+ry,x+res,y+ry);
  //           //vertex(x,y+ry);
  //           //vertex(x+r,y+ry);
  //         }

  //     }
  //  }
  //};


  public void connect() {
    stroke(0);
    strokeWeight(1);
    fill(0);
    //if(region.size()>0)text(region.size(),x + r/2,y+res/2);
    //rect(x,y,res,ry);

    for (int i=0; i<region.size(); i++) {
      cell c = region.get(i);
      //c.col = col;
      c.link = true;
      c.parent = id;
      //if(line(x+res/2,y+res/2,c.x+res/2,c.y+res/2);
    }
  };
};
public PImage load(String s){
  PImage img = loadImage(s);
  img.loadPixels();
  //float mean = 0;
  for(int i=0;i<img.width;i++){
    for(int j=0;j<img.height;j++){
      int p = i + j * img.width;
      //float b = map(brightness(img.pixels[p]),0,255,0,100);
      float b = blue(img.pixels[p]);
      //if(blue(img.pixels[p])>ma) ma = red(img.pixels[p]);
      //mean += brightness(img.pixels[p]);
      mean +=(red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
      //mean += (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
      //println(red(img.pixels[p]));
      //else img.pixels[p] = color(100,100,100,100);
      //if(red(img.pixels[p])<ma) ma = red(img.pixels[p]);
  }}
  mean /= img.pixels.length;
  float r = mean;
  mean = (159 - mean);
  
  println("mean " + mean);
  img.updatePixels();
  //img.loadPixels();
  ////float mean = 0;
  //for(int i=0;i<img.width;i++){
  //  for(int j=0;j<img.height;j++){
  //    int p = i + j * img.width;
  //    //float b = map(brightness(img.pixels[p]),0,255,0,100);
  //    float b = blue(img.pixels[p]);
  //    if(blue(img.pixels[p])>ma) ma = red(img.pixels[p]);
  //    variance += mean * brightness(img.pixels[p]);
  //    //mean += (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
  //    //println(red(img.pixels[p]));
  //    //else img.pixels[p] = color(100,100,100,100);
  //    //if(red(img.pixels[p])<ma) ma = red(img.pixels[p]);
  //}}
  //mean /= img.pixels.length;
  
  
  //println("mean " + variance);
  //img.updatePixels();
  //img.loadPixels();
  for(int i=0;i<img.width;i++){
    for(int j=0;j<img.height;j++){
      int p = i + j * img.width;
      float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
      a1 = brightness(img.pixels[p]);
      float a2 = red(img.pixels[p]);
      float a3 = green(img.pixels[p]);
      float a4 = blue(img.pixels[p]);
      //img.pixels[p] = color((((mean*a1)/(a1-mean) - (mean - a1))*((mean*a1)/(a1-mean) + (mean - a1))*((mean*a1)/(a1-mean) - (mean - a1)))*cutoff);
      //img.pixels[p] = color(sqrt((mean - a1)*(mean - a1)));
      //a1 = (mean - (mean - a1));
      //float a = mean*mean - ((((mean+a1)/(mean-a1)) - (mean - a1))*(((mean+a1)/(mean-a1) - (mean - a1))))-255;
      //float a = mean*mean - (mean - (mean - a1))*(mean - (mean - a1))-255;
      //float a = mean*mean - (mean - (mean - a1))*(mean + (mean - a1))-255;
      //float a = mean*mean - ((mean - a1))*((mean + a1))-255;
      //float a = mean*mean*mean - ((((mean-a1)/(a1 - mean)) - (mean + a1))*(((-mean+a1)/(mean - a1)+ (mean - a1)))*(((-mean+a1)/(mean - a1)- (mean + a1))))-255;
      //variance += mean*mean*mean*mean - ((((mean)) - (-mean - a2))*(((mean)) - (mean - a3))*(((mean)) - (mean - a4))*(((mean)) - (-mean - a1)))-255;
      variance += mean - a1;
      //variance += a;
      //float 
      //if(a<cutoff)
      //img.pixels[p] = color(255-a);
      //else img.pixels[p] = color(0);
      //println("pixel " + color(a1-mean));
      //mean += brightness(img.pixels[p]);
  }}
  //img.updatePixels();
  variance /= img.pixels.length ;
  //variance += 10000;
  //println("variance " + variance);
  img.loadPixels();
  for(int i=0;i<img.width;i++){
    for(int j=0;j<img.height;j++){
      int p = i + j * img.width;
      float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
      //a1 = brightness(img.pixels[p]);
      float a2 = red(img.pixels[p]);
      float a3 = green(img.pixels[p]);
      float a4 = blue(img.pixels[p]);
      //img.pixels[p] = color((((mean*a1)/(a1-mean) - (mean - a1))*((mean*a1)/(a1-mean) + (mean - a1))*((mean*a1)/(a1-mean) - (mean - a1)))*cutoff);
      //img.pixels[p] = color(sqrt((mean - a1)*(mean - a1)));
      //a1 = (mean - (mean - a1));
      //float a = mean*mean - ((((mean+a1)/(mean-a1)) - (mean - a1))*(((mean+a1)/(mean-a1) - (mean - a1))))-255;
      //float a = mean*mean - (mean - (mean - a1))*(mean - (mean - a1))-255;
      //float a = mean*mean - (mean - (mean - a1))*(mean + (mean - a1))-255;
      //float a = mean*mean - ((mean - a1))*((mean + a1))-255;
      //float a = mean*mean*mean - ((((mean-a1)/(a1 - mean)) - (mean + a1))*(((-mean+a1)/(mean - a1)+ (mean - a1)))*(((-mean+a1)/(mean - a1)- (mean + a1))))-255;
      float a = variance*variance*variance*variance - ((((variance+a1)) + (variance - a2))*(((variance)) + (variance + a3))*(((variance)) - (variance + a4))*(((variance)) + (variance + a1)))-255;
      //float 
      //if(a<cutoff)
      img.pixels[p] = color(255-a);
      //else img.pixels[p] = color(0);
      //println("pixel " + color(a1-variance));
      //variance += brightness(img.pixels[p]);
  }}
  img.updatePixels();
  //img.resize(0.5);
  return img;
};
class Img {
  float Mean = 0,Variance,VarianceR,VarianceG,VarianceB,VarianceBR;
  PImage img, mean,mean_,meanG, threshold, variance,varianceR,varianceG,varianceB,varianceBR,
                  kMeans, kNearest,sobel, sobelx, sobely,sobel2, sobel2x, sobel2y, sobelMax,sobelMin,sobelG,gradientB, blur,combined;
  
  int [][]SobelH = {{-1, -2, -1}, 
                    {0, 0, 0}, 
                    {1, 2, 1}};

  int [][]SobelV = {{-1, 0, 1}, 
                    {-2, 0, 2}, 
                    {-1, 0, 1}};
                    
  int [][]SobelH_ = {{-2, -1, 0}, 
                    {-1, 0, 1}, 
                    {0, 1, 2}};

  int [][]SobelV_ = {{0, 1, 2}, 
                    {-1, 0, 1}, 
                    {-2, -1, 0}};
                    
  int [][]edgev = {{-1, -2, -1}, 
                   {0, 0, 0}, 
                   {1, 2, 1}};

  int [][]edgeh = {{-1, 0, 1}, 
                   {-2, 0, 2}, 
                   {-1, 0, 1}};

  int [][]LapLacian = {{0, 1, 0}, 
                      {-1, 4, -1}, 
                      {0, 1, 0}};

  int [][]LapLacianD = {{-1, -1, -1}, 
                        {-1, 8, -1}, 
                        {-1, -1, -1}};
                        
  int [][]edge = {{-1, 1, -1}, 
                  {-1, 0, -1}, 
                  {-1, -1, -1}};
                  
  int [][]meanX = {{1,1,1}, 
                   {0,0,0}, 
                   {1,1,1}};

  int [][]meanY = {{1,1,1}, 
                   {2,0,2}, 
                   {1,0,1}};
                   
  int [][]neighbours;
  float [][]gradient;
  Img(String s) {
    img = loadImage(s);
    neighbours = new int[img.width][img.height];
    gradient = new float[img.width][img.height];
  };

  Img(PImage p) {
    img = p;
  };

  public void threshold(float a) {
    threshold = new PImage(img.width, img.height, RGB);
    img.loadPixels();
    threshold.loadPixels();
    if (mean==null) {
      for (int i=0; i<img.width; i++) {
        for (int j=0; j<img.height; j++) {
          int p = i + j * img.width;
          float b = brightness(img.pixels[p]);
          if (b>a)b = 0;
          threshold.pixels[p] = color(255-b);
        }
      }
    } else {
      //for (int i=0; i<mean.width; i++) {
      //  for (int j=0; j<mean.height; j++) {
      //    int p = i + j * mean.width;
      //    float b = brightness(mean.pixels[p]);
      //    //if (b>a)b = 0;
      //    threshold.pixels[p] = color(b);
      //  }
      //}
    }

    threshold.updatePixels();

    threshold.loadPixels();
    for (int i=0; i<img.pixels.length; i++) {
      float b = brightness(mean.pixels[i]);
      //println(b);
      //if (b<a)b=0;
      b = 255;

      threshold.pixels[i] = color(random(b));
      //threshold.pixels[i] = color(b);
    }
    threshold.updatePixels();
  };

  public void threshold(float a, PImage im) {
    threshold = new PImage(im.width, im.height, RGB);
    threshold.loadPixels();
    im.loadPixels();

    for (int i=0; i<im.width; i++) {
      for (int j=0; j<im.height; j++) {
        
        int p = i + j * im.width;
        float b = brightness(im.pixels[p]);
        boolean k = getTMin(i,j,im,a);
        //b = 255;
        if (k)b = 0;
        else b = 255;
  
        threshold.pixels[p] = color((b));
    }}
    threshold.updatePixels();
  };
  
  public boolean getTMin(int x, int y,PImage im,float t) {
    
    float []min = new float[2];
    min[0] = 255;
    
    boolean k = false;
    int p = x + y * im.width;
    
    for (int i=x-1; i<=x+1; i++) {
      for (int j=y-1; j<=y+1; j++) {
        
        int p1 = i+j*im.width;
        
        if(p1>0&&p1<im.pixels.length&&p1!=p){
          
          float c = brightness(im.pixels[p1]);
          
          if(min[0]>c){
            min[0] = c;
            min[1] = p1;
          }}}
    }
    
    int p1 = (int)min[1];
    
    float c = brightness(im.pixels[p]);
    float c2 = brightness(im.pixels[p1]);
    float t2 = 30;
    //println(min[0]);
    float d = abs((c)-c2);
    
    //if(c<=c2&&c<t||c2<t&&d<t2)k = true;
    //if(c<t&&(c<=c2)||c2<t&&(d<t2)||c2<t&&d<t2)k = true;
    //if(c<t&&(c>=c2)^c>t&&(d>t2)||c2<t&&d<t2)k = true;
    if(c2<c){
      if(c2<t+t2)k = true;
    }
    else{
      //if(c<t)k = true;
    }
    //println(c,k,t,t+t2);
    //println(c,c2,c<t,d<t2,t,k,d);
    //println(d<t2&&c2>c,c2<c,c,c2,k);
    return k;
    
  };

  public void mean() {

    mean = new PImage(img.width, img.height, RGB);
    float mean_ = 0;
    img.loadPixels();
    for (int i=0; i<img.pixels.length; i++) {
      float b = brightness(img.pixels[i]);
      mean_ += b;
    }

    mean_ /= img.pixels.length;

    mean.loadPixels();
    for (int i=0; i<img.pixels.length; i++) {
      float b = brightness(img.pixels[i]);
      float a = mean_ - b;
      mean.pixels[i] = color(255-a);
    }

    mean.updatePixels();
  };

  public void mean(float k) {

    mean = new PImage(img.width, img.height, RGB);
    Mean = 0;
    img.loadPixels();
    for (int i=0; i<img.pixels.length; i++) {
      float b = brightness(img.pixels[i]);
      Mean += b;
    }

    Mean /= img.pixels.length;
    //Mean = k + Mean;

    mean.loadPixels();
    for (int i=0; i<img.pixels.length; i++) {
      float b = brightness(img.pixels[i]);
      float a = Mean - b;
      mean.pixels[i] = color(255-a);
    }

    mean.updatePixels();
  };

  public void mean(int a) {
    mean = new PImage(img.width, img.height, RGB);
    mean.loadPixels();
    mean_ = new PImage(img.width, img.height, RGB);
    mean_.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        //float b = brightness(img.pixels[p]);

        float []mn = getNeighboursMean(i, j, a);
        float m = mn[0];
        //println(mean_);
        float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        float a2 = red(img.pixels[p]);
        float a3 = green(img.pixels[p]);
        float a4 = blue(img.pixels[p]);

        float a5 = brightness(img.pixels[p]);
        //println(mean_ - b);

        //img.pixels[p] = color(b);
        //float k = mean_*mean_*mean_*mean_*mean_ -(-mean_ -a1)*(-mean_ +a1)*(-mean_ -a2)*(-mean_ +a2)*(-mean_ -a3)*(-mean_ +a3)*(-mean_ -a4)*(-mean_ +a4)*(-mean_ -a5)*(-mean_ +a5);
        //mean.pixels[p] = color((255)-k);
        //if ((mean_ -b)<20)
        //mean.pixels[p] = color(255-(mean_ -(-mean_ -a1)));
        //mean.pixels[p] = color(255-(mean_*mean_ -(-mean_ -a1)));
        mean_.pixels[p] = color(255-(m - a5));
        mean.pixels[p] = color(255-(m - a5)*25);
        //mean.pixels[p] = color(0);
        //mean.pixels[p] = color(random(255));
        //else mean.pixels[p] = color(255);
      }
    }
    mean.updatePixels();
  };
  
  public void mean(int a,PImage img) {
    mean = new PImage(img.width, img.height, RGB);
    mean.loadPixels();
    mean_ = new PImage(img.width, img.height, RGB);
    mean_.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        //float b = brightness(img.pixels[p]);

        float []mn = getNeighboursMean(i, j, a,img);
        float m = mn[0];
        //println(mean_);
        float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        float a2 = red(img.pixels[p]);
        float a3 = green(img.pixels[p]);
        float a4 = blue(img.pixels[p]);

        float a5 = brightness(img.pixels[p]);
        //println(mean_ - b);

        //img.pixels[p] = color(b);
        //float k = mean_*mean_*mean_*mean_*mean_ -(-mean_ -a1)*(-mean_ +a1)*(-mean_ -a2)*(-mean_ +a2)*(-mean_ -a3)*(-mean_ +a3)*(-mean_ -a4)*(-mean_ +a4)*(-mean_ -a5)*(-mean_ +a5);
        //mean.pixels[p] = color((255)-k);
        //if ((mean_ -b)<20)
        //mean.pixels[p] = color(255-(mean_ -(-mean_ -a1)));
        //mean.pixels[p] = color(255-(mean_*mean_ -(-mean_ -a1)));
        mean_.pixels[p] = color(255-(m - a5));
        mean.pixels[p] = color(255-(m - a5)*25);
        //mean.pixels[p] = color(0);
        //mean.pixels[p] = color(random(255));
        //else mean.pixels[p] = color(255);
      }
    }
    mean.updatePixels();
  };
  
  public void mean(int a,float mult) {
    mean = new PImage(img.width, img.height, RGB);
    mean.loadPixels();
    mean_ = new PImage(img.width, img.height, RGB);
    mean_.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        //float b = brightness(img.pixels[p]);

        float []mn = getNeighboursMean(i, j, a);
        float m = mn[0];
        //println(mean_);
        float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        float a2 = red(img.pixels[p]);
        float a3 = green(img.pixels[p]);
        float a4 = blue(img.pixels[p]);

        float a5 = brightness(img.pixels[p]);
        //println(mean_ - b);

        //img.pixels[p] = color(b);
        //float k = mean_*mean_*mean_*mean_*mean_ -(-mean_ -a1)*(-mean_ +a1)*(-mean_ -a2)*(-mean_ +a2)*(-mean_ -a3)*(-mean_ +a3)*(-mean_ -a4)*(-mean_ +a4)*(-mean_ -a5)*(-mean_ +a5);
        //mean.pixels[p] = color((255)-k);
        //if ((mean_ -b)<20)
        //mean.pixels[p] = color(255-(mean_ -(-mean_ -a1)));
        //mean.pixels[p] = color(255-(mean_*mean_ -(-mean_ -a1)));
        mean_.pixels[p] = color(255-(m - a5));
        mean.pixels[p] = color(255-(m - a5)*mult);
        //mean.pixels[p] = color(0);
        //mean.pixels[p] = color(random(255));
        //else mean.pixels[p] = color(255);
      }
    }
    mean.updatePixels();
  };
  
  public void mean(int a,PImage img,float mult) {
    mean = new PImage(img.width, img.height, RGB);
    mean.loadPixels();
    mean_ = new PImage(img.width, img.height, RGB);
    mean_.loadPixels();
    meanG = new PImage(img.width, img.height, RGB);
    meanG.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        //float b = brightness(img.pixels[p]);

        float []mn = getNeighboursMean(i, j, a,img);
        float m = mn[0];
        float mx = mn[1];
        float my = mn[2];
        //println(mean_);
        float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        float a2 = red(img.pixels[p]);
        float a3 = green(img.pixels[p]);
        float a4 = blue(img.pixels[p]);

        float a5 = brightness(img.pixels[p]);
        //println(mean_ - b);

        //img.pixels[p] = color(b);
        //float k = mean_*mean_*mean_*mean_*mean_ -(-mean_ -a1)*(-mean_ +a1)*(-mean_ -a2)*(-mean_ +a2)*(-mean_ -a3)*(-mean_ +a3)*(-mean_ -a4)*(-mean_ +a4)*(-mean_ -a5)*(-mean_ +a5);
        //mean.pixels[p] = color((255)-k);
        //if ((mean_ -b)<20)
        //mean.pixels[p] = color(255-(mean_ -(-mean_ -a1)));
        //mean.pixels[p] = color(255-(mean_*mean_ -(-mean_ -a1)));
        mean_.pixels[p] = color(255-(m - a5));
        mean.pixels[p] = color(255-(m - a5)*mult);
        meanG.pixels[p] = color(0,(mx)*mult,(my)*mult);
        //mean.pixels[p] = color(0);
        //mean.pixels[p] = color(random(255));
        //else mean.pixels[p] = color(255);
      }
    }
    mean.updatePixels();
  };

  public void mean_(int a) {
    mean = new PImage(img.width, img.height, RGB);

    mean.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;

        float []mean_ = getNeighboursMean(i, j, a);
        float m = mean_[0];
        //println(mean_);
        //float b = brightness(img.pixels[p]);
        //println(mean_ - b);

        //img.pixels[p] = color(b);
        //if ((mean_ -b)<20)
        float k = m-((m)/(b)*(m)/(b)) *((m)*(b)/(m));
        //k = m*m - (m-b)*(m-b);
        //println(k);
        //k = m - b;
        float t1 = (k);
        //k = (m*m -t1*t1);
        float t = 2.0f;
        //k = 2.0 / (1.0 + exp(-2.0 * k)) - 1.0;
        k = t/ (1.0f + exp(-t * (k))) - 1.0f;

        mean.pixels[p] = color(k*255);
        //mean

        //mean.pixels[p] = color(b);
        //else mean.pixels[p] = color(255);
      }
    }
    mean.updatePixels();
  };


  public void meanR(int a) {
    mean = new PImage(img.width, img.height, RGB);
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float b = brightness(img.pixels[p]);
        img.pixels[p] = color(b);
      }
    }
  };

  public void meanG(int a) {
    mean = new PImage(img.width, img.height, RGB);
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float b = brightness(img.pixels[p]);
        img.pixels[p] = color(b);
      }
    }
  };

  public void meanB(int a) {
    mean = new PImage(img.width, img.height, RGB);
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float b = brightness(img.pixels[p]);
        img.pixels[p] = color(b);
      }
    }
  };

  public void meanRGB() {
    mean = new PImage(img.width, img.height, RGB);
    float mean_ = 0;
    img.loadPixels();
    for (int i=0; i<img.pixels.length; i++) {
      float b = brightness(img.pixels[i]);
      mean_ += b;
    }

    mean_ /= img.pixels.length;
    mean_ = 150 +mean_;

    mean.loadPixels();
    for (int i=0; i<img.pixels.length; i++) {
      float r = red(img.pixels[i]);
      float g = green(img.pixels[i]);
      float b = blue(img.pixels[i]);
      float br = brightness(img.pixels[i]);
      float a = mean_ - (r+g+b+br)/4;
      mean.pixels[i] = color(255-a);
    }

    mean.updatePixels();
  };

  public void localMean() {
    mean = new PImage(img.width, img.height, RGB);
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float b = brightness(img.pixels[p]);
      }
    }
  };

  public void kMeans() {
    kMeans = new PImage(img.width, img.height, RGB);
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        //float b = map(brightness(img.pixels[p]),0,255,0,100);
        float b = brightness(img.pixels[p]);
        img.pixels[p] = color(b);
      }
    }
  };

  public void kNearest(float a) {
    variance = new PImage(img.width, img.height, RGB);
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float b = brightness(img.pixels[p]);

        img.pixels[p] = color(b);
      }
    }
  };

  public void variance() {
    variance = new PImage(img.width, img.height, RGB);
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float var = getNeighboursVar(0, 0, 0);
        float a1 = red(img.pixels[p]);
        float a2 = green(img.pixels[p]);
        float a3 = blue(img.pixels[p]);
        float a4 = brightness(img.pixels[p]);

        float a = var*var*var*var - ((((var)) + (var - a2))*(((var)) + (var + a3))*(((var)) - (var + a4))*(((var)) + (var + a1)))-255;
        variance.pixels[p] = color(255-a);
      }
    }
  };

  public void variance(int a) {
    variance = new PImage(img.width, img.height, RGB);
    img.loadPixels();
    variance.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float var = getNeighboursVar(i, j, a);
        //float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        float a1 = (red(mean.pixels[p]) + green(mean.pixels[p]) + blue(mean.pixels[p]) + brightness(mean.pixels[p]))/4;
        float a2 = red(mean.pixels[p]);
        float a3 = green(mean.pixels[p]);
        float a4 = blue(mean.pixels[p]);
        float a5 = brightness(mean.pixels[p]);


        float k = var*4;
        float r = sqrt((var + ( var - a1)) * a1 + (var + ( var - a2)) * a2 + (var + ( var - a3)) * a3 + (var + ( var - a4)) * a4);
        //float r = var*var*var - (var + a1)*(var - a1)*(var + a2)*(var - a2)*(var + a3)*(var - a3)*(var + a4)*(var - a4)*(var + a5)*(var - a5);
        //float r = var*var*var*var*var*var*var*var*var - (var + a1)*(var - a1)*(var + a2)*(var - a2)*(var + a3)*(var - a3)*(var + a4)*(var - a4)*(var + a5)*(var - a5);
        //float r = var*var*var*var*var*var*var*var*var - (-var + a1)*(-var - a1)*(-var + a2)*(-var - a2)*(-var + a3)*(-var - a3)*(-var + a4)*(-var - a4)*(-var + a5)*(-var - a5);
        //println(k);
        //r = ((var-brightness(mean.pixels[p]))*20);
        //r = var;
        r = (var*var-(var - a5)*(var + a5));
        //r = var+a1;
        //println(r);
        //println(var,r);
        variance.pixels[p] = color(r);
      }
    }
    variance.updatePixels();
  };
  
  public void variance(int a, PImage imgg) {
    variance = new PImage(img.width, img.height, RGB);
    img.loadPixels();
    variance.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float var = getNeighboursVar(i, j, a,imgg);
        //float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        float a1 = (red(mean.pixels[p]) + green(mean.pixels[p]) + blue(mean.pixels[p]) + brightness(mean.pixels[p]))/4;
        float a2 = red(img.pixels[p]);
        float a3 = green(img.pixels[p]);
        float a4 = blue(img.pixels[p]);
        float a5 = brightness(img.pixels[p]);
        float a6 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        float a7 = (red(imgg.pixels[p]) + green(imgg.pixels[p]) + blue(imgg.pixels[p]) + brightness(imgg.pixels[p]))/4;


        float k = var*4;
        //float r = sqrt((var + ( var - a1)) * a1 + (var + ( var - a2)) * a2 + (var + ( var - a3)) * a3 + (var + ( var - a4)) * a4);
        //float r = var*var*var - (var + a1)*(var - a1)*(var + a2)*(var - a2)*(var + a3)*(var - a3)*(var + a4)*(var - a4)*(var + a5)*(var - a5);
        //float r = var*var*var*var*var*var*var*var*var - (var + a1)*(var - a1)*(var + a2)*(var - a2)*(var + a3)*(var - a3)*(var + a4)*(var - a4)*(var + a5)*(var - a5);
        //float r = var*var*var*var*var*var*var*var*var - (-var + a1)*(-var - a1)*(-var + a2)*(-var - a2)*(-var + a3)*(-var - a3)*(-var + a4)*(-var - a4)*(-var + a5)*(-var - a5);
        //println(k);
        //r = ((var-brightness(mean.pixels[p]))*20);
        //r = var;
        //float r = sqrt(var*var-(var - a1)*(var - a6)+a5*a5);
        // r = var + (a6-a1);
        float r = (var +(a7));
        if(r<200)r=0;
        //r = (a1 - var);
        //println(r);
        //println(var,r);
        variance.pixels[p] = color(r);
      }
    }
    variance.updatePixels();
  };

  public void variance(int a, int n) {
    variance = new PImage(img.width, img.height, RGB);
    img.loadPixels();
    //variance.loadPixels();
    Variance = 0;
    for (int i=0; i<img.pixels.length; i++) {
      float a1 = (red(img.pixels[i]) + green(img.pixels[i]) + blue(img.pixels[i]) + brightness(img.pixels[i]))/4;
      float a2 = (red(mean.pixels[i]) + green(mean.pixels[i]) + blue(mean.pixels[i]) + brightness(mean.pixels[i]))/4;
      //float a2 = (red(threshold.pixels[i]) + green(threshold.pixels[i]) + blue(threshold.pixels[i]) + brightness(threshold.pixels[i]))/4;
      //a2 = brightness(mean.pixels[i]);
      //println("mean " + i + " " + a2);
      Variance += (a2 - a1);
      //float a1 = red(img.pixels[p]);
      //float a2 = green(img.pixels[p]);
      //float a3 = blue(img.pixels[p]);
      //float a4 = brightness(img.pixels[p]);

      //float k = var*4;
      //float r = (var + ( var - a1)) * a1 + (var + ( var - a2)) * a2 + (var + ( var - a3)) * a3 + (var + ( var - a4)) * a4;
      ////println(k);
      ////println(var,r);
      //variance.pixels[p] = color(255-(k-r));
    }
    //variance.updatePixels();
    Variance /= img.pixels.length;
    //Variance = sqrt(Variance);
    //var = abs(var);
    println("Variance " + Variance);
    variance.loadPixels();
    img.loadPixels();
    println(Variance);
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float m = brightness(mean.pixels[p]);
        //float a1 = red(img.pixels[p]);
        float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        //float a1 = (red(mean.pixels[p]) + green(mean.pixels[p]) + blue(mean.pixels[p]) + brightness(mean.pixels[p]))/4;
        float a2 = red(img.pixels[p]);
        float a3 = green(img.pixels[p]);
        float a4 = blue(img.pixels[p]);
        float a5 = brightness(img.pixels[p]);

        float k = Variance*4;
        //float r = sqrt(Variance*Variance - a5*a5);
        float r = Variance*Variance - (Variance - a1)*(Variance + a2);
        //float b = Variance*Variance*Variance*Variance -((((Variance+a1)) + (Variance - a2))*(((Variance)) + (Variance + a3))*(((Variance)) + (Variance + a4))*((Variance) + (Variance + a1)))-255;
        //println(r);
        ////println(Variance,r);
        variance.pixels[p] = color(r);
      }
    }
    variance.updatePixels();
  };

  public void variance2(int a) {
    variance = new PImage(img.width, img.height, RGB);
    variance.loadPixels();
    varianceR = new PImage(img.width, img.height, RGB);
    varianceR.loadPixels();
    varianceG = new PImage(img.width, img.height, RGB);
    varianceG.loadPixels();
    varianceB = new PImage(img.width, img.height, RGB);
    varianceB.loadPixels();
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float []v = getNeighboursVar2(i, j, a);
        float a1 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
        //float a1 = (red(mean.pixels[p]) + green(mean.pixels[p]) + blue(mean.pixels[p]) + brightness(mean.pixels[p]))/4;
        float a2 = red(img.pixels[p]);
        float a3 = green(img.pixels[p]);
        float a4 = blue(img.pixels[p]);
        float a5 = brightness(img.pixels[p]);
        
        float var = v[0];

        float k = var*4;
        //float r = (var + ( var - a1)) * a1 + (var + ( var - a2)) * a2 + (var + ( var - a3)) * a3 + (var + ( var - a4)) * a4;
        //float r = var*var*var - (var + a1)*(var - a1)*(var + a2)*(var - a2)*(var + a3)*(var - a3)*(var + a4)*(var - a4)*(var + a5)*(var - a5);
        //float r = var*var*var*var*var*var*var*var*var - (var + a1)*(var - a1)*(var + a2)*(var - a2)*(var + a3)*(var - a3)*(var + a4)*(var - a4)*(var + a5)*(var - a5);
        //float r = var*var*var*var*var*var*var*var*var - (-var + a1)*(-var - a1)*(-var + a2)*(-var - a2)*(-var + a3)*(-var - a3)*(-var + a4)*(-var - a4)*(-var + a5)*(-var - a5);
        //println(k);
        //println(var,r);
        variance.pixels[p] = color((k));
      }
    }
    variance.updatePixels();
  };

  public void kNearest() {
    variance = new PImage(img.width, img.height, RGB);
    img.loadPixels();

    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float b = brightness(img.pixels[p]);

        img.pixels[p] = color(b);
      }
    }
  };
  
  public void combine(PImage a,PImage b){
    combined = new PImage(img.width, img.height, RGB);
    combined.loadPixels();
    for (int i=0; i<img.pixels.length; i++) {
      
      float b1 = brightness(a.pixels[i]);
      float b2 = brightness(b.pixels[i]);
      
      if(b1<b2)combined.pixels[i] = color(b1+offset);
      else combined.pixels[i] = color(b2+offset);
      
    }
    combined.updatePixels();
  };

  public void blur(int a) {
    blur = new PImage(img.width, img.height, RGB);
    //sobelG = new PImage(img.width, img.height, RGB);
    blur.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float mean = getNeighboursBlur(i, j, a);
        float b = brightness(img.pixels[p]);
        //blur.pixels[p] = color(255-(mean*(mult2)-b)+offset);
        blur.pixels[p] = color(mean);
        //sobelG.pixels[p] = color((gradient[i][j]*100));
        //println(gradient[i][j],green(sobelG.pixels[p]));
      }
    }
    blur.updatePixels();
    
  };
  
  
  
  public void blurS(int a) {
    blur = new PImage(img.width, img.height, RGB);
    
    blur.loadPixels();
    //gradientB = new PImage(img.width, img.height, RGB);
    //gradientB.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float b = brightness(img.pixels[p]);
        float bx = getNeighboursBlurX(i, j, a);
        float by = getNeighboursBlurY(i, j, a);
        blur.pixels[p] = color(((bx+by)/2));
      }
    }
    blur.updatePixels();
  };
  
  public float getNeighboursBlur(int x, int y,int a){
    float mean = 0;
    int count = 0;
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
        int p = i + j * img.width;
        if (p<img.pixels.length&&p>0) {
          float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;
          mean += brightness(img.pixels[p]);
          count++;
      }}
    }
    mean /= count;
    return mean;
  };

  public float getNeighboursBlurX(int x, int y,int a){
    float mean = 0;
    int count = 0;
    int count2 = 0;
    for (int i=x-a; i<=x+a; i++) {
        int p = (i) + y * img.width;
        count2++;
        int n = (y-a+count);
        float k = (a-abs(n-y));
        k = 2.0f / (1.0f + exp(-2.0f * k)) - 1.0f;
        //println(k);
        //k = 1;
        int p1 = (i) + (n) * img.width;
        if (p<img.pixels.length&&p>0&&p1>0&&p1<img.pixels.length) {
          float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;
           mean += brightness(img.pixels[p])*k;
           //+brightness(img.pixels[p1])*k)/2;
           count++;
      }
    }
    return mean/count;
  };
  
  public float getNeighboursBlurY(int x, int y,int a){
    float mean = 0;
    int count = 0;
    int count2 = 0;
    //print("h");
      for (int j=y-a; j<=y+a; j++) {
        int p = x + (j) * img.width;
        int n = (x-a+count);
        float k = (a-abs(n-x));
        k = 2.0f / (1.0f + exp(-2.0f * k)) - 1.0f;
        //k = 1;
        int p1 = (n) + (j) * img.width;
        
        if (p<img.pixels.length&&p>0&&p1>0&&p1<img.pixels.length) {
          float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;
           mean += brightness(img.pixels[p])*k;
           //+brightness(img.pixels[p1])*k)/2;
           count++;
      }
    }
    return mean/count;
    //return sqrt(mean*mean);
  };

  public void getNeighboursAv(int x, int y) {
    for (int i=x-1; i<=x+1; i++) {
      for (int j=y-1; j<=y+1; j++) {
      }
    }
  };

  public float[] getNeighboursMean(int x, int y, int a) {
    float mean = 0;
    int count = 0;
    int count2 = 0;
    float mx = 0;
    float my = 0;
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
        int p = i + j * img.width;
        if (p<img.pixels.length&&p>0) {
          mean += brightness(img.pixels[p]);
          count++;
          if (brightness(img.pixels[p])>10)count2++;
          
          int x1 = 0 + i - x + 1;
          int y1 = 0 + j - y + 1;
          if(x1>=0&&x1<3&&y1>=0&&y1<3){
            mx += meanX[x1][y1];
            my += meanY[x1][y1];
          }
        }
      }
    }
    mean /= count;
    mx /= count;
    my /= count;
    float []val = {mean,mx,my};
    return val;
  };
  
  public float []getNeighboursMean(int x, int y, int a,PImage img) {
    float mean = 0;
    int count = 0;
    int count2 = 0;
    float mx = 0;
    float my = 0;
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
        int p = i + j * img.width;
        if (p<img.pixels.length&&p>0) {
          mean += brightness(img.pixels[p]);
          count++;
          int x1 = 0 + i - x + 1;
          int y1 = 0 + j - y + 1;
          //println(x1);
          
          if(x1>=0&&x1<3&&y1>=0&&y1<3){
            mx += meanX[x1][y1] * brightness(img.pixels[p]);
            my += meanY[x1][y1] * brightness(img.pixels[p]);
            //println(
            //count++;
          }
        }
      }
    }
    
    mean /= count;
    mx /= count;
    my /= count;
    //println(mx,my,count2);
    float []val = {mean,mx,my};
    return val;
  };

  public float getNeighboursMean_(int x, int y, int a) {
    float mean = 0;
    int count = 0;
    int count2 = 0;
    int p1 = x + y * img.width;
    float b1 = (red(img.pixels[p1])+green(img.pixels[p1])+blue(img.pixels[p1])+brightness(img.pixels[p1]))/4;
    float k = 40;
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
        int p = i + j * img.width;
        if (p<img.pixels.length&&p>0) {
          float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;
          if (abs(b1-b)<k
            //if(abs(red(img.pixels[p1])-red(img.pixels[p]))<k
            //  ||abs(green(img.pixels[p1])-red(img.pixels[p]))>k
            //  ||abs(blue(img.pixels[p1])-red(img.pixels[p]))<k
            ) {
            //if(true){
            mean += brightness(img.pixels[p]);
            //mean += b;
            count2++;
          } else mean += 15;
          //else mean -=20;
          count++;
        }
      }
    }
    //if (count2<(a*2*a*2)/(a*4)) mean = 1;
    if (mean<k*(a*2*a*2)) mean = 0;
    //if(count2>8) mean = 0;
    return mean/count;
  };

  public float getNeighboursVar(int x, int y, int a) {
    float variance = 0;
    int count = 0;
    int count2 = 0;
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
        int p = i + j * img.width;
        if (p<img.pixels.length&&p>0) {
          float a1 = 0;
          if(threshold==null){
            a1 = (red(mean.pixels[p]) + green(mean.pixels[p]) + blue(mean.pixels[p]) + brightness(mean.pixels[p]))/4;
            a1 = brightness(mean.pixels[p]);
          }else a1 = (red(threshold.pixels[p]) + green(threshold.pixels[p]) + blue(threshold.pixels[p]) + brightness(threshold.pixels[p]))/4;
          float a2 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
          //variance += brightness(threshold.pixels[p]) - brightness(img.pixels[p]);
          variance += a2-a1;

          count++;
        }
      }
    }
    return sqrt((variance*variance)/count);
  };

  public float getNeighboursVar(int x, int y, int a,PImage mean) {
    float variance = 0;
    int count = 0;
    int count2 = 0;
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
        int p = i + j * img.width;
        if (p<img.pixels.length&&p>0) {
          float a1 = 0;
            a1 = (red(mean.pixels[p]) + green(mean.pixels[p]) + blue(mean.pixels[p]) + brightness(mean.pixels[p]))/4;
            a1 = brightness(mean.pixels[p]);
          float a2 = (red(img.pixels[p]) + green(img.pixels[p]) + blue(img.pixels[p]) + brightness(img.pixels[p]))/4;
          //variance += brightness(threshold.pixels[p]) - brightness(img.pixels[p]);
          variance += a2-a1;

          count++;
        }
      }
    }
    return sqrt((variance*variance)/count);
  };
  
  public float []getNeighboursVar2(int x, int y, int a) {
    float variance = 0;
    float varianceR = 0;
    float varianceG = 0;
    float varianceB = 0;
    int count = 0;
    int count2 = 0;
    float a1r = 0,a1g = 0,a1b = 0,a1br = 0;
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
        int p = i + j * img.width;
        if (p<img.pixels.length&&p>0) {
          
          
          float a2r = red(img.pixels[p]);
          float a2g = green(img.pixels[p]);
          float a2b = blue(img.pixels[p]);
          float a2br = red(img.pixels[p]);
          
          varianceR += a1r-a2r;
          varianceG += a1g-a2g;
          varianceB += a1b-a2b;
          
          variance += a1br-a2br;

          count++;
        }
      }
    }
    variance /= count;
    varianceR /= count;
    varianceG /= count;
    varianceB /= count;
    
    float []val = {variance,varianceR,varianceG,varianceB};
    return val;
  };

  public void getNeighbours2Min(int x, int y, int a, int b) {
    for (int i=x-a; i<=x+b; i++) {
      for (int j=y-a; j<=y+b; j++) {
      }
    }
  };

  public void getNeighbours2Min(int x, int y, int a) {
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
      }
    }
  };
  
  public void sobel(int a,float mult) {
    sobel = new PImage(img.width, img.height, RGB);
    sobel.loadPixels();
    sobelx = new PImage(img.width, img.height, RGB);
    sobelx.loadPixels();
    sobely = new PImage(img.width, img.height, RGB);
    sobely.loadPixels();
    sobelG = new PImage(img.width, img.height, RGB);
    sobelG.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {

        int p = i + j * img.width;
        float[] val = getSobel(i, j);
        float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;
        float r = red(img.pixels[p]);
        float g = green(img.pixels[p]);
        float b1 = blue(img.pixels[p]);
        float b2 = brightness(img.pixels[p]);
        //println(val.length);
        float v = val[0];
        //println(val[3]);

        //float k = ((v/b)*(v)/(b)) *((b/v)*(v))-((v)/(b));
        //k = (b*b - (b-v)*(b+v));
        //k = (v*v - (v-b)*(v+b));
        
        //println(k,b);
        //k = abs(v-b);
        //k = v-b;
        float k = v * mult;
        float t = 2.0f;
        //k = 2.0 / (1.0 + exp(-2.0 * k)) - 1.0;
        //k = (t/ (1.0 + exp(-t * (k))) - 1.0)*val[0]*mult;
        //sobel.pixels[p] = color(255);
        //println(k);
        //if(k>val[0])
        //if(k<7)
        //sobel.pixels[p] = color(k);
        //if(255-(k-b2)<255&&val[3]>6)
        //if(val[3]<8)
        //if(255-(k-b2)<255)
        //sobel.pixels[p] = color(255);
        if(a==0)sobel.pixels[p] = color(255-(k-(b2)));
        //sobel.pixels[p] = color(0);
        if(a==1)sobel.pixels[p] = color(255-k);
        if(a==2)sobel.pixels[p] = color(b2-k);
        //else 
        sobelx.pixels[p] = color(val[1]);
        sobely.pixels[p] = color(val[2]);
        sobelG.pixels[p] = color(0,val[1]*mult2,val[2]*mult2);
        //println(val[4]);
        //if(val[4]>0)println("val " + brightness(sobelG.pixels[p]),val[4]);

        //color col
      }
    }
    sobel.updatePixels();
    sobelx.updatePixels();
    sobely.updatePixels();
    //sobelG.updatePixels();
  };

  public void sobel(PImage img,int a) {
    sobel = new PImage(img.width, img.height, RGB);
    sobel.loadPixels();
    sobelx = new PImage(img.width, img.height, RGB);
    sobelx.loadPixels();
    sobely = new PImage(img.width, img.height, RGB);
    sobely.loadPixels();
    sobelG = new PImage(img.width, img.height, RGB);
    sobelG.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {

        int p = i + j * img.width;
        float[] val = getSobel(i, j);
        float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;
        float r = red(img.pixels[p]);
        float g = green(img.pixels[p]);
        float b1 = blue(img.pixels[p]);
        float b2 = brightness(img.pixels[p]);
        //println(val.length);
        float v = val[0];
        //println(val[3]);
        float k = v*mult;
        
        if(a==0)sobel.pixels[p] = color(255-(k-(b2)));
        //sobel.pixels[p] = color(0);
        if(a==1)sobel.pixels[p] = color(255-k);
        if(a==2)sobel.pixels[p] = color(b2-k);
        //sobel.pixels[p] = color(((b2)-k));
        //sobel.pixels[p] = color(0);
        //sobel.pixels[p] = color(k);
        //sobel.pixels[p] = color(k-r,k-g,k-b1);
        //sobel.pixels[p] = color((k-r),(k-g),(k-b1));
        //sobel.pixels[p] = color(r-k,g-k,b1-k);
        //float s = brightness(sobel.pixels[p]);
        //if(s>250)sobel.pixels[p] = color(0);
        //println(sobel.pixels[p]);
        //if(k<200)sobel.pixels[p] = color(img.pixels[p]);
        //sobel.pixels[p] = color(val[0]);
        //else sobel.pixels[p] = color(255);
        sobelx.pixels[p] = color(val[1]);
        sobely.pixels[p] = color(val[2]);
        sobelG.pixels[p] = color(0,0,0,val[4]);
        
        //println(brightness(sobelG.pixels[p]));

        //color col
      }
    }
    sobel.updatePixels();
    sobelx.updatePixels();
    sobely.updatePixels();
    sobelG.updatePixels();
  };

  public float []getSobel(int x, int y) {
    float val = 0;
    float vy = 0;
    float vx = 0;
    float vd = 0;
    float hd = 0;
    float eh = 0;
    float ev =0;
    img.loadPixels();
    int count = 0;
    int count2 = 0;
    int p1 = x + y * img.width;
    float b1 = (red(img.pixels[p1])+green(img.pixels[p1])+blue(img.pixels[p1])+brightness(img.pixels[p1]))/4;
    float k = 40;
    int z = 1;
    for (int i=x-z; i<=x+z; i++) {
      for (int j=y-z; j<=y+z; j++) {

        int p = i + j * img.width;
        if (p>0&&p<img.pixels.length) {
          float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;
          if (abs(red(img.pixels[p1])-red(img.pixels[p]))<k
            ||abs(green(img.pixels[p1])-green(img.pixels[p]))<k
            ||abs(blue(img.pixels[p1])-blue(img.pixels[p]))<k
            ||brightness(img.pixels[p1])-brightness(img.pixels[p])<k
            ) {
            count2 ++;
            
            int x1 = 0 + i - x + 1;
            int y1 = 0 + j - y + 1;
            
            float col = brightness(img.pixels[p]);
            col = b;
            float v = SobelH[x1][y1] * col;
            float h = SobelV[x1][y1] * col;
            float vd_ = SobelH[x1][y1] * col;
            float hd_ = SobelV[x1][y1] * col;
            float eh_ = SobelV[x1][y1] * col;
            float ev_ = SobelH[x1][y1] * col;
            
            //v = LapLacian[x1][y1] * col;
            //h = LapLacianD[x1][y1] * col;

            //println(col);
            vx += v;
            vy += h;
            vd += vd_;
            hd += hd_;
            ev += ev_;
            eh += eh_;
            }

          //neighbours[x][y] 
          count ++;
        }
      }
    }

    vx/= count;
    vy/= count;

    //if(vx<0)vx = 0;
    //if(vy<0)vy = 0;

    //val = sqrt(vx*vx + vy*vy);
    val = sqrt(vx*vx + vy*vy + vd*vd + hd*hd + ev*ev + eh*eh);
    //println(atan2(vy,vx));
    float [] sob = {val, vx, vy, count2,atan2(vy,vx)};
    gradient[x][y] = atan2(vy,vx);
    return sob;
  };

  public float []getSobel(int x, int y, PImage img) {
    float val = 0;
    float vy = 0;
    float vx = 0;
    img.loadPixels();
    int count = 0;
    for (int i=x-1; i<=x+1; i++) {
      for (int j=y-1; j<=y+1; j++) {

        int p = i + j * img.width;
        if (p>0&&p<img.pixels.length) {
          int x1 = 0 + i - x + 1;
          int y1 = 0 + j - y + 1;

          float col = brightness(img.pixels[p]);
          col = (red(img.pixels[p])+blue(img.pixels[p])+green(img.pixels[p])+brightness(img.pixels[p]))/4;
          float v = SobelH[x1][y1] * col;
          float h = SobelV[x1][y1] * col;

          //println(col);
          vx += v;
          vy += h;

          count ++;
        }
      }
    }

    vx/= count;
    vy/= count;

    val = sqrt(vx*vx + vy*vy);
    //println(count);
    float [] sob = {val, vx, vy, count};
    return sob;
  };
  
  public void sobel2(int a,float mult2) {
    sobel2 = new PImage(img.width, img.height, RGB);
    sobel2.loadPixels();
    sobel2x = new PImage(img.width, img.height, RGB);
    sobel2x.loadPixels();
    sobel2y = new PImage(img.width, img.height, RGB);
    sobel2y.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {
        int p = i + j * img.width;
        float []mean = getSobel2(i, j, a);
        float b = brightness(img.pixels[p]);
        //println(mean[1]);
        sobel2.pixels[p] = color(255-(mean[0]*(mult2)-b)+offset);
        sobel2x.pixels[p] = color(255-(mean[1]*(mult2)-b)+offset);
        sobel2y.pixels[p] = color(255-(mean[2]*(mult2)-b)+offset);
        //sobel2.pixels[p] = color(mean);
        //sobelG.pixels[p] = color((gradient[i][j]*100));
        //println(gradient[i][j],green(sobelG.pixels[p]));
      }
    }
    sobel2.updatePixels();
    
  };
  
  public float []getSobel2(int x, int y,int a){
    float mean = 0;
    float meany = 0;
    float meana = 0;
    float meanb = 0;
    int count = 0;
    for (int i=x-a; i<=x+a; i++) {
      for (int j=y-a; j<=y+a; j++) {
        int p = i + j * img.width;
        if (p<img.pixels.length&&p>0) {
          float b = (red(img.pixels[p])+green(img.pixels[p])+blue(img.pixels[p])+brightness(img.pixels[p]))/4;
          float c = (i-x);
          float d = (j-y);
          float e = x - i;
          float f = y - j;
          //if(c==0)c=1;
          meany += brightness(img.pixels[p])*(c+d);
          mean += brightness(img.pixels[p])*(e+f);
          meana += brightness(img.pixels[p])*(c+f);
          meanb += brightness(img.pixels[p])*(e+d);
          //mean += (brightness(img.pixels[p])*(c+d) + brightness(img.pixels[p])*(e+f))/2;
          //mean += brightness(img.pixels[p])*(c+d);
          count++;
      }}
    }
    //return sqrt((mean/count)*mean/count+meany/count*meany/count);
    //gradient[x][y] = atan2((meany+meana)/2,(mean+meanb)/2);
    float val = sqrt((mean/count)*mean/count+meany/count*meany/count+((meana/count)*meana/count+meanb/count*meanb/count));
    float valx = (mean + meana)/2;
    float valy = (meany + meanb)/2;
    float [] sob = {val,valx,valy};
    return sob;
    //return sqrt(((meana/count)*meana/count+meanb/count*meanb/count));
    //return sqrt((meany/count)*meany/count+meanb/count*meanb/count);
  };
 
  
  public void sobelMax(float t){
    sobelMax = new PImage(img.width, img.height, RGB);
    sobelMax.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {

        int p = i + j * img.width;
        
        boolean max = getNeighboursMax(i,j,t);
        if(!max)sobelMax.pixels[p] = color(255);
        else sobelMax.pixels[p] = combined.pixels[p];
      }}
  };
  
  public boolean getNeighboursMax(int x, int y,float t) {
    
    float []max = new float[7];
    max[0] = 255;
    
    boolean k = false;
    int p = x + y * img.width;
    //float myGradient = gradient[x][y];
    //float myGradient_ = atan2(sobely.pixels[p],sobelx.pixels[p]);
    
    for (int i=x-1; i<=x+1; i++) {
      for (int j=y-1; j<=y+1; j++) {
        
        int p1 = i+j*sobel.width;
        
        if(p1>0&&p1<sobel.pixels.length){
          float c = 0;
          if(combined==null)c = brightness(sobel.pixels[p1]);
          else c = brightness(combined.pixels[p1]);
          if(max[0]>c){
            max[0] = c;
            max[3] = p1;
          }}}
    }
    int p1 = (int)max[3];
    boolean k2 = false;
    float c = brightness(combined.pixels[p]);
    float c2 = brightness(combined.pixels[p1]);
    //float t = 30;
    float t2 = radians(45);
    float t3 = radians(10);
    float d1 = abs(max[0]-(255-(c)));
    float d2 = abs(atan2(sobely.pixels[p] - sobely.pixels[p1],sobelx.pixels[p] - sobelx.pixels[p1]));
    //println(d2);
    float d3 = abs(max[0]-c);
    //if(d2<t2||c2<=255-c)k = true;
    if(c<t)k = true;
    //println(max[0],brightness(combined.pixels[p]),x,y);
    //if(max[0]<=brightness(combined.pixels[p])||k2)k=true;
    
    // keep this one for cartoons
    //if((max[0]<=brightness(combined.pixels[p]))||d1>t&&max[0]<=brightness(combined.pixels[p]))k = true;
    return k;
  };
  
  public void sobelMax(PImage sobel,float t){
    sobelMax = new PImage(img.width, img.height, RGB);
    sobelMax.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {

        int p = i + j * img.width;
        
        boolean max = getNeighboursMax(i,j,sobel,t);
        sobelMax.pixels[p] = color(255);
        if( max)sobelMax.pixels[p] = color(0);
        //else if(max&&brightness(sobel.pixels[p])<255-t)sobelMax.pixels[p] = color(0);
      }}
  };
  
  public boolean getNeighboursMax(int x, int y,PImage sobel,float t) {
    
    float []min = new float[2];
    min[0] = 255;
    
    boolean k = false;
    int p = x + y * img.width;
    float myGradient = gradient[x][y];
    
    for (int i=x-1; i<=x+1; i++) {
      for (int j=y-1; j<=y+1; j++) {
        
        int p1 = i+j*sobel.width;
        
        if(p1>0&&p1<sobel.pixels.length){
          float c = 0;
          c = brightness(sobel.pixels[p1]);
          if(min[0]>c){
            min[0] = c;
            min[1] = p1;
          }}}
    }
    int p1 = (int)min[1];
    boolean k2 = false;
    float c = brightness(sobel.pixels[p]);
    float c1 = brightness(sobel.pixels[p1]);
    float t1 = radians(45);
    float t2 = 30;
    float d = abs(c1-(255-(c)));
    //println(d2);
    float d1 = abs(c1-c);
    float g = atan2(green(sobelG.pixels[p]), blue(sobelG.pixels[p]));
    float g1 = atan2(green(sobelG.pixels[p1]), blue(sobelG.pixels[p1]));
    float d2 = abs(g-g1);
    //println(d3,d1,c,c2,min[0]);
    //if(d3<t)k = true;
    //println(g,g1,g-g1,t1);
    //if(c<t&&c<=c1||d2<t&&d<t2)k = true;
    if(c>c1&&c1<t&&d2<t1&&d1<t2||c<t&&c<=c1)k = true;

    // for(int i=0;i<8;i++){
    //   float theta = radians(45)*i;
    //   float theta2 = radians(45)*(i+1);

    //   if(g>theta&&g<theta2){
    //     if(g2>theta-PI&&g2<theta2-PI||g2>theta+PI&&g2<theta2+PI)
    //   }
    // }
    //println(min[0],brightness(combined.pixels[p]),x,y);
    //if(min[0]<=brightness(combined.pixels[p])||k2)k=true;
    
    // keep this one for cartoons
    //if((min[0]<=brightness(combined.pixels[p]))||d1>t&&min[0]<=brightness(combined.pixels[p]))k = true;
    return k;
  };
  
  
  public void sobelMin(){
    sobelMin = new PImage(img.width, img.height, RGB);
    sobelMin.loadPixels();
    for (int i=0; i<img.width; i++) {
      for (int j=0; j<img.height; j++) {

        int p = i + j * img.width;
        
        boolean min = getNeighboursMin(i,j);
        if(!min)sobelMin.pixels[p] = color(255);
        else sobelMin.pixels[p] = sobel.pixels[p];
      }}
  };
  
  public boolean getNeighboursMin(int x, int y) {
    
    float []max = new float[3];
    max[0] = 0;
    boolean k = false;
    int p = x + y * img.width;
    float myGradient = brightness(sobelG.pixels[p]);
    for (int i=x-1; i<=x+1; i++) {
      for (int j=y-1; j<=y+1; j++) {
        
        int p1 = i+j*sobel.width;
        
        if(p1>0&&p1<sobel.pixels.length&&p1!=p){
        float cGradient = brightness(sobelG.pixels[p1]);
        
        //if(cGradient==-1/myGradient||cGradient==PI-(-1/myGradient)){
        //if(cGradient==myGradient){
          float c = brightness(sobel.pixels[p1]);
          if(max[0]>c){
            max[0] = c;
            max[1] = i;
            max[2] = j;
          }
        //}
      }}
    }
    //println(max[0],brightness(blur.pixels[x+y*sobel.width]));
    int p2 = (int)max[1] + (int)max[2] * sobelG.width;
    //if(p2
    //println((int)max[0],(int)max[1],x,y);
    float cGradient = brightness(sobelG.pixels[p]);
    //if(max[0]>=brightness(blur.pixels[x+y*sobel.width])||(cGradient==-1/myGradient||cGradient==PI-(-1/myGradient)))k=true;
    boolean k2 = false;
    float r = red(blur.pixels[p]);
    float g = green(blur.pixels[p]);
    float b = blue(blur.pixels[p]);
    float b1 = brightness(blur.pixels[p]);
    float r1 = red(blur.pixels[p2]);
    float g1 = green(blur.pixels[p2]);
    float b2 = blue(blur.pixels[p2]);
    float b3 = brightness(blur.pixels[p2]);
    float t = 0;
    if(abs(r-r1)<t||abs(g-g1)<t||abs(b-b2)<t||abs(b1-b3)<t)k2 = true;
    
    //if(max[0]<=brightness(blur.pixels[x+y*sobel.width])||(cGradient==-1/myGradient||cGradient==PI-(-1/myGradient))||k2)k=true;
    if(max[0]<=brightness(blur.pixels[x+y*sobel.width])&&(cGradient!=-1/myGradient&&cGradient!=PI-(-1/myGradient)))k=true;
    //if(max[0]<=brightness(blur.pixels[x+y*sobel.width])||(cGradient==myGradient))k=true;
    //if(max[0]<=brightness(blur.pixels[x+y*sobel.width]))k=true;
    return k;
  };
};
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_200714a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
