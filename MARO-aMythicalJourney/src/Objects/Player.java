package Objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Manager.CollisionManager;
import Manager.GameManager;
import Manager.InputManager;
import Manager.TileManager;
import Rendering.IOUtils;

public class Player {

    public enum Movement {
        Left, 
        Right, 
        Up, 
        Down
    }

    private Movement currentDirection = Movement.Right;
    private int yPosition;
    private int xPosition;
    private float Health = 10;
    private String name;
    private String playerImageNames[];
    private BufferedImage playerImages[];
    private boolean canWhaterWalk;

    public Player(int XPos, int YPos, String PlayerName) {
        this.xPosition = XPos;
        this.yPosition = YPos;
        this.name = PlayerName;
        this.playerImageNames = new String[]{"link_right.png", "link_left.png", "link_right.png", "link_top.png", "link_back.png"};
        this.playerImages = new BufferedImage[5];
        this.canWhaterWalk = false;
    }

//SETTER
    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public void toggleCanWhaterWalk() {
        this.canWhaterWalk = !canWhaterWalk;
    }

    public void setHealth(float health) {
        this.Health = health;
    }

    public void Damage() {
    	if((this.Health - 0.5) > 0) {
    		this.Health -= 0.5;	
    	} else {
    		this.Health = 0f;
    	}
    }

//GETTER
    public int getXPosition() {
        return this.xPosition;
    }

    public String getName() {
        return name;
    }

    public int getYPosition() {
        return this.yPosition;
    }

    public float getHealth() {
        return this.Health;
    }

    public boolean isCanWhaterWalk() {
        return canWhaterWalk;
    }

//MOVEMENT
    public void moveUP() {
        if (this.currentDirection == Movement.Up) {
            if (TileManager.checkMapCollision()) {
                GameManager.getInstance().getTiles().changeMap(this.currentDirection);
                return;
            }
            MovementTimer.init(currentDirection);
            (new MovementTimer()).start();
            return;
        }
        this.currentDirection = Movement.Up;
    }

    public void moveDown() {
        if (this.currentDirection == Movement.Down) {
            if (TileManager.checkMapCollision()) {
                GameManager.getInstance().getTiles().changeMap(this.currentDirection);
                return;
            }
            if (!CollisionManager.playerCollidesWithObstacle()) {
                MovementTimer.init(currentDirection);
                (new MovementTimer()).start();
                return;
            }
        }
        this.currentDirection = Movement.Down;
    }

    public void moveRight() {
        if (this.currentDirection == Movement.Right) {
            if (TileManager.checkMapCollision()) {
                GameManager.getInstance().getTiles().changeMap(this.currentDirection);
                return;
            }
            if (!CollisionManager.playerCollidesWithObstacle()) {
                MovementTimer.init(currentDirection);
                (new MovementTimer()).start();
                return;
            }
        }
        this.currentDirection = Movement.Right;
    }

    public void moveLeft() {
        if (this.currentDirection == Movement.Left) {
            if (TileManager.checkMapCollision()) {
                GameManager.getInstance().getTiles().changeMap(this.currentDirection);
                return;
            }
            if (!CollisionManager.playerCollidesWithObstacle()) {
                MovementTimer.init(currentDirection);
                (new MovementTimer()).start();
                return;
            }
        }
        this.currentDirection = Movement.Left;
    }

    public void setPlayerDirection(Movement movement) {
        this.currentDirection = movement;
    }

    public Movement getPlayerDirection() {
        return currentDirection;
    }

//COLLISION
    public Rectangle getBoundingBox() {
        return new Rectangle(xPosition + 1, yPosition + 1, 64 - 1, 64 - 1);
    }

    public Point getTileInFront() {
        Point p = null;
        switch (currentDirection) {
            case Right:
                p = new Point((xPosition / 64) + 1, yPosition / 64);
                break;
            case Left:
                p = new Point((xPosition / 64) - 1, yPosition / 64);
                break;
            case Up:
                p = new Point(xPosition / 64, (yPosition / 64) - 1);
                break;
            case Down:
                p = new Point(xPosition / 64, (yPosition / 64) + 1);
                break;
        }
        return p;
    }

//GRAPHICS
    public BufferedImage getSprite() {
        switch (this.currentDirection) {
            case Left:
                if (playerImages[1] == null) {
                    playerImages[1] = IOUtils.load("Images", playerImageNames[1]);
                }
                return playerImages[1];

            case Right:
                if (playerImages[2] == null) {
                    playerImages[2] = IOUtils.load("Images", playerImageNames[2]);
                }
                return playerImages[2];

            case Up:
                if (playerImages[4] == null) {
                    playerImages[4] = IOUtils.load("Images", playerImageNames[4]);
                }
                return playerImages[4];

            case Down:
                if (playerImages[3] == null) {
                    playerImages[3] = IOUtils.load("Images", playerImageNames[3]);
                }
                return playerImages[3];

            default:
                if (playerImages[0] == null) {
                    playerImages[0] = IOUtils.load("Images", playerImageNames[0]);
                }
                return playerImages[0];
        }
    }

    public static class MovementTimer extends Thread {

        private static int start;

        private static final int SPEED = 250;//ms
        private static int status;

        private static boolean horizontal = true;//x
        private static int multiplier = 1;

        public static void init(Movement m) {
            status = 0;
            switch (m) {
                case Right:
                    start = GameManager.getInstance().GetPlayer().getXPosition();
                    multiplier = 1;
                    horizontal = true;
                    break;
                case Down:
                    start = GameManager.getInstance().GetPlayer().getYPosition();
                    multiplier = 1;
                    horizontal = false;
                    break;
                case Up:
                    start = GameManager.getInstance().GetPlayer().getYPosition();
                    horizontal = false;
                    multiplier = -1;
                    break;
                case Left:
                    start = GameManager.getInstance().GetPlayer().getXPosition();
                    horizontal = true;
                    multiplier = -1;
                    break;
            }
        }

        @Override
        public void run() {
            InputManager.blockInput = true;
            try {
                for (int time = 0; time < SPEED; time++) {
                    status = time;
                    Thread.sleep(1);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            status = SPEED;
            setPosition();
            InputManager.blockInput = false;
        }

        public static void setPosition() {
            if (InputManager.blockInput != true) {
                return;
            }
            int px = ((int) Math.round(((double) status / SPEED) * 64));
            if (horizontal) {
                GameManager.getInstance().GetPlayer().setXPosition(start + (multiplier * px));
            } else {
                GameManager.getInstance().GetPlayer().setYPosition(start + (multiplier * px));
            }
            if (GameManager.getInstance().getItemManager().containCurrentMapItem()) {
                Item i = CollisionManager.getInstance().getItemCollision();
                if (i != null && i.isConsumable()) {
                    Inventory.getInstance().addItem(i);
                }
            }
        }
    }
}
