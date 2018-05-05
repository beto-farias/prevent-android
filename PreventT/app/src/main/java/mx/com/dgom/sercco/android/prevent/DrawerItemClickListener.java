package mx.com.dgom.sercco.android.prevent;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by beto on 10/12/15.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        selectItem(position);
    }


    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {


        // Highlight the selected item, update the title, and close the drawer
       // mDrawerList.setItemChecked(position, true);
      //  setTitle(mPlanetTitles[position]);
       // mDrawerLayout.closeDrawer(mDrawerList);
    }
}