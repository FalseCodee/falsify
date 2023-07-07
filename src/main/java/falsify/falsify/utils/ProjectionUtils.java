package falsify.falsify.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import falsify.falsify.mixin.MixinGameRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ProjectionUtils {
    public static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Matrix4f modelMatrix = new Matrix4f();
    private static final Matrix4f projectionMatrix = new Matrix4f();
    private static Camera camera = mc.gameRenderer.getCamera();

    public static void update(float tickDelta) {
        camera = mc.gameRenderer.getCamera();
        modelMatrix.set(RenderSystem.getModelViewMatrix());
        double fov = ((MixinGameRenderer.Accessor)mc.gameRenderer).getCurrentFov(camera, tickDelta, true);
        projectionMatrix.set(mc.gameRenderer.getBasicProjectionMatrix(fov));
        projectionMatrix.rotate(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        projectionMatrix.rotate(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
    }
    public static Vec2f toScreenXY(Vec3d pos) {
        Vector4f transformedMatrix = getTransformedMatrix(pos);
        int width = mc.getWindow().getScaledWidth();
        int height = mc.getWindow().getScaledHeight();

        transformedMatrix.x = width / 2f + (0.5f * transformedMatrix.x * width + 0.5f);
        transformedMatrix.y = height / 2f - (0.5f * transformedMatrix.y * height + 0.5f);

        return new Vec2f(transformedMatrix.x, transformedMatrix.y);
    }

    private static Vector4f getTransformedMatrix(Vec3d pos) {
        Vec3d relativePos = camera.getPos().subtract(pos);
        Vector4f vector4f = new Vector4f((float) relativePos.x, (float) relativePos.y, (float) relativePos.z, 1f);

        transform(vector4f, modelMatrix);
        transform(vector4f, projectionMatrix);

        if (vector4f.w > 0.0f) {
            vector4f.x *= -100000;
            vector4f.y *= -100000;
        } else {
            float invert = 1f / vector4f.w;
            vector4f.x *= invert;
            vector4f.y *= invert;
        }

        return vector4f;
    }

    private static void transform(Vector4f vec, Matrix4f matrix) {
        float x = vec.x;
        float y = vec.y;
        float z = vec.z;
        vec.x = x * matrix.m00() + y * matrix.m10() + z * matrix.m20() + matrix.m30();
        vec.y = x * matrix.m01() + y * matrix.m11() + z * matrix.m21() + matrix.m31();
        vec.z = x * matrix.m02() + y * matrix.m12() + z * matrix.m22() + matrix.m32();
        vec.w = x * matrix.m03() + y * matrix.m13() + z * matrix.m23() + matrix.m33();
    }
}
