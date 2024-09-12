My class notes

Steps to push code to github:
1. Git add
2. Git commit
3. Git push

Override toString, hashcode, equals for:

Game, Board, Move, Piece, Position

Classes:

public class Person {

    // Field
    private String name;

    // Constructor
    public Person(STring name) {
        this.name = name;
    }
    
    // Method
    public void sayName() {
        System.out.println(name);
    }
}

Static means you can call a method with having an instantiation of the class.

public Class HelloPerson {

    public static void main(String[] args) {
        var person = new Person("James Gosling");
        person.sayName();
    }
}

public enum Peak {
    NEBO, PROVO, SANTAQUIN, TIMPANOGOS, CASCADE, SPANISH, LONE
}

public static void main(String[] args) {

    try {
        var e = Enum.valueOf(Peak.class, args[0].toUpperCase());
        
        if (e == Peak.LONE) {
            System.out.println("You chose Lone Peak");
        }
    } catch (IllegalArgumentException ex) {
        System.out.println("Unknown peak provided");

Object Class (capital O)

toClass()

toString() : String

equals() : boolean

hashCode() : int

clone() : Object --> Used to create copies

wait()

notify()

Wait and notify are used for synchronization.

Overriding equals():

@Override
public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null !! getClass() != o.getClass()) return false;
    EqualExample that = (EqualExample) o;
    return value.equals(that.value);
}

Overriding hashCode():

@Override
public int hashCode() {

    return 71 * value.hashCode();
}
The goal is to nearly uniquely represent each object.

record PetRecord(int id, String name, String type) {}