public class Brick extends GameObject {

    private boolean isDestroyed;
    public Brick(int posX, int posY, int width, int height, String path)
    {
        super(posX, posY, width, height, path);
        this.gameObjCollider = new BoxCollider(posX, posY, width, height, this);
        isDestroyed = false;
    }
    public boolean isDestroyed()
    {
        return isDestroyed;
    }

    @Override
    public void update() {

    }

    @Override
    public void onHit() {
        isDestroyed = true;
        //if this is not null:
        //destroy brick
    }
}
