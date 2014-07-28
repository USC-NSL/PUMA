package nsl.stg.uiautomator.core;

import java.io.File;

import android.app.UiAutomation;
import android.graphics.Point;
import android.os.RemoteException;

import com.android.uiautomator.core.UiAutomatorBridge;
import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiWatcher;

public class MyUiDevice {

	private UiDevice dev;

	private UiAutomation mAutomation;
	private UiAutomatorBridge mBridge;
	// reference to self
	private static MyUiDevice sDevice;

	public MyUiDevice() {
		if (dev == null) {
			dev = UiDevice.getInstance();
		}
	}

	public void initialize(UiAutomatorBridge uaBridge) {
		dev.initialize(uaBridge);
		this.mBridge = uaBridge;
	}

	public UiAutomatorBridge getUiAutomatorBridge() {
		if (mBridge == null) {
			throw new RuntimeException("MyUiDevice not initialized");
		}

		return mBridge;
	}

	public UiAutomation getUiAutomation() {
		return this.mAutomation;
	}

	public void setUiAutomation(UiAutomation uiauto) {
		this.mAutomation = uiauto;
	}

	// =========================================

	public void clearLastTraversedText() {
		dev.clearLastTraversedText();
	}

	public boolean click(int x, int y) {
		return dev.click(x, y);
	}

	public boolean drag(int startX, int startY, int endX, int endY, int steps) {
		return dev.drag(startX, startY, endX, endY, steps);
	}

	public void dumpWindowHierarchy(String fileName) {
		dev.dumpWindowHierarchy(fileName);
	}

	public void freezeRotation() throws RemoteException {
		dev.freezeRotation();
	}

	public String getCurrentActivityName() {
		return dev.getCurrentActivityName();
	}

	public String getCurrentPackageName() {
		return dev.getCurrentPackageName();
	}

	public int getDisplayHeight() {
		return dev.getDisplayHeight();
	}

	public int getDisplayRotation() {
		return dev.getDisplayRotation();
	}

	public Point getDisplaySizeDp() {
		return dev.getDisplaySizeDp();
	}

	public int getDisplayWidth() {
		return dev.getDisplayWidth();
	}

	public static MyUiDevice getInstance() {
		if (sDevice == null) {
			sDevice = new MyUiDevice();
		}
		return sDevice;
	}

	public String getLastTraversedText() {
		return dev.getLastTraversedText();
	}

	public String getProductName() {
		return dev.getProductName();
	}

	public boolean hasAnyWatcherTriggered() {
		return dev.hasAnyWatcherTriggered();
	}

	public boolean hasWatcherTriggered(String watcherName) {
		return dev.hasWatcherTriggered(watcherName);
	}

	public boolean isNaturalOrientation() {
		return dev.isNaturalOrientation();
	}

	public boolean isScreenOn() throws RemoteException {
		return dev.isScreenOn();
	}

	public boolean openNotification() {
		return dev.openNotification();
	}

	public boolean openQuickSettings() {
		return dev.openQuickSettings();
	}

	public boolean pressBack() {
		return dev.pressBack();
	}

	public boolean pressDPadCenter() {
		return dev.pressDPadCenter();
	}

	public boolean pressDPadDown() {
		return dev.pressDPadDown();
	}

	public boolean pressDPadLeft() {
		return dev.pressDPadLeft();
	}

	public boolean pressDPadRight() {
		return dev.pressDPadRight();
	}

	public boolean pressDPadUp() {
		return dev.pressDPadUp();
	}

	public boolean pressDelete() {
		return dev.pressDelete();
	}

	public boolean pressEnter() {
		return dev.pressEnter();
	}

	public boolean pressHome() {
		return dev.pressHome();
	}

	public boolean pressKeyCode(int keyCode) {
		return dev.pressKeyCode(keyCode);
	}

	public boolean pressKeyCode(int keyCode, int metaState) {
		return dev.pressKeyCode(keyCode, metaState);
	}

	public boolean pressMenu() {
		return dev.pressMenu();
	}

	public boolean pressRecentApps() throws RemoteException {
		return dev.pressRecentApps();
	}

	public boolean pressSearch() {
		return dev.pressSearch();
	}

	public void registerWatcher(String name, UiWatcher watcher) {
		dev.registerWatcher(name, watcher);
	}

	public void removeWatcher(String name) {
		dev.removeWatcher(name);
	}

	public void resetWatcherTriggers() {
		dev.resetWatcherTriggers();
	}

	public void runWatchers() {
		dev.runWatchers();
	}

	public void setCompressedLayoutHeirarchy(boolean compressed) {
		dev.setCompressedLayoutHeirarchy(compressed);
	}

	public void setOrientationLeft() throws RemoteException {
		dev.setOrientationLeft();
	}

	public void setOrientationNatural() throws RemoteException {
		dev.setOrientationNatural();
	}

	public void setOrientationRight() throws RemoteException {
		dev.setOrientationRight();
	}

	public void sleep() throws RemoteException {
		dev.sleep();
	}

	public boolean swipe(int startX, int startY, int endX, int endY, int steps) {
		return dev.swipe(startX, startY, endX, endY, steps);
	}

	public boolean swipe(Point[] segments, int segmentSteps) {
		return dev.swipe(segments, segmentSteps);
	}

	public boolean takeScreenshot(File storePath) {
		return dev.takeScreenshot(storePath);
	}

	public boolean takeScreenshot(File storePath, float scale, int quality) {
		return dev.takeScreenshot(storePath, scale, quality);
	}

	public void unfreezeRotation() throws RemoteException {
		dev.unfreezeRotation();
	}

	public void waitForIdle() {
		dev.waitForIdle();
	}

	public void waitForIdle(long timeout) {
		dev.waitForIdle(timeout);
	}

	public boolean waitForWindowUpdate(String packageName, long timeout) {
		return dev.waitForWindowUpdate(packageName, timeout);
	}

	public void wakeUp() throws RemoteException {
		dev.wakeUp();
	}
}
