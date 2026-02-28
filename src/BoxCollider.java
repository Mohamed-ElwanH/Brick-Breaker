public class BoxCollider extends Collider {

    private GameObject gameObject;
    public BoxCollider(int posX, int posY, int width, int height, GameObject colliderParent)
    {
        super(posX, posY, width, height);
        this.gameObject = colliderParent;
    }

    @Override
    public void OnCollisionEnter() {
        gameObject.onHit();
    }
}
