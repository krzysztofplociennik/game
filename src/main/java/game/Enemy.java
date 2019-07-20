package game;


import javafx.scene.image.ImageView;

public class Enemy {
    private ImageView imageView;
    private boolean shootAbility = false;
    private int ID;

    public Enemy(ImageView imageView, boolean shootAbility) {
        this.imageView = imageView;
        this.shootAbility = shootAbility;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public boolean isShootAbility() {
        return shootAbility;
    }

    public void setShootAbility(boolean shootAbility) {
        this.shootAbility = shootAbility;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
