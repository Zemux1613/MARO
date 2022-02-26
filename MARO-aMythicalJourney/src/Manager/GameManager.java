package Manager;

import java.awt.Color;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import Manager.InputManager.Event;
import Manager.SoundManager.Sound;
import Manager.TileManager.Tile;
import Npc.Goblin;
import Npc.NPC;
import Npc.Spider;
import Objects.Boat;
import Objects.Inventory;
import Objects.Item;
import Objects.MapItem;
import Objects.MapItemCoin;
import Objects.Player;
import Rendering.GameImage;
import Rendering.GameText;

public class GameManager implements InputListener {

    private static GameManager Manager = null;
    private TileManager Tile = new TileManager();
    private Player PlayerOne = null;

    private Inventory inventory;
    private List<NPC> npcs;

    private ArrayList<Event> events;

    private ItemManager itemManager;
    private Boolean showInventory;
    private Boolean inMenu = false;

    private Boat boat;

    private GameManager() {
        //GameSaver.loadFile();
        //PlayerOne = GameSaver.loadPlayer();
        PlayerOne = new Player(64 * 2, 64 * 4, "");
        events = new ArrayList<Event>();
        npcs = new ArrayList<NPC>();

        boat = new Boat(320, 200);

        npcs.add(new Spider(128, 128));
        npcs.add(new Goblin(64, 64));

        inventory = Inventory.getInstance();

        itemManager = new ItemManager();
        itemManager.addItem("0", new MapItemCoin());
//        itemManager.addItem("0", new MapItem("Sword", true, false, 0, 0, 64, 64, "Coin.png"));
        itemManager.addItem("1", new MapItem("Bomb", true, true, 0, 0, 64, 64, "bomb.png"));
        itemManager.addItem("2", new MapItem("Bomb", true, true, 320, 320, 64, 64, "bomb.png"));
        itemManager.addItem("2", new MapItem("Bomb", true, true, 320, 200, 64, 64, "bomb.png"));
        itemManager.addItem("2", new MapItem("Bomb", true, true, 270, 250, 64, 64, "bomb.png"));

        Inventory.getInstance().addCoins(40);
        showInventory = true;
    }

    public void spawnNpc() {
        int x, y;

        x = new Random().nextInt(12 * 64);
        y = new Random().nextInt(8 * 64);

        npcs.add(new Spider(x, y));

    }

    public void initInputManager(Frame frame) {
        InputManager.getInstance().setFrame(frame);
        InputManager.getInstance().setInputListener(this);
    }

    public TileManager getTiles() {
        return Tile;
    }

    public Boolean getInMenu() {
        return inMenu;
    }

    public void setInMenu(Boolean inMenu) {
        this.inMenu = inMenu;
    }
    
    public static GameManager getInstance() {

        if (Manager == null) {
            return Manager = new GameManager();
        } else {
            return Manager;
        }
    }

    public Boat getBoat() {
        return boat;
    }

    public void setBoat(Boat boat) {
        this.boat = boat;
    }

    public void SetPlayer(Player Player) {
        PlayerOne = Player;
    }

    public Player GetPlayer() {
        return PlayerOne;
    }

    public Item getSelectedItem() {
        return Inventory.getInstance().getInventory(Inventory.getInstance().getSelectedItem()).getItem();
    }

    public GameImage getPlayerToRender() {
        return new GameImage(PlayerOne.getSprite(), PlayerOne.getXPosition(), PlayerOne.getYPosition());
    }

    public GameImage getBoatToRender() {
        return new GameImage(boat.getSprint(), boat.getX(), boat.getY());
    }

    public ArrayList<GameImage> getInventoryToRender() {
        if (showInventory) {
            return Inventory.getInstance().getInventoryToRender();
        } else {
            return new ArrayList<GameImage>();
        }
    }

    public GameText getCoinsToRender() {

        // return new GameText("Coins: " + Inventory.getInstance().getCoins(), 0, 20, 0, 0);
        return new GameText("Coins: " + Inventory.getInstance().getCoins(), 325, 20, 0, 0, "Helvetica", Color.yellow, 20);

    }

    public List<GameImage> getNPCsToRender() {
        List<GameImage> gimages = new ArrayList<GameImage>();

        for (NPC npc : npcs) {
            gimages.add(new GameImage(npc.getSprite(), npc.getX(), npc.getY()));
        }

        return gimages;
    }

