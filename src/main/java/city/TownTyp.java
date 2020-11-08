package city;

public enum TownTyp {
    bigTown, town, smallTown, bigVillage, village, smallVillage;

    @Override
    public String toString(){
        switch (this) {
            case bigTown:
                return "Große Stadt";
            case town:
                return "Stadt";
            case smallTown:
                return "Kleine Stadt";
            case bigVillage:
                return "Großes Dorf";
            case village:
                return "Dorf";
            case smallVillage:
                return "Kleines Dorf";
        }
        return "Nichts";
    }

    public int getAverage(){
        switch (this) {
            case bigTown:
                return 25000;
            case town:
                return 10000;
            case smallTown:
                return 4000;
            case bigVillage:
                return 1000;
            case village:
                return 500;
            case smallVillage:
                return 100;
        }
        return 0;
    }

    public static TownTyp getTownTyp(int population){
        if(population > bigTown.getAverage()/2){
            return bigTown;
        }
        if(population > town.getAverage()/2){
            return town;
        }
        if(population > smallTown.getAverage()/2){
            return smallTown;
        }
        if(population > bigVillage.getAverage()/2){
            return bigVillage;
        }
        if(population > village.getAverage()/2){
            return village;
        }
        return smallVillage;
    }
}
