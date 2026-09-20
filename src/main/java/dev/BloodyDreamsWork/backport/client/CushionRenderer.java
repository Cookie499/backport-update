package dev.BloodyDreamsWork.backport.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.BloodyDreamsWork.backport.Backport;
import dev.BloodyDreamsWork.backport.content.CushionEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class CushionRenderer extends EntityRenderer<CushionEntity, CushionRenderer.State> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Backport.MODID, "cushion"), "main");

    private static final Map<DyeColor, Identifier> TEXTURES = textures();

    private final ModelPart root;

    public CushionRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.root = context.bakeLayer(LAYER);
        this.shadowRadius = 0.5F;
    }

    public static LayerDefinition createLayer() {
        // Vanilla 26.3 CushionModel.createBodyLayer shrinks the box by 0.005px per face with a
        // negative CubeDeformation instead of offsetting the model's depth. That keeps the bottom
        // face off the support block's surface, so the plain (unculled) entityCutout render type
        // is enough and no extra translate is needed.
        MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("cushion",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-8.0F, -4.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(-0.005F)),
                PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(CushionEntity entity, State state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.color = entity.getColor();
        state.yRot = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yRot));
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        collector.submitModelPart(this.root, poseStack,
                RenderTypes.entityCutout(TEXTURES.get(state.color)),
                state.lightCoords, OverlayTexture.NO_OVERLAY, null);

        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }

    private static Map<DyeColor, Identifier> textures() {
        Map<DyeColor, Identifier> result = new EnumMap<>(DyeColor.class);
        for (DyeColor color : DyeColor.values()) {
            result.put(color, Identifier.fromNamespaceAndPath(Backport.MODID,
                    "textures/entity/cushion/" + color.getName() + "_cushion.png"));
        }
        return Collections.unmodifiableMap(result);
    }

    public static class State extends EntityRenderState {
        public DyeColor color = DyeColor.WHITE;
        public float yRot;
    }
}
