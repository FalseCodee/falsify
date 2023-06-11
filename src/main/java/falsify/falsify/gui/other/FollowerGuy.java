package falsify.falsify.gui.other;

import falsify.falsify.Falsify;
import falsify.falsify.gui.clickgui.Clickable;
import falsify.falsify.gui.editor.Renderable;
import falsify.falsify.utils.MathUtils;
import falsify.falsify.utils.PID;
import falsify.falsify.utils.RenderHelper;
import falsify.falsify.utils.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;

public class FollowerGuy extends Clickable {
    private Vec3d inertia = new Vec3d(0.0, 0.0, 0.0);
    public double mass = 10;
    public FollowerGuy(double x, double y) {
        super(x, y, 5, 5);
        setInertia(new Vec3d(Math.random(), Math.random(), 0));
        this.mass = Math.random() * 5000;
        this.width = this.mass/5000*10+1;
        this.height = this.mass/5000*10+1;
    }

    @Override
    public boolean handleClick(double x, double y, int button) {
        this.x = 100;
        this.y = 100;
        return false;
    }
    public void update(int mouseX, int mouseY, double delta, ArrayList<FollowerGuy> guys) {
        if(x < 0) {
            x = Falsify.mc.currentScreen.width-10;
        }
        if(x+width > Falsify.mc.currentScreen.width) {
            x = 10;
        }
        if(y < 0) {
            y = Falsify.mc.currentScreen.height-10;
        }
        if(y+height > Falsify.mc.currentScreen.height) {
            y = 10;
        }
            //if((mouseX-x)*(mouseX-x) + (mouseY-y)*(mouseY-y) > 5000) {
                for (FollowerGuy guy : guys) {
                    if (guy == this) continue;

                    reverseGravity(new Vec3d(guy.getX(), guy.getY(), 0), guy.mass, delta);
                }
                reverseGravity(new Vec3d(0, y, 0), 5000, delta);
                reverseGravity(new Vec3d(Falsify.mc.currentScreen.width, y, 0), 5000, delta);
                reverseGravity(new Vec3d(x, 0, 0), 5000, delta);
                reverseGravity(new Vec3d(x, Falsify.mc.currentScreen.height - 85, 0), 5000, delta);
            //}
            //reverseGravity(new Vec3d(Falsify.mc.currentScreen.width/2, Falsify.mc.currentScreen.height/2, 0),10000, delta);
        reverseGravity(new Vec3d(mouseX, mouseY, 0),10000, delta);


        x += inertia.getX();
        y += inertia.getY();
        inertia = inertia.multiply(0.900);
    }

    public void doGravity(Vec3d pos, double mass, double delta) {
        Vec3d dir = pos.subtract(new Vec3d(x+width/2, y+height/2, 0));
        double distance = dir.length();
        double forceMag = (0.06674*mass*this.mass)/((distance*distance)+mass/16)*delta;
        addInertia(dir.normalize().multiply(forceMag));
    }

    public void reverseGravity(Vec3d pos, double mass, double delta) {
        Vec3d dir = new Vec3d(x+width/2, y+height/2, 0).subtract(pos);
        double distance = dir.length();
        double forceMag = (0.06674*mass*this.mass)/((distance*distance)+1)*delta;
        addInertia(dir.normalize().multiply(forceMag));
    }
    public void setInertia(Vec3d inertia) {
        this.inertia = inertia;
    }

    public void addInertia(Vec3d inertia) {
        this.inertia = this.inertia.add(inertia.multiply(1/this.mass));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawSmoothRect(RenderHelper.colorLerp(Color.WHITE, Color.ORANGE, MathUtils.clamp((float) (inertia.length()), 0.0F, 1.0F)), matrices, (float) x, (float) y, (float) (x+width), (float) (y+height), (float) width/2.0f, new int[] {5, 5, 5, 5});
        //drawRect(Color.WHITE, matrices, (float) x, (float) y, (float) (x+width), (float) (y+height));
    }
}
