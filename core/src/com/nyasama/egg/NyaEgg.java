package com.nyasama.egg;

import cn.ac.nya.nsgdx.NSGDX;
import cn.ac.nya.nsgdx.entity.Exectuor;
import cn.ac.nya.nsgdx.utility.IObject;
import cn.ac.nya.nsgdx.utility.Renderer;
import cn.ac.nya.nsgdx.utility.Utility;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by drzzm on 2018.2.14.
 */
public class NyaEgg extends NSGDX {

    public class Bullet extends cn.ac.nya.nsgdx.entity.Bullet {

        public Bullet(Vector2 pos, Vector2 aim, Texture tex) {
            super(pos, aim, tex);
            scale = 0.25F;
        }

        @Override
        public Result onUpdate(int t) {
            final float border = 32.0F;
            if (pos.x > devWidth + border || pos.x < -border) return Result.END;
            if (pos.y > devHeight + border || pos.y < -border) return Result.END;

            return super.onUpdate(t);
        }

    }

    public class Launcher extends Exectuor {

        private int t;

        public Launcher(int t) {
            this.t = t;
        }

        @Override
        public Result onUpdate(int t) {
            if (t == this.t) {
                Vector2 center = Utility.vec2h(devWidth, devHeight);
                Bullet[] bullets = new Bullet[128];
                for (int i = 0; i < bullets.length; i++) {
                    float angle = (float) i / (float) bullets.length * 360.0F;
                    Vector2 pos = Utility.vec2(1.0F, 0.0F).rotate(angle).nor().scl(8.0F);
                    bullets[i] = new Bullet(center.cpy().add(pos), center, get("arrow"));
                    Vector2 vec = Utility.vec2(1.0F, 0.0F).rotate(angle);
                    bullets[i].setVel(vec).setAcc(vec.rotate(90).scl(0.05F));

                    Utility.Color3 color = Utility.hsv2RGB(angle, 1.0F, 1.0F);
                    bullets[i].setColor(color.r, color.g, color.b);

                    poolCluster.add(bullets[i]);
                }
                return Result.END;
            }
            return Result.DONE;
        }
    }

    @Override
    protected void render(Renderer renderer) {
        renderer.begin();
        renderer.drawString(4, 16, 1, Color.WHITE, "frame: " + counter());
        renderer.drawString(4, devHeight - 8, 1, Color.WHITE, poolCluster.toString());
        renderer.end();
    }

    @Override
    protected void loadAssets() {
        load("dot");
        load("star");
        load("arrow");
    }

    @Override
    protected void initEntity() {
        poolCluster.add(new Exectuor() {
            @Override
            public Result onUpdate(int t) {
                if (t % 640 == 0) {
                    for (int i = 1; i <= 64; i++)
                        poolCluster.add(new Launcher(i * 10 + t));
                }
                return Result.DONE;
            }
        });
    }

}
