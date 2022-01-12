package com.bobmowzie.mowziesmobs.client.model.tools.geckolib;

import com.bobmowzie.mowziesmobs.client.model.tools.RigUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.world.phys.*;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public class MowzieGeoBone extends GeoBone {
    private Matrix4f modelSpaceXform;
    private boolean trackXform;
    public Matrix4f rotMat;

    private Matrix4f worldSpaceXform;
    private Matrix3f worldSpaceNormal;

    public MowzieGeoBone() {
        super();
        modelSpaceXform = new Matrix4f();
        modelSpaceXform.setIdentity();
        trackXform = false;
        rotMat = null;

        worldSpaceXform = new Matrix4f();
        worldSpaceXform.setIdentity();
        worldSpaceNormal = new Matrix3f();
        worldSpaceNormal.setIdentity();
    }

    public MowzieGeoBone getParent() {
        return (MowzieGeoBone) parent;
    }

    public boolean isTrackingXform() {
        return trackXform;
    }

    public void setTrackXform(boolean trackXform) {
        this.trackXform = trackXform;
    }

    public Matrix4f getModelSpaceXform() {
        setTrackXform(true);
        return modelSpaceXform;
    }

    public Vec3 getModelPosition() {
        Matrix4f matrix = getModelSpaceXform();
        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix);
        return new Vec3(-vec.getX() * 16f, vec.getY() * 16f, vec.getZ() * 16f);
    }

    public void setModelPosition(Vec3 pos) {
        // TODO: Doesn't work on bones with parent transforms
        MowzieGeoBone parent = getParent();
        Matrix4f identity = new Matrix4f();
        identity.setIdentity();
        Matrix4f matrix = parent == null ? identity : parent.getModelSpaceXform().copy();
        matrix.invert();
        Vector4f vec = new Vector4f(-(float) pos.getX() / 16f, (float) pos.getY() / 16f, (float) pos.getZ() / 16f, 1);
        vec.transform(matrix);
        setPosition(-vec.getX() * 16f, vec.getY() * 16f, vec.getZ() * 16f);
    }

    public Matrix4f getModelRotationMat() {
        Matrix4f matrix = getModelSpaceXform().copy();
        RigUtils.removeMatrixTranslation(matrix);
        return matrix;
    }

    public void setModelRotationMat(Matrix4f mat) {
        rotMat = mat;
    }

    public void setWorldSpaceNormal(Matrix3f worldSpaceNormal) {
        this.worldSpaceNormal = worldSpaceNormal;
    }

    public Matrix3f getWorldSpaceNormal() {
        return worldSpaceNormal;
    }

    public void setWorldSpaceXform(Matrix4f worldSpaceXform) {
        this.worldSpaceXform = worldSpaceXform;
    }

    public Matrix4f getWorldSpaceXform() {
        return worldSpaceXform;
    }

    // Position utils
    public void addPosition(Vec3 vec) {
        addPosition((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void addPosition(float x, float y, float z) {
        addPositionX(x);
        addPositionY(y);
        addPositionZ(z);
    }

    public void addPositionX(float x) {
        setPositionX(getPositionX() + x);
    }

    public void addPositionY(float y) {
        setPositionY(getPositionY() + y);
    }

    public void addPositionZ(float z) {
        setPositionZ(getPositionZ() + z);
    }

    public void setPosition(Vec3 vec) {
        setPosition((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void setPosition(float x, float y, float z) {
        setPositionX(x);
        setPositionY(y);
        setPositionZ(z);
    }

    public Vec3 getPosition() {
        return new Vec3(getPositionX(), getPositionY(), getPositionZ());
    }

    // Rotation utils
    public void addRotation(Vec3 vec) {
        addRotation((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void addRotation(float x, float y, float z) {
        addRotationX(x);
        addRotationY(y);
        addRotationZ(z);
    }

    public void addRotationX(float x) {
        setRotationX(getRotationX() + x);
    }

    public void addRotationY(float y) {
        setRotationY(getRotationY() + y);
    }

    public void addRotationZ(float z) {
        setRotationZ(getRotationZ() + z);
    }

    public void setRotation(Vec3 vec) {
        setRotation((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void setRotation(float x, float y, float z) {
        setRotationX(x);
        setRotationY(y);
        setRotationZ(z);
    }

    public Vec3 getRotation() {
        return new Vec3(getRotationX(), getRotationY(), getRotationZ());
    }

    // Scale utils
    public void multiplyScale(Vec3 vec) {
        multiplyScale((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void multiplyScale(float x, float y, float z) {
        setScaleX(getScaleX() * x);
        setScaleY(getScaleY() * y);
        setScaleZ(getScaleZ() * z);
    }

    public void setScale(Vec3 vec) {
        setScale((float) vec.getX(), (float) vec.getY(), (float) vec.getZ());
    }

    public void setScale(float x, float y, float z) {
        setScaleX(x);
        setScaleY(y);
        setScaleZ(z);
    }

    public Vec3 getScale() {
        return new Vec3(getScaleX(), getScaleY(), getScaleZ());
    }

    public void addRotationOffsetFromBone(MowzieGeoBone source) {
        setRotationX(getRotationX() + source.getRotationX() - source.getInitialSnapshot().rotationValueX);
        setRotationY(getRotationY() + source.getRotationY() - source.getInitialSnapshot().rotationValueY);
        setRotationZ(getRotationZ() + source.getRotationZ() - source.getInitialSnapshot().rotationValueZ);
    }
}