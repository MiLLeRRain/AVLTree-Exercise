//public class BTree {
//
//    private Person root;
//
//    public BTree() {
//        this.root = new Person("Karstern");
//    }
//
//    public void addPeople(Person p) {
//        addPeople(root, p);
//    }
//
//    private void addPeople(Person addTo, Person p) {
//        if (p.compareTo(addTo) < 0) {
//            if (addTo.getLeft() == null) {
//                addTo.setLeft(p);
//            } else addPeople(addTo.getLeft(), p);
//        } else {
//            if (addTo.getRight() == null) {
//                addTo.setRight(p);
//            } else addPeople(addTo.getRight(), p);
//        }
//
//    }
//
//    public void printAll() {
//        printDP(root, ""); // Depth first, Pre-order
//    }
//
//    private void printDP(Person root, String s) {
//        if (root != null) {
//
//            printDP(root.getLeft(), s+"-");
//            printDP(root.getRight(), s+"-");
//
//            System.out.println(s + root);
//        }
//    }
//
//}
