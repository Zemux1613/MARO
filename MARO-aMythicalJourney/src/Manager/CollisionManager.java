package Manager;

import Manager.TileManager.Tile;
import Npc.NPC;
import Objects.Item;
import Objects.MapItem;
import Objects.Player;
import Rendering.GameImage;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    private static CollisionManager instance = null;
    private boolean[][] obstacles;
    private ArrayList<Item> items;
    private ArrayList<NPC> npcs;
    private int tileSize;

    public static CollisionManager getInstance() {
        if (instance == null) {
            instance = new CollisionManager();
        }
        return instance;
    }

    public CollisionManager() {
        this.obstacles = new boolean[0][0];
		this.npcs = new ArrayList<NPC>();
        this.items = new ArrayList<>();
        this.tileSize = 64;
    }

//ADD OBJECTS WITH COLLISION
    public void addObstacle(int x, int y) {
        this.obstacles[x][y] = true;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void addNpc(NPC npc) {
        this.npcs.add(npc);
    }

//TEST FOR A COLLISION
    public boolean getObstacleCollision(int x, int y) {
        return this.obstacles[x / this.tileSize][y / this.tileSize];
    }

    public NPC getNPCCollision() {
        Rectangle playerBBox = GameManager.getInstance().GetPlayer().getBoundingBox();

        for (int i = 0; i < this.npcs.size(); i++) {
            NPC npc = this.npcs.get(i);

            Rectangle npcBBox = new Rectangle(npc.getX(), npc.getY(), npc.getWidth(), npc.getHeight());

            if (playerBBox.intersects(npcBBox)) {
                return npc;
            }
        }
        return null;
    }

    public Item getItemCollision() {
        Rectangle playerBBox = GameManager.getInstance().GetPlayer().getBoundingBox();
        for (Item item : GameManager.getInstance().getItemManager().getItemsForMap(TileManager.getMapID().toString())) {
            Rectangle itemBBox = new Rectangle(((MapItem) item).getX(), ((MapItem) item).getY(), ((MapItem) item).getWidth(), ((MapItem) item).getHeight());
            
            if (playerBBox.intersects(itemBBox)) {
                GameManager.getInstance().getItemManager().removeItem(TileManager.getMapID().toString(), item);
                //if (Inventory.addItem(item)) {
                //	this.items.remove(i);
                //}
                return item;
            }
        }
        return null;
    }

    public static boolean playerCollidesWithObstacle() {
        Point p = GameManager.getInstance().GetPlayer().getTileInFront();
        Tile t = TileManager.getObstacal(p.y, p.x);
        return t.isObstacal();
    }

    public boolean collidesWithObstacle(NPC npc, List<GameImage> gameImages) {
        for (int i = 0; i < gameImages.size(); i++) {
            if (npc.getBoundingBox().intersects(gameImages.get(i).getBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesWithObstacle(NPC npc, Player player) {
        if (npc.getBoundingBox().intersects(player.getBoundingBox())) {
            return true;
        }
        return false;
    }

    // collides with map item -> if consumable put in inventory
    public MapItem collidesWithMapItem(Player player, List<MapItem> mapItems) {
        if (mapItems != null) {
            for (int i = 0; i < mapItems.size(); i++) {
                if (player.getBoundingBox().intersects(mapItems.get(i).getBoundingBox())) {
                    return mapItems.get(i);
                }
            }
        }
        return null;
    }
}
