
     //   Toast toast = Toast.makeText(context,
	//	"here", Toast.LENGTH_LONG);
     //   toast.show();

package ac.uk.brookes.lh09092543.reversi;
import com.example.drawtest.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.util.Log;
import android.widget.Toast;
import android.graphics.Rect;
import android.os.Bundle;
import android.content.Intent;
import android.view.SoundEffectConstants;

public class DrawView extends View implements OnTouchListener {
	
	int chipSet,boardType,timeLimit,boardIndent,border,boardSize,player,
		width,height,buttonWidth,buttonHeight,tileSize;
	boolean singlePlayer, timed,init;
	String[] playerName;
	String[] playerID;
	Bitmap tileImage,exitButton;
	Rect exitButtonRect;
	Paint paint;
	Tile[] tileBoard; 
	Context context;
	
    public DrawView(Context context,Bundle gameVariables) {
        super(context);  
        this.context = context;
    	chipSet = gameVariables.getInt("chipSet");
    	boardType = gameVariables.getInt("boardType");
    	timeLimit = gameVariables.getInt("timeLimit");
    	playerName = gameVariables.getStringArray("playerName");
    	playerID = gameVariables.getStringArray("playerID");
    	boardIndent = 50; 
    	border = 10;
    	boardSize = 8;
    	player = 1;
    	paint = new Paint();
    	tileBoard = new Tile[boardSize*boardSize];
    	init = false;
    	createBitmaps();
    }
    
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        width = xNew;
        height = yNew;
    	buttonWidth = width/3;
    	buttonHeight = height/14;
    	tileSize = (width-(2*boardIndent))/boardSize;
    	exitButtonRect = new Rect(((width/2)-(buttonWidth/2)),height-(height/10),
				((width/2)-(buttonWidth/2))+buttonWidth,(height-(height/10))+buttonHeight);
   }
    @Override
    public void onDraw(Canvas canvas) {
    
        Toast toast = Toast.makeText(context," "+timeLimit, Toast.LENGTH_LONG);
        toast.show();
        
    	//Draw border
    	paint.setColor(getResources().getColor(R.color.brown));
    	canvas.drawRect(boardIndent-border,boardIndent-border,(width-boardIndent)+border,(width-boardIndent)+border,paint);
    	
    	//Place exit button
    	canvas.drawBitmap(exitButton,null,exitButtonRect,null);
    	
    	if(init == false){
    		//Only create the board once
    		createBoard(canvas,tileBoard,tileSize,boardIndent);
	    	init = true; 
    	}
    		//Draw the board after every move
    		drawBoard(tileBoard);
    }
    
    public int beenTouched(int x, int y){
    	//Called by GameActivity. This method acts when the screen is touched.
    	checkForFlips(Qtouched(tileBoard,player,x,y));
    	
    	if(player == 1)
    		player = 2;
    	else
    		player = 1;
    	
    	if(exitButtonRect.contains(x,y)){
    		playSoundEffect(SoundEffectConstants.CLICK);
    		return 0;
    	}
    	//Re-draw the board
    	invalidate(); 	
    	return 1;
    }
    
    
    public void createBoard(Canvas canvas, Tile[] board,int tileSize, int indent){
   //Populate the tile[] with objects. This method also lays them out.
    	for(int j = 0; j < boardSize; j++){
    		for(int i = 0; i < boardSize; i++){
    			board[i+(j*boardSize)]=new Tile(context,canvas,(i*tileSize)+indent,indent + (j*tileSize),tileSize,tileImage);
    		}
    	}
    	
    }
    
    public void drawBoard(Tile[] board){
    	//Re-draws the board but calling draw method on each tile.
    	for(int i = 0; i < (boardSize*boardSize); i++){
    		board[i].drawTile();
    	}
    }
	
	
	public int Qtouched(Tile[] tile,int player, int x, int y){
		//Calls the Qtouched method for each tile.
		//Returns an int for the tile that was touched.
    	for(int i = 0; i < (boardSize*boardSize); i++){
    		if(tile[i].Qtouched(player,x,y))
    		return i;
    	}
    	return 0;
	}
	
	public boolean checkForFlips(int element){
		//Checks what needs to be updated after a tile is pressed.
		checkRowsForward(element);
		checkRowsReverse(element);
		checkColoumnsUp(element);
		checkColoumnsDown(element);
		//checkDiagLeft(element);
		//checkDiagRight(element);
		return true;
	}
	
	public boolean checkRowsForward(int element){
		int row = (element/boardSize)+1;
		
		for(int i = element+1; i < (row*boardSize)-1; i++){
			if(tileBoard[i].state==player){
				for(int j = i-1; j > element;j--){
					tileBoard[j].flip();
				}
				return true;
			}
		}
		return false;
	}

	public boolean checkRowsReverse(int element){
		int row = (element/boardSize)+1; 
		
		for(int i = element-1; i >= ((row-1)*boardSize); i--){
			if(tileBoard[i].state==player){
				for(int j = i+1; j < element;j++){
					tileBoard[j].flip();
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean checkColoumnsUp(int element){
		int lastTile = element % boardSize;
		for(int i = element-boardSize; i >= lastTile; i=i-boardSize){			
			if(tileBoard[i].state==player){
				for(int j = i+boardSize; j < element;j=j+boardSize){
					Log.d("row",""+j);
					tileBoard[j].flip();
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean checkColoumnsDown(int element){
		int absLastTile = boardSize*boardSize;
		int lastTile = absLastTile-(boardSize-(element % boardSize));

		for(int i = element+boardSize; i < lastTile; i=i+boardSize){			
			if(tileBoard[i].state==player){
				for(int j = i-boardSize; j > element;j=j-boardSize){
					tileBoard[j].flip();
				}
				return true;
			}
		}
		return false;
	}
	

	
	//Yet to implement
	public boolean checkDiagLeft(int element){
		return true;
	}
	
	//Yet to implement
	public boolean checkDiagRight(int element){
		return true;
	}

	public void createBitmaps(){
		//Creates bitmaps from the PNG files in drawable.
		tileImage = BitmapFactory.decodeResource(getResources(),R.drawable.felt);
		exitButton = BitmapFactory.decodeResource(getResources(),R.drawable.exit);
	}
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
	

	



