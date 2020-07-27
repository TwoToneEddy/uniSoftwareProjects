package ac.uk.brookes.lh09092543.reversi;

import com.example.drawtest.R;

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
		
	int topLeftPosX,topLeftPosY,size,state,lineThickness;
	Bitmap tileImage;
	Rect tileRect;
	Paint myPaint;
	Context context;
	Canvas canvas;
	
	public Tile(Context context,Canvas canvas, int topLeftPosX, int topLeftPosY, int size,Bitmap tileImage){
		this.canvas = canvas;
		this.topLeftPosX = topLeftPosX;
		this.topLeftPosY = topLeftPosY;
		this.size = size;
		this.context = context;
		this.tileImage = tileImage;
		
		lineThickness = 2;
		tileRect = new Rect(topLeftPosX+lineThickness,topLeftPosY+lineThickness,(topLeftPosX+size)-lineThickness,(topLeftPosY+size)-lineThickness);
		state=0;
		myPaint = new Paint();
	}
	
	public void drawTile(){
		switch(state){
			case 0: //Draw blank Tile
				drawBlankTile();
				break;
			case 1: //Draw Tile with a white chip
				drawBlankTile();
				drawChip(context.getResources().getColor(R.color.white));
				break;
			case 2: // Draw Tile with a black chip
				drawBlankTile();
				drawChip(context.getResources().getColor(R.color.black));
				break;
			default:
		}

	}
	
	public void drawBlankTile(){
		
		myPaint.setColor(context.getResources().getColor(R.color.black));
		myPaint.setStrokeWidth(3);
		canvas.drawRect(topLeftPosX,topLeftPosY,topLeftPosX+size,topLeftPosY+size,myPaint);
		
		canvas.drawBitmap(tileImage,null,tileRect,null);	
	}
	
	public void drawChip(int chipColor){
		myPaint.setColor(chipColor);
		myPaint.setStrokeWidth(3);
		canvas.drawCircle(topLeftPosX+(size/2),topLeftPosY+(size/2),20,myPaint);
	}
	
	public Boolean Qtouched(int player,int X, int Y){
		if(tileRect.contains(X,Y)){
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

	public void flip(){
		if(state != 0){
			if(state == 1)
				state = 2;
			else
				state=1;
		}
	}
	
}
