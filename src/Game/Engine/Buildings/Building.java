package Game.Engine.Buildings;
import Game.Engine.Colonist.Career.Profession.Profession;
import Game.Engine.Colonist.Colonist;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import Game.Engine.Inventory.Inventory;
import Game.Engine.Inventory.Items.Item;
import Game.Engine.Inventory.Items.ItemStack;
import Game.Engine.Map.Tile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.imageio.ImageIO;

@JsonIgnoreProperties({"colonists"})
public abstract class Building {
    private Inventory inv;
    protected String name;

    private int id;
    private Building prefStor;
    private Image image;

    private BuildingType type;

    private ArrayList<Tile> coords;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private Class<? extends Profession> compatibleProfession;

    protected int colonlimit;

    protected ArrayList<Colonist> colonists;
    protected Profession compatible;

    private Inventory neededResources;


    public Building(String name, int limit,Class<? extends Profession> compatible,int storage,BuildingType type) {
        this.name = name;
        this.colonlimit= limit;
        this.type = type;
        this.compatibleProfession = compatible;
        this.inv = new Inventory(storage);
        colonists = new ArrayList<>();
        this.id = ID_GENERATOR.incrementAndGet();
        File f = new File("Resources/Graphics/" + getClass().getSimpleName() + ".jpg");
        if (f.exists()) {
            try {
                image = ImageIO.read(f);
            } catch (IOException e) {
                image = null;
            }
        }
        this.coords = new ArrayList<>();
        this.neededResources = new Inventory();
        setNeededRes();
        getNeededResources().setMaxWeight((int) getNeededResources().getCurrentWeight());

    }

    public abstract void setNeededRes();
    public Class<? extends Profession> getCompatible() {
        return compatibleProfession;
    }
    public int getColonlimit(){
        return colonlimit;
    }
    public Inventory getNeededResources(){
        return neededResources;
    }
    public void addNeededRes(Item item, int quantity){
        neededResources.add(item,quantity);
    }
    public void setcoords(Tile tile){
        this.coords.add(tile);
    }
    public ArrayList<Tile> getCoords() { return coords; }
    public String getName() {
        return name+" "+ colonists.size();
    }
    public ArrayList<Colonist> getColonists(){
        return colonists;
    }

    public BuildingType getBType(){
        return type;
    }



    public Inventory getInv(){
        return inv;
    }



    @Override
    public String toString() {
        return name+colonists.size();
    }


    public boolean isCompatible(Colonist selected) {
        return (getCompatible() == null||getCompatible().isInstance(selected.getProfession()));
    }
    public boolean isJobCompatible(Colonist selected) {
        if(getCompatible() != null) {
            return (getCompatible().isInstance(selected.getProfession()));
        }
        return false;
    }
    public Image getImage() {
        return image;
    }

    public boolean hasVacancy() {
        return colonists.size() < colonlimit;
    }

    public int getId() { return  id;
    }


    public String getType() {
        return name;
    }

    public void addColonist(Colonist colonist) {
        this.colonists.add(colonist);
    }

    public void setPrefStor(Building stor){
        this.prefStor = stor;
    }

    public Building getPreferredStorage() {

        return prefStor;
    }

    public float getNeededResourcesWeight() {
        return neededResources.getMaxWeight();
    }
}