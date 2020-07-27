package ac.uk.brookes.lh09092543.othello;

import java.io.IOException;
import java.io.InputStream;

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
import android.provider.ContactsContract;
import android.content.Intent;
import android.view.SoundEffectConstants;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Handler;
import android.os.CountDownTimer;
import android.media.MediaPlayer;
import java.util.Timer;
import java.util.TimerTask;


public class GameView extends View implements OnTouchListener {
	
	
	int chipSet,boardType,timeLimit,boardIndent,boardYOffset,border,boardSize,player,otherPlayer,
		width,buttonWidth,buttonHeight,tileSize,sDU,beepUri,line,passCounter;
	int[] playerScores = new int[2];
	boolean singlePlayer, timed,init;
	String[] playerName;
	String[] playerID;
	String time;
	GameTimer timer;
	Bitmap tileImage,exitButton,passButton,player1Image,player2Image;
	Rect exitButtonRect,passButtonRect,player1ImageRect,player2ImageRect;
	Paint paint,textPaint;
	Tile[] tileBoard; 
	Context context;
	
    public GameView(Context context,Bundle gameVariables) {
        super(context);  
        this.context = context;
        
        //extract the bundle passed from the game activity
    	chipSet = gameVariables.getInt("chipSet");
    	boardType = gameVariables.getInt("boardType");
    	timeLimit = gameVariables.getInt("timeLimit");
    	playerName = gameVariables.getStringArray("playerName");
    	playerID = gameVariables.getStringArray("playerID");
    	timed = gameVariables.getBoolean("timed");
    	singlePlayer = gameVariables.getBoolean("singlePlayer");
    	
    	//Initialise variables
    	time = "";
    	timer = new GameTimer((timeLimit*1000)+999,100);
    
    	boardSize = 8; 	//tiles in x and y, eg 8 = 64 tiles
    	player = 1;
    	otherPlayer = 2;
    	passCounter = 0;
    	
    	paint = new Paint();
    	textPaint = new Paint();
    	tileBoard = new Tile[boardSize*boardSize];
    	
    	//Init allows an initial configuration to run as a run show in onCreate.
    	init = false;
    	
    	//Set the text color
    	textPaint.setColor(getResources().getColor(R.color.brown));
    	textPaint.setTextSize(20);
    	
    	//Any bitmaps we're going to need, convert them now.
    	createBitmaps();
    	
    
    }
    
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        
        //Determine the screen width as its created.
        width = xNew;
        
        //Create a scalable unit that is determined once the screen size is set.
        sDU = width/16; 
        
        //Set all other dimensions in sDU
    	boardYOffset = 4*sDU;
    	boardIndent = sDU;
    	border = sDU/4;
    	line = sDU/15;
    	
    	//Define the tile size
    	tileSize = (width-(2*boardIndent))/boardSize;
    	