    public List<GameImage> GetObjectsToRender(Tile[][] tiles2) {
        return Tile.convertToImage(tiles2);
    }

    public Boolean getShowInventory() {
        return showInventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<GameImage> getItemsToRender() {
        return itemManager.getItemsForMapToRender(Tile.getCurrMapID() + "");
    }

    public void update() {
        if (!inMenu) {
            for (NPC npc : npcs) {
                npc.moveToPlayer(PlayerOne);
                if (CollisionManager.getInstance().collidesWithObstacle(npc, GetObjectsToRender(TileManager.getObstacles()))) {
                    npc.damage(1);
                }
                if (CollisionManager.getInstance().collidesWithObstacle(npc, PlayerOne)) {

                	PlayerOne.Damage();
                    SoundManager.getInstance().playSound(Sound.TEST2);                    
                    
                    /**
                     * TODO: Bessere Option finden einen Tod zu haendeln
                     */
                    if(PlayerOne.getHealth() == 0) {
                    	int showInternalConfirmDialog = JOptionPane.showInternalConfirmDialog(null, PlayerOne.getName() + " du bist gestorben!");
                    	if(showInternalConfirmDialog == 0) {
                    		System.exit(0);
                    	}
                    }
                    
                    // Kein NPC Movement mehr wenn der Spieler Tot ist
                    if(PlayerOne.getHealth() > 0) {
                    	
                    	if (npc.getX() < PlayerOne.getXPosition()) {
                    		npc.MoveLeft();
                    	} else if (npc.getX() > PlayerOne.getXPosition()) {
                    		npc.MoveRight();
                    	} else if (npc.getY() > PlayerOne.getYPosition()) {
                    		npc.MoveUP();
                    	} else if (npc.getY() < PlayerOne.getYPosition()) {
                    		npc.MoveDown();
                    	} else {
                    		npc.MoveUP();
                    	}
                
                    }
            
                }

            }
            for (int i = 0; i < events.size(); i++) {
                Event event = events.remove(i);
                switch (event) {
                    case MOVE_DOWN:
                        PlayerOne.moveDown();
                        break;
                    case MOVE_UP:
                        PlayerOne.moveUP();
                        break;
                    case MOVE_LEFT:
                        PlayerOne.moveLeft();
                        break;
                    case MOVE_RIGHT:
                        PlayerOne.moveRight();
                        break;
                    case TRIGGER_INVENTORY:
                        showInventory = !showInventory;
                        break;
                    case SELECT_ITEM1:
                        Inventory.getInstance().setSelectedItem(1);
                        break;
                    case SELECT_ITEM2:
                        Inventory.getInstance().setSelectedItem(2);
                        break;
                    case SELECT_ITEM3:
                        Inventory.getInstance().setSelectedItem(3);
                        break;
                    case SELECT_ITEM4:
                        Inventory.getInstance().setSelectedItem(4);
                        break;
                    case OPEN_MENU:
                        setInMenu(true);
                        break;
                    case USE_ITEM:
                        ItemManager.useCurrentItem();
                        break;
                    case INTERACT_WHIT_SOMTHING:
                        break;
                    default:
                        break;
                }
            }
        } else if (inMenu) {
            for (int i = 0; i < events.size(); i++) {
                Event event = events.remove(i);
                switch (event) {
                    case OPEN_MENU:
                        setInMenu(false);
                        break;
                        
                        default:
                        	break;
                }
            }
        }
        Player.MovementTimer.setPosition();
    }

    private void checkItemCollision() {
        MapItem item = CollisionManager.getInstance().collidesWithMapItem(PlayerOne,
                itemManager.getItemsForMap(Tile.getCurrMapID() + ""));
        if (item != null) {
            itemManager.removeItem(Tile.getCurrMapID() + "", item);
            if (item.isConsumable()) {
                Inventory.getInstance().addItem(item);
            }
        }
    }

    // INPUT LISTENER
    @Override
    public void onPlayerOpenInventory() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerInteract() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayerMove(Event event) {
        events.add(event);
    }

    @Override
    public void onUseSelectedItem(Event event) {
        events.add(event);
    }

    @Override
    public void OnSelectItem(Event event) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onEvent(Event event) {
        events.add(event);
    }

    public void clearNpc() {
        npcs.clear();
    }
    
    public ItemManager getItemManager() {
        return itemManager;
    }
}
