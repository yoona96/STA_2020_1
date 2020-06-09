public abstract class Function {

    public Function() {}

    protected int mode;
    protected int fid;

    abstract protected void cancel();
    protected void changeMode(int mode) {}
    public abstract void changeValue(int diff);
    public abstract void changeType();
    protected abstract int getMode();
}