package com.example.bedspaceex.Activities;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.bedspaceex.Models.BedSpaces;
import com.example.bedspaceex.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UploadYourOfferTest {
    @Rule
    public  ActivityTestRule<MainActivity> mMainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void saveToDatabase() {
//        onView(withId(R.id.btn_Upload_Offer)).perform(click());

        String ownerName = "Felix Samuel";
        String hall = "Queen Idia";
        String price = "100000";
        String roomNo = "C50";
        String phoneNo = "08101128804";

        onView(withId(R.id.menu_item_sell)).perform(click());
//        String[] myArray =
//                mBedSpaceListActivityActivityTestRule.getActivity().getResources()
//                        .getStringArray(R.array.halls);
//        int size = myArray.length;
//        for (int i=0; i<size; i++) {
//            onView(withId(R.id.spinner_Hall)).perform(click());
//            onData(is(myArray[i])).perform(click());
//        }
        onView(withId(R.id.spinner_hall)).perform(click());
        onData(is(hall)).perform(click());

        onView(withId(R.id.editText_owner_name)).perform(typeText(ownerName))
                .check(matches(withText(containsString(ownerName))));

        onView(withId(R.id.editText_price)).perform(typeText(price))
                .check(matches(withText(containsString(price))));

        onView(withId(R.id.editText_roomNo)).perform(typeText(roomNo))
                .check(matches(withText(containsString(roomNo))));

        onView(withId(R.id.editText_phone_number)).perform(typeText(phoneNo))
                .check(matches(withText(containsString(phoneNo))));

        onView(withId(R.id.btn_upload_offer)).perform(click());

//        pressBack();
        List<BedSpaces> list = mMainActivityActivityTestRule.getActivity().mSpaces;
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_halls));
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_bedspaces));
        onView(withId(R.id.rvBedSpaceList)).perform(RecyclerViewActions.scrollToPosition(list.size()-1));

        int newOfferIndex = list.size()-1;
        String newSeller = list.get(newOfferIndex).getOwnerName();
        String newPrice = list.get(newOfferIndex).getPrice();
        String newRoomNo = list.get(newOfferIndex).getRoomNumber();
        String newPhoneNo = list.get(newOfferIndex).getPhoneNumber();
        String newHall = list.get(newOfferIndex).getHall();

        assertEquals(ownerName, newSeller);
        assertEquals(price, newPrice);
        assertEquals(roomNo, newRoomNo);
        assertEquals(phoneNo, newPhoneNo);
        assertEquals(hall, newHall);

    }
}