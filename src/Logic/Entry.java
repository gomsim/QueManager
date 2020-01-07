package Logic;

/**
 * Class representing, and only representing, an entry number.
 */
public class Entry {

    private int number;

    public Entry(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }

    public boolean equals(Object object){
        if (!(object instanceof Entry))
            return false;
        return number == ((Entry) object).number;
    }

    public String toString(){
        return "" + number;
    }
}
