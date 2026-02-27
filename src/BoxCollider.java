public class BoxCollider extends Collider {

    private GameObject gameObject;
    public BoxCollider(int posX, int posY, int width, int height, GameObject other)
    {
        super(posX, posY, width, height);
        this.gameObject = other;
    }

    @Override
    public void OnCollisionEnter() {
        gameObject.onHit();
    }
}
