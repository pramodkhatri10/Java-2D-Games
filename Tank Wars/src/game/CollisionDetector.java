package game;

import java.awt.*;
import java.util.ArrayList;

class CollisionDetector {

    ArrayList<Object> collide (ArrayList<Object> GameObj) {

        for (int i = 0; i < GameObj.size(); i++) {

            for (int j = i; j < GameObj.size(); j++) {
                Object ObjTank1 = GameObj.get(i);
                Object ObjTank2 = GameObj.get(j);

                if (i != j) {

                    if (ObjTank1 instanceof Bullet && ObjTank2 instanceof Tank && !(((Bullet) ObjTank1).getOwner().equals(((Tank) ObjTank2).getTag())) && !((Bullet) ObjTank1).BulletCollision) {
                        if (ObjTank1.getShape.intersects(ObjTank2.getShape)) {
                            ObjTank1.collision();
                            ((Bullet) ObjTank1).setSmallExplosion(false); //Utilizes large explosion image
                            ObjTank2.collision();
                        }


                    }
                    if (ObjTank1 instanceof Tank && ObjTank2 instanceof Bullet && !((Bullet) ObjTank2).getOwner().equals(((Tank) ObjTank1).getTag()) && !((Bullet) ObjTank2).BulletCollision) {
                        if (ObjTank1.getShape.intersects(ObjTank2.getShape)) {
                            ((Bullet) ObjTank2).setSmallExplosion(false); //Utilizes large explosion image
                            ObjTank2.collision();
                            ObjTank1.collision();
                        }

                    }

                    if (((ObjTank2 instanceof Bullet && ObjTank1 instanceof WallBreakable && !((Bullet) ObjTank2).BulletCollision))) {
                        if (ObjTank1.getShape.intersects(ObjTank2.getShape)) {
                            ObjTank2.collision();
                            ObjTank1.collision();
                        }

                    }


                    if (ObjTank1 instanceof Tank && ObjTank2 instanceof WallBreakable) {
                        Rectangle r1 = ((Tank) ObjTank1).getOffsetBounds();
                        if (r1.intersects(ObjTank2.getShape)) {

                            ((Tank) ObjTank1).setNoMovement(true);

                        }

                    }

                    if (ObjTank1 instanceof WallBreakable && ObjTank2 instanceof Tank) {

                        Rectangle r2 = ((Tank) ObjTank2).getOffsetBounds();
                        if (r2.intersects(ObjTank1.getShape)) {

                            ((Tank) ObjTank2).setNoMovement(true);

                        }

                    }

                    if (ObjTank1 instanceof Tank && ObjTank2 instanceof PowerUp) {
                        if (ObjTank1.getShape.intersects(ObjTank2.getShape)) {
                            if (((PowerUp) ObjTank2).HealthGain) {
                                ((Tank) ObjTank1).setHealth(100);
                                System.out.println("Health Power Up Used");
                                GameObj.remove(j);

                            }
                            if (((PowerUp) ObjTank2).BoostGain) {
                                ((Tank) ObjTank1).setAddBoost(System.currentTimeMillis());
                                ((Tank) ObjTank1).setSpeedBoost(true);
                                System.out.println("Boost Power up Used");
                                GameObj.remove(j);
                            }
                        }

                    }
                }

            }
        }

        return GameObj;
    }
}
