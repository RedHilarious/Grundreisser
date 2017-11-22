package business.domainModel;

import java.util.ArrayList;

import Exceptions.FurnitureException;
/**
 * Klasse ist ein Moder eines Inventars.
 * Implementierung wird nicht genutzt.
 * @author Rahel Habacker, Jonas Theis, Elizabeth Schneiß
 */
public class Inventory {
	private ArrayList<Furniture> furniture;
	
	public Inventory(String name){
		this.furniture = new ArrayList<Furniture>();
	}
	
	public void addFurniture(Furniture newFurniture){
		furniture.add(newFurniture);
	}
	
	public Furniture getFurnit(int i){
		return furniture.get(i);
	}
	
	public ArrayList<Furniture> getFurniture(){
		return furniture;
	}
	
	public void removeFurniture(Furniture rmFurniture)throws FurnitureException{
		if(!furniture.remove(rmFurniture)){
			throw new FurnitureException("Object not in Inventory");
		}
	}
	
	public void delFurniture(Furniture delFurniture)throws FurnitureException{
		if(!furniture.remove(delFurniture)){
			throw new FurnitureException("Object not in Inventory");
		}
		delFurniture = null;
	}
}