    	//Create rectangles for the below images
    	exitButtonRect = new Rect(1*sDU,22*sDU,4*sDU,24*sDU);
    	passButtonRect = new Rect(11*sDU,22*sDU,14*sDU,24*sDU);
    	player1ImageRect = new Rect(2*sDU,2*sDU,4*sDU,4*sDU);
    	player2ImageRect = new Rect(12*sDU,2*sDU,14*sDU,4*sDU);
    	
   }
    
   
    
    @Override
    public void onDraw(Canvas canvas) {

    	//Draw border around the board
        paint.setColor(getResources().getColor(R.color.brown));
    	canvas.drawRect(boardIndent-border,(boardIndent-border)+boardYOffset,
    					(width-boardIndent)+border,((width-boardIndent)+border)+boardYOffset,paint);

    	//Place exit button
    	canvas.drawBitmap(exitButton,null,exitButtonRect,null);
    	canvas.drawBitmap(passButton,null,passButtonRect,null);
    	
    	//Draw a rectangle around the player who's go it is in their color, this just shows who's go it
    	//is and what color their tiles are.
    	paint.setColor(getResources().getColor(R.color.black));
    	if(player == 1){
    		canvas.drawRect(((2*sDU)-border)-line,((2*sDU)-border)-line,((4*sDU)+border)+line,((4*sDU)+border)+line,paint);
    		if(chipSet == 0)
    			paint.setColor(getResources().getColor(R.color.white));
    		else
    			paint.setColor(getResources().getColor(R.color.red));
    		canvas.drawRect((2*sDU)-border,(2*sDU)-border,(4*sDU)+border,(4*sDU)+border,paint);
    	}
    	else{
    		canvas.drawRect(((12*sDU)-border)-line,((2*sDU)-border)-line,((14*sDU)+border)+line,((4*sDU)+border)+line,paint);
    		if(chipSet == 0)
    			paint.setColor(getResources().getColor(R.color.black));
    		else
    			paint.setColor(getResources().getColor(R.color.blue));
    		canvas.drawRect((12*sDU)-border,(2*sDU)-border,(14*sDU)+border,(4*sDU)+border,paint);
    	}
    	
    	//Place the players photo above the board
    	canvas.drawBitmap(player1Image,null,player1ImageRect,null);
    	canvas.drawBitmap(player2Image,null,player2ImageRect,null);
    	
 
    	if(init == false){
    		//Only execute this on start up
    		//Fill the array of tiles
    		createBoard(canvas);
    		
    		//Configure the inital state of the board
    		tileBoard[27].state = 1;
    		tileBoard[28].state = 2;
    		tileBoard[35].state = 2;
    		tileBoard[36].state = 1;
    		playerScores[0]=2;
    		playerScores[1]=2;
    		//Draw the markers that indicate all valid moves.
    		drawPotentialMoves();
	    	init = true; 
	    	if(timed)
	    		timer.start();
    	}
    		//Call this every time invalidate() is called.
    		updateScores();
    		drawBoard();
    		//Place the timer above the board if the game is timed.
    		if(timed)
    			canvas.drawText(""+time,7*sDU,sDU,textPaint);
    		
    		//Place the players names and scores above the board
    		canvas.drawText(playerName[0]+" "+playerScores[0],2*sDU,sDU,textPaint);
    	    canvas.drawText(playerName[1]+" "+playerScores[1],12*sDU,sDU,textPaint);
    		
    	    //If we're playing against the computer and its the computers go
    	    //let the computer play.
    	    if((singlePlayer == true)&& (player == 2)){
    	    	computerPlay();
    	    }	
    	    canvas.restore();
    }
    
    public int beenTouched(int x, int y){
    	//Returns a 0 if the exit button has been pressed
    	//Returns a 1 if a tile has been pressed
    	//Returns a 2 if the board is full and there is a winner
    	
    	//Called by GameActivity. This method acts when the screen is touched.
    	timer.cancel();
    	
    	//Calls each tiles Qtouched method to decide if the tile has been touched.
    	//If the pass button has been pressed the go is skipped
    	//If the pass button has been pressed 4 time consecutivley the game will also end
    	if((checkForFlips(Qtouched(x,y),true)!=0)||passButtonRect.contains(x,y)){
    		if(passButtonRect.contains(x,y)){
    			passCounter++;
    		}else{
    			passCounter = 0;
    		}
    		
    		if(passCounter < 4){
    			
		    	//Change players
		    	if(player == 1){
		    		player = 2;
		    		otherPlayer = 1;
		    		if(timed)
		    			timer.start();
		    	}
		    	else{
		    		player = 1;
		    		otherPlayer = 2;
		    		if(timed)
		    			timer.start();
		    	}
		    	//Re-draw the board
		    	updateScores();
		    	drawPotentialMoves();
		    	invalidate();
		    	return 1;
    		}
    	}
    	//Exit if the exit button has been pressed.
    	if(exitButtonRect.contains(x,y)){
    		playSoundEffect(SoundEffectConstants.CLICK);
    		timer.cancel();
    		return 0;
    	}
    	
    	
    	//If the board is full return a 2 to indicated there is a winner.
    	if((playerScores[0]+playerScores[1]==(boardSize*boardSize))||((playerScores[0]==0)||(playerScores[1]==0))||
    			(passCounter ==4)){
			return 2;
		}
    	return 1;
    }
    
    
    public void createBoard(Canvas canvas){
    	//Populate the tile[] with objects. This method also lays them out.
    	//This method only creates the board, it doesn't draw it.
    	for(int j = 0; j < boardSize; j++){
    		for(int i = 0; i < boardSize; i++){
    			tileBoard[i+(j*boardSize)]=new Tile(context,canvas,(i*tileSize)+boardIndent,(boardIndent + (j*tileSize))+boardYOffset,tileSize,tileImage,chipSet);
    		}
    	}
    	
    }
    
    public void drawBoard(){
    	//Re-draws the board but calling draw method on each tile.
    	for(int i = 0; i < (boardSize*boardSize); i++){
    		tileBoard[i].drawTile();
    	}
    }
	
	
	public int Qtouched(int x, int y){
		//Calls the Qtouched method for each tile.
		//Returns an int for the tile that was touched.
    	for(int i = 0; i < (boardSize*boardSize); i++){
    		if(tileBoard[i].Qtouched(player,x,y))
    		return i;
    	}
    	return 0;
	}
	
	public int checkForFlips(int element, boolean realMove){
		//Returns the number of consequent flips a move will trigger on a given element,
		//if realMove it true the flips will actually happen, if not the method will
		//only return the number of potential filps.
		int Qflipped = 0;
		
		Qflipped = Qflipped + checkRowsForward(element,realMove);
		Qflipped = Qflipped + checkRowsReverse(element,realMove);
		Qflipped = Qflipped + checkColoumnsUp(element,realMove);
		Qflipped = Qflipped + checkColoumnsDown(element,realMove);
		Qflipped = Qflipped + checkDiagUpLeft(element,realMove);
		Qflipped = Qflipped + checkDiagUpRight(element,realMove);
		Qflipped = Qflipped + checkDiagDownLeft(element,realMove);
		Qflipped = Qflipped + checkDiagDownRight(element,realMove);
		
		return Qflipped;
	}
	
	//The following methods check for flips and if realMove is true the execute them.
	//They return the number of flips they find
	
	public int checkRowsForward(int element,boolean realMove){
		int row = (element/boardSize)+1;
		boolean potentialFlip = false;
		int flipCounter = 0;
		
		if(element%boardSize == 7) //if the element is far right there can be no right flips
			return 0;
		
		if(tileBoard[element+1].state == player)
			return 0;
		
		for(int i = element+1; i <= (row*boardSize)-1; i++){
			
			if(tileBoard[i].state==(0))
				return 0;
			if(tileBoard[i].state==(3))
				return 0;
			
			if(tileBoard[i].state==otherPlayer)
				potentialFlip = true;
				
			if((tileBoard[i].state==player)&&(potentialFlip == true)){
				for(int j = i-1; j > element;j--){
					flipCounter++;
					if(realMove)
						tileBoard[j].flip();
				}
				return flipCounter;
			}
		}
		return 0;
	}
	
	public int checkRowsReverse(int element,boolean realMove){
		int row = (element/boardSize)+1;
		boolean potentialFlip = false; 
		int flipCounter = 0;

		if(element%boardSize == 0) //if the element is far left there can be no left flips
			return 0;
		if(tileBoard[element-1].state == player)
			return 0;
		
		for(int i = element-1; i >= ((row-1)*boardSize); i--){

			if(tileBoard[i].state==3)
				return 0;
			if(tileBoard[i].state==0)
				return 0;

			if(tileBoard[i].state==otherPlayer)
				potentialFlip = true;
			if((tileBoard[i].state==player)&&(potentialFlip == true)){
				for(int j = i+1; j < element;j++){
					flipCounter++;
					if(realMove)
						tileBoard[j].flip();
				}
				return flipCounter;
			}
		}
		return 0;
	}
	
	public int checkColoumnsUp(int element,boolean realMove){
		int lastTile = element % boardSize;
		boolean potentialFlip = false;
		int row = (element/boardSize)+1;
		int flipCounter = 0;
		
		if(row == 1)
			return 0;
		if(tileBoard[element-boardSize].state == player)
			return 0;
		
		for(int i = element-boardSize; i >= lastTile; i=i-boardSize){
			if(tileBoard[i].state==3)
				return 0;
			if(tileBoard[i].state==0)
				return 0;
			if(tileBoard[i].state==otherPlayer)
				potentialFlip = true;
			if((tileBoard[i].state==player)&&(potentialFlip == true)){
				for(int j = i+boardSize; j < element;j=j+boardSize){
					flipCounter++;
					if(realMove)
						tileBoard[j].flip();
						flipCounter++;
				}
				return flipCounter;
			}
		}
		return 0;
	}
	
	public int checkColoumnsDown(int element,boolean realMove){
		int absLastTile = boardSize*boardSize;
		int lastTile = absLastTile-(boardSize-(element % boardSize));
		boolean potentialFlip = false;
		int row = (element/boardSize)+1;
		int flipCounter = 0;

		if(row == 8)
			return 0;
		if(tileBoard[element+boardSize].state == player)
			return 0;
		
		for(int i = element+boardSize; i < lastTile; i=i+boardSize){				
			if(tileBoard[i].state==3)	
				return 0;		
			if(tileBoard[i].state==0)	
				return 0;
			if(tileBoard[i].state==otherPlayer)
				potentialFlip = true;
			if((tileBoard[i].state==player)&&(potentialFlip == true)){
				for(int j = i-boardSize; j > element;j=j-boardSize){
					flipCounter++;
					if(realMove)
						tileBoard[j].flip();
				}
				return flipCounter;
			}
		}
		return 0;
	}
	
	public int checkDiagUpRight(int element,boolean realMove){
			
			int iterations;
			boolean potentialFlip = false;
			int row = (element/boardSize)+1;
			int flipCounter = 0;
			
			if(element<=0)
				return 0;
			if(row == 1)
				return 0;
			if(element%boardSize == 7) //if the element is far right there can be no right flips
				return 0;
				
			int indexSize = boardSize-1;
			int vIterations = element/boardSize;					//Max number of iterations limited by top of board
			int hIterations = (boardSize-(element%boardSize))-1;	//Max number of iterations limited by left of board
			
			if(vIterations<=hIterations)
				iterations = vIterations;
			else
				iterations = hIterations;
			
			if(tileBoard[element-indexSize].state == player)
				return 0;
			
			for(int i = 0; i < iterations; i++){
				if(tileBoard[element-((i+1)*indexSize)].state==3)
					return 0;
				if(tileBoard[element-((i+1)*indexSize)].state==0)
						return 0;
				if(tileBoard[element-((i+1)*indexSize)].state==otherPlayer)
					potentialFlip = true;
				if((tileBoard[element-((i+1)*indexSize)].state==player)&&(potentialFlip == true)){
					for(int j = element-(i*indexSize); j <= (element-indexSize);j=j+(indexSize)){
						flipCounter++;
						if(realMove)
							tileBoard[j].flip();
					}
					return flipCounter;
				}
			}
			return 0;
		}
		
	public int checkDiagDownRight(int element,boolean realMove){
			
			int row = (element/boardSize)+1;
			int iterations;
			boolean potentialFlip = false;
			int flipCounter = 0;
			
			if(element%boardSize <=0)
				return 0;
			if(row == 8)
				return 0;
			if(element%boardSize == 7) //if the element is far right there can be no right flips
				return 0;
			
			
			int indexSize = boardSize+1;
			int vIterations = ((boardSize*boardSize)-element)/boardSize;	//Max number of iterations limited by top of board
			int hIterations = (boardSize-(element%boardSize))-1;	//Max number of iterations limited by left of board
			
			if(vIterations<=hIterations)
				iterations = vIterations;
			else
				iterations = hIterations;
			
			if(tileBoard[element+indexSize].state == player)
				return 0;
			
			for(int i = 0; i < iterations; i++){
				if(tileBoard[element+((i+1)*indexSize)].state==3)
					return 0;
				if(tileBoard[element+((i+1)*indexSize)].state==0)
					return 0;
				if(tileBoard[element+((i+1)*indexSize)].state==otherPlayer)
					potentialFlip = true;
				if((tileBoard[element+((i+1)*indexSize)].state==player)&&(potentialFlip == true)){
					for(int j = element+(i*indexSize); j >= (element+indexSize);j=j-(indexSize)){
						flipCounter++;
						if(realMove)
							tileBoard[j].flip();
					}
					return flipCounter;
				}
			}
			return 0;
		}
		
	public int checkDiagUpLeft(int element,boolean realMove){
			
			int row = (element/boardSize)+1;
			int iterations;
			boolean potentialFlip = false;
			int flipCounter = 0;
			
			if(element<=0)
				return 0;
			if(element%boardSize == 0) //if the element is far left there can be no left flips
				return 0;
			if(row == 1)
				return 0;
			
			int indexSize = boardSize+1;
			int vIterations = element/boardSize;					//Max number of iterations limited by top of board
			int hIterations = element%boardSize;	//Max number of iterations limited by Right of board
			

			
			if(vIterations<=hIterations)
				iterations = vIterations;
			else
				iterations = hIterations;
			
			if(tileBoard[element-indexSize].state == player)
				return 0;
			
			
			for(int i = 0; i < iterations; i++){
				if(tileBoard[element-((i+1)*indexSize)].state==3)
					return 0;
				if(tileBoard[element-((i+1)*indexSize)].state==0)
					return 0;
				if(tileBoard[element-((i+1)*indexSize)].state==otherPlayer)
					potentialFlip = true;
				if((tileBoard[element-((i+1)*indexSize)].state==player)&&(potentialFlip == true)){
					for(int j = element-(i*indexSize); j <= (element-indexSize);j=j+(indexSize)){
						flipCounter++;
						if(realMove)
							tileBoard[j].flip();
					}
					return flipCounter;
				}
			}
			return 0;
		}
		
	public int checkDiagDownLeft(int element,boolean realMove){
			
			int row = (element/boardSize)+1;
			int iterations;
			boolean potentialFlip = false;
			int flipCounter = 0;
			
			if(element<=0)
				return 0;
			if(row == 8)
				return 0;
			if(element%boardSize == 0) //if the element is far left there can be no left flips
				return 0;
			
			int indexSize = boardSize-1;
			int vIterations = ((boardSize*boardSize)-element)/boardSize;	//Max number of iterations limited by top of board
			int hIterations = element%boardSize;	//Max number of iterations limited by Right of board
			

			
			if(vIterations<=hIterations)
				iterations = vIterations;
			else
				iterations = hIterations;
			
			if(tileBoard[element+indexSize].state == player)
				return 0;
			
			for(int i = 0; i < iterations; i++){
				if(tileBoard[element+((i+1)*indexSize)].state==3)
					return 0;
				if(tileBoard[element+((i+1)*indexSize)].state==0)
					return 0;
				if(tileBoard[element+((i+1)*indexSize)].state==otherPlayer)
					potentialFlip = true;
				if((tileBoard[element+((i+1)*indexSize)].state==player)&&(potentialFlip == true)){
					for(int j = element+(i*indexSize); j >= (element+indexSize);j=j-(indexSize)){
						flipCounter++;
						if(realMove)
							tileBoard[j].flip();
					}
					return flipCounter;
				}
			}
			return 0;
		}

	public void updateScores(){
		//updates the players scores
		int player1 = 0;
		int player2 = 0;
		for(int i = 0; i < boardSize*boardSize;i++){
			if(tileBoard[i].state == 1)
				player1++;
			if(tileBoard[i].state == 2)
				player2++;
		}
		playerScores[0]=player1;
		playerScores[1]=player2;
				
	}
	
	void drawPotentialMoves(){
		//Draws any legal moves on the board
		for (int i = 0; i < boardSize*boardSize;i++){
			if((tileBoard[i].state == 0)||(tileBoard[i].state == 3)){
				tileBoard[i].potentialFlips = checkForFlips(i,false);
				if(tileBoard[i].potentialFlips>0){
					tileBoard[i].state = 3;
					
				}else{
					tileBoard[i].state = 0;
				}
			}
		}
	
		
	}
	

	public void createBitmaps(){
		//Creates bitmaps from the PNG files in drawable.
		if(boardType == 0)
			tileImage = BitmapFactory.decodeResource(getResources(),R.drawable.felt);
		else
			tileImage = BitmapFactory.decodeResource(getResources(),R.drawable.wood);
		
		exitButton = BitmapFactory.decodeResource(getResources(),R.drawable.exitbutton);
		passButton = BitmapFactory.decodeResource(getResources(),R.drawable.passbutton);
		
		//Get the player URI and use it to get the photo
		Uri player1URI = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(playerID[0]));
	    InputStream photoStream1 = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), player1URI);
	    if (photoStream1 != null) {
	          player1Image = BitmapFactory.decodeStream(photoStream1);
	         try {
	         	photoStream1.close();
	         } catch (IOException e) { 
		        e.printStackTrace();
	         }
	    }else{
	    	player1Image= BitmapFactory.decodeResource(getResources(),android.R.drawable.sym_def_app_icon);
	    }
	    
	    //Get the player URI and use it to get the photo
	    Uri player2URI = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(playerID[1]));
	    InputStream photoStream2 = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), player2URI);
	    if (photoStream2 != null) {
	          player2Image = BitmapFactory.decodeStream(photoStream2);
	         try {
	         	photoStream2.close();
	         } catch (IOException e) { 
		        e.printStackTrace();
	         }
	    }else{
	    	player2Image= BitmapFactory.decodeResource(getResources(),android.R.drawable.sym_def_app_icon);
	    }
	}
	
	public void computerPlay(){
		//Emulates a player, makes the move with the highest number of flips.
		
		int highest = 0;
		int highestElement = 0;
		for(int i = 0; i < boardSize*boardSize; i++){
			if((tileBoard[i].potentialFlips > highest)&&(tileBoard[i].state == 3)){
				highest = tileBoard[i].potentialFlips;
				highestElement=i;
			}
		}
		if(timed)
			timer.cancel();
		tileBoard[highestElement].computerFlip();
		checkForFlips(highestElement,true);
		
		player = 1;
		otherPlayer = 2;
		if(timed)
			timer.start();
		drawPotentialMoves();
		invalidate();
		
	}
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void playBeep(){
		Thread t = new Thread(){
            public void run(){
                MediaPlayer player = null;
                player = MediaPlayer.create(context,R.raw.beep);
                player.start();
                try {
                	Thread.sleep(player.getDuration()+100);
                	player.release();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();
	}
	
	public void restartGame(){
		for( int i = 0; i < boardSize*boardSize;i++){
			tileBoard[i].state = 0;
			tileBoard[i].drawTile();
		}
		//Configure the inital state of the board
		tileBoard[27].state = 1;
		tileBoard[28].state = 2;
		tileBoard[35].state = 2;
		tileBoard[36].state = 1;
		playerScores[0]=2;
		playerScores[1]=2;
		//Draw the markers that indicate all valid moves.
		drawPotentialMoves();
		invalidate();
		
	}
	
	public class GameTimer extends CountDownTimer {

		public GameTimer(long millisInFuture,long countDownInterval){
			super(millisInFuture,countDownInterval);
		}
		
		
		public void onTick(long millisUntilFinished){
			time = "00:"+millisUntilFinished/1000;
			invalidate();
			
		}
		
		public void onFinish(){
			playBeep();
			cancel();
			start();
		}
		
	}
		
}