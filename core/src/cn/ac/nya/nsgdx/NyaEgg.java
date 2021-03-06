package cn.ac.nya.nsgdx;

import cn.ac.nya.nsgdx.entity.Exectuor;
import cn.ac.nya.nsgdx.utility.Renderer;
import cn.ac.nya.nsgdx.utility.Utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import static cn.ac.nya.nsgdx.utility.Renderer.Texture;

/**
 * Created by drzzm on 2018.2.14.
 */
public class NyaEgg extends NSGDX {

    public class Bullet extends cn.ac.nya.nsgdx.entity.Bullet {

        public Player thePlayer;

        public Bullet(Vector2 pos, Vector2 aim, Texture tex) {
            super(pos, aim, tex);
            scale = 0.25F;
        }

        @Override
        public Result onUpdate(int t) {
            final float border = 32.0F;
            if (pos.x > devWidth + border || pos.x < -border) return Result.END;
            if (pos.y > devHeight + border || pos.y < -border) return Result.END;

            if (thePlayer.doJudge(pos.cpy())) return Result.END;

            return super.onUpdate(t);
        }

    }

    public class Player extends cn.ac.nya.nsgdx.entity.Player {

        public int miss = 0;
        public Vector2 vel;

        public Player(Texture tex) {
            super(tex);

            vel = Utility.vec2(0, 0);
        }

        @Override
        public Result onUpdate(int t) {
            Vector2 vec;

            if (Gdx.input.isTouched()) {
                vec = Utility.vec2((float) Gdx.input.getDeltaX(), (float) Gdx.input.getDeltaY());
                vec = vec.scl(0.5F).scl(1.0F, -1.0F);
            } else {
                if (Gdx.input.isKeyPressed(Input.Keys.UP))
                    vel.add(0, 0.1F);
                else if (vel.y > 0) vel.y = 0;
                if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
                    vel.add(0, -0.1F);
                else if (vel.y < 0) vel.y = 0;
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    vel.add(-0.1F, 0);
                else if (vel.x < 0) vel.x = 0;
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    vel.add(0.1F, 0);
                else if (vel.x > 0) vel.x = 0;

                vec = vel.cpy();
                if (vec.len() > 0.5F) vec = vec.nor().scl(0.5F);

                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                    vec = vec.scl(2.0F);
                else
                    vec = vec.scl(8.0F);
            }

            move(vec);

            return super.onUpdate(t);
        }

        public boolean doJudge(Vector2 vec) {
            final float len = 4.0F; vec.sub(pos);
            if (Math.abs(vec.x) < len && Math.abs(vec.y) < len) {
                if (vec.len() < len) {
                    miss += 1;
                    return true;
                }
            }
            return false;
        }

    }

    public class ArrowLauncher extends Exectuor {

        private int t;
        private Vector2 center;

        private Player thePlayer;

        public ArrowLauncher(int t, Player player) {
            this.t = t;
            this.center = Utility.vec2h(devWidth, devHeight);
            thePlayer = player;
        }

        @Override
        public Result onUpdate(int t) {
            if (t == this.t) {
                Bullet[] bullets = new Bullet[64];
                for (int i = 0; i < bullets.length; i++) {
                    float angle = (float) i / (float) bullets.length * 360.0F;
                    angle += (360.0F * (float) Math.abs(Math.sin((float) t / 256.0F)));
                    angle %= 360.0F;
                    Vector2 pos = Utility.vec2(1.0F, 0.0F).rotate(angle).nor().scl(8.0F);
                    bullets[i] = new Bullet(center.cpy().add(pos), center, get("arrow"));
                    Vector2 vec = Utility.vec2(1.0F, 0.0F).rotate(angle);
                    bullets[i].setVel(vec).setAcc(vec.rotate(90).scl(0.05F));

                    angle = colorMod(t, angle);
                    angle %= 360.0F;
                    Utility.Color3 color = Utility.hsv2RGB(angle, 1.0F, 1.0F);
                    bullets[i].setColor(color.r, color.g, color.b);

                    bullets[i].thePlayer = thePlayer;

                    poolCluster.add(bullets[i]);
                }
                return Result.END;
            }
            return Result.DONE;
        }

        public float colorMod(int t, float angle) {
            if ((t % 1280) < 640) angle = (float) t;
            else angle += (t % 360);
            return angle;
        }
    }

    public class StarLauncher extends Exectuor {

        private int t;
        private Vector2 center;

        private Player thePlayer;

        public StarLauncher(int t, Player player) {
            this.t = t;
            this.center = Utility.vec2h(devWidth, devHeight);
            thePlayer = player;
        }

        @Override
        public Result onUpdate(int t) {
            if (t == this.t) {
                Bullet[] bullets = new Bullet[64];
                for (int i = 0; i < bullets.length; i++) {
                    float angle = (float) i / (float) bullets.length * 360.0F;
                    angle += (360.0F * (float) Math.abs(Math.sin((float) t / 256.0F)));
                    angle %= 360.0F;
                    Vector2 pos = Utility.vec2(1.0F, 0.0F).rotate(angle).nor().scl(8.0F);
                    bullets[i] = new Bullet(center.cpy().add(pos), center, get("star"));
                    Vector2 vec = Utility.vec2(1.0F, 0.0F).rotate(angle);
                    bullets[i].setVel(vec).setAcc(vec.rotate(90).scl(0.075F * (float) Math.abs(Math.sin((float) t / 128.0F))));

                    angle = colorMod(t, angle);
                    angle %= 360.0F;
                    Utility.Color3 color = Utility.hsv2RGB(angle, 1.0F, 1.0F);
                    bullets[i].setColor(color.r, color.g, color.b);

                    bullets[i].thePlayer = thePlayer;

                    poolCluster.add(bullets[i]);
                }
                return Result.END;
            }
            return Result.DONE;
        }

        public float colorMod(int t, float angle) {
            if ((t % 1280) < 640) angle = (float) t;
            else angle += (t % 360);
            return angle;
        }
    }

    @Override
    protected void render(Renderer renderer) {
        renderer.begin();
        renderer.drawString(4, 16, 1, Color.WHITE, "frame: " + poolCluster.tickTime());
        renderer.drawString(4, devHeight - 4, 1, Color.WHITE, poolCluster.toString());

        if (player != null) {
            renderer.drawString(devWidth - 96, devHeight - 32, 2, Color.valueOf("FF9800"), "MISS");
            renderer.drawString(devWidth - 96, devHeight - 64, 2, Color.valueOf("FF9800"), Integer.toString(player.miss));
        }

        renderer.end();
    }

    @Override
    protected void loadAssets() {
        load("dot");
        load("star");
        load("arrow");
    }

    Player player;

    @Override
    protected void initEntity() {
        player = new Player(get("dot"));
        player.look(Utility.vec2h(devWidth, devHeight));
        player.flash(Utility.vec2h(devWidth, devHeight).scl(1.0F, 0.5F));
        poolCluster.add(player);

        poolCluster.add(new Exectuor() {
            int type = 0;

            @Override
            public Result onUpdate(int t) {
                if ((t % 320) == 0) {
                    if (type == 0) {
                        for (int i = 0; i < 64; i++)
                            poolCluster.add(new ArrowLauncher(i * 5 + t, player));
                        type = 1;
                    } else {
                        for (int i = 0; i < 64; i++)
                            poolCluster.add(new StarLauncher(i * 5 + t, player));
                        type = 0;
                    }
                }
                return Result.DONE;
            }
        });
    }

}
