package ac.uk.brookes.lh09092543.othello;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.util.Log;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

//Resources.getSystem().getColor(R.color.white)
//testing
public class Tile {
		
	int topLeftPosX,topLeftPosY,size,state,lineThickness,chipSet,potentialFlips;
	Bitmap tileImage;
	Rect tileRect;
	Paint myPaint;
	Context context;
	Canvas canvas;
	
	public Tile(Context context,Canvas canvas, int topLeftPosX, int topLeftPosY, int size,Bitmap tileImage,int chipSet){
		this.canvas = canvas;
		this.topLeftPosX = topLeftPosX;
		this.topLeftPosY = topLeftPosY;
		this.size = size;
		this.context = context;
		this.tileImage = tileImage;
		this.chipSet = chipSet;
		
		lineThickness = 2;
		tileRect = new Rect(topLeftPosX+lineThickness,topLeftPosY+lineThickness,(topLeftPosX+size)-lineThickness,(topLeftPosY+size)-lineThickness);
		
		//State determines what state the tile is in:
		//0	= 	Blank tile with no chip
		//1	=	Tile with player 1's chip on
		//2	=	Tile with player 2's chip on
		//3	=	Tile with a potential move indicator
		state=0;
		myPaint = new Paint();
		potentialFlips = 0;
	}
	
	public void drawTile(){
		switch(state){
			case 0: //Draw blank Tile
				drawBlankTile();
				break;
			case 1: //Draw Tile with a player 1 chip
				drawBlankTile();
				if(chipSet == 0)
					drawChip(context.getResources().getColor(R.color.white));
				else
					drawChip(context.getResources().getColor(R.color.red));
				break;
			case 2: // Draw Tile with a player 2 chip
				drawBlankTile();
				if(chipSet == 0)
					drawChip(context.getResources().getColor(R.color.black));
				else
					drawChip(context.getResources().getColor(R.color.blue));
				break;
			case 3: // Draw Tile with a move indicator on
				drawBlankTile();
				drawMoveIndicator();
				break;
			default:
		}

	}
	
	public void drawBlankTile(){
		//Draws just the blank tile
		myPaint.setColor(context.getResources().getColor(R.color.black));
		myPaint.setStrokeWidth(3);
		canvas.drawRect(topLeftPosX,topLeftPosY,topLeftPosX+size,topLeftPosY+size,myPaint);
		
		canvas.drawBitmap(tileImage,null,tileRect,null);	
	}
	
	public void drawChip(int chipColor){
		//Draws a chip on the tile of a given color
		myPaint.setColor(chipColor);
		myPaint.setStrokeWidth(3);
		canvas.drawCircle(topLeftPosX+(size/2),topLeftPosY+(size/2),20,myPaint);
	}
	
	public void drawMoveIndicator(){
		//Draws the indicator to show a valid move
		myPaint.setColor(context.getResources().getColor(R.color.black));
		myPaint.setStrokeWidth(3);
		canvas.drawCircle(topLeftPosX+(size/2),topLeftPosY+(size/2),12,myPaint);
		
		myPaint.setColor(context.getResources().getColor(R.color.white));
		myPaint.setStrokeWidth(3);
		canvas.drawCircle(topLeftPosX+(size/2),topLeftPosY+(size/2),6,myPaint);
		
	}
	
	public Boolean Qtouched(int player,int X, int Y){
		//If a tile is touched and its a valid move it returns true and changes the state
		if((tileRect.contains(X,Y))&& state == 3){
					if(player == 1){
						state=1;
						return true;
					}
					if(player==2){
						state = 2;
						return true;
					}
				}
		return false;
	}
	
	public Boolean computerFlip(){
		state = 2;
		return true;
	}
	

	public void flip(){
		//Flips a chip
		if(state != 0){
			if(state == 1)
				state = 2;
			else
				state=1;
		}
	}
	
}
