package com.github.laefye.cinema.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.*;
import net.minecraft.util.math.Vec3f;

public class ScreenRenderer implements WorldRenderEvents.AfterTranslucent {
    private final Screen parent;

    public ScreenRenderer(Screen screen) {
        this.parent = screen;
    }

    @Override
    public void afterTranslucent(WorldRenderContext context) {
        //
        RenderSystem.enableTexture();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //
        RenderSystem._setShaderTexture(0, parent.playerTexture.textureId);
        //
        var cameraPos = context.camera().getPos();
        var matrix = context.matrixStack();
        matrix.push();

        matrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        matrix.translate(parent.info.x, parent.info.y, parent.info.z);
        matrix.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) parent.info.rotation));

        var matrix4f = matrix.peek().getPositionMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, 0.5f * parent.info.width, 0.5f * parent.info.height, 0).texture(1, 0).next();
        bufferBuilder.vertex(matrix4f, -0.5f * parent.info.width, 0.5f * parent.info.height, 0).texture(0, 0).next();
        bufferBuilder.vertex(matrix4f, -0.5f * parent.info.width, -0.5f * parent.info.height, 0).texture(0, 1).next();
        bufferBuilder.vertex(matrix4f, 0.5f * parent.info.width, -0.5f * parent.info.height, 0).texture(1, 1).next();
        tessellator.draw();

        matrix.pop();
    }
}
