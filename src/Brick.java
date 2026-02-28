public class Brick extends GameObject {

    private boolean isDestroyed;
    private String brickType;
    private int hitsToBreak;
    public Brick(int posX, int posY, int width, int height, String type, int hits)
    {
        super(posX, posY, width, height, "/UIElements/" + type + ".png");
        this.setGameObjCollider(new BoxCollider(posX, posY, width, height, this));
        isDestroyed = false;
        brickType = type;
        hitsToBreak = hits;
    }
    public boolean isDestroyed()
    {
        return isDestroyed;
    }
    public String getBrickType()
    {
        return brickType;
    }
    public int getHitsToBreak()
    {
        return hitsToBreak;
    }

    @Override
    public void update() {}

    @Override
    public void onHit()
    {
        if(hitsToBreak > 0)
        {
            hitsToBreak--;
            if(hitsToBreak == 2)
            {
                String newPath = "/UIElements/" + brickType + "Split.png";
                this.setGameObjSprite(this.loadSprite(newPath, this.getGameObjWidth(), this.getGameObjHeight()));
            }
            if(hitsToBreak == 1)
            {
                String newPath = "/UIElements/" + brickType + "Broken.png";
                this.setGameObjSprite(this.loadSprite(newPath, this.getGameObjWidth(), this.getGameObjHeight()));
            }
        }
        if(hitsToBreak == 0)
            isDestroyed = true;
    }

}
