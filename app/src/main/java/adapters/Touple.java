package adapters;

public class Touple<A, B> {

    private A a;

    private B b;

    public Touple() {
    }

    public Touple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public Touple<A, B> setA(A a) {
        this.a = a;
        return this;
    }

    public B getB() {
        return b;
    }

    public Touple<A, B> setB(B b) {
        this.b = b;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Touple<?, ?> touple = (Touple<?, ?>) o;

        if (a != null ? !a.equals(touple.a) : touple.a != null) {
            return false;
        }
        return b != null ? b.equals(touple.b) : touple.b == null;
    }

    @Override
    public int hashCode() {
        int result = a != null ? a.hashCode() : 0;
        result = 31 * result + (b != null ? b.hashCode() : 0);
        return result;
    }
}
