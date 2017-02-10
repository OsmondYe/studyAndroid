package com.osmond.study.skydrm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.Direction;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import com.osmond.study.skydrm.common.SkyDRMConstants;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class SkyDRMBlackBoxTest {

    private static UiDevice mDevice;

    private static String User = "osmond.ye@nextlabs.com";
    private static String Pass = "123blue!";

    private static long WAIT_UI_TIMEOUT = 5_000;
    private static long NETWORK_TIMEOUT = 10_000;


    BySelector trait_HomeActiviy = By.res(SkyDRMConstants.APP_PACKAGE_NAME, "activity_main").clazz("android.widget.RelativeLayout");


    @BeforeClass
    static public void startTargetAppFromHomeScreen() throws Exception {
        // init mDevice
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // find system's launcher to prepare to fire the target Apps
        final String launcherPackage = utils_getSystemLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 2_000);

        // launch target app
        final Intent intent = InstrumentationRegistry
                .getContext()
                .getPackageManager()
                .getLaunchIntentForPackage(SkyDRMConstants.APP_PACKAGE_NAME);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        InstrumentationRegistry
                .getContext()
                .startActivity(intent);
        // wait for app displayed firstly
        mDevice.wait(Until.hasObject(By.pkg(SkyDRMConstants.APP_PACKAGE_NAME).depth(0)), 2_000);

        // Env setup ok , for the next steps all other test casees
    }

    static private String utils_getSystemLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    @Test
    public void test_SplashLoginLogout() throws Exception {
        // make sure mDevice is valid
        assertThat(mDevice, notNullValue());

        // UI elements
        BySelector loginBtn = By.res(SkyDRMConstants.APP_PACKAGE_NAME, "login");

        // sub routine
        routine_SplashPage();

        // Test login Button
        UiObject2 loginButton = mDevice.findObject(loginBtn);
        if (loginButton.isEnabled()) {
            loginButton.click();        // will goto another Activitys
        }

        // for login
        routine_LoginFromLoginPage("osmond.ye@nextlabs.com", "123blue!");

        // must in Home pages
        // wait for into Home Pages
        mDevice.wait(Until.hasObject(By.res(SkyDRMConstants.APP_PACKAGE_NAME, "activity_main")), 2_000);

        //
        // for other test in Home
        //
        // for logout
        routine_LogoutFromHomePage();

    }

    @Test
    public void test_Login() throws Exception {
        // depend SplashPage
        // UI elements

        mDevice.waitForIdle();

        BySelector loginBtn = By.res(SkyDRMConstants.APP_PACKAGE_NAME, "login");

        UiObject2 loginButton = mDevice.wait(Until.findObject(loginBtn),NETWORK_TIMEOUT);
        if (loginButton.isEnabled()) {
            loginButton.click();        // will goto another Activitys
        }

        routine_LoginFromLoginPage(User,Pass);

    }

    @Test
    public void test_Logout() throws Exception {
        routine_LogoutFromHomePage();
    }

    private void routine_SplashPage() {

        mDevice.waitForIdle();
        // common UI
        BySelector brandSplash = By.res("com.skydrm.rmc:id/rl_splash");
        BySelector viewPager = By.res("com.skydrm.rmc:id/splash_view_pager").clazz("android.support.v4.view.ViewPager");

        mDevice.waitForWindowUpdate(SkyDRMConstants.APP_PACKAGE_NAME, WAIT_UI_TIMEOUT);

        // wait brand splash visible
        mDevice.wait(Until.hasObject(brandSplash), WAIT_UI_TIMEOUT);

        mDevice.waitForWindowUpdate(SkyDRMConstants.APP_PACKAGE_NAME, WAIT_UI_TIMEOUT);

        // wait brand splash gone
        mDevice.wait(Until.gone(brandSplash), NETWORK_TIMEOUT);


        mDevice.waitForIdle();


        UiObject2 aViewPager = mDevice.wait(Until.findObject(viewPager),WAIT_UI_TIMEOUT);

        // ViewPager test
        //  Next
        for (int i = 0; i < 5; i++) {
            aViewPager.swipe(Direction.LEFT, 0.80F);
        }
        //  Pre
        for (int i = 0; i < 5; i++) {
            aViewPager.swipe(Direction.RIGHT, 0.80F);
        }
    }

    private void routine_LoginFromLoginPage(String email, String pass) throws Exception {

        mDevice.wait(Until.hasObject(By.res(SkyDRMConstants.APP_PACKAGE_NAME, "login_webView")), WAIT_UI_TIMEOUT);

        // wait web-content displayed
        mDevice.waitForWindowUpdate(SkyDRMConstants.APP_PACKAGE_NAME, WAIT_UI_TIMEOUT);

        // find 3 elemtns
        UiObject emailUI = mDevice.findObject(new UiSelector().className("android.widget.EditText").index(0));
        UiObject passUI = mDevice.findObject(new UiSelector().className("android.widget.EditText").index(1));
        UiObject loginBtn = mDevice.findObject(new UiSelector().description("LOG IN").className("android.widget.Button").index(2));

        // input email
        emailUI.click();
        emailUI.setText(email);
        // input passwd
        passUI.click();
        passUI.setText(pass);
        // press login
        loginBtn.click();
        //
        mDevice.waitForWindowUpdate(SkyDRMConstants.APP_PACKAGE_NAME, NETWORK_TIMEOUT);

    }

    private void routine_LogoutFromHomePage() throws Exception {

        // direct the flow into Profile Page
        UiObject profileBtn = mDevice.findObject(new UiSelector()
                .className("android.widget.CheckedTextView")
                .packageName("com.skydrm.rmc")
                .resourceId("com.skydrm.rmc:id/item_name")
                .text("Profile"));
        profileBtn.click();

        // watt for profile page displayed
        mDevice.wait(Until.hasObject(By.res(SkyDRMConstants.APP_PACKAGE_NAME, "header_info_profile")), 2_000);

        // into User section in Profile page
        UiObject2 userDetail = mDevice.findObject(By.res("com.skydrm.rmc:id/ll_user_detail"));
        userDetail.click();

        // wait for logout button displayed
        UiObject2 logoutBtn = mDevice.wait(Until.findObject(By.res("com.skydrm.rmc:id/bt_logout_user_info_view")),WAIT_UI_TIMEOUT);
        logoutBtn.click();

        // find OK button in Dialog
        mDevice.wait(Until.findObject(By.res("android:id/button1")), 2_000).click();
    }

}
