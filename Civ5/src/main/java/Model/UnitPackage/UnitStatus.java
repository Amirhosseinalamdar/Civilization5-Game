package Model.UnitPackage;

import Model.ImageBase;
import javafx.scene.image.Image;

public enum UnitStatus {
    ACTIVE(null),
    SLEEP(ImageBase.SLEEP_ICON.getImage()),
    ALERT(ImageBase.ALERT_ICON.getImage()),
    FORTIFY(ImageBase.FORTIFY_ICON.getImage()),
    HEAL(ImageBase.HEAL_ICON.getImage()),
    SETTLE(null),
    SIEGEPREP(ImageBase.SETUP_RANDED_ICON.getImage()),
    ATTACK(ImageBase.ATTACK_ICON.getImage()),
    RAID(null),
    FOUND_CITY(null),
    DO_NOTHING(ImageBase.DO_NOTHING_ICON.getImage()),
    BUILD_IMPROVEMENT(null),
    MOVE(null),
    CLEAR_LAND(ImageBase.AXE_ICON.getImage()),
    WAKE(null),
    CANCEL_MISSION(null),
    HAS_PATH(null),
    PILLAGE(ImageBase.PILLAGE_ICON.getImage()),
    REPAIR(null),
    GARRISON(ImageBase.GARRISON_ICON.getImage());
    private final Image image;

    UnitStatus(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
