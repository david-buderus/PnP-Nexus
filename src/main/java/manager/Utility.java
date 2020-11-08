package manager;

import city.Town;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.item.*;
import model.loot.DungeonLootFactory;
import model.upgrade.UpgradeFactory;
import model.upgrade.UpgradeModel;
import ui.utility.MemoryView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public abstract class Utility {

	private static final Random rand = new Random();

	public static final Collection<String> rarities = Arrays.asList("gew\u00f6hnlich", "selten", "episch", "legend\u00e4r");
	public static final ListProperty<Armor> armorList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<DungeonLootFactory> dungeonLootList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<Event> eventList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<Jewellery> jewelleryList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<Plant> plantList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<Spell> spellList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<Weapon> weaponList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<UpgradeFactory> upgradeList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<UpgradeModel> upgradeModelList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<Item> itemList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<CraftingBonus> craftingBonusList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<Fabrication> fabricationList = new SimpleListProperty<>(FXCollections.observableArrayList());
	public static final ListProperty<Inconsistency> inconsistencyList = new SimpleListProperty<>(FXCollections.observableArrayList());

	public static final MapProperty<UpgradeModel, UpgradeFactory> upgradeMap = new SimpleMapProperty<>(FXCollections.observableHashMap());

	public static final ListProperty<Town> townList = new SimpleListProperty<>(FXCollections.observableArrayList());

	public static MemoryView memoryView;

	public static final BooleanBinding inconsistent = Bindings.isEmpty(inconsistencyList).not();

	public static void init(){
	    upgradeList.addListener((ob, o, n) ->{
	    	upgradeMap.clear();
            ObservableList<UpgradeModel> list = FXCollections.observableArrayList();

            for (UpgradeFactory factory : n){
                Collection<UpgradeModel> models = factory.getModels();
            	list.addAll(models);

            	for(UpgradeModel model : models){
            		upgradeMap.put(model, factory);
				}
            }

            upgradeModelList.set(list);
        });
    }

    public static Item getItem(String name){
		Item item = new Item();
		item.setName(name);
		return itemList.stream().filter(x -> x.getName().toLowerCase().equals(name.toLowerCase()))
				.findFirst().orElse(item);
	}

	public static String getRarity() {
		double percent = rand.nextDouble();

		if (percent < 0.01) {
			return "legend\u00e4r";
		}
		if (percent < 0.05) {
			return "episch";
		}
		if (percent < 0.20) {
			return "selten";
		}
		return "gew\u00f6hnlich";
	}

	public static double getRarity(String rarity) {
		if (rarity.equals("legend\u00e4r")) {
			return 0.01;
		}
		if (rarity.equals("episch")) {
			return 0.05;
		}
		if (rarity.equals("selten")) {
			return 0.20;
		}
		return 1;
	}

	public static Collection<String> getMaterial() {
		double percent = rand.nextDouble();

		if (percent < 0.01) {
			return Arrays.asList("Adamantium", "Drachenschuppe", "Weltenholz");
		}
		if (percent < 0.1) {
			return Arrays.asList("Orichalcum", "Wei\u00dfgold", "Verzaubertes Entholz", "Magisches Pergament", "Verstärktes Bestienleder");
		}
		if (percent < 0.25) {
			return Arrays.asList("Mithril", "Platin", "Entholz", "Magisches Papier", "Bestienleder");
		}
		if (percent < 0.5) {
			return Arrays.asList("Stahl", "Gold", "Verzaubertes Holz", "Verzauberter Stoffballen", "Verstärktes Leder");
		}
		if (percent < 0.9) {
			return Arrays.asList("Eisen", "Silber", "Leder", "Pergament");
		}
		return Arrays.asList("Stein", "Holz", "Stoff", "Papier");
	}

	public static String asRomanNumber(int x){
		String in = String.valueOf(x);
		StringBuilder out = new StringBuilder();

		for(int i=0; i<in.length(); i++){
			String[] symbols = getSymbols((int) Math.pow(10, i));
			int digit = Integer.parseInt(String.valueOf(in.charAt(in.length()-1-i)));

			if(digit == 4) {
				out.insert(0,symbols[0] + symbols[1]);
				continue;
			}
			if(digit == 9) {
				out.insert(0,symbols[0] + symbols[2]);
				continue;
			}
			if(digit > 4){
				for(int j=5; j<digit; j++){
					out.insert(0, symbols[0]);
				}
				out.insert(0, symbols[1]);
				continue;
			}
			for(int j=0; j<digit; j++){
				out.insert(0, symbols[0]);
			}
		}
		return out.toString();
	}

	private static String[] getSymbols(int pot){
		switch (pot){
			case 1: return new String[]{"I", "V", "X"};
			case 10: return new String[]{"X", "L", "C"};
			case 100: return new String[]{"C", "D", "M"};
			default: return new String[]{"", "", ""};
		}
	}

	/**
	 * Visualises the amount of copper coins
	 * ins copper, silver and gold
	 *
	 * @param cost amount of copper coins
	 * @return human-readable format
	 */
	public static String visualiseSell(int cost){
		String copper = cost % 100 + "K";
		cost /= 100;
		String silver = cost % 100 + "S";
		cost /= 100;
		String gold = cost + "G";

		StringBuilder result = new StringBuilder();

		if(!gold.equals("0G")){
			result.append(gold).append(" ");
		}
		if(!silver.equals("0S")){
			result.append(silver).append(" ");
		}
		if(!copper.equals("0K")){
			result.append(copper);
		}

		if(result.length() == 0){
			return "0K";
		}

		return result.toString();
	}

	public static int calculateWeaponDamage(int tier, int step, int startValue){
		if(tier < 1){
			return startValue - step;
		} else if(tier < 4){
			return calculateWeaponDamage(tier-1, step, startValue) + step;
		} else {
			return calculateWeaponDamage(tier-1, step, startValue) + 2*step;
		}
	}

	public static Armor generateCommonHeavyArmor(int tier, String name, String position){
		return generateCommonHeavyArmor(tier, name, position, 0);
	}

	public static Armor generateCommonHeavyArmor(int tier, String name, String position, int weight){
		Armor armor = new Armor();
		armor.setName(name);
		armor.setWeight(weight);
		armor.setTier(tier);
		armor.setSubTyp(position);
		armor.setProtection(Utility.calculateHeavyArmorProtection(tier, position));
		armor.setRarity("gewöhnlich");
		return armor;
	}

	private static int calculateHeavyArmorProtection(int tier, String position){
		switch (position){
			case "Kopf":
			case "Arme":
				return calculateHeavyArmorProtection(tier, 3);
			case "Oberkörper":
				return calculateHeavyArmorProtection(tier, 5);
			case "Beine":
				return calculateHeavyArmorProtection(tier, 4);
		}
		return 0;
	}

	private static int calculateHeavyArmorProtection(int tier, int startValue){
		if(tier < 1){
			return startValue - 2;
		} else if(tier == 3){
			return calculateHeavyArmorProtection(tier-1, startValue);
		} else {
			return calculateHeavyArmorProtection(tier-1, startValue) + 2;
		}
	}

	public static Armor generateCommonLightArmor(int tier, String name, String position){
		return generateCommonLightArmor(tier, name, position, 0);
	}

	public static Armor generateCommonLightArmor(int tier, String name, String position, int weight){
		Armor armor = new Armor();
		armor.setName(name);
		armor.setWeight(weight);
		armor.setTier(tier);
		armor.setSubTyp(position);
		armor.setProtection(Utility.calculateLightArmorProtection(tier, position));
		armor.setRarity("gewöhnlich");
		return armor;
	}

	public static int calculateLightArmorProtection(int tier, String position){
		switch (position){
			case "Kopf":
			case "Arme":
				return calculateLightArmorProtection(tier, 1);
			case "Oberkörper":
				return calculateLightArmorProtection(tier, 3);
			case "Beine":
				return calculateLightArmorProtection(tier, 2);
		}
		return 0;
	}

	private static int calculateLightArmorProtection(int tier, int startValue){
		if(tier < 1){
			return startValue - 1;
		} else if(tier == 5){
			return calculateLightArmorProtection(tier-1, startValue) + 2;
		} else {
			return calculateLightArmorProtection(tier-1, startValue) + 1;
		}
	}
}
