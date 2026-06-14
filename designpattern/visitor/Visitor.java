package visitor;

// Visitor interface — one visit() method per element type
public interface Visitor {
    void visit(VegItem item);
    void visit(NonVegItem item);
    void visit(BeverageItem item);
    void visit(AlcoholicItem item);
}